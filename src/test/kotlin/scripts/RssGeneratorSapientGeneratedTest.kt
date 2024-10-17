
package link.kotlin.scripts

import com.rometools.rome.feed.synd.*
import com.rometools.rome.io.SyndFeedOutput
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import link.kotlin.scripts.dsl.Article
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

class RssGeneratorSapientGeneratedTest {

    private lateinit var rssGenerator: RssGenerator

    @BeforeEach
    fun setUp() {
        rssGenerator = RssGenerator.default()
    }

    @Test
    fun `generate should create valid RSS feed with correct metadata`() {
        val articles = listOf(
            Article(
                title = "Test Article",
                url = "https://test.com/article",
                body = "Test body",
                author = "Test Author",
                date = LocalDate.of(2023, 1, 1),
                type = link.kotlin.scripts.dsl.LinkType.article,
                filename = "test-article"
            )
        )

        val result = rssGenerator.generate(articles, "test-feed")

        assertTrue(result.contains("<title>Kotlin Programming Language</title>"))
        assertTrue(result.contains("<link>https://kotlin.link/</link>"))
        assertTrue(result.contains("<uri>https://kotlin.link/test-feed</uri>"))
        assertTrue(result.contains("<description>News, blog posts, projects, podcasts, videos and other. All information about Kotlin.</description>"))
        assertTrue(result.contains("<generator>Kotlin 1.4.10</generator>"))
    }

    @Test
    fun `generate should include all provided articles`() {
        val articles = listOf(
            Article(
                title = "Article 1",
                url = "https://test.com/article1",
                body = "Body 1",
                author = "Author 1",
                date = LocalDate.of(2023, 1, 1),
                type = link.kotlin.scripts.dsl.LinkType.article,
                filename = "article-1"
            ),
            Article(
                title = "Article 2",
                url = "https://test.com/article2",
                body = "Body 2",
                author = "Author 2",
                date = LocalDate.of(2023, 1, 2),
                type = link.kotlin.scripts.dsl.LinkType.article,
                filename = "article-2"
            )
        )

        val result = rssGenerator.generate(articles, "test-feed")

        assertTrue(result.contains("<title>Article 1</title>"))
        assertTrue(result.contains("<title>Article 2</title>"))
        assertTrue(result.contains("<link>https://kotlin.link/articles/article-1</link>"))
        assertTrue(result.contains("<link>https://kotlin.link/articles/article-2</link>"))
    }

    @Test
    fun `generate should handle empty article list`() {
        val result = rssGenerator.generate(emptyList(), "empty-feed")

        assertFalse(result.contains("<entry>"))
        assertTrue(result.contains("<title>Kotlin Programming Language</title>"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["feed1", "feed2", "long-feed-name-with-dashes"])
    fun `generate should use provided feed name in URI`(feedName: String) {
        val result = rssGenerator.generate(emptyList(), feedName)

        assertTrue(result.contains("<uri>https://kotlin.link/$feedName</uri>"))
    }

    @Test
    fun `generate should set correct dates for articles`() {
        val article = Article(
            title = "Test Article",
            url = "https://test.com/article",
            body = "Test body",
            author = "Test Author",
            date = LocalDate.of(2023, 6, 15),
            type = link.kotlin.scripts.dsl.LinkType.article,
            filename = "test-article"
        )

        val result = rssGenerator.generate(listOf(article), "test-feed")

        val expectedDate = Date.from(article.date.atStartOfDay().toInstant(ZoneOffset.UTC))
        assertTrue(result.contains("<updated>${expectedDate.toInstant()}</updated>"))
    }

    @Test
    fun `generate should handle articles with null description`() {
        val article = Article(
            title = "Test Article",
            url = "https://test.com/article",
            body = "Test body",
            author = "Test Author",
            date = LocalDate.now(),
            type = link.kotlin.scripts.dsl.LinkType.article,
            filename = "test-article",
            description = ""
        )

        val result = rssGenerator.generate(listOf(article), "test-feed")

        assertTrue(result.contains("<description></description>"))
    }

    @Test
    fun `generate should set correct categories`() {
        val result = rssGenerator.generate(emptyList(), "test-feed")

        assertTrue(result.contains("<category>Kotlin</category>"))
        assertTrue(result.contains("<category>JVM</category>"))
        assertTrue(result.contains("<category>Programming</category>"))
        assertTrue(result.contains("<category>Android</category>"))
    }

    @Test
    fun `generate should set correct author and webMaster`() {
        val result = rssGenerator.generate(emptyList(), "test-feed")

        assertTrue(result.contains("<author>ruslan@ibragimov.by (Ruslan Ibragimov)</author>"))
        assertTrue(result.contains("<webMaster>ruslan@ibragimov.by (Ruslan Ibragimov)</webMaster>"))
    }

    @Test
    fun `generate should set correct copyright`() {
        val result = rssGenerator.generate(emptyList(), "test-feed")

        assertTrue(result.contains("<copyright>CC0 1.0 Universal (CC0 1.0)</copyright>"))
    }

    @Test
    fun `generate should set correct language`() {
        val result = rssGenerator.generate(emptyList(), "test-feed")

        assertTrue(result.contains("<language>en</language>"))
    }

    @Test
    fun `generate should handle articles with special characters in title and description`() {
        val article = Article(
            title = "Test & Article <with> Special \"Characters\"",
            url = "https://test.com/article",
            body = "Test body",
            author = "Test Author",
            date = LocalDate.now(),
            type = link.kotlin.scripts.dsl.LinkType.article,
            filename = "test-article",
            description = "Description with <html> tags & special \"characters\""
        )

        val result = rssGenerator.generate(listOf(article), "test-feed")

        assertTrue(result.contains("<title>Test &amp; Article &lt;with&gt; Special &quot;Characters&quot;</title>"))
        assertTrue(result.contains("Description with &lt;html&gt; tags &amp; special &quot;characters&quot;"))
    }

//    @Test
//    fun `toSyndEntry should correctly map Article to SyndEntry`() {
//        val article = Article(
//            title = "Test Article",
//            url = "https://test.com/article",
//            body = "Test body",
//            author = "Test Author",
//            date = LocalDate.of(2023, 6, 15),
//            type = link.kotlin.scripts.dsl.LinkType.article,
//            filename = "test-article",
//            description = "Test description"
//        )
//
//        val syndEntry = toSyndEntry(article)
//
//        assertEquals("https://test.com/article", syndEntry.uri)
//        assertEquals("https://kotlin.link/articles/test-article", syndEntry.link)
//        assertEquals("Test Article", syndEntry.title)
//        assertEquals("Test Author", syndEntry.author)
//        assertEquals(Date.from(article.date.atStartOfDay().toInstant(ZoneOffset.UTC)), syndEntry.updatedDate)
//        assertEquals("Test description", syndEntry.description.value)
//    }
}
