
package link.kotlin.scripts

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.*
import kotlinx.coroutines.runBlocking
import link.kotlin.scripts.model.ApplicationConfiguration
import link.kotlin.scripts.model.Link
import link.kotlin.scripts.utils.HttpClient
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.client.methods.HttpGet
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LinksProcessorSapientGeneratedTest {

    private lateinit var configuration: ApplicationConfiguration
    private lateinit var mapper: ObjectMapper
    private lateinit var httpClient: HttpClient
    private lateinit var linksChecker: LinksChecker
    private lateinit var markdownRenderer: MarkdownRenderer
    private lateinit var linksProcessor: LinksProcessor

    @BeforeEach
    fun setup() {
        configuration = mockk()
        mapper = mockk()
        httpClient = mockk()
        linksChecker = mockk()
        markdownRenderer = mockk()
        linksProcessor = LinksProcessor.default(configuration, mapper, httpClient, linksChecker, markdownRenderer)
    }

    @Test
    fun `process should handle github link`() = runBlocking {
        val link = Link(github = "kotlin/kotlin", name = null, href = null)
        val githubMetadata = GithubMetadata(
            stargazersCount = 100,
            pushedAt = Instant.now(),
            description = "Kotlin language",
            archived = false,
            topics = listOf("kotlin", "programming-language")
        )

        coEvery { configuration.dryRun } returns false
        coEvery { configuration.ghToken } returns "token"
        coEvery { httpClient.execute(any()) } returns mockHttpResponse(200, "{}")
        coEvery { mapper.readValue<GithubRepoResponse>(any<String>()) } returns mockGithubRepoResponse()
        coEvery { mapper.readValue<GithubRepoTopicsResponse>(any<String>()) } returns GithubRepoTopicsResponse(listOf("kotlin", "programming-language"))
        coEvery { linksChecker.check(any()) } just Runs

        val result = linksProcessor.process(link)

        expectThat(result) {
            get { name }.isEqualTo("kotlin/kotlin")
            get { href }.isEqualTo("https://github.com/kotlin/kotlin")
            get { star }.isNotNull()
            get { update }.isNotNull()
            get { tags }.contains("kotlin", "programming-language")
        }
    }

    @Test
    fun `process should handle bitbucket link`() = runBlocking {
        val link = Link(bitbucket = "atlassian/bitbucket", name = null, href = null)
        val bitbucketResponse = BitbucketResponse(size = 50)

        coEvery { httpClient.execute(any()) } returns mockHttpResponse(200, "{\"size\": 50}")
        coEvery { mapper.readValue<BitbucketResponse>(any<String>()) } returns bitbucketResponse
        coEvery { linksChecker.check(any()) } just Runs

        val result = linksProcessor.process(link)

        expectThat(result) {
            get { name }.isEqualTo("atlassian/bitbucket")
            get { href }.isEqualTo("https://bitbucket.org/atlassian/bitbucket")
            get { star }.isEqualTo(50)
        }
    }

    @Test
    fun `process should handle kug link`() = runBlocking {
        val link = Link(kug = "KotlinMoscow", name = null, href = null)

        coEvery { linksChecker.check(any()) } just Runs

        val result = linksProcessor.process(link)

        expectThat(result) {
            get { name }.isEqualTo("KotlinMoscow")
        }
    }

//    @Test
//    fun `process should handle regular link`() = runBlocking {
//        val link = Link(name = "Kotlin Blog", href = "https://blog.jetbrains.com/kotlin/")
//
//        coEvery { linksChecker.check(any()) } just Runs
//
//        val result = linksProcessor.process(link)
//
//        expectThat(result).isEqualTo(link)
//    }
//
//    @Test
//    fun `process should handle markdown description`() = runBlocking {
//        val link = Link(name = "Test", href = "https://test.com", desc = "**Bold** description")
//
//        coEvery { linksChecker.check(any()) } just Runs
//        coEvery { markdownRenderer.render(any()) } returns "<strong>Bold</strong> description"
//
//        val result = linksProcessor.process(link)
//
//        expectThat(result.desc).isEqualTo("<strong>Bold</strong> description")
//    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `process should respect dryRun configuration`(dryRun: Boolean) = runBlocking {
        val link = Link(github = "kotlin/kotlin")

        coEvery { configuration.dryRun } returns dryRun
        coEvery { configuration.ghToken } returns "token"
        coEvery { httpClient.execute(any()) } returns mockHttpResponse(200, "{}")
        coEvery { mapper.readValue<GithubRepoResponse>(any<String>()) } returns mockGithubRepoResponse()
        coEvery { mapper.readValue<GithubRepoTopicsResponse>(any<String>()) } returns GithubRepoTopicsResponse(emptyList())
        coEvery { linksChecker.check(any()) } just Runs

        linksProcessor.process(link)

        if (dryRun) {
            coVerify(exactly = 0) { httpClient.execute(any()) }
        } else {
            coVerify(atLeast = 1) { httpClient.execute(any()) }
        }
    }

    @Test
    fun `process should handle errors when fetching github metadata`() = runBlocking {
        val link = Link(github = "kotlin/kotlin")

        coEvery { configuration.dryRun } returns false
        coEvery { configuration.ghToken } returns "token"
        coEvery { httpClient.execute(any()) } throws Exception("API Error")
        coEvery { linksChecker.check(any()) } just Runs

        val result = linksProcessor.process(link)

        expectThat(result) {
            get { name }.isEqualTo("kotlin/kotlin")
            get { href }.isEqualTo("https://github.com/kotlin/kotlin")
            get { star }.isNull()
            get { update }.isNull()
        }
    }

    private fun mockHttpResponse(statusCode: Int, body: String): HttpResponse {
        val response: HttpResponse = mockk()
        val statusLine: StatusLine = mockk()
        every { statusLine.statusCode } returns statusCode
        every { response.statusLine } returns statusLine
        every { response.entity.content } returns body.byteInputStream()
        return response
    }

    private fun mockGithubRepoResponse(): GithubRepoResponse {
        return GithubRepoResponse(
            stargazersCount = 100,
            pushedAt = Instant.now(),
            description = "Kotlin language",
            archived = false
        )
    }
}
