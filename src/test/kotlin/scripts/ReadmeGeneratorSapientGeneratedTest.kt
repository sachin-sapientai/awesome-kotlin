
package link.kotlin.scripts

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import link.kotlin.scripts.dsl.Category
import link.kotlin.scripts.dsl.Subcategory
import link.kotlin.scripts.model.Link
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class ReadmeGeneratorSapientGeneratedTest {

    private lateinit var readmeGenerator: ReadmeGenerator

    @BeforeEach
    fun setUp() {
        readmeGenerator = ReadmeGenerator.default()
    }

    @Test
    fun `generate should return correct markdown for empty list`() {
        val result = readmeGenerator.generate(emptyList())
        assertTrue(result.contains("# Awesome Kotlin"))
        assertTrue(result.contains("A curated list of awesome Kotlin related stuff"))
        assertFalse(result.contains("Kotlin User Groups"))
    }

    @Test
    fun `generate should filter out Kotlin User Groups category`() {
        val categories = listOf(
            Category("Kotlin User Groups", mutableListOf()),
            Category("Libraries", mutableListOf())
        )
        val result = readmeGenerator.generate(categories)
        assertFalse(result.contains("Kotlin User Groups"))
        assertTrue(result.contains("Libraries"))
    }

//    @Test
//    fun `generate should include table of contents and links`() {
//        val categories = listOf(
//            Category("Libraries", mutableListOf(
//                Subcategory("Web", mutableListOf(
//                    Link("Ktor", "https://ktor.io", "Web framework", emptyList(), emptyList(), null, null, null, false)
//                ))
//            ))
//        )
//        val result = readmeGenerator.generate(categories)
//        assertTrue(result.contains("## Table of Contents"))
//        assertTrue(result.contains("### Libraries"))
//        assertTrue(result.contains("* Web"))
//        assertTrue(result.contains("## Libraries"))
//        assertTrue(result.contains("### Web"))
//        assertTrue(result.contains("* [Ktor](https://ktor.io) - Web framework"))
//    }

    @ParameterizedTest
    @CsvSource(
        "Test Name, test-name",
        "Test, Name, test-name",
        "Test/Name, test-name",
        "Test\\Name, test-name",
        "Test  Name, test-name"
    )
    fun `normalizeName should handle various input correctly`(input: String, expected: String) {
        assertEquals(expected, normalizeName(input))
    }

    @Test
    fun `getAnchor should return correct anchor tag`() {
        assertEquals("<a name=\"test-name\"></a>", getAnchor("Test Name"))
    }

    @Test
    fun `link should return correct markdown link`() {
        assertEquals("[Test](#test)", link("Test", "test"))
    }

    @Test
    fun `getCategoryName should return correct category header`() {
        val result = getCategoryName("Libraries")
        assertTrue(result.startsWith("## <a name=\"libraries\"></a>Libraries"))
        assertTrue(result.contains("Back ⇈"))
    }

    @Test
    fun `getSubcategoryName should return correct subcategory header`() {
        val result = getSubcategoryName("Web", "Libraries")
        assertTrue(result.startsWith("### <a name=\"libraries-web\"></a>Web"))
        assertTrue(result.contains("Back ⇈"))
    }

    @Test
    fun `getTocCategoryName should return correct TOC category entry`() {
        assertEquals("### <a name=\"libraries-category\"></a>[Libraries](#libraries)", getTocCategoryName("Libraries"))
    }

    @Test
    fun `getTocSubcategoryName should return correct TOC subcategory entry`() {
        assertEquals("* <a name=\"libraries-web-subcategory\"></a>[Web](#libraries-web)", getTocSubcategoryName("Web", "Libraries"))
    }

    @Test
    fun `tableOfContent should generate correct TOC structure`() {
        val categories = listOf(
            Category("Libraries", mutableListOf(
                Subcategory("Web", mutableListOf()),
                Subcategory("Testing", mutableListOf())
            ))
        )
        val result = tableOfContent(categories)
        assertTrue(result.contains("### <a name=\"libraries-category\"></a>[Libraries](#libraries)"))
        assertTrue(result.contains("* <a name=\"libraries-web-subcategory\"></a>[Web](#libraries-web)"))
        assertTrue(result.contains("* <a name=\"libraries-testing-subcategory\"></a>[Testing](#libraries-testing)"))
    }

//    @Test
//    fun `getLinks should generate correct links structure`() {
//        val categories = listOf(
//            Category("Libraries", mutableListOf(
//                Subcategory("Web", mutableListOf(
//                    Link("Ktor", "https://ktor.io", "Web framework", emptyList(), emptyList(), null, null, null, false)
//                ))
//            ))
//        )
//        val result = getLinks(categories)
//        assertTrue(result.contains("## <a name=\"libraries\"></a>Libraries"))
//        assertTrue(result.contains("### <a name=\"libraries-web\"></a>Web"))
//        assertTrue(result.contains("* [Ktor](https://ktor.io) - Web framework"))
//    }
//
//    @Test
//    fun `getLinks should not include archived links`() {
//        val categories = listOf(
//            Category("Libraries", mutableListOf(
//                Subcategory("Web", mutableListOf(
//                    Link("Ktor", "https://ktor.io", "Web framework", emptyList(), emptyList(), null, null, null, false),
//                    Link("Old Framework", "https://old.com", "Archived framework", emptyList(), emptyList(), null, null, null, true)
//                ))
//            ))
//        )
//        val result = getLinks(categories)
//        assertTrue(result.contains("* [Ktor](https://ktor.io) - Web framework"))
//        assertFalse(result.contains("* [Old Framework](https://old.com) - Archived framework"))
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = ["", "<p>Description</p>\n", "Simple description"])
//    fun `getLinks should handle different description formats`(description: String) {
//        val categories = listOf(
//            Category("Libraries", mutableListOf(
//                Subcategory("Web", mutableListOf(
//                    Link("Ktor", "https://ktor.io", description, emptyList(), emptyList(), null, null, null, false)
//                ))
//            ))
//        )
//        val result = getLinks(categories)
//        if (description.isEmpty()) {
//            assertTrue(result.contains("* [Ktor](https://ktor.io)"))
//        } else {
//            assertTrue(result.contains("* [Ktor](https://ktor.io) - ${description.removePrefix("<p>").removeSuffix("</p>\n")}"))
//        }
//    }
//
//    @Test
//    fun `generateReadme should produce complete README content`() {
//        val categories = listOf(
//            Category("Libraries", mutableListOf(
//                Subcategory("Web", mutableListOf(
//                    Link("Ktor", "https://ktor.io", "Web framework", emptyList(), emptyList(), null, null, null, false)
//                ))
//            ))
//        )
//        val result = generateReadme(categories)
//        assertTrue(result.contains("# Awesome Kotlin"))
//        assertTrue(result.contains("## Table of Contents"))
//        assertTrue(result.contains("### Libraries"))
//        assertTrue(result.contains("* [Ktor](https://ktor.io) - Web framework"))
//        assertTrue(result.contains("[![CC0]"))
//    }
}
