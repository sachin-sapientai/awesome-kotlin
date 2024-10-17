
package link.kotlin.scripts

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import link.kotlin.scripts.utils.HttpClient
import org.apache.http.client.methods.HttpGet
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.net.http.HttpResponse

class KotlinVersionFetcherSapientGeneratedTest {

    private lateinit var httpClient: HttpClient
    private lateinit var kotlinVersionFetcher: KotlinVersionFetcher

    @BeforeEach
    fun setup() {
        httpClient = mockk()
        kotlinVersionFetcher = KotlinVersionFetcher.default(httpClient)
    }

//    @Test
//    fun `getLatestVersions returns correct versions for given branches`() = runBlocking {
//        val xmlResponse = """
//            <?xml version="1.0" encoding="UTF-8"?>
//            <metadata>
//              <versioning>
//                <versions>
//                  <version>1.3.72</version>
//                  <version>1.4.32</version>
//                  <version>1.5.0</version>
//                  <version>1.5.10</version>
//                  <version>1.6.0</version>
//                </versions>
//              </versioning>
//            </metadata>
//        """.trimIndent()
//
//        coEvery { httpClient.execute(any()) } returns HttpResponse(200, xmlResponse)
//
//        val branches = listOf("1.3", "1.4", "1.5", "1.6")
//        val result = kotlinVersionFetcher.getLatestVersions(branches)
//
//        assertEquals(listOf("1.3.72", "1.4.32", "1.5.10", "1.6.0"), result)
//    }

//    @Test
//    fun `getLatestVersions handles empty version list`() = runBlocking {
//        val xmlResponse = """
//            <?xml version="1.0" encoding="UTF-8"?>
//            <metadata>
//              <versioning>
//                <versions>
//                </versions>
//              </versioning>
//            </metadata>
//        """.trimIndent()
//
//        coEvery { httpClient.execute(any()) } returns HttpResponse(200, xmlResponse)
//
//        val branches = listOf("1.3", "1.4")
//        val result = kotlinVersionFetcher.getLatestVersions(branches)
//
//        assertEquals(listOf("", ""), result)
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideInvalidVersions")
//    fun `getLatestVersions filters out invalid versions`(invalidVersion: String) = runBlocking {
//        val xmlResponse = """
//            <?xml version="1.0" encoding="UTF-8"?>
//            <metadata>
//              <versioning>
//                <versions>
//                  <version>1.3.72</version>
//                  <version>$invalidVersion</version>
//                  <version>1.4.0</version>
//                </versions>
//              </versioning>
//            </metadata>
//        """.trimIndent()
//
//        coEvery { httpClient.execute(any()) } returns HttpResponse(200, xmlResponse)
//
//        val branches = listOf("1.3", "1.4")
//        val result = kotlinVersionFetcher.getLatestVersions(branches)
//
//        assertEquals(listOf("1.3.72", "1.4.0"), result)
//    }

//    @Test
//    fun `getLatestVersions handles null response`() = runBlocking {
//        coEvery { httpClient.execute(any()) } returns HttpResponse(200, null)
//
//        val branches = listOf("1.3", "1.4")
//        assertThrows(NullPointerException::class.java) {
//            runBlocking {
//                kotlinVersionFetcher.getLatestVersions(branches)
//            }
//        }
//    }
//
//    @Test
//    fun `getLatestVersions handles malformed XML`() = runBlocking {
//        val xmlResponse = "This is not valid XML"
//
//        coEvery { httpClient.execute(any()) } returns HttpResponse(200, xmlResponse)
//
//        val branches = listOf("1.3", "1.4")
//        assertThrows(com.fasterxml.jackson.core.JsonParseException::class.java) {
//            runBlocking {
//                kotlinVersionFetcher.getLatestVersions(branches)
//            }
//        }
//    }

    @Test
    fun `MavenMetadata data class works correctly`() {
        val versioning = MavenVersioning(listOf("1.0.0", "1.1.0", "1.2.0"))
        val metadata = MavenMetadata(versioning)

        assertEquals(versioning, metadata.versioning)
        assertEquals(listOf("1.0.0", "1.1.0", "1.2.0"), metadata.versioning.versions)
    }

    @Test
    fun `MavenVersioning data class works correctly`() {
        val versions = listOf("1.0.0", "1.1.0", "1.2.0")
        val versioning = MavenVersioning(versions)

        assertEquals(versions, versioning.versions)
    }

    companion object {
        @JvmStatic
        fun provideInvalidVersions() = listOf(
            Arguments.of("1.3.72-RC"),
            Arguments.of("1.3.72-SNAPSHOT"),
            Arguments.of("not-a-version"),
            Arguments.of("1.3"),
            Arguments.of("1.3.72.1")
        )
    }
}
