package link.kotlin.scripts.dsl

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.property.checkAll
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.string
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import link.kotlin.scripts.model.Link
import java.time.LocalDate

class DSLSapientGeneratedTest : StringSpec({

    "PlatformType enum should have correct values" {
        PlatformType.values().map { it.name } shouldContainExactly listOf(
            "ANDROID", "COMMON", "IOS", "JS", "JVM", "NATIVE", "WASM"
        )
    }

    "Subcategory should add links correctly" {
        val subcategory = Subcategory("Test", mutableListOf())
        val link = Link("Test Link", "http://test.com", "", emptyList(), emptyList())
        subcategory.apply { +link }
        subcategory.links shouldContainExactly listOf(link)
    }

    "Category should add subcategories correctly" {
        val category = Category("Test", mutableListOf())
        val subcategory = Subcategory("Sub", mutableListOf())
        category.apply { +subcategory }
        category.subcategories shouldContainExactly listOf(subcategory)
    }

    "category DSL function should create Category correctly" {
        val result = category("Test") {
            subcategory("Sub") {
                link {
                    name = "Link"
                    href = "http://test.com"
                }
            }
        }
        result.name shouldBe "Test"
        result.subcategories.size shouldBe 1
        result.subcategories[0].name shouldBe "Sub"
        result.subcategories[0].links.size shouldBe 1
        result.subcategories[0].links[0].name shouldBe "Link"
    }

    "LinkBuilder should build Link correctly" {
        val builder = LinkBuilder().apply {
            name = "Test"
            href = "http://test.com"
            desc = "Description"
            github = "github"
            bitbucket = "bitbucket"
            kug = "kug"
            awesome()
            setPlatforms(PlatformType.ANDROID, PlatformType.IOS)
            setTags("tag1", "tag2")
        }
        val link = builder.toLink()
        link.name shouldBe "Test"
        link.href shouldBe "http://test.com"
        link.desc shouldBe "Description"
        link.github shouldBe "github"
        link.bitbucket shouldBe "bitbucket"
        link.kug shouldBe "kug"
        link.awesome shouldBe true
        link.platforms shouldContainExactly listOf(PlatformType.ANDROID, PlatformType.IOS)
        link.tags shouldContainExactly listOf("tag1", "tag2")
    }

    "Article should be created correctly with default values" {
        val article = Article(
            title = "Test",
            url = "http://test.com",
            body = "Body",
            author = "Author",
            date = LocalDate.now(),
            type = LinkType.article
        )
        article.title shouldBe "Test"
        article.url shouldBe "http://test.com"
        article.body shouldBe "Body"
        article.author shouldBe "Author"
        article.type shouldBe LinkType.article
        article.categories shouldBe emptyList()
        article.features shouldContainExactly listOf(ArticleFeature.highlightjs)
        article.description shouldBe ""
        article.filename shouldBe ""
        article.lang shouldBe LanguageCodes.EN
        article.enclosure shouldBe null
    }

    "LanguageCodes.contains should work correctly" {
        LanguageCodes.contains("english") shouldBe true
        LanguageCodes.contains("french") shouldBe false
    }

    "LinkType enum should have correct values" {
        LinkType.values().map { it.name } shouldContainExactly listOf(
            "article", "video", "slides", "webinar"
        )
    }

    "ArticleFeature enum should have correct values" {
        ArticleFeature.values().map { it.name } shouldContainExactly listOf(
            "mathjax", "highlightjs"
        )
    }

    "Enclosure data class should be created correctly" {
        val enclosure = Enclosure("http://test.com", 100)
        enclosure.url shouldBe "http://test.com"
        enclosure.size shouldBe 100
    }

    "LanguageCodes enum should have correct values and ids" {
        checkAll(arbitrary { LanguageCodes.values().random() }) { languageCode ->
            when (languageCode) {
                LanguageCodes.EN -> languageCode.id shouldBe "english"
                LanguageCodes.RU -> languageCode.id shouldBe "russian"
                LanguageCodes.IT -> languageCode.id shouldBe "italian"
                LanguageCodes.ZH -> languageCode.id shouldBe "chinese"
                LanguageCodes.HE -> languageCode.id shouldBe "hebrew"
            }
        }
    }
})