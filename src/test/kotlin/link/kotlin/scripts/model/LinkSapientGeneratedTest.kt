package link.kotlin.scripts.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import link.kotlin.scripts.dsl.PlatformType

class LinkSapientGeneratedTest : FunSpec({

    test("Link data class should have correct default values") {
        val link = Link()
        link.name shouldBe null
        link.github shouldBe null
        link.bitbucket shouldBe null
        link.kug shouldBe null
        link.href shouldBe null
        link.desc shouldBe null
        link.platforms shouldBe emptyList()
        link.tags shouldBe emptyList()
        link.star shouldBe null
        link.update shouldBe null
        link.archived shouldBe false
        link.unsupported shouldBe false
        link.awesome shouldBe false
    }

    test("Link data class should correctly set and get all properties") {
        val link = Link(
            name = "Test Link",
            github = "https://github.com/test",
            bitbucket = "https://bitbucket.org/test",
            kug = "Test KUG",
            href = "https://test.com",
            desc = "Test description",
            platforms = listOf(PlatformType.ANDROID, PlatformType.IOS),
            tags = listOf("test", "kotlin"),
            star = 100,
            update = "2023-05-20",
            archived = true,
            unsupported = true,
            awesome = true
        )

        link.name shouldBe "Test Link"
        link.github shouldBe "https://github.com/test"
        link.bitbucket shouldBe "https://bitbucket.org/test"
        link.kug shouldBe "Test KUG"
        link.href shouldBe "https://test.com"
        link.desc shouldBe "Test description"
        link.platforms shouldBe listOf(PlatformType.ANDROID, PlatformType.IOS)
        link.tags shouldBe listOf("test", "kotlin")
        link.star shouldBe 100
        link.update shouldBe "2023-05-20"
        link.archived shouldBe true
        link.unsupported shouldBe true
        link.awesome shouldBe true
    }

    test("Link data class should support null values for nullable properties") {
        val link = Link(
            name = null,
            github = null,
            bitbucket = null,
            kug = null,
            href = null,
            desc = null,
            star = null,
            update = null
        )

        link.name shouldBe null
        link.github shouldBe null
        link.bitbucket shouldBe null
        link.kug shouldBe null
        link.href shouldBe null
        link.desc shouldBe null
        link.star shouldBe null
        link.update shouldBe null
    }

    test("Link data class should correctly handle empty lists") {
        val link = Link(platforms = emptyList(), tags = emptyList())
        link.platforms shouldBe emptyList()
        link.tags shouldBe emptyList()
    }

    test("Link data class should correctly compare equality") {
        val link1 = Link(name = "Test", github = "https://github.com/test")
        val link2 = Link(name = "Test", github = "https://github.com/test")
        val link3 = Link(name = "Different", github = "https://github.com/different")

        link1 shouldBe link2
        link1 shouldBe link1
        link1 shouldBe link1.copy()
        link1 shouldBe link2.copy()
        link1 shouldBe link2
        link1 should { it != link3 }
    }

    test("Link data class should produce correct hash code") {
        val link1 = Link(name = "Test", github = "https://github.com/test")
        val link2 = Link(name = "Test", github = "https://github.com/test")

        link1.hashCode() shouldBe link2.hashCode()
    }

    test("Link data class toString should contain all properties") {
        val link = Link(
            name = "Test Link",
            github = "https://github.com/test",
            bitbucket = "https://bitbucket.org/test",
            kug = "Test KUG",
            href = "https://test.com",
            desc = "Test description",
            platforms = listOf(PlatformType.ANDROID),
            tags = listOf("test"),
            star = 100,
            update = "2023-05-20",
            archived = true,
            unsupported = true,
            awesome = true
        )

        val toStringResult = link.toString()

        toStringResult should { it.contains("name=Test Link") }
        toStringResult should { it.contains("github=https://github.com/test") }
        toStringResult should { it.contains("bitbucket=https://bitbucket.org/test") }
        toStringResult should { it.contains("kug=Test KUG") }
        toStringResult should { it.contains("href=https://test.com") }
        toStringResult should { it.contains("desc=Test description") }
        toStringResult should { it.contains("platforms=[ANDROID]") }
        toStringResult should { it.contains("tags=[test]") }
        toStringResult should { it.contains("star=100") }
        toStringResult should { it.contains("update=2023-05-20") }
        toStringResult should { it.contains("archived=true") }
        toStringResult should { it.contains("unsupported=true") }
        toStringResult should { it.contains("awesome=true") }
    }

    test("Link data class should work with copy function") {
        val originalLink = Link(name = "Original", github = "https://github.com/original")
        val copiedLink = originalLink.copy(name = "Copied")

        copiedLink.name shouldBe "Copied"
        copiedLink.github shouldBe "https://github.com/original"
    }

    test("Link data class should handle all PlatformType values") {
        val allPlatforms = PlatformType.values().toList()
        val link = Link(platforms = allPlatforms)

        link.platforms shouldBe allPlatforms
    }

    test("Property-based testing for Link data class") {
        checkAll(
            Arb.string(),
            Arb.string(),
            Arb.string(),
            Arb.string(),
            Arb.string(),
            Arb.string(),
            Arb.list(Arb.enum<PlatformType>()),
            Arb.list(Arb.string()),
            Arb.int(),
            Arb.string(),
            Arb.boolean(),
            Arb.boolean(),
            Arb.boolean()
        ) { name, github, bitbucket, kug, href, desc, platforms, tags, star, update, archived, unsupported, awesome ->
            val link = Link(name, github, bitbucket, kug, href, desc, platforms, tags, star, update, archived, unsupported, awesome)

            link.name shouldBe name
            link.github shouldBe github
            link.bitbucket shouldBe bitbucket
            link.kug shouldBe kug
            link.href shouldBe href
            link.desc shouldBe desc
            link.platforms shouldBe platforms
            link.tags shouldBe tags
            link.star shouldBe star
            link.update shouldBe update
            link.archived shouldBe archived
            link.unsupported shouldBe unsupported
            link.awesome shouldBe awesome
        }
    }
})