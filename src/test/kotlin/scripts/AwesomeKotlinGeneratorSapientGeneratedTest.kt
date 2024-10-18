
package link.kotlin.scripts

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import link.kotlin.scripts.dsl.Article
import link.kotlin.scripts.dsl.Category
import link.kotlin.scripts.dsl.LinkBuilder
import link.kotlin.scripts.model.ApplicationConfiguration
import link.kotlin.scripts.model.default
import link.kotlin.scripts.scripting.ScriptEvaluator
import link.kotlin.scripts.utils.*
import java.util.stream.Stream
import java.nio.file.Path

class AwesomeKotlinGeneratorSapientGeneratedTest {

    private lateinit var awesomeKotlinGenerator: AwesomeKotlinGenerator
    private lateinit var linksSource: LinksSource
    private lateinit var articlesSource: ArticlesSource
    private lateinit var readmeGenerator: ReadmeGenerator
    private lateinit var siteGenerator: SiteGenerator

    @BeforeEach
    fun setup() {
        linksSource = mockk()
        articlesSource = mockk()
        readmeGenerator = mockk()
        siteGenerator = mockk()

        awesomeKotlinGenerator = AwesomeKotlinGenerator.default()
    }

    @Test
    fun `test getLinks`() {
        val expectedLinks = listOf(Category("Test Category", mutableListOf()))
        every { linksSource.getLinks() } returns expectedLinks

        val result = awesomeKotlinGenerator.getLinks()

        assertEquals(expectedLinks, result)
        verify(exactly = 1) { linksSource.getLinks() }
    }

    @Test
    fun `test getArticles`() {
        val expectedArticles = listOf(mockk<Article>())
        every { articlesSource.getArticles() } returns expectedArticles

        val result = awesomeKotlinGenerator.getArticles()

        assertEquals(expectedArticles, result)
        verify(exactly = 1) { articlesSource.getArticles() }
    }

    @Test
    fun `test generateReadme`() {
        val links = listOf(Category("Test Category", mutableListOf()))
        val generatedReadme = "# Awesome Kotlin"
        every { readmeGenerator.generate(links) } returns generatedReadme
        mockkStatic("link.kotlin.scripts.utils.WriteFileKt")
        every { writeFile(any<String>(), any()) } just Runs

        awesomeKotlinGenerator.generateReadme(links)

        verify(exactly = 1) { readmeGenerator.generate(links) }
        verify(exactly = 1) { writeFile("./readme/README.md", generatedReadme) }
    }

    @Test
    fun `test generateSiteResources`() {
        val links = listOf(Category("Test Category", mutableListOf()))
        val articles = listOf(mockk<Article>())

        every { siteGenerator.createDistFolders() } just Runs
        every { siteGenerator.copyResources() } just Runs
        every { siteGenerator.generateLinksJson(links) } just Runs
        every { siteGenerator.generateKotlinVersionsJson() } just Runs
        every { siteGenerator.generateFeeds(articles) } just Runs
        every { siteGenerator.generateSitemap(articles) } just Runs
        every { siteGenerator.generateArticles(articles) } just Runs

        awesomeKotlinGenerator.generateSiteResources(links, articles)

        verifyOrder {
            siteGenerator.createDistFolders()
            siteGenerator.copyResources()
            siteGenerator.generateLinksJson(links)
            siteGenerator.generateKotlinVersionsJson()
            siteGenerator.generateFeeds(articles)
            siteGenerator.generateSitemap(articles)
            siteGenerator.generateArticles(articles)
        }
    }

//    @Test
//    fun `test default companion object creation`() {
//        mockkObject(ApplicationConfiguration.Companion)
//        mockkObject(HttpClient.Companion)
//        mockkObject(KotlinObjectMapper)
//        mockkObject(Cache.Companion)
//        mockkObject(ScriptEvaluator.Companion)
//        mockkObject(KotlinVersionFetcher.Companion)
//        mockkObject(SitemapGenerator.Companion)
//        mockkObject(SiteGenerator.Companion)
//        mockkObject(LinksChecker.Companion)
//        mockkObject(LinksProcessor.Companion)
//        mockkObject(CategoryProcessor.Companion)
//        mockkObject(GithubTrending.Companion)
//        mockkObject(LinksSource.Companion)
//        mockkObject(ArticlesSource.Companion)
//        mockkObject(ReadmeGenerator.Companion)
//
//        every { ApplicationConfiguration.default() } returns mockk()
//        every { HttpClient.default() } returns mockk()
//        every { KotlinObjectMapper.default() } returns mockk()
//        every { Cache.default(any(), any()) } returns mockk()
//        every { ScriptEvaluator.default(any()) } returns mockk()
//        every { KotlinVersionFetcher.default(any()) } returns mockk()
//        every { SitemapGenerator.default(any()) } returns mockk()
//        every { SiteGenerator.default(any(), any(), any(), any(), any()) } returns mockk()
//        every { LinksChecker.default(any()) } returns mockk()
//        every { LinksProcessor.default(any(), any(), any(), any(), any()) } returns mockk()
//        every { CategoryProcessor.default(any()) } returns mockk()
//        every { GithubTrending.default(any()) } returns mockk()
//        every { LinksSource.default(any(), any(), any()) } returns mockk()
//        every { ArticlesSource.default(any(), any()) } returns mockk()
//        every { ReadmeGenerator.default() } returns mockk()
//
//        val result = AwesomeKotlinGenerator.default()
//
//        assertNotNull(result)
//        assertTrue(result is AwesomeKotlinGenerator)
//    }

    @ParameterizedTest
    @MethodSource("provideNullAndNonNullValues")
    fun `test null safety in LinkBuilder`(name: String?, href: String?, desc: String?) {
        val linkBuilder = LinkBuilder().apply {
            this.name = name
            this.href = href
            this.desc = desc
        }

        val link = linkBuilder.toLink()

        assertEquals(name, link.name)
        assertEquals(href, link.href)
        assertEquals(desc, link.desc)
    }

    companion object {
        @JvmStatic
        fun provideNullAndNonNullValues(): Stream<Arguments> = Stream.of(
            Arguments.of(null, null, null),
            Arguments.of("Name", null, null),
            Arguments.of(null, "http://example.com", null),
            Arguments.of(null, null, "Description"),
            Arguments.of("Name", "http://example.com", "Description")
        )
    }
}
