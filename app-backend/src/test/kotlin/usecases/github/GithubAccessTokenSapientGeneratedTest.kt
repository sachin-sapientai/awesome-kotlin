package usecases.github

import di.SuspendBean1
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GithubAccessTokenSapientGeneratedTest {

    private lateinit var githubAuthConfig: GithubAuthConfig
    private lateinit var httpClient: HttpClient
    private lateinit var githubAccessToken: GithubAccessToken

    @BeforeEach
    fun setUp() {
        githubAuthConfig = GithubAuthConfig(
            clientId = "test-client-id",
            clientSecret = "test-client-secret",
            redirectUri = "http://test-redirect-uri.com"
        )
        httpClient = HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }
            engine {
                addHandler { request ->
                    when (request.url.toString()) {
                        "https://github.com/login/oauth/access_token?client_id=test-client-id&client_secret=test-client-secret&code=test-code&redirect_uri=http://test-redirect-uri.com" -> {
                            respond(
                                content = """
                                {
                                    "access_token": "test-access-token",
                                    "expires_in": 3600,
                                    "refresh_token": "test-refresh-token",
                                    "refresh_token_expires_in": 86400,
                                    "token_type": "bearer",
                                    "scope": "user,repo"
                                }
                                """.trimIndent(),
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }
                        else -> error("Unhandled ${request.url}")
                    }
                }
            }
        }
        githubAccessToken = GithubAccessToken(githubAuthConfig, httpClient)
    }

    @Test
    fun `test invoke method returns correct GithubAccessTokenResponse`() = runBlocking {
        val response = githubAccessToken.invoke("test-code")

        assertEquals("test-access-token", response.accessToken)
        assertEquals(3600, response.expiresIn)
        assertEquals("test-refresh-token", response.refreshToken)
        assertEquals(86400, response.refreshTokenExpiresIn)
        assertEquals("bearer", response.tokenType)
        assertEquals("user,repo", response.scope)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "test-code", "1234567890"])
    fun `test accessTokenUrl generates correct URL`(code: String) {
        val url = githubAccessToken.accessTokenUrl(code)
        
        assertTrue(url.startsWith("https://github.com/login/oauth/access_token"))
        assertTrue(url.contains("client_id=test-client-id"))
        assertTrue(url.contains("client_secret=test-client-secret"))
        assertTrue(url.contains("code=$code"))
        assertTrue(url.contains("redirect_uri=http://test-redirect-uri.com"))
    }

    @Test
    fun `test GithubAccessTokenResponse data class`() {
        val response = GithubAccessToken.GithubAccessTokenResponse(
            accessToken = "test-token",
            expiresIn = 3600,
            refreshToken = "test-refresh",
            refreshTokenExpiresIn = 86400,
            tokenType = "bearer",
            scope = "user,repo"
        )

        assertEquals("test-token", response.accessToken)
        assertEquals(3600, response.expiresIn)
        assertEquals("test-refresh", response.refreshToken)
        assertEquals(86400, response.refreshTokenExpiresIn)
        assertEquals("bearer", response.tokenType)
        assertEquals("user,repo", response.scope)
    }

    @Test
    fun `test invoke method handles null values`() = runBlocking {
        val mockHttpClient = mockk<HttpClient>()
        val nullResponse = GithubAccessToken.GithubAccessTokenResponse(
            accessToken = "",
            expiresIn = 0,
            refreshToken = "",
            refreshTokenExpiresIn = 0,
            tokenType = "",
            scope = ""
        )
        coEvery { mockHttpClient.post(any<String>()).body<GithubAccessToken.GithubAccessTokenResponse>() } returns nullResponse

        val githubAccessToken = GithubAccessToken(githubAuthConfig, mockHttpClient)
        val response = githubAccessToken.invoke("test-code")

        assertEquals("", response.accessToken)
        assertEquals(0, response.expiresIn)
        assertEquals("", response.refreshToken)
        assertEquals(0, response.refreshTokenExpiresIn)
        assertEquals("", response.tokenType)
        assertEquals("", response.scope)
    }

    @Test
    fun `test invoke method throws exception on network error`() = runBlocking {
        val mockHttpClient = mockk<HttpClient>()
        coEvery { mockHttpClient.post(any<String>()).body<GithubAccessToken.GithubAccessTokenResponse>() } throws Exception("Network error")

        val githubAccessToken = GithubAccessToken(githubAuthConfig, mockHttpClient)
        
        assertThrows(Exception::class.java) {
            runBlocking { githubAccessToken.invoke("test-code") }
        }
    }
}