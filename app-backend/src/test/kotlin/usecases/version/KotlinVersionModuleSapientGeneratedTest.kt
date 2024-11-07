package usecases.version

import HttpClientModule
import XmlModule
import di.bean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KotlinVersionModuleSapientGeneratedTest {

    private lateinit var xmlModule: XmlModule
    private lateinit var httpClientModule: HttpClientModule
    private lateinit var kotlinVersionModule: KotlinVersionModule

    @BeforeEach
    fun setup() {
        xmlModule = mockk()
        httpClientModule = mockk()
        kotlinVersionModule = KotlinVersionModule(xmlModule, httpClientModule)
    }

    @Test
    fun `versionFetcher is initialized with correct dependencies`() {
        // Arrange
        val mockXmlMapper = mockk<Any>()
        val mockHttpClient = mockk<Any>()

        every { xmlModule.xmlMapper.get } returns mockXmlMapper
        every { httpClientModule.httpClient.get } returns mockHttpClient

        // Act
        val versionFetcher = kotlinVersionModule.versionFetcher

        // Assert
        assertNotNull(versionFetcher)
        assertTrue(versionFetcher is MavenCentralKotlinVersionFetcher)

        verify(exactly = 1) { xmlModule.xmlMapper.get }
        verify(exactly = 1) { httpClientModule.httpClient.get }
    }

    @Test
    fun `versionFetcher is lazily initialized`() {
        // Arrange
        val mockXmlMapper = mockk<Any>()
        val mockHttpClient = mockk<Any>()

        every { xmlModule.xmlMapper.get } returns mockXmlMapper
        every { httpClientModule.httpClient.get } returns mockHttpClient

        // Act & Assert
        // Accessing versionFetcher multiple times should not trigger multiple initializations
        repeat(3) {
            assertNotNull(kotlinVersionModule.versionFetcher)
        }

        verify(exactly = 1) { xmlModule.xmlMapper.get }
        verify(exactly = 1) { httpClientModule.httpClient.get }
    }

    @Test
    fun `versionFetcher is open for override`() {
        // This test verifies that versionFetcher is marked as open
        // by creating a subclass and overriding the property

        // Arrange
        val mockVersionFetcher = mockk<KotlinVersionFetcher>()

        // Act
        val customModule = object : KotlinVersionModule(xmlModule, httpClientModule) {
            override val versionFetcher: KotlinVersionFetcher = mockVersionFetcher
        }

        // Assert
        assertTrue(customModule.versionFetcher === mockVersionFetcher)
    }
}