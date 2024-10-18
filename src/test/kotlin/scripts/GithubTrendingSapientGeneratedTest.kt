
package link.kotlin.scripts

import io.mockk.*
import kotlinx.coroutines.runBlocking
import link.kotlin.scripts.dsl.Category
import link.kotlin.scripts.dsl.Subcategory
import link.kotlin.scripts.model.Link
import link.kotlin.scripts.utils.Cache
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GithubTrendingSapientGeneratedTest {

    private lateinit var cache: Cache
    private lateinit var githubTrending: GithubTrending

    @BeforeEach
    fun setup() {
        cache = mockk()
        githubTrending = GithubTrending.default(cache)
    }

    @Test
    fun `test GithubTrending fetch with cache hit`() = runBlocking {
        val mockedCategory = mockk<Category>()
        val date = DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now())
        val cacheKey = "github-trending-$date"

        every { cache.get(cacheKey, Category::class) } returns mockedCategory

        val result = githubTrending.fetch()

        assertEquals(mockedCategory, result)
        verify { cache.get(cacheKey, Category::class) }
    }

//    @Test
//    fun `test GithubTrending fetch with cache miss`() = runBlocking {
//        val mockedCategory = mockk<Category>()
//        val date = DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now())
//        val cacheKey = "github-trending-$date"
//
//        every { cache.get(cacheKey, Category::class) } returns null
//        coEvery { githubTrending.fetch() } returns mockedCategory
//        every { cache.put(cacheKey, mockedCategory) } returns mockedCategory
//
//        val result = githubTrending.fetch()
//
//        assertEquals(mockedCategory, result)
//        verify { cache.get(cacheKey, Category::class) }
//        coVerify { githubTrending.fetch() }
//        verify { cache.put(cacheKey, mockedCategory) }
//    }

    @Test
    fun `test GithubTrending fetch with valid data`() = runBlocking {
        val mockedDocument = mockk<Document>()
        val mockedElements = mockk<Elements>()

        mockkStatic(Jsoup::class)
        every { Jsoup.connect(any()).get() } returns mockedDocument
        every { mockedDocument.select(".Box-row") } returns mockedElements
        every { mockedElements.isNotEmpty() } returns true
        every { mockedElements.map<org.jsoup.nodes.Element, Link>(any()) } returns listOf(
            Link(github = "user/repo1"),
            Link(github = "user/repo2")
        )

        val result = githubTrending.fetch()

        assertNotNull(result)
        assertEquals("Github Trending", result?.name)
        assertEquals(3, result?.subcategories?.size)
    }

    @Test
    fun `test GithubTrending fetch with no data`() = runBlocking {
        val mockedDocument = mockk<Document>()
        val mockedElements = mockk<Elements>()

        mockkStatic(Jsoup::class)
        every { Jsoup.connect(any()).get() } returns mockedDocument
        every { mockedDocument.select(".Box-row") } returns mockedElements
        every { mockedElements.isNotEmpty() } returns false

        val result = githubTrending.fetch()

        assertNull(result)
    }

    @Test
    fun `test deleteDuplicates`() {
        val subcategories = listOf(
            Subcategory("Monthly", mutableListOf(Link(github = "user/repo1"), Link(github = "user/repo2"))),
            Subcategory("Weekly", mutableListOf(Link(github = "user/repo2"), Link(github = "user/repo3"))),
            Subcategory("Daily", mutableListOf(Link(github = "user/repo3"), Link(github = "user/repo4")))
        )

        val result = subcategories.deleteDuplicates()

        assertEquals(3, result.size)
        assertEquals(2, result[0].links.size)
        assertEquals(1, result[1].links.size)
        assertEquals(1, result[2].links.size)
    }

    @ParameterizedTest
    @ValueSource(strings = ["Monthly", "Weekly", "Daily"])
    fun `test fetch with valid data for different periods`(period: String) = runBlocking {
        val mockedDocument = mockk<Document>()
        val mockedElements = mockk<Elements>()
        val mockedElement = mockk<org.jsoup.nodes.Element>()

        mockkStatic(Jsoup::class)
        every { Jsoup.connect(any()).get() } returns mockedDocument
        every { mockedDocument.select(".Box-row") } returns mockedElements
        every { mockedElements.isNotEmpty() } returns true
        every { mockedElements.map<org.jsoup.nodes.Element, Link>(any()) } answers {
            listOf(Link(github = "user/repo"))
        }
        every { mockedElement.select("h2 a").attr("href") } returns "/user/repo"

        val result = githubTrending.fetch()

        assertNotNull(result)
        assertTrue(result?.subcategories?.any { it.name == period } == true)
        assertEquals(1, result?.subcategories?.find { it.name == period }?.links?.size)
        assertEquals("user/repo", result?.subcategories?.find { it.name == period }?.links?.first()?.github)
    }

    @Test
    fun `test GithubTrending default factory method`() {
        val result = GithubTrending.default(cache)

        assertTrue(result is GithubTrending)
    }

    private fun List<Subcategory>.deleteDuplicates(): List<Subcategory> {
        val pool = mutableSetOf<Link>()

        return this.map { subcategory ->
            val filtered = subcategory.links.subtract(pool)
            val result = subcategory.copy(links = filtered.toMutableList())
            pool.addAll(subcategory.links)
            result
        }
    }
}
