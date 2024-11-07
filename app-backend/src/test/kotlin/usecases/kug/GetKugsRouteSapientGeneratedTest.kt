package usecases.kug

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import ktor.KtorRoute
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals

class GetKugsRouteSapientGeneratedTest {

    private lateinit var kugDao: KugDao
    private lateinit var getKugsRoute: GetKugsRoute

    @BeforeEach
    fun setup() {
        kugDao = mockk()
        getKugsRoute = GetKugsRoute(kugDao)
    }

    @Test
    fun `test get kugs route returns all kugs`() = testApplication {
        val fakeKugs = listOf("Kug1", "Kug2", "Kug3")
        coEvery { kugDao.getAll() } returns fakeKugs

        application {
            routing {
                getKugsRoute.apply { install() }
            }
        }

        val response = client.get("/kugs")
        assertEquals(200, response.status.value)
        assertEquals(fakeKugs, response.bodyAsText())

        verify(exactly = 1) { kugDao.getAll() }
    }

    @Test
    fun `test get kugs route with empty list`() = testApplication {
        coEvery { kugDao.getAll() } returns emptyList()

        application {
            routing {
                getKugsRoute.apply { install() }
            }
        }

        val response = client.get("/kugs")
        assertEquals(200, response.status.value)
        assertEquals("[]", response.bodyAsText())

        verify(exactly = 1) { kugDao.getAll() }
    }

    @Test
    fun `test get kugs route handles exceptions`() = testApplication {
        coEvery { kugDao.getAll() } throws RuntimeException("Database error")

        application {
            routing {
                getKugsRoute.apply { install() }
            }
        }

        val response = client.get("/kugs")
        assertEquals(500, response.status.value)

        verify(exactly = 1) { kugDao.getAll() }
    }

    @Test
    fun `test get kugs route with null response`() = testApplication {
        coEvery { kugDao.getAll() } returns null

        application {
            routing {
                getKugsRoute.apply { install() }
            }
        }

        val response = client.get("/kugs")
        assertEquals(200, response.status.value)
        assertEquals("null", response.bodyAsText())

        verify(exactly = 1) { kugDao.getAll() }
    }

    @Test
    fun `test route installation`() {
        val routing = mockk<Routing>(relaxed = true)
        runBlocking {
            getKugsRoute.apply { routing.install() }
        }
        verify(exactly = 1) { routing.get("/kugs", any()) }
    }
}