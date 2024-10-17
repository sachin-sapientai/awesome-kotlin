
package link.kotlin.scripts

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SitemapSapientGeneratedTest {

    @Test
    fun `test empty sitemap generation`() {
        val result = sitemap { }
        val expected = """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
            </urlset>
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun `test sitemap generation with single URL`() {
        val result = sitemap {
            +SitemapUrl("https://example.com")
        }
        val expected = """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
            <url>
              <loc>https://example.com</loc>
            </url>
            </urlset>
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun `test sitemap generation with multiple URLs`() {
        val result = sitemap {
            +SitemapUrl("https://example.com")
            +SitemapUrl("https://example.com/page1")
            +SitemapUrl("https://example.com/page2")
        }
        val expected = """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
            <url>
              <loc>https://example.com</loc>
            </url>
            <url>
              <loc>https://example.com/page1</loc>
            </url>
            <url>
              <loc>https://example.com/page2</loc>
            </url>
            </urlset>
        """.trimIndent()
        assertEquals(expected, result)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "https://example.com", "https://test.org/page"])
    fun `test SitemapUrl creation with various URLs`(url: String) {
        val sitemapUrl = SitemapUrl(url)
        assertEquals(url, sitemapUrl.url)
    }

    @Test
    fun `test Sitemap class initialization`() {
        val sitemap = Sitemap()
        val result = sitemap.getSiteMap()
        val expected = """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
            </urlset>
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun `test Sitemap class with single URL addition`() {
        val sitemap = Sitemap()
        sitemap.apply { +SitemapUrl("https://example.com") }
        val result = sitemap.getSiteMap()
        val expected = """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
            <url>
              <loc>https://example.com</loc>
            </url>
            </urlset>
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun `test Sitemap class with multiple URL additions`() {
        val sitemap = Sitemap()
        sitemap.apply {
            +SitemapUrl("https://example.com")
            +SitemapUrl("https://example.com/page1")
            +SitemapUrl("https://example.com/page2")
        }
        val result = sitemap.getSiteMap()
        val expected = """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
            <url>
              <loc>https://example.com</loc>
            </url>
            <url>
              <loc>https://example.com/page1</loc>
            </url>
            <url>
              <loc>https://example.com/page2</loc>
            </url>
            </urlset>
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun `test sitemap function with empty callback`() {
        val result = sitemap {}
        val expected = """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
            </urlset>
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun `test sitemap function with URL additions`() {
        val result = sitemap {
            +SitemapUrl("https://example.com")
            +SitemapUrl("https://example.com/page1")
        }
        val expected = """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
            <url>
              <loc>https://example.com</loc>
            </url>
            <url>
              <loc>https://example.com/page1</loc>
            </url>
            </urlset>
        """.trimIndent()
        assertEquals(expected, result)
    }
}
