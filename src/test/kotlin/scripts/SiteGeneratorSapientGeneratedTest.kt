//
//package link.kotlin.scripts
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import io.mockk.*
//import kotlinx.coroutines.runBlocking
//import link.kotlin.scripts.dsl.Article
//import link.kotlin.scripts.dsl.Category
//import link.kotlin.scripts.dsl.LinkType
//import link.kotlin.scripts.model.toDto
//import link.kotlin.scripts.utils.writeFile
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.io.TempDir
//import java.nio.file.Files
//import java.nio.file.Path
//import java.time.LocalDate
//import kotlin.test.assertEquals
//import kotlin.test.assertTrue
//
//class SiteGeneratorSapientGeneratedTest {
//
//    private lateinit var objectMapper: ObjectMapper
//    private lateinit var kotlinVersionFetcher: KotlinVersionFetcher
//    private lateinit var sitemapGenerator: SitemapGenerator
//    private lateinit var pagesGenerator: PagesGenerator
//    private lateinit var rssGenerator: RssGenerator
//    private lateinit var siteGenerator: SiteGenerator
//
//    @TempDir
//    lateinit var tempDir: Path
//
//    @BeforeEach
//    fun setup() {
//        objectMapper = mockk(relaxed = true)
//        kotlinVersionFetcher = mockk()
//        sitemapGenerator = mockk()
//        pagesGenerator = mockk()
//        rssGenerator = mockk()
//
//        siteGenerator = SiteGenerator.default(
//            objectMapper,
//            kotlinVersionFetcher,
//            sitemapGenerator,
//            pagesGenerator,
//            rssGenerator
//        )
//
//        mockkStatic("link.kotlin.scripts.utils.FileUtilsKt")
//        every { writeFile(any(), any()) } just Runs
//        every { copyResources(any()) } just Runs
//    }
//
//    @Test
//    fun `createDistFolders creates necessary directories`() {
//        val distPath = tempDir.resolve("app-frontend/dist")
//        val articlesPath = distPath.resolve("articles")
//
//        mockkStatic(Files::class)
//        every { Files.notExists(distPath) } returns true
//        every { Files.notExists(articlesPath) } returns true
//        every { Files.createDirectory(any()) } returns mockk()
//
//        siteGenerator.createDistFolders()
//
//        verify(exactly = 1) { Files.createDirectory(distPath) }
//        verify(exactly = 1) { Files.createDirectory(articlesPath) }
//    }
//
//    @Test
//    fun `copyResources copies all required files`() {
//        siteGenerator.copyResources()
//
//        verify(exactly = 1) {
//            copyResources(
//                "./app-frontend/pages/github.css" to "./app-frontend/dist/github.css",
//                "./app-frontend/pages/styles.css" to "./app-frontend/dist/styles.css",
//                "./app-frontend/pages/highlight.min.js" to "./app-frontend/dist/highlight.min.js",
//                "./app-frontend/root/robots.txt" to "./app-frontend/dist/robots.txt",
//                "./app-frontend/root/awesome-kotlin.svg" to "./app-frontend/dist/awesome-kotlin.svg"
//            )
//        }
//    }
//
//    @Test
//    fun `generateLinksJson generates correct JSON for categories`() {
//        val categories = listOf(
//            Category("Test Category", mutableListOf())
//        )
//        val expectedJson = "test json"
//        every { objectMapper.writeValueAsString(any()) } returns expectedJson
//
//        siteGenerator.generateLinksJson(categories)
//
//        verify(exactly = 1) {
//            writeFile("./app-frontend/app/links.json", expectedJson)
//        }
//        verify(exactly = 1) {
//            objectMapper.writeValueAsString(categories.map { it.toDto() })
//        }
//    }
//
////    @Test
////    fun `generateKotlinVersionsJson fetches and writes correct versions`() {
////        val versions = listOf(
////            KotlinVersion("1.9.0", "1.9"),
////            KotlinVersion("2.0.0", "2.0")
////        )
////        coEvery { kotlinVersionFetcher.getLatestVersions(listOf("1.9", "2.0")) } returns versions
////        every { objectMapper.writeValueAsString(versions) } returns "versions json"
////
////        runBlocking {
////            siteGenerator.generateKotlinVersionsJson()
////        }
////
////        verify(exactly = 1) {
////            writeFile("./app-frontend/app/versions.json", "versions json")
////        }
////    }
//
//    @Test
//    fun `generateSitemap creates sitemap for articles`() {
//        val articles = listOf(
//            Article("Test", "url", "body", "author", LocalDate.now(), LinkType.article)
//        )
//        every { sitemapGenerator.generate(articles) } returns "sitemap content"
//
//        siteGenerator.generateSitemap(articles)
//
//        verify(exactly = 1) {
//            writeFile("./app-frontend/dist/sitemap.xml", "sitemap content")
//        }
//    }
//
//    @Test
//    fun `generateFeeds creates RSS feeds`() {
//        val articles = List(25) {
//            Article("Test $it", "url", "body", "author", LocalDate.now(), LinkType.article)
//        }
//        every { rssGenerator.generate(articles.take(20), "rss.xml") } returns "rss content"
//        every { rssGenerator.generate(articles, "rss-full.xml") } returns "full rss content"
//
//        siteGenerator.generateFeeds(articles)
//
//        verify(exactly = 1) {
//            writeFile("./app-frontend/dist/rss.xml", "rss content")
//            writeFile("./app-frontend/dist/rss-full.xml", "full rss content")
//        }
//    }
//
//    @Test
//    fun `generateArticles delegates to pagesGenerator`() {
//        val articles = listOf(
//            Article("Test", "url", "body", "author", LocalDate.now(), LinkType.article)
//        )
//
//        siteGenerator.generateArticles(articles)
//
//        verify(exactly = 1) {
//            pagesGenerator.generate(articles, "./app-frontend/dist")
//        }
//    }
//}
