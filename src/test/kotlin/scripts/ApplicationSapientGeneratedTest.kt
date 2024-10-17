
package link.kotlin.scripts

import io.ktor.http.*
import io.mockk.*
import kotlinx.coroutines.runBlocking
import link.kotlin.scripts.dsl.Article
import link.kotlin.scripts.dsl.*
import link.kotlin.scripts.model.Link
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.system.exitProcess
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ApplicationSapientGeneratedTest {

    private lateinit var generator: AwesomeKotlinGenerator

    @BeforeEach
    fun setup() {
        generator = mockk()
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

//    @Test
//    fun `main function executes successfully`() = runBlocking {
//        // Arrange
//        val articles = listOf(mockk<Article>())
//        val links = listOf(mockk<Link>())
//
//        every { generator.getArticles() } returns articles
//        every { generator.getLinks() } returns links
//        every { generator.generateReadme(any()) } just Runs
//        every { generator.generateSiteResources(any(), any()) } just Runs
//
//        mockkObject(AwesomeKotlinGenerator)
//        every { AwesomeKotlinGenerator.default() } returns generator
//
//        mockkStatic("kotlin.system.ProcessKt")
//        every { exitProcess(0) } throws RuntimeException("Exit 0")
//
//        // Act & Assert
//        assertThrows<RuntimeException>("Exit 0") { main() }
//
//        // Verify
//        verify(exactly = 1) {
//            generator.getArticles()
//            generator.getLinks()
//            generator.generateReadme(links)
//            generator.generateSiteResources(links, articles)
//        }
//    }
//
//    @Test
//    fun `main function handles exceptions`() = runBlocking {
//        // Arrange
//        every { generator.getArticles() } throws RuntimeException("Test exception")
//
//        mockkObject(AwesomeKotlinGenerator)
//        every { AwesomeKotlinGenerator.default() } returns generator
//
//        mockkStatic("kotlin.system.ProcessKt")
//        every { exitProcess(1) } throws RuntimeException("Exit 1")
//
//        // Act & Assert
//        assertThrows<RuntimeException>("Exit 1") { main() }
//    }

    @Test
    fun `logger is initialized`() {
        // Arrange & Act
        val loggerField = ContentType.Application::class.java.getDeclaredField("LOGGER")
        loggerField.isAccessible = true
        val logger = loggerField.get(null)

        // Assert
        assertNotNull(logger)
    }

//    @Test
//    fun `Category data class works correctly`() {
//        // Arrange
//        val category = Category("Test Category", mutableListOf())
//        val subcategory = Subcategory("Test Subcategory", mutableListOf())
//
//        // Act
//        category + subcategory
//
//        // Assert
//        assertEquals(1, category.subcategories.size)
//        assertEquals("Test Subcategory", category.subcategories[0].name)
//    }
//
//    @Test
//    fun `Subcategory data class works correctly`() {
//        // Arrange
//        val subcategory = Subcategory("Test Subcategory", mutableListOf())
//        val link = Link("Test Link", "http://test.com", "Test Description", emptyList(), emptyList(), null, null, null, false)
//
//        // Act
//        subcategory + link
//
//        // Assert
//        assertEquals(1, subcategory.links.size)
//        assertEquals("Test Link", subcategory.links[0].name)
//    }

    @Test
    fun `category DSL function works correctly`() {
        // Arrange & Act
        val category = category("Test Category") {
            subcategory("Test Subcategory") {
                link {
                    name = "Test Link"
                    href = "http://test.com"
                    desc = "Test Description"
                }
            }
        }

        // Assert
        assertEquals("Test Category", category.name)
        assertEquals(1, category.subcategories.size)
        assertEquals("Test Subcategory", category.subcategories[0].name)
        assertEquals(1, category.subcategories[0].links.size)
        assertEquals("Test Link", category.subcategories[0].links[0].name)
    }

    @Test
    fun `LinkBuilder builds Link correctly`() {
        // Arrange
        val linkBuilder = LinkBuilder().apply {
            name = "Test Link"
            href = "http://test.com"
            desc = "Test Description"
            github = "testuser/testrepo"
            awesome()
            setPlatforms(PlatformType.ANDROID, PlatformType.JVM)
            setTags("kotlin", "test")
        }

        // Act
        val link = linkBuilder.toLink()

        // Assert
        assertEquals("Test Link", link.name)
        assertEquals("http://test.com", link.href)
        assertEquals("Test Description", link.desc)
        assertEquals("testuser/testrepo", link.github)
        assertEquals(true, link.awesome)
        assertEquals(listOf(PlatformType.ANDROID, PlatformType.JVM), link.platforms)
        assertEquals(listOf("kotlin", "test"), link.tags)
    }

    @Test
    fun `LanguageCodes contains function works correctly`() {
        // Assert
        assert(LanguageCodes.contains("english"))
        assert(LanguageCodes.contains("russian"))
        assert(!LanguageCodes.contains("spanish"))
    }
}
