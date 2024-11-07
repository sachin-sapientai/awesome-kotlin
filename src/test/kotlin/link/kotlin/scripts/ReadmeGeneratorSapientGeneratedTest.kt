package link.kotlin.scripts

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.forAll
import link.kotlin.scripts.dsl.Category
import link.kotlin.scripts.dsl.Link
import link.kotlin.scripts.dsl.Subcategory

class ReadmeGeneratorSapientGeneratedTest : FunSpec({

    test("ReadmeGenerator.default should return MarkdownReadmeGenerator") {
        val generator = ReadmeGenerator.default()
        generator shouldBe MarkdownReadmeGenerator()
    }

    test("normalizeName should convert string to lowercase and replace special characters with dashes") {
        forAll(Arb.string()) { input ->
            val result = normalizeName(input)
            result.lowercase() == result &&
                    !result.contains(Regex("[ ,/\\\\]")) &&
                    !result.contains("--")
        }
    }

    test("getAnchor should return correct HTML anchor") {
        val name = "Test Name"
        val result = getAnchor(name)
        result shouldBe """<a name="test-name"></a>"""
    }

    test("link should return correct markdown link") {
        val name = "Test"
        val url = "Test URL"
        val result = link(name, url)
        result shouldBe "[Test](#test-url)"
    }

    test("getCategoryName should return correct category name format") {
        val name = "Category"
        val result = getCategoryName(name)
        result shouldBe """## <a name="category"></a>Category <sup>[Back ⇈](#category-category)</sup>"""
    }

    test("getSubcategoryName should return correct subcategory name format") {
        val name = "Subcategory"
        val namespace = "Namespace"
        val result = getSubcategoryName(name, namespace)
        result shouldBe """### <a name="namespace-subcategory"></a>Subcategory <sup>[Back ⇈](#namespace-subcategory-subcategory)</sup>"""
    }

    test("getTocCategoryName should return correct TOC category name format") {
        val name = "Category"
        val result = getTocCategoryName(name)
        result shouldBe """### <a name="category-category"></a>[Category](#category)"""
    }

    test("getTocSubcategoryName should return correct TOC subcategory name format") {
        val name = "Subcategory"
        val namespace = "Namespace"
        val result = getTocSubcategoryName(name, namespace)
        result shouldBe """* <a name="namespace-subcategory-subcategory"></a>[Subcategory](#namespace-subcategory)"""
    }

    test("tableOfContent should generate correct table of contents") {
        val links = listOf(
            Category("Category1", listOf(
                Subcategory("Subcategory1", emptyList()),
                Subcategory("Subcategory2", emptyList())
            )),
            Category("Category2", listOf(
                Subcategory("Subcategory3", emptyList())
            ))
        )
        val result = tableOfContent(links)
        result shouldBe """
            ### <a name="category1-category"></a>[Category1](#category1)
            * <a name="category1-subcategory1-subcategory"></a>[Subcategory1](#category1-subcategory1)
            * <a name="category1-subcategory2-subcategory"></a>[Subcategory2](#category1-subcategory2)

            ### <a name="category2-category"></a>[Category2](#category2)
            * <a name="category2-subcategory3-subcategory"></a>[Subcategory3](#category2-subcategory3)
        """.trimIndent()
    }

    test("getLinks should generate correct links structure") {
        val links = listOf(
            Category("Category1", listOf(
                Subcategory("Subcategory1", listOf(
                    Link("Link1", "http://example1.com", "Description1", star = false, archived = false),
                    Link("Link2", "http://example2.com", "Description2", star = true, archived = false)
                ))
            ))
        )
        val result = getLinks(links)
        result shouldBe """
            ## <a name="category1"></a>Category1 <sup>[Back ⇈](#category1-category)</sup>
            ### <a name="category1-subcategory1"></a>Subcategory1 <sup>[Back ⇈](#category1-subcategory1-subcategory)</sup>
            * [Link1](http://example1.com) - Description1
            * [Link2](http://example2.com) - Description2

        """.trimIndent()
    }

    test("generateReadme should produce correct README content") {
        val links = listOf(
            Category("Category1", listOf(
                Subcategory("Subcategory1", listOf(
                    Link("Link1", "http://example1.com", "Description1", star = false, archived = false)
                ))
            ))
        )
        val result = generateReadme(links)
        result.contains("# Awesome Kotlin") shouldBe true
        result.contains("## Table of Contents") shouldBe true
        result.contains("### <a name=\"category1-category\"></a>[Category1](#category1)") shouldBe true
        result.contains("* [Link1](http://example1.com) - Description1") shouldBe true
    }

    test("MarkdownReadmeGenerator should filter out 'Kotlin User Groups'") {
        val generator = MarkdownReadmeGenerator()
        val links = listOf(
            Category("Kotlin User Groups", emptyList()),
            Category("Other Category", emptyList())
        )
        val result = generator.generate(links)
        result.contains("Kotlin User Groups") shouldBe false
        result.contains("Other Category") shouldBe true
    }
})