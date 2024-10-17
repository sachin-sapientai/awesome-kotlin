
package link.kotlin.scripts

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import link.kotlin.scripts.dsl.Article
import link.kotlin.scripts.dsl.LinkType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate

class ArticlesProcessorSapientGeneratedTest {

    private lateinit var markdownRenderer: MarkdownRenderer
    private lateinit var articlesProcessor: ArticlesProcessor

    @BeforeEach
    fun setUp() {
        markdownRenderer = mockk()
        articlesProcessor = ArticlesProcessor.default(markdownRenderer)
    }

    @Test
    fun `process should update article with rendered HTML`() = runBlocking {
        val article = Article(
            title = "Test Article",
            url = "https://example.com",
            body = "# Test Content",
            author = "Test Author",
            date = LocalDate.now(),
            type = LinkType.article
        )
        val renderedHtml = "<h1>Test Content</h1>"

        every { markdownRenderer.render(article.body) } returns renderedHtml

        val processedArticle = articlesProcessor.process(article)

        assertEquals(renderedHtml, processedArticle.description)
        assertEquals(renderedHtml, processedArticle.body)
        assertEquals("test-article.html", processedArticle.filename)

        verify { markdownRenderer.render(article.body) }
    }

    @ParameterizedTest
    @CsvSource(
        "Simple Title, simple-title.html",
        "Title with Spaces, title-with-spaces.html",
        "Title with Numbers 123, title-with-numbers-123.html",
        "Title with Symbols !@#$%^&*, title-with-symbols.html",
        "Заголовок на русском, zagolovok-na-russkom.html"
    )
    fun `getFileName should generate correct filename`(title: String, expected: String) {
        assertEquals(expected, getFileName(title))
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "---test---",
        "-test-",
        "test-",
        "-test"
    ])
    fun `getFileName should handle edge cases with dashes`(title: String) {
        val result = getFileName(title)
        assertFalse(result.startsWith("-"))
        assertFalse(result.endsWith("-"))
        assertFalse(result.contains("--"))
    }

    @Test
    fun `translit should correctly transliterate Russian characters`() {
        assertEquals("Privet, mir!", "Привет, мир!".translit())
        assertEquals("Kotlin - otlichnyy yazyk programmirovaniya", "Kotlin - отличный язык программирования".translit())
    }

    @Test
    fun `translit should handle mixed language input`() {
        assertEquals("Hello, Mir! Kak dela?", "Hello, Мир! Как дела?".translit())
    }

    @Test
    fun `translit should return original string for non-Russian input`() {
        val input = "Hello, World! 123"
        assertEquals(input, input.translit())
    }

    @Test
    fun `process should handle null description`() = runBlocking {
        val article = Article(
            title = "Test Article",
            url = "https://example.com",
            body = "# Test Content",
            author = "Test Author",
            date = LocalDate.now(),
            type = LinkType.article,
            description = ""
        )
        val renderedHtml = "<h1>Test Content</h1>"

        every { markdownRenderer.render(article.body) } returns renderedHtml

        val processedArticle = articlesProcessor.process(article)

        assertEquals(renderedHtml, processedArticle.description)
    }

    @Test
    fun `process should not modify original article`() = runBlocking {
        val article = Article(
            title = "Test Article",
            url = "https://example.com",
            body = "# Test Content",
            author = "Test Author",
            date = LocalDate.now(),
            type = LinkType.article
        )
        val renderedHtml = "<h1>Test Content</h1>"

        every { markdownRenderer.render(article.body) } returns renderedHtml

        val processedArticle = articlesProcessor.process(article)

        assertNotSame(article, processedArticle)
        assertEquals("# Test Content", article.body)
        assertEquals("", article.description)
        assertEquals("", article.filename)
    }
}
