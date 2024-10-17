//
//package link.kotlin.scripts.model
//
//import io.mockk.every
//import io.mockk.mockk
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import link.kotlin.scripts.dsl.Category
//import link.kotlin.scripts.dsl.Link
//import link.kotlin.scripts.dsl.PlatformType
//import link.kotlin.scripts.dsl.Subcategory
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.EnumSource
//import org.junit.jupiter.params.provider.ValueSource
//
//@ExperimentalCoroutinesApi
//class DtoSapientGeneratedTest {
//
//    @Test
//    fun `LinkDto creation with all fields`() {
//        val linkDto = LinkDto(
//            name = "Test Link",
//            href = "https://test.com",
//            desc = "Test description",
//            platforms = listOf(PlatformType.JVM, PlatformType.JS),
//            tags = setOf("test", "kotlin"),
//            star = 5,
//            update = "2023-05-01",
//            state = LinkStateDto.AWESOME
//        )
//
//        assertEquals("Test Link", linkDto.name)
//        assertEquals("https://test.com", linkDto.href)
//        assertEquals("Test description", linkDto.desc)
//        assertEquals(listOf(PlatformType.JVM, PlatformType.JS), linkDto.platforms)
//        assertEquals(setOf("test", "kotlin"), linkDto.tags)
//        assertEquals(5, linkDto.star)
//        assertEquals("2023-05-01", linkDto.update)
//        assertEquals(LinkStateDto.AWESOME, linkDto.state)
//    }
//
//    @Test
//    fun `LinkDto creation with minimal fields`() {
//        val linkDto = LinkDto(
//            name = "Minimal Link",
//            href = "https://minimal.com",
//            desc = "",
//            platforms = emptyList(),
//            tags = emptySet(),
//            state = LinkStateDto.DEFAULT
//        )
//
//        assertEquals("Minimal Link", linkDto.name)
//        assertEquals("https://minimal.com", linkDto.href)
//        assertEquals("", linkDto.desc)
//        assertTrue(linkDto.platforms.isEmpty())
//        assertTrue(linkDto.tags.isEmpty())
//        assertNull(linkDto.star)
//        assertNull(linkDto.update)
//        assertEquals(LinkStateDto.DEFAULT, linkDto.state)
//    }
//
//    @ParameterizedTest
//    @EnumSource(LinkStateDto::class)
//    fun `LinkStateDto enum values`(state: LinkStateDto) {
//        assertTrue(LinkStateDto.values().contains(state))
//    }
//
////    @Test
////    fun `Link toDto conversion`() {
////        val link = Link(
////            name = "Test Link",
////            href = "https://test.com",
////            desc = "Test description",
////            platforms = listOf(PlatformType.ANDROID),
////            tags = listOf("android", "kotlin"),
////            awesome = true
////        )
////
////        val linkDto = link.toDto()
////
////        assertEquals("Test Link", linkDto.name)
////        assertEquals("https://test.com", linkDto.href)
////        assertEquals("Test description", linkDto.desc)
////        assertEquals(listOf(PlatformType.ANDROID), linkDto.platforms)
////        assertEquals(setOf("android", "kotlin"), linkDto.tags)
////        assertEquals(LinkStateDto.AWESOME, linkDto.state)
////    }
//
////    @Test
////    fun `Link toDto conversion with null name throws error`() {
////        val link = Link(
////            name = null,
////            href = "https://test.com",
////            desc = "Test description",
////            platforms = emptyList(),
////            tags = emptyList()
////        )
////
////        assertThrows(IllegalStateException::class.java) {
////            link.toDto()
////        }
////    }
////
////    @Test
////    fun `Link toDto conversion with null href throws error`() {
////        val link = Link(
////            name = "Test Link",
////            href = null,
////            desc = "Test description",
////            platforms = emptyList(),
////            tags = emptyList()
////        )
////
////        assertThrows(IllegalStateException::class.java) {
////            link.toDto()
////        }
////    }
////
////    @ParameterizedTest
////    @ValueSource(booleans = [true, false])
////    fun `Link toDto conversion with different states`(isArchived: Boolean) {
////        val link = Link(
////            name = "Test Link",
////            href = "https://test.com",
////            desc = "Test description",
////            platforms = emptyList(),
////            tags = emptyList(),
////            archived = isArchived
////        )
////
////        val linkDto = link.toDto()
////
////        assertEquals(if (isArchived) LinkStateDto.ARCHIVED else LinkStateDto.DEFAULT, linkDto.state)
////    }
////
////    @Test
////    fun `SubcategoryDto creation and conversion`() {
////        val link1 = mockk<Link>()
////        val link2 = mockk<Link>()
////        every { link1.toDto() } returns LinkDto("Link1", "https://link1.com", "", emptyList(), emptySet(), state = LinkStateDto.DEFAULT)
////        every { link2.toDto() } returns LinkDto("Link2", "https://link2.com", "", emptyList(), emptySet(), state = LinkStateDto.DEFAULT)
////
////        val subcategory = Subcategory("Test Subcategory", mutableListOf(link1, link2))
////        val subcategoryDto = subcategory.toDto()
////
////        assertEquals("Test Subcategory", subcategoryDto.name)
////        assertEquals(2, subcategoryDto.links.size)
////        assertEquals("Link1", subcategoryDto.links[0].name)
////        assertEquals("Link2", subcategoryDto.links[1].name)
////    }
////
////    @Test
////    fun `CategoryDto creation and conversion`() {
////        val subcategory1 = mockk<Subcategory>()
////        val subcategory2 = mockk<Subcategory>()
////        every { subcategory1.toDto() } returns SubcategoryDto("Subcategory1", emptyList())
////        every { subcategory2.toDto() } returns SubcategoryDto("Subcategory2", emptyList())
////
////        val category = Category("Test Category", mutableListOf(subcategory1, subcategory2))
////        val categoryDto = category.toDto()
////
////        assertEquals("Test Category", categoryDto.name)
////        assertEquals(2, categoryDto.subcategories.size)
////        assertEquals("Subcategory1", categoryDto.subcategories[0].name)
////        assertEquals("Subcategory2", categoryDto.subcategories[1].name)
////    }
//
//    @Test
//    fun `Category toDto conversion with empty subcategories`() {
//        val category = Category("Empty Category", mutableListOf())
//        val categoryDto = category.toDto()
//
//        assertEquals("Empty Category", categoryDto.name)
//        assertTrue(categoryDto.subcategories.isEmpty())
//    }
//
//    @Test
//    fun `Subcategory toDto conversion with empty links`() {
//        val subcategory = Subcategory("Empty Subcategory", mutableListOf())
//        val subcategoryDto = subcategory.toDto()
//
//        assertEquals("Empty Subcategory", subcategoryDto.name)
//        assertTrue(subcategoryDto.links.isEmpty())
//    }
//
//    @Test
//    fun `Link toDto conversion handles all state combinations`() = runTest {
//        val testCases = listOf(
//            Triple(true, false, false) to LinkStateDto.AWESOME,
//            Triple(false, true, false) to LinkStateDto.ARCHIVED,
//            Triple(false, false, true) to LinkStateDto.UNSUPPORTED,
//            Triple(false, false, false) to LinkStateDto.DEFAULT
//        )
//
//        testCases.forEach { (states, expectedState) ->
//            val (awesome, archived, unsupported) = states
//            val link = Link(
//                name = "Test Link",
//                href = "https://test.com",
//                desc = "Test description",
//                platforms = emptyList(),
//                tags = emptyList(),
//                awesome = awesome,
//                archived = archived,
//                unsupported = unsupported
//            )
//
//            val linkDto = link.toDto()
//            assertEquals(expectedState, linkDto.state)
//        }
//    }
//}
