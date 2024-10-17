
package link.kotlin.scripts.dsl

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate
import link.kotlin.scripts.model.Link

class DSLSapientGeneratedTest {

    @Test
    fun testCategoryCreation() {
        val category = category("Test Category") {
            subcategory("Test Subcategory") {
                link {
                    name = "Test Link"
                    href = "https://example.com"
                }
            }
        }

        assertEquals("Test Category", category.name)
        assertEquals(1, category.subcategories.size)
        assertEquals("Test Subcategory", category.subcategories[0].name)
        assertEquals(1, category.subcategories[0].links.size)
        assertEquals("Test Link", category.subcategories[0].links[0].name)
        assertEquals("https://example.com", category.subcategories[0].links[0].href)
    }

    @Test
    fun testSubcategoryAddition() {
        val category = Category("Test Category", mutableListOf())
        category.subcategory("Test Subcategory") {
            link {
                name = "Test Link"
                href = "https://example.com"
            }
        }

        assertEquals(1, category.subcategories.size)
        assertEquals("Test Subcategory", category.subcategories[0].name)
        assertEquals(1, category.subcategories[0].links.size)
    }

    @Test
    fun testLinkAddition() {
        val subcategory = Subcategory("Test Subcategory", mutableListOf())
        subcategory.link {
            name = "Test Link"
            href = "https://example.com"
            desc = "Test Description"
            github = "test/repo"
            awesome()
            setPlatforms(PlatformType.ANDROID, PlatformType.IOS)
            setTags("kotlin", "test")
        }

        assertEquals(1, subcategory.links.size)
        val link = subcategory.links[0]
        assertEquals("Test Link", link.name)
        assertEquals("https://example.com", link.href)
        assertEquals("Test Description", link.desc)
        assertEquals("test/repo", link.github)
        assertTrue(link.awesome)
        assertEquals(listOf(PlatformType.ANDROID, PlatformType.IOS), link.platforms)
        assertEquals(listOf("kotlin", "test"), link.tags)
    }

    @Test
    fun testLinkBuilderToLink() {
        val linkBuilder = LinkBuilder().apply {
            name = "Test Link"
            href = "https://example.com"
            desc = "Test Description"
            github = "test/repo"
            bitbucket = "test/repo"
            kug = "test-kug"
            awesome()
            setPlatforms(PlatformType.JVM)
            setTags("kotlin")
        }

        val link = linkBuilder.toLink()

        assertEquals("Test Link", link.name)
        assertEquals("https://example.com", link.href)
        assertEquals("Test Description", link.desc)
        assertEquals("test/repo", link.github)
        assertEquals("test/repo", link.bitbucket)
        assertEquals("test-kug", link.kug)
        assertTrue(link.awesome)
        assertEquals(listOf(PlatformType.JVM), link.platforms)
        assertEquals(listOf("kotlin"), link.tags)
    }

    @ParameterizedTest
    @EnumSource(PlatformType::class)
    fun testPlatformTypes(platform: PlatformType) {
        val linkBuilder = LinkBuilder()
        linkBuilder.setPlatforms(platform)
        val link = linkBuilder.toLink()
        assertEquals(listOf(platform), link.platforms)
    }

    @Test
    fun testArticleCreation() {
        val article = Article(
            title = "Test Article",
            url = "https://example.com/article",
            body = "Test body",
            author = "Test Author",
            date = LocalDate.now(),
            type = LinkType.article,
            categories = listOf("Kotlin", "Testing"),
            features = listOf(ArticleFeature.mathjax, ArticleFeature.highlightjs),
            description = "Test description",
            filename = "test-article.md",
            lang = LanguageCodes.EN,
            enclosure = Enclosure("https://example.com/video.mp4", 1024)
        )

        assertEquals("Test Article", article.title)
        assertEquals("https://example.com/article", article.url)
        assertEquals("Test body", article.body)
        assertEquals("Test Author", article.author)
        assertEquals(LocalDate.now(), article.date)
        assertEquals(LinkType.article, article.type)
        assertEquals(listOf("Kotlin", "Testing"), article.categories)
        assertEquals(listOf(ArticleFeature.mathjax, ArticleFeature.highlightjs), article.features)
        assertEquals("Test description", article.description)
        assertEquals("test-article.md", article.filename)
        assertEquals(LanguageCodes.EN, article.lang)
        assertNotNull(article.enclosure)
        assertEquals("https://example.com/video.mp4", article.enclosure?.url)
        assertEquals(1024, article.enclosure?.size)
    }

    @ParameterizedTest
    @EnumSource(LanguageCodes::class)
    fun testLanguageCodesContains(language: LanguageCodes) {
        assertTrue(LanguageCodes.contains(language.id))
    }

    @ParameterizedTest
    @ValueSource(strings = ["english", "russian", "italian", "chinese", "hebrew"])
    fun testLanguageCodesContainsValidLanguages(language: String) {
        assertTrue(LanguageCodes.contains(language))
    }

    @Test
    fun testLanguageCodesContainsInvalidLanguage() {
        assertFalse(LanguageCodes.contains("invalid"))
    }
}
