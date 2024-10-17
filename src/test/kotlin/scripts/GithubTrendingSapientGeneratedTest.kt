//
//package link.kotlin.scripts
//
//import io.mockk.*
//import kotlinx.coroutines.runBlocking
//import link.kotlin.scripts.dsl.Category
//import link.kotlin.scripts.dsl.Subcategory
//import link.kotlin.scripts.model.Link
//import link.kotlin.scripts.utils.Cache
//import org.jsoup.Jsoup
//import org.jsoup.nodes.Document
//import org.jsoup.select.Elements
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.ValueSource
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//
//class GithubTrendingSapientGeneratedTest {
//
//    private lateinit var cache: Cache
//    private lateinit var jSoupGithubTrending: JSoupGithubTrending
//    private lateinit var cachedGithubTrending: CachedGithubTrending
//
//    @BeforeEach
//    fun setup() {
//        cache = mockk()
//        jSoupGithubTrending = spyk(JSoupGithubTrending())
//        cachedGithubTrending = CachedGithubTrending(cache, jSoupGithubTrending)
//    }
//
//    @Test
//    fun `test CachedGithubTrending fetch with cache hit`() = runBlocking {
//        val mockedCategory = mockk<Category>()
//        val date = DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now())
//        val cacheKey = "github-trending-$date"
//
//        every { cache.get(cacheKey, Category::class) } returns mockedCategory
//
//        val result = cachedGithubTrending.fetch()
//
//        assertEquals(mockedCategory, result)
//        verify { cache.get(cacheKey, Category::class) }
//        verify(exactly = 0) { jSoupGithubTrending.fetch() }
//    }
//
//    @Test
//    fun `test CachedGithubTrending fetch with cache miss`() = runBlocking {
//        val mockedCategory = mockk<Category>()
//        val date = DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now())
//        val cacheKey = "github-trending-$date"
//
//        every { cache.get(cacheKey, Category::class) } returns null
//        coEvery { jSoupGithubTrending.fetch() } returns mockedCategory
//        every { cache.put(cacheKey, mockedCategory) } returns mockedCategory
//
//        val result = cachedGithubTrending.fetch()
//
//        assertEquals(mockedCategory, result)
//        verify { cache.get(cacheKey, Category::class) }
//        coVerify { jSoupGithubTrending.fetch() }
//        verify { cache.put(cacheKey, mockedCategory) }
//    }
//
//    @Test
//    fun `test JSoupGithubTrending fetch with valid data`() = runBlocking {
//        val mockedDocument = mockk<Document>()
//        val mockedElements = mockk<Elements>()
//
//        mockkStatic(Jsoup::class)
//        every { Jsoup.connect(any()).get() } returns mockedDocument
//        every { mockedDocument.select(".Box-row") } returns mockedElements
//        every { mockedElements.isNotEmpty() } returns true
//        every { mockedElements.map<Link>(any()) } returns listOf(
//            Link(github = "user/repo1"),
//            Link(github = "user/repo2")
//        )
//
//        val result = jSoupGithubTrending.fetch()
//
//        assertNotNull(result)
//        assertEquals("Github Trending", result?.name)
//        assertEquals(3, result?.subcategories?.size)
//    }
//
//    @Test
//    fun `test JSoupGithubTrending fetch with no data`() = runBlocking {
//        val mockedDocument = mockk<Document>()
//        val mockedElements = mockk<Elements>()
//
//        mockkStatic(Jsoup::class)
//        every { Jsoup.connect(any()).get() } returns mockedDocument
//        every { mockedDocument.select(".Box-row") } returns mockedElements
//        every { mockedElements.isNotEmpty() } returns false
//
//        val result = jSoupGithubTrending.fetch()
//
//        assertNull(result)
//    }
//
//    @Test
//    fun `test deleteDuplicates`() {
//        val subcategories = listOf(
//            Subcategory("Monthly", mutableListOf(Link(github = "user/repo1"), Link(github = "user/repo2"))),
//            Subcategory("Weekly", mutableListOf(Link(github = "user/repo2"), Link(github = "user/repo3"))),
//            Subcategory("Daily", mutableListOf(Link(github = "user/repo3"), Link(github = "user/repo4")))
//        )
//
//        val result = jSoupGithubTrending.deleteDuplicates(subcategories)
//
//        assertEquals(3, result.size)
//        assertEquals(2, result[0].links.size)
//        assertEquals(1, result[1].links.size)
//        assertEquals(1, result[2].links.size)
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = ["Monthly", "Weekly", "Daily"])
//    fun `test toSubcategory with valid data`(period: String) = runBlocking {
//        val mockedDocument = mockk<Document>()
//        val mockedElements = mockk<Elements>()
//        val mockedElement = mockk<org.jsoup.nodes.Element>()
//
//        mockkStatic(Jsoup::class)
//        every { Jsoup.connect(any()).get() } returns mockedDocument
//        every { mockedDocument.select(".Box-row") } returns mockedElements
//        every { mockedElements.isNotEmpty() } returns true
//        every { mockedElements.map<Link>(any()) } answers {
//            firstArg<(org.jsoup.nodes.Element) -> Link>().invoke(mockedElement)
//        }
//        every { mockedElement.select("h2 a").attr("href") } returns "/user/repo"
//
//        val result = jSoupGithubTrending.toSubcategory(period to "https://github.com/trending/kotlin?since=${period.lowercase()}")
//
//        assertNotNull(result)
//        assertEquals(period, result?.name)
//        assertEquals(1, result?.links?.size)
//        assertEquals("user/repo", result?.links?.first()?.github)
//    }
//
//    @Test
//    fun `test toSubcategory with no data`() = runBlocking {
//        val mockedDocument = mockk<Document>()
//        val mockedElements = mockk<Elements>()
//
//        mockkStatic(Jsoup::class)
//        every { Jsoup.connect(any()).get() } returns mockedDocument
//        every { mockedDocument.select(".Box-row") } returns mockedElements
//        every { mockedElements.isNotEmpty() } returns false
//
//        val result = jSoupGithubTrending.toSubcategory("Monthly" to "https://github.com/trending/kotlin?since=monthly")
//
//        assertNull(result)
//    }
//
//    @Test
//    fun `test GithubTrending default factory method`() {
//        val result = GithubTrending.default(cache)
//
//        assertTrue(result is CachedGithubTrending)
//    }
//}
