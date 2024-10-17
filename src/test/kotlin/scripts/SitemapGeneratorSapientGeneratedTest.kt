
package link.kotlin.scripts

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import link.kotlin.scripts.dsl.Article
import link.kotlin.scripts.model.ApplicationConfiguration
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.util.stream.Stream

class SitemapGeneratorSapientGeneratedTest {

    private lateinit var configuration: ApplicationConfiguration
    private lateinit var sitemapGenerator: SitemapGenerator

    @BeforeEach
    fun setUp() {
        configuration = mockk()
        every { configuration.siteUrl } returns "https://example.com/"
        sitemapGenerator = SitemapGenerator.default(configuration)
    }

    @Test
    fun `generate sitemap with no articles`() {
        val result = sitemapGenerator.generate(emptyList())
        assertTrue(result.contains("<url><loc>https://example.com/</loc></url>"))
        assertFalse(result.contains("<url><loc>https://example.com/articles/"))
    }

    @Test
    fun `generate sitemap with single article`() {
        val article = Article(
            title = "Test Article",
            url = "https://example.com/test",
            body = "Test body",
            author = "Test Author",
            date = LocalDate.now(),
            type = link.kotlin.scripts.dsl.LinkType.article,
            filename = "test-article"
        )
        val result = sitemapGenerator.generate(listOf(article))
        assertTrue(result.contains("<url><loc>https://example.com/</loc></url>"))
        assertTrue(result.contains("<url><loc>https://example.com/articles/test-article</loc></url>"))
    }

    @ParameterizedTest
    @MethodSource("provideMultipleArticles")
    fun `generate sitemap with multiple articles`(articles: List<Article>) {
        val result = sitemapGenerator.generate(articles)
        assertTrue(result.contains("<url><loc>https://example.com/</loc></url>"))
        articles.forEach { article ->
            assertTrue(result.contains("<url><loc>https://example.com/articles/${article.filename}</loc></url>"))
        }
    }

    @Test
    fun `generate sitemap with custom site URL`() {
        val customConfig = mockk<ApplicationConfiguration>()
        every { customConfig.siteUrl } returns "https://custom.com/"
        val customGenerator = SitemapGenerator.default(customConfig)
        
        val article = Article(
            title = "Custom Article",
            url = "https://custom.com/test",
            body = "Custom body",
            author = "Custom Author",
            date = LocalDate.now(),
            type = link.kotlin.scripts.dsl.LinkType.article,
            filename = "custom-article"
        )
        
        val result = customGenerator.generate(listOf(article))
        assertTrue(result.contains("<url><loc>https://custom.com/</loc></url>"))
        assertTrue(result.contains("<url><loc>https://custom.com/articles/custom-article</loc></url>"))
    }

//    @Test
//    fun `generate sitemap with null safety check`() {
//        val nullConfig = mockk<ApplicationConfiguration>()
//        every { nullConfig.siteUrl } returns null
//        val nullGenerator = SitemapGenerator.default(nullConfig)
//
//        val result = nullGenerator.generate(emptyList())
//        assertTrue(result.contains("<url><loc></loc></url>"))
//    }
//
//    @Test
//    fun `verify SitemapGenerator_default extension function`() {
//        val defaultGenerator = SitemapGenerator.default()
//        assertNotNull(defaultGenerator)
//        assertTrue(defaultGenerator is DefaultSitemapGenerator)
//    }

    companion object {
        @JvmStatic
        fun provideMultipleArticles(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    listOf(
                        Article(
                            title = "Article 1",
                            url = "https://example.com/1",
                            body = "Body 1",
                            author = "Author 1",
                            date = LocalDate.now(),
                            type = link.kotlin.scripts.dsl.LinkType.article,
                            filename = "article-1"
                        ),
                        Article(
                            title = "Article 2",
                            url = "https://example.com/2",
                            body = "Body 2",
                            author = "Author 2",
                            date = LocalDate.now(),
                            type = link.kotlin.scripts.dsl.LinkType.article,
                            filename = "article-2"
                        )
                    )
                ),
                Arguments.of(
                    listOf(
                        Article(
                            title = "Article 3",
                            url = "https://example.com/3",
                            body = "Body 3",
                            author = "Author 3",
                            date = LocalDate.now(),
                            type = link.kotlin.scripts.dsl.LinkType.article,
                            filename = "article-3"
                        ),
                        Article(
                            title = "Article 4",
                            url = "https://example.com/4",
                            body = "Body 4",
                            author = "Author 4",
                            date = LocalDate.now(),
                            type = link.kotlin.scripts.dsl.LinkType.article,
                            filename = "article-4"
                        ),
                        Article(
                            title = "Article 5",
                            url = "https://example.com/5",
                            body = "Body 5",
                            author = "Author 5",
                            date = LocalDate.now(),
                            type = link.kotlin.scripts.dsl.LinkType.article,
                            filename = "article-5"
                        )
                    )
                )
            )
        }
    }
}
