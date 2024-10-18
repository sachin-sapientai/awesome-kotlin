
package link.kotlin.scripts.model

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import link.kotlin.scripts.dsl.Category
import link.kotlin.scripts.dsl.PlatformType
import link.kotlin.scripts.dsl.Subcategory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

@ExperimentalCoroutinesApi
class DtoSapientGeneratedTest {

    @Test
    fun `LinkDto creation with all fields`() {
        val linkDto = LinkDto(
            name = "Test Link",
            href = "https://test.com",
            desc = "Test description",
            platforms = listOf(PlatformType.JVM, PlatformType.JS),
            tags = setOf("test", "kotlin"),
            star = 5,
            update = "2023-05-01",
            state = LinkStateDto.AWESOME
        )

        assertEquals("Test Link", linkDto.name)
        assertEquals("https://test.com", linkDto.href)
        assertEquals("Test description", linkDto.desc)
        assertEquals(listOf(PlatformType.JVM, PlatformType.JS), linkDto.platforms)
        assertEquals(setOf("test", "kotlin"), linkDto.tags)
        assertEquals(5, linkDto.star)
        assertEquals("2023-05-01", linkDto.update)
        assertEquals(LinkStateDto.AWESOME, linkDto.state)
    }

    @Test
    fun `LinkDto creation with minimal fields`() {
        val linkDto = LinkDto(
            name = "Minimal Link",
            href = "https://minimal.com",
            desc = "",
            platforms = emptyList(),
            tags = emptySet(),
            state = LinkStateDto.DEFAULT
        )

        assertEquals("Minimal Link", linkDto.name)
        assertEquals("https://minimal.com", linkDto.href)
        assertEquals("", linkDto.desc)
        assertTrue(linkDto.platforms.isEmpty())
        assertTrue(linkDto.tags.isEmpty())
        assertNull(linkDto.star)
        assertNull(linkDto.update)
        assertEquals(LinkStateDto.DEFAULT, linkDto.state)
    }

    @ParameterizedTest
    @EnumSource(LinkStateDto::class)
    fun `LinkStateDto enum values`(state: LinkStateDto) {
        assertTrue(LinkStateDto.values().contains(state))
    }

    @Test
    fun `CategoryDto creation and conversion`() {
        val subcategory1 = mockk<SubcategoryDto>()
        val subcategory2 = mockk<SubcategoryDto>()
        every { subcategory1.name } returns "Subcategory1"
        every { subcategory2.name } returns "Subcategory2"

        val categoryDto = CategoryDto("Test Category", listOf(subcategory1, subcategory2))

        assertEquals("Test Category", categoryDto.name)
        assertEquals(2, categoryDto.subcategories.size)
        assertEquals("Subcategory1", categoryDto.subcategories[0].name)
        assertEquals("Subcategory2", categoryDto.subcategories[1].name)
    }

    @Test
    fun `Category toDto conversion with empty subcategories`() {
        val category = Category("Empty Category", mutableListOf())
        val categoryDto = category.toDto()

        assertEquals("Empty Category", categoryDto.name)
        assertTrue(categoryDto.subcategories.isEmpty())
    }

    @Test
    fun `SubcategoryDto creation`() {
        val linkDto1 = mockk<LinkDto>()
        val linkDto2 = mockk<LinkDto>()

        val subcategoryDto = SubcategoryDto("Test Subcategory", listOf(linkDto1, linkDto2))

        assertEquals("Test Subcategory", subcategoryDto.name)
        assertEquals(2, subcategoryDto.links.size)
    }
}
