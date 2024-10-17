
package link.kotlin.scripts.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import io.mockk.mockkStatic
import io.mockk.every
import io.mockk.unmockkAll
import java.lang.System.getenv

class ApplicationConfigurationSapientGeneratedTest {

    @Test
    fun `test DataApplicationConfiguration with default values`() {
        val config = DataApplicationConfiguration()
        assertEquals("", config.ghToken)
        assertEquals("https://kotlin.link/", config.siteUrl)
        assertTrue(config.cacheEnabled)
        assertFalse(config.dryRun)
    }
//
//    @ParameterizedTest
//    @ValueSource(strings = ["true", "false"])
//    fun `test DataApplicationConfiguration with custom cache enabled value`(cacheEnabled: String) {
//        mockkStatic(::getenv)
//        every { getenv("AWESOME_KOTLIN_CACHE") } returns cacheEnabled
//
//        val config = DataApplicationConfiguration()
//        assertEquals(cacheEnabled.toBoolean(), config.cacheEnabled)
//
//        unmockkAll()
//    }
//
//    @Test
//    fun `test DataApplicationConfiguration with custom values`() {
//        mockkStatic(::getenv)
//        every { getenv("GH_TOKEN") } returns "custom_token"
//        every { getenv("AWESOME_KOTLIN_CACHE") } returns "false"
//
//        val config = DataApplicationConfiguration(
//            siteUrl = "https://custom.url/"
//        )
//
//        assertEquals("custom_token", config.ghToken)
//        assertEquals("https://custom.url/", config.siteUrl)
//        assertFalse(config.cacheEnabled)
//        assertFalse(config.dryRun)
//
//        unmockkAll()
//    }
//
//    @Test
//    fun `test ApplicationConfiguration default with non-empty ghToken`() {
//        mockkStatic(::getenv)
//        every { getenv("GH_TOKEN") } returns "non_empty_token"
//
//        val config = ApplicationConfiguration.default()
//        assertEquals("non_empty_token", config.ghToken)
//        assertFalse(config.dryRun)
//
//        unmockkAll()
//    }
//
//    @Test
//    fun `test ApplicationConfiguration default with empty ghToken`() {
//        mockkStatic(::getenv)
//        every { getenv("GH_TOKEN") } returns null
//
//        val config = ApplicationConfiguration.default()
//        assertEquals("", config.ghToken)
//        assertTrue(config.dryRun)
//
//        unmockkAll()
//    }

//    @Test
//    fun `test env function with existing environment variable`() {
//        mockkStatic(::getenv)
//        every { getenv("EXISTING_VAR") } returns "existing_value"
//
//        assertEquals("existing_value", env("EXISTING_VAR", "default"))
//
//        unmockkAll()
//    }

//    @Test
//    fun `test env function with non-existing environment variable`() {
//        mockkStatic(::getenv)
//        every { getenv("NON_EXISTING_VAR") } returns null
//
//        assertEquals("default", env("NON_EXISTING_VAR", "default"))
//
//        unmockkAll()
//    }
}
