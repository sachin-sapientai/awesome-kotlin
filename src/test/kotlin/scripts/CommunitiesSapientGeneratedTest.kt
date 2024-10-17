
package link.kotlin.scripts.import

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.commonmark.node.*
import org.commonmark.parser.Parser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CommunitiesSapientGeneratedTest {

    @BeforeEach
    fun setUp() {
        mockkStatic(Files::class)
        mockkStatic(Paths::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test convertToAwesomeLink with valid input`() = runBlocking {
        val mockContent = """
            # Europe

            * [Kotlin User Group Vienna](https://www.meetup.com/Kotlin-User-Group-Vienna/), Austria
            * [Belgium Kotlin User Group](https://www.meetup.com/Belgium-Kotlin-User-Group/), Belgium
        """.trimIndent()

        every { Files.readString(any()) } returns mockContent
        every { Paths.get(any<String>()) } returns mockk()

        convertToAwesomeLink()

        verify { Files.readString(any()) }
        verify { Paths.get("src/main/kotlin/link/kotlin/scripts/import/user-group-list.md") }
    }

    @Test
    fun `test convertToAwesomeLink with empty input`() = runBlocking {
        every { Files.readString(any()) } returns ""
        every { Paths.get(any<String>()) } returns mockk()

        convertToAwesomeLink()

        verify { Files.readString(any()) }
        verify { Paths.get("src/main/kotlin/link/kotlin/scripts/import/user-group-list.md") }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "# Europe\n\n* [Kotlin User Group Vienna](https://www.meetup.com/Kotlin-User-Group-Vienna/), Austria",
        "# Asia\n\n* [Japan Kotlin User Group](https://kotlin.connpass.com/), Japan",
        "# North America\n\n* [New York Kotlin Meetup](https://www.meetup.com/New-York-Kotlin-Meetup/), USA"
    ])
    fun `test convertToAwesomeLink with different regions`(input: String) = runBlocking {
        every { Files.readString(any()) } returns input
        every { Paths.get(any<String>()) } returns mockk()

        convertToAwesomeLink()

        verify { Files.readString(any()) }
        verify { Paths.get("src/main/kotlin/link/kotlin/scripts/import/user-group-list.md") }
    }

//    @Test
//    fun `test Parser creation and parsing`() {
//        val parser = Parser.builder().build()
//        assertNotNull(parser)
//
//        val node = parser.parse("# Test\n\n* [Link](http://example.com)")
//        assertTrue(node is Document)
//        assertEquals(2, node.childCount)
//    }

    @Test
    fun `test Document processing`() {
        val parser = Parser.builder().build()
        val document = parser.parse("""
            # Europe
            * [Kotlin User Group Vienna](https://www.meetup.com/Kotlin-User-Group-Vienna/), Austria
            # Asia
            * [Japan Kotlin User Group](https://kotlin.connpass.com/), Japan
        """.trimIndent())

        val process = mutableMapOf<Heading, BulletList>()
        var child: Node? = document.firstChild

        while (child != null) {
            if (child is Heading && child.next is BulletList) {
                process[child] = child.next as BulletList
            }
            child = child.next
        }

        assertEquals(2, process.size)
        assertTrue(process.keys.all { it is Heading })
        assertTrue(process.values.all { it is BulletList })
    }

    @Test
    fun `test StringBuilder content generation`() {
        val parser = Parser.builder().build()
        val document = parser.parse("""
            # Europe
            * [Kotlin User Group Vienna](https://www.meetup.com/Kotlin-User-Group-Vienna/), Austria
        """.trimIndent())

        val process = mutableMapOf<Heading, BulletList>()
        var child: Node? = document.firstChild

        while (child != null) {
            if (child is Heading && child.next is BulletList) {
                process[child] = child.next as BulletList
            }
            child = child.next
        }

        val categoryBuilder = StringBuilder()
        categoryBuilder.append("""
            // Don't edit manually, check Communities.kt
            category("Kotlin User Groups") {
        """.trimIndent())
        categoryBuilder.append("\n")

        process.forEach { (heading, bulletList) ->
            val subcategory = (heading.firstChild as Text).literal
            val subcategoryBuilder = StringBuilder()
            subcategoryBuilder.append("""  subcategory("$subcategory") {""")
            subcategoryBuilder.append("\n")

            var item: Node? = bulletList.firstChild
            while (item != null) {
                val paragraph = (item as ListItem).firstChild as Paragraph
                val link = paragraph.firstChild as Link
                val text = (link.firstChild as Text).literal.replace("\"", "\\\"")
                val href = link.destination
                val description = (paragraph.lastChild as Text).literal.removePrefix(", ")

                subcategoryBuilder.append("""
                    |    link {
                    |      kug = "$text"
                    |      desc = "$description"
                    |      href = "$href"
                    |      setTags("$description")
                    |    }
                    |
                """.trimMargin())

                item = item.next
            }
            subcategoryBuilder.append("  }\n")
            categoryBuilder.append(subcategoryBuilder)
        }

        categoryBuilder.append("}")
        categoryBuilder.append("\n")

        val result = categoryBuilder.toString()
        assertTrue(result.contains("category(\"Kotlin User Groups\")"))
        assertTrue(result.contains("subcategory(\"Europe\")"))
        assertTrue(result.contains("kug = \"Kotlin User Group Vienna\""))
        assertTrue(result.contains("desc = \"Austria\""))
        assertTrue(result.contains("href = \"https://www.meetup.com/Kotlin-User-Group-Vienna/\""))
    }
}
