
package link.kotlin.scripts

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import link.kotlin.scripts.dsl.Category
import link.kotlin.scripts.dsl.Subcategory
import link.kotlin.scripts.model.Link

class CategoryProcessorSapientGeneratedTest {

    private lateinit var linksProcessor: LinksProcessor
    private lateinit var categoryProcessor: CategoryProcessor

    @BeforeEach
    fun setup() {
        linksProcessor = mockk()
        categoryProcessor = CategoryProcessor.default(linksProcessor)
    }

    @Test
    fun `process should return processed category with processed links`() = runBlocking {
        // Given
        val link1 = Link("Link 1", "http://example1.com", "Description 1")
        val link2 = Link("Link 2", "http://example2.com", "Description 2")
        val subcategory = Subcategory("Subcategory", mutableListOf(link1, link2))
        val category = Category("Category", mutableListOf(subcategory))

        val processedLink1 = link1.copy(name = "Processed Link 1")
        val processedLink2 = link2.copy(name = "Processed Link 2")

        coEvery { linksProcessor.process(link1) } returns processedLink1
        coEvery { linksProcessor.process(link2) } returns processedLink2

        // When
        val result = categoryProcessor.process(category)

        // Then
        assertEquals("Category", result.name)
        assertEquals(1, result.subcategories.size)
        assertEquals("Subcategory", result.subcategories[0].name)
        assertEquals(2, result.subcategories[0].links.size)
        assertEquals(processedLink1, result.subcategories[0].links[0])
        assertEquals(processedLink2, result.subcategories[0].links[1])

        coVerify(exactly = 1) { linksProcessor.process(link1) }
        coVerify(exactly = 1) { linksProcessor.process(link2) }
    }

    @Test
    fun `process should handle empty category`() = runBlocking {
        // Given
        val category = Category("Empty Category", mutableListOf())

        // When
        val result = categoryProcessor.process(category)

        // Then
        assertEquals("Empty Category", result.name)
        assertEquals(0, result.subcategories.size)
    }

    @Test
    fun `process should handle category with empty subcategories`() = runBlocking {
        // Given
        val subcategory = Subcategory("Empty Subcategory", mutableListOf())
        val category = Category("Category", mutableListOf(subcategory))

        // When
        val result = categoryProcessor.process(category)

        // Then
        assertEquals("Category", result.name)
        assertEquals(1, result.subcategories.size)
        assertEquals("Empty Subcategory", result.subcategories[0].name)
        assertEquals(0, result.subcategories[0].links.size)
    }

    @Test
    fun `process should handle exception in linksProcessor`() = runBlocking {
        // Given
        val link = Link("Faulty Link", "http://example.com", "Description")
        val subcategory = Subcategory("Subcategory", mutableListOf(link))
        val category = Category("Category", mutableListOf(subcategory))

        coEvery { linksProcessor.process(link) } throws RuntimeException("Processing error")

        // When/Then
        assertThrows<RuntimeException> {
            runBlocking {
                categoryProcessor.process(category)
            }
        }

        coVerify(exactly = 1) { linksProcessor.process(link) }
    }

    @Test
    fun `process should handle null values in links`() = runBlocking {
        // Given
        val link = Link(null, null, null)
        val subcategory = Subcategory("Subcategory", mutableListOf(link))
        val category = Category("Category", mutableListOf(subcategory))

        val processedLink = link.copy(name = "Processed Link")
        coEvery { linksProcessor.process(link) } returns processedLink

        // When
        val result = categoryProcessor.process(category)

        // Then
        assertEquals("Category", result.name)
        assertEquals(1, result.subcategories.size)
        assertEquals("Subcategory", result.subcategories[0].name)
        assertEquals(1, result.subcategories[0].links.size)
        assertEquals(processedLink, result.subcategories[0].links[0])

        coVerify(exactly = 1) { linksProcessor.process(link) }
    }

//    @Test
//    fun `default should create ParallelCategoryProcessor`() {
//        // Given
//        val linksProcessor = mockk<LinksProcessor>()
//
//        // When
//        val processor = CategoryProcessor.default(linksProcessor)
//
//        // Then
//        assert(processor is ParallelCategoryProcessor)
//    }
}
