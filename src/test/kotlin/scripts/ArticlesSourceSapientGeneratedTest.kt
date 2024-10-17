
package link.kotlin.scripts

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.util.stream.Stream
import link.kotlin.scripts.dsl.Article
import link.kotlin.scripts.dsl.LinkType
import link.kotlin.scripts.scripting.ScriptEvaluator

class ArticlesSourceSapientGeneratedTest {

    private lateinit var scriptEvaluator: ScriptEvaluator
    private lateinit var articlesProcessor: ArticlesProcessor
    private lateinit var articlesSource: ArticlesSource

    @BeforeEach
    fun setup() {
        scriptEvaluator = mockk()
        articlesProcessor = mockk()
        articlesSource = ArticlesSource.default(scriptEvaluator, articlesProcessor)
    }

    @Test
    fun `getArticles returns sorted and processed articles`() {
        val path1 = mockk<Path>()
        val path2 = mockk<Path>()
        every { path1.fileName } returns Paths.get("article1.awesome.kts")
        every { path2.fileName } returns Paths.get("article2.awesome.kts")

        mockkStatic(Files::class)
        every { Files.list(any()) } returns Stream.of(path1, path2)
        every { Files.readString(path1) } returns "content1"
        every { Files.readString(path2) } returns "content2"

        val article1 = Article("Title 1", "url1", "body1", "author1", LocalDate.of(2022, 1, 1), LinkType.article)
        val article2 = Article("Title 2", "url2", "body2", "author2", LocalDate.of(2022, 1, 2), LinkType.article)

        every { scriptEvaluator.eval("content1", path1.toString(), Article::class) } returns article1
        every { scriptEvaluator.eval("content2", path2.toString(), Article::class) } returns article2

        every { articlesProcessor.process(article1) } returns article1
        every { articlesProcessor.process(article2) } returns article2

        val result = articlesSource.getArticles()

        assertEquals(2, result.size)
        assertEquals(article2, result[0])
        assertEquals(article1, result[1])

        verifyOrder {
            Files.list(Paths.get("articles"))
            Files.readString(path1)
            Files.readString(path2)
            scriptEvaluator.eval("content1", path1.toString(), Article::class)
            scriptEvaluator.eval("content2", path2.toString(), Article::class)
            articlesProcessor.process(article2)
            articlesProcessor.process(article1)
        }
    }

    @Test
    fun `getArticles filters non-awesome kts files`() {
        val path1 = mockk<Path>()
        val path2 = mockk<Path>()
        every { path1.fileName } returns Paths.get("article1.awesome.kts")
        every { path2.fileName } returns Paths.get("article2.txt")

        mockkStatic(Files::class)
        every { Files.list(any()) } returns Stream.of(path1, path2)
        every { Files.readString(path1) } returns "content1"

        val article1 = Article("Title 1", "url1", "body1", "author1", LocalDate.of(2022, 1, 1), LinkType.article)

        every { scriptEvaluator.eval("content1", path1.toString(), Article::class) } returns article1
        every { articlesProcessor.process(article1) } returns article1

        val result = articlesSource.getArticles()

        assertEquals(1, result.size)
        assertEquals(article1, result[0])

        verify(exactly = 0) { Files.readString(path2) }
    }

    @Test
    fun `getArticles handles empty directory`() {
        mockkStatic(Files::class)
        every { Files.list(any()) } returns Stream.empty()

        val result = articlesSource.getArticles()

        assertTrue(result.isEmpty())
    }

    @ParameterizedTest
    @ValueSource(strings = ["article.awesome.kts", "test.awesome.kts", "kotlin.awesome.kts"])
    fun `getArticles processes files with correct extension`(fileName: String) {
        val path = mockk<Path>()
        every { path.fileName } returns Paths.get(fileName)

        mockkStatic(Files::class)
        every { Files.list(any()) } returns Stream.of(path)
        every { Files.readString(path) } returns "content"

        val article = Article("Title", "url", "body", "author", LocalDate.now(), LinkType.article)

        every { scriptEvaluator.eval("content", path.toString(), Article::class) } returns article
        every { articlesProcessor.process(article) } returns article

        val result = articlesSource.getArticles()

        assertEquals(1, result.size)
        assertEquals(article, result[0])
    }

    @Test
    fun `getArticles handles exception during file processing`() {
        val path = mockk<Path>()
        every { path.fileName } returns Paths.get("article.awesome.kts")

        mockkStatic(Files::class)
        every { Files.list(any()) } returns Stream.of(path)
        every { Files.readString(path) } throws RuntimeException("File read error")

        val result = articlesSource.getArticles()

        assertTrue(result.isEmpty())
    }

//    @Test
//    fun `getArticles handles null article from script evaluator`() {
//        val path = mockk<Path>()
//        every { path.fileName } returns Paths.get("article.awesome.kts")
//
//        mockkStatic(Files::class)
//        every { Files.list(any()) } returns Stream.of(path)
//        every { Files.readString(path) } returns "content"
//
//        every { scriptEvaluator.eval("content", path.toString(), Article::class) } returns null
//
//        val result = articlesSource.getArticles()
//
//        assertTrue(result.isEmpty())
//    }
}
