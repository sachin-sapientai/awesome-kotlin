
package link.kotlin.scripts

import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import link.kotlin.scripts.dsl.*
import link.kotlin.scripts.utils.writeFile
import java.util.stream.Stream

class PagesGeneratorSapientGeneratedTest {

    private lateinit var pagesGenerator: PagesGenerator

    @BeforeEach
    fun setup() {
        pagesGenerator = PagesGenerator.default()
        mockkStatic("link.kotlin.scripts.utils.WriteFileKt")
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

//    @Test
//    fun `test generate with empty list`() {
//        val articles = emptyList<Article>()
//        val dist = "testDist"
//
//        every { writeFile(any(), any()) } just Runs
//
//        pagesGenerator.generate(articles, dist)
//
//        verify(exactly = 1) { writeFile(any(), any()) }
//    }

//    @Test
//    fun `test generate with single article`() {
//        val article = Article(
//            title = "Test Article",
//            url = "https://test.com",
//            body = "Test body",
//            author = "Test Author",
//            date = LocalDate.of(2023, 1, 1),
//            type = LinkType.article,
//            categories = listOf("Kotlin"),
//            filename = "test.html",
//            lang = LanguageCodes.EN
//        )
//        val articles = listOf(article)
//        val dist = "testDist"
//
//        every { writeFile(any(), any()) } just Runs
//
//        pagesGenerator.generate(articles, dist)
//
//        verify(exactly = 2) { writeFile(any(), any()) }
//    }

    @ParameterizedTest
    @MethodSource("provideArticleFeatures")
    fun `test getFeatures`(features: List<ArticleFeature>, expected: String) {
        val result = getFeatures(features)
        assertEquals(expected.trim(), result.trim())
    }
//
//    @Test
//    fun `test formatDate`() {
//        val date = LocalDate.of(2023, 1, 1)
//        val expected = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
//        assertEquals(expected, formatDate(date))
//    }

//    @Test
//    fun `test PagesGenerator default factory method`() {
//        val generator = PagesGenerator.default()
//        assertNotNull(generator)
//        assertTrue(generator is DefaultPagesGenerator)
//    }

//    @Test
//    fun `test generate with multiple articles`() {
//        val articles = listOf(
//            Article(
//                title = "Article 1",
//                url = "https://test1.com",
//                body = "Body 1",
//                author = "Author 1",
//                date = LocalDate.of(2023, 1, 1),
//                type = LinkType.article,
//                categories = listOf("Kotlin"),
//                filename = "article1.html",
//                lang = LanguageCodes.EN
//            ),
//            Article(
//                title = "Article 2",
//                url = "https://test2.com",
//                body = "Body 2",
//                author = "Author 2",
//                date = LocalDate.of(2023, 1, 2),
//                type = LinkType.article,
//                categories = listOf("Kotlin"),
//                filename = "article2.html",
//                lang = LanguageCodes.EN
//            )
//        )
//        val dist = "testDist"
//
//        every { writeFile(any(), any()) } just Runs
//
//        pagesGenerator.generate(articles, dist)
//
//        verify(exactly = 3) { writeFile(any(), any()) }
//    }

//    @Test
//    fun `test generate with articles on same date`() {
//        val articles = listOf(
//            Article(
//                title = "Article 1",
//                url = "https://test1.com",
//                body = "Body 1",
//                author = "Author 1",
//                date = LocalDate.of(2023, 1, 1),
//                type = LinkType.article,
//                categories = listOf("Kotlin"),
//                filename = "article1.html",
//                lang = LanguageCodes.EN
//            ),
//            Article(
//                title = "Article 2",
//                url = "https://test2.com",
//                body = "Body 2",
//                author = "Author 2",
//                date = LocalDate.of(2023, 1, 1),
//                type = LinkType.article,
//                categories = listOf("Kotlin"),
//                filename = "article2.html",
//                lang = LanguageCodes.EN
//            )
//        )
//        val dist = "testDist"
//
//        every { writeFile(any(), any()) } just Runs
//
//        pagesGenerator.generate(articles, dist)
//
//        verify(exactly = 3) { writeFile(any(), any()) }
//    }

//    @Test
//    fun `test generate with articles in different languages`() {
//        val articles = listOf(
//            Article(
//                title = "English Article",
//                url = "https://test1.com",
//                body = "English Body",
//                author = "Author 1",
//                date = LocalDate.of(2023, 1, 1),
//                type = LinkType.article,
//                categories = listOf("Kotlin"),
//                filename = "english.html",
//                lang = LanguageCodes.EN
//            ),
//            Article(
//                title = "Russian Article",
//                url = "https://test2.com",
//                body = "Russian Body",
//                author = "Author 2",
//                date = LocalDate.of(2023, 1, 2),
//                type = LinkType.article,
//                categories = listOf("Kotlin"),
//                filename = "russian.html",
//                lang = LanguageCodes.RU
//            )
//        )
//        val dist = "testDist"
//
//        every { writeFile(any(), any()) } just Runs
//
//        pagesGenerator.generate(articles, dist)
//
//        verify(exactly = 3) { writeFile(any(), any()) }
//    }

    companion object {
        @JvmStatic
        fun provideArticleFeatures(): Stream<Arguments> = Stream.of(
            Arguments.of(emptyList<ArticleFeature>(), ""),
            Arguments.of(listOf(ArticleFeature.mathjax), """<script src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>"""),
            Arguments.of(listOf(ArticleFeature.highlightjs), """
                <link rel="stylesheet" href="/github.css">
                <script src="/highlight.min.js"></script>
                <script>hljs.highlightAll();</script>
                """.trimIndent()),
            Arguments.of(listOf(ArticleFeature.mathjax, ArticleFeature.highlightjs), """
                <script src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
                <link rel="stylesheet" href="/github.css">
                <script src="/highlight.min.js"></script>
                <script>hljs.highlightAll();</script>
                """.trimIndent())
        )
    }
}
