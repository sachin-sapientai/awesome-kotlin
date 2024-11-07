package usecases.github

import ConfigModule
import HttpClientModule
import GithubAuthConfig
import GithubRedirectUrl
import GithubRedirectRoute
import GithubAccessToken
import GithubCallbackRoute
import di.bean
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import com.typesafe.config.Config
import io.ktor.client.HttpClient

class GithubModuleSapientGeneratedTest {

    private lateinit var configModule: ConfigModule
    private lateinit var httpClientModule: HttpClientModule
    private lateinit var githubModule: GithubModule
    private lateinit var mockConfig: Config
    private lateinit var mockHttpClient: HttpClient

    @BeforeEach
    fun setup() {
        configModule = mockk()
        httpClientModule = mockk()
        mockConfig = mockk()
        mockHttpClient = mockk()

        every { configModule.config.get } returns mockConfig
        every { httpClientModule.httpClient.get } returns mockHttpClient

        githubModule = GithubModule(configModule, httpClientModule)
    }

    @Test
    fun `test githubAuthConfig initialization`() {
        val mockGithubConfig = mockk<Config>()
        every { mockConfig.getConfig("github") } returns mockGithubConfig

        val mockGithubAuthConfig = mockk<GithubAuthConfig>()
        mockkObject(Hocon)
        every { Hocon.decodeFromConfig<GithubAuthConfig>(mockGithubConfig) } returns mockGithubAuthConfig

        val result = githubModule.githubAuthConfig.get
        assertEquals(mockGithubAuthConfig, result)

        verify { mockConfig.getConfig("github") }
        verify { Hocon.decodeFromConfig<GithubAuthConfig>(mockGithubConfig) }
    }

    @Test
    fun `test githubRedirectUrl initialization`() {
        val mockGithubAuthConfig = mockk<GithubAuthConfig>()
        every { githubModule.githubAuthConfig.get } returns mockGithubAuthConfig

        val mockGithubRedirectUrl = mockk<GithubRedirectUrl>()
        mockkConstructor(GithubRedirectUrl::class)
        every { constructedWith<GithubRedirectUrl>(matchMap { it["githubAuthConfig"] == mockGithubAuthConfig }) } returns mockGithubRedirectUrl

        val result = githubModule.githubRedirectUrl.get
        assertEquals(mockGithubRedirectUrl, result)

        verify { GithubRedirectUrl(githubAuthConfig = mockGithubAuthConfig) }
    }

    @Test
    fun `test githubRedirectRoute initialization`() {
        val mockGithubRedirectUrl = mockk<GithubRedirectUrl>()
        every { githubModule.githubRedirectUrl.get } returns mockGithubRedirectUrl

        val mockGithubRedirectRoute = mockk<GithubRedirectRoute>()
        mockkConstructor(GithubRedirectRoute::class)
        every { constructedWith<GithubRedirectRoute>(matchMap { it["githubRedirectUrl"] == mockGithubRedirectUrl }) } returns mockGithubRedirectRoute

        val result = githubModule.githubRedirectRoute.get
        assertEquals(mockGithubRedirectRoute, result)

        verify { GithubRedirectRoute(githubRedirectUrl = mockGithubRedirectUrl) }
    }

    @Test
    fun `test githubAccessToken initialization`() {
        val mockGithubAuthConfig = mockk<GithubAuthConfig>()
        every { githubModule.githubAuthConfig.get } returns mockGithubAuthConfig

        val mockGithubAccessToken = mockk<GithubAccessToken>()
        mockkConstructor(GithubAccessToken::class)
        every { constructedWith<GithubAccessToken>(matchMap { 
            it["githubAuthConfig"] == mockGithubAuthConfig && it["httpClient"] == mockHttpClient
        }) } returns mockGithubAccessToken

        val result = githubModule.githubAccessToken.get
        assertEquals(mockGithubAccessToken, result)

        verify { GithubAccessToken(githubAuthConfig = mockGithubAuthConfig, httpClient = mockHttpClient) }
    }

    @Test
    fun `test githubCallbackRoute initialization`() {
        val mockGithubAccessToken = mockk<GithubAccessToken>()
        every { githubModule.githubAccessToken.get } returns mockGithubAccessToken

        val mockGithubCallbackRoute = mockk<GithubCallbackRoute>()
        mockkConstructor(GithubCallbackRoute::class)
        every { constructedWith<GithubCallbackRoute>(matchMap { it["githubAccessToken"] == mockGithubAccessToken }) } returns mockGithubCallbackRoute

        val result = githubModule.githubCallbackRoute.get
        assertEquals(mockGithubCallbackRoute, result)

        verify { GithubCallbackRoute(githubAccessToken = mockGithubAccessToken) }
    }

    @Test
    fun `test null safety for configModule and httpClientModule`() {
        assertNotNull(githubModule.githubAuthConfig)
        assertNotNull(githubModule.githubRedirectUrl)
        assertNotNull(githubModule.githubRedirectRoute)
        assertNotNull(githubModule.githubAccessToken)
        assertNotNull(githubModule.githubCallbackRoute)
    }
}