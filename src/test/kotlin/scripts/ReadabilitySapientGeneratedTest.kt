
package link.kotlin.scripts.import

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import link.kotlin.scripts.dsl.Article
import link.kotlin.scripts.utils.HttpClient
import org.apache.http.client.methods.HttpGet
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate

class ReadabilitySapientGeneratedTest {

    private lateinit var httpClient: HttpClient
    private lateinit var readability: Readability

    @BeforeEach
    fun setup() {
        httpClient = mockk()
        readability = Readability(httpClient)
    }

//    @Test
//    fun `getArticle returns correct response`() = runBlocking {
//        val url = "https://example.com"
//        val expectedResponse = """
//            {
//                "title": "Test Article",
//                "content": "<p>Test content</p>",
//                "author": "John Doe",
//                "date_published": "2023-05-01T12:00:00.000Z",
//                "lead_image_url": "https://example.com/image.jpg",
//                "dek": "Test description",
//                "url": "https://example.com",
//                "domain": "example.com",
//                "excerpt": "Test excerpt",
//                "word_count": 100,
//                "direction": "ltr",
//                "total_pages": 1,
//                "rendered_pages": 1,
//                "next_page_url": null
//            }
//        """.trimIndent()
//
//        coEvery { httpClient.execute(any()) } returns expectedResponse
//
//        val result = readability.getArticle(url)
//
//        assertEquals(expectedResponse, result)
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = ["", "https://example.com", "https://blog.jetbrains.com/kotlin/2017/03/kotlin-1-1-1-is-out/"])
//    fun `getArticle handles different URLs`(url: String) = runBlocking {
//        val expectedResponse = """{"title": "Test", "content": "Content", "author": "Author", "url": "$url", "domain": "example.com", "excerpt": "", "word_count": 2, "direction": "ltr", "total_pages": 1, "rendered_pages": 1}"""
//        coEvery { httpClient.execute(any()) } returns expectedResponse
//
//        val result = readability.getArticle(url)
//
//        assertEquals(expectedResponse, result)
//    }

    @Test
    fun `ReadabilityResponse toArticle conversion`() {
        val response = ReadabilityResponse(
            title = "Test Article",
            content = "<p>Test content</p>",
            author = "John Doe",
            date_published = "2023-05-01T12:00:00.000Z",
            lead_image_url = "https://example.com/image.jpg",
            dek = "Test description",
            url = "https://example.com",
            domain = "example.com",
            excerpt = "Test excerpt",
            word_count = 100,
            direction = "ltr",
            total_pages = 1,
            rendered_pages = 1,
            next_page_url = null
        )

        val result = response.toArticle()

        assertTrue(result.contains("title = \"Test Article\""))
        assertTrue(result.contains("url = \"https://example.com\""))
        assertTrue(result.contains("author = \"John Doe\""))
        assertTrue(result.contains("date = LocalDate.of(2023, 5, 1)"))
        assertTrue(result.contains("<p>Test content</p>"))
    }

    @Test
    fun `ReadabilityResponse toArticle handles null date_published`() {
        val response = ReadabilityResponse(
            title = "Test Article",
            content = "<p>Test content</p>",
            author = "John Doe",
            date_published = null,
            lead_image_url = null,
            dek = null,
            url = "https://example.com",
            domain = "example.com",
            excerpt = "Test excerpt",
            word_count = 100,
            direction = "ltr",
            total_pages = 1,
            rendered_pages = 1,
            next_page_url = null
        )

        val result = response.toArticle()

        assertTrue(result.contains("title = \"Test Article\""))
        assertTrue(result.contains("url = \"https://example.com\""))
        assertTrue(result.contains("author = \"John Doe\""))
        assertTrue(result.contains("date = LocalDate.of("))
    }

//    @Test
//    fun `toMarkdown extension function`() {
//        val input = "<p>Test content</p>"
//        val result = input.toMarkdown()
//        assertEquals(input, result)
//    }
//
//    @Test
//    fun `main function executes without exceptions`() {
//        assertDoesNotThrow {
//            main()
//        }
//    }

//    @Test
//    fun `Article file is created`() = runBlocking {
//        val mapper = jacksonObjectMapper()
//        val mockResponse = """
//            {
//                "title": "Test Article",
//                "content": "<p>Test content</p>",
//                "author": "John Doe",
//                "date_published": "2023-05-01T12:00:00.000Z",
//                "url": "https://example.com",
//                "domain": "example.com",
//                "excerpt": "Test excerpt",
//                "word_count": 100,
//                "direction": "ltr",
//                "total_pages": 1,
//                "rendered_pages": 1
//            }
//        """.trimIndent()
//
//        coEvery { httpClient.execute(any()) } returns mockResponse
//
//        main()
//
//        val filePath = Paths.get("./articles/english/2023/Test Article.kts")
//        assertTrue(Files.exists(filePath))
//
//        val fileContent = String(Files.readAllBytes(filePath))
//        assertTrue(fileContent.contains("title = \"Test Article\""))
//        assertTrue(fileContent.contains("url = \"https://example.com\""))
//        assertTrue(fileContent.contains("author = \"John Doe\""))
//        assertTrue(fileContent.contains("date = LocalDate.of(2023, 5, 1)"))
//    }
}
