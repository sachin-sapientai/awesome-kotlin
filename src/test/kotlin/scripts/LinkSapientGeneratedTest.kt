
package link.kotlin.scripts.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import link.kotlin.scripts.dsl.PlatformType

class LinkSapientGeneratedTest {

    @Test
    fun testLinkCreation() {
        val link = Link(
            name = "Test Link",
            github = "test/repo",
            href = "https://test.com",
            desc = "Test description",
            platforms = listOf(PlatformType.JVM, PlatformType.JS),
            tags = listOf("test", "kotlin"),
            star = 100,
            update = "2023-05-01",
            archived = false,
            unsupported = false,
            awesome = true
        )

        assertEquals("Test Link", link.name)
        assertEquals("test/repo", link.github)
        assertNull(link.bitbucket)
        assertNull(link.kug)
        assertEquals("https://test.com", link.href)
        assertEquals("Test description", link.desc)
        assertEquals(listOf(PlatformType.JVM, PlatformType.JS), link.platforms)
        assertEquals(listOf("test", "kotlin"), link.tags)
        assertEquals(100, link.star)
        assertEquals("2023-05-01", link.update)
        assertFalse(link.archived)
        assertFalse(link.unsupported)
        assertTrue(link.awesome)
    }

    @Test
    fun testLinkCreationWithNullValues() {
        val link = Link()

        assertNull(link.name)
        assertNull(link.github)
        assertNull(link.bitbucket)
        assertNull(link.kug)
        assertNull(link.href)
        assertNull(link.desc)
        assertTrue(link.platforms.isEmpty())
        assertTrue(link.tags.isEmpty())
        assertNull(link.star)
        assertNull(link.update)
        assertFalse(link.archived)
        assertFalse(link.unsupported)
        assertFalse(link.awesome)
    }
//
//    @ParameterizedTest
//    @MethodSource("provideLinkData")
//    fun testLinkDataClass(
//        name: String?,
//        github: String?,
//        bitbucket: String?,
//        kug: String?,
//        href: String?,
//        desc: String?,
//        platforms: List<PlatformType>,
//        tags: List<String>,
//        star: Int?,
//        update: String?,
//        archived: Boolean,
//        unsupported: Boolean,
//        awesome: Boolean
//    ) {
//        val link = Link(name, github, bitbucket, kug, href, desc, platforms, tags, star, update, archived, unsupported, awesome)
//
//        assertEquals(name, link.name)
//        assertEquals(github, link.github)
//        assertEquals(bitbucket, link.bitbucket)
//        assertEquals(kug, link.kug)
//        assertEquals(href, link.href)
//        assertEquals(desc, link.desc)
//        assertEquals(platforms, link.platforms)
//        assertEquals(tags, link.tags)
//        assertEquals(star, link.star)
//        assertEquals(update, link.update)
//        assertEquals(archived, link.archived)
//        assertEquals(unsupported, link.unsupported)
//        assertEquals(awesome, link.awesome)
//    }

    @Test
    fun testLinkCopy() {
        val originalLink = Link(
            name = "Original Link",
            github = "original/repo",
            href = "https://original.com",
            desc = "Original description",
            platforms = listOf(PlatformType.JVM),
            tags = listOf("original"),
            star = 50,
            update = "2023-04-01",
            archived = false,
            unsupported = false,
            awesome = false
        )

        val copiedLink = originalLink.copy(
            name = "Copied Link",
            github = "copied/repo",
            star = 100,
            awesome = true
        )

        assertEquals("Copied Link", copiedLink.name)
        assertEquals("copied/repo", copiedLink.github)
        assertEquals("https://original.com", copiedLink.href)
        assertEquals("Original description", copiedLink.desc)
        assertEquals(listOf(PlatformType.JVM), copiedLink.platforms)
        assertEquals(listOf("original"), copiedLink.tags)
        assertEquals(100, copiedLink.star)
        assertEquals("2023-04-01", copiedLink.update)
        assertFalse(copiedLink.archived)
        assertFalse(copiedLink.unsupported)
        assertTrue(copiedLink.awesome)
    }

    @Test
    fun testLinkEquality() {
        val link1 = Link(
            name = "Test Link",
            github = "test/repo",
            href = "https://test.com",
            desc = "Test description",
            platforms = listOf(PlatformType.JVM),
            tags = listOf("test"),
            star = 100,
            update = "2023-05-01",
            archived = false,
            unsupported = false,
            awesome = true
        )

        val link2 = Link(
            name = "Test Link",
            github = "test/repo",
            href = "https://test.com",
            desc = "Test description",
            platforms = listOf(PlatformType.JVM),
            tags = listOf("test"),
            star = 100,
            update = "2023-05-01",
            archived = false,
            unsupported = false,
            awesome = true
        )

        val link3 = Link(
            name = "Different Link",
            github = "different/repo",
            href = "https://different.com",
            desc = "Different description",
            platforms = listOf(PlatformType.JS),
            tags = listOf("different"),
            star = 200,
            update = "2023-05-02",
            archived = true,
            unsupported = true,
            awesome = false
        )

        assertEquals(link1, link2)
        assertNotEquals(link1, link3)
        assertEquals(link1.hashCode(), link2.hashCode())
        assertNotEquals(link1.hashCode(), link3.hashCode())
    }

    @Test
    fun testLinkToString() {
        val link = Link(
            name = "Test Link",
            github = "test/repo",
            href = "https://test.com",
            desc = "Test description",
            platforms = listOf(PlatformType.JVM),
            tags = listOf("test"),
            star = 100,
            update = "2023-05-01",
            archived = false,
            unsupported = false,
            awesome = true
        )

        val expectedString = "Link(name=Test Link, github=test/repo, bitbucket=null, kug=null, href=https://test.com, desc=Test description, platforms=[JVM], tags=[test], star=100, update=2023-05-01, archived=false, unsupported=false, awesome=true)"
        assertEquals(expectedString, link.toString())
    }

//    companion object {
//        @JvmStatic
//        fun provideLinkData() = listOf(
//            Arguments.of("Link 1", "github1", "bitbucket1", "kug1", "https://link1.com", "Desc 1", listOf(PlatformType.JVM), listOf("tag1"), 100, "2023-05-01", false, false, true),
//            Arguments.of("Link 2", null, null, null, "https://link2.com", "Desc 2", listOf(PlatformType.JS, PlatformType.NATIVE), listOf("tag2", "tag3"), null, null, true, false, false),
//            Arguments.of(null, "github3", null, "kug3", null, null, emptyList(), emptyList(), 50, "2023-04-30", false, true, false)
//        )
//    }
}
