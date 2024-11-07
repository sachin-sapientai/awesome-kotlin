package com.example.test

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import config.dotenv
import di.bean
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ConfigModuleSapientGeneratedTest {

    private lateinit var configModule: ConfigModule

    @BeforeEach
    fun setup() {
        configModule = ConfigModule()
    }

    @Test
    fun testOverridesDefault() {
        val overrides = configModule.overrides.get
        assertEquals(emptyMap(), overrides)
    }

    @Test
    fun testDotenvInitialization() {
        mockkStatic(::dotenv)
        val mockDotenv = mockk<Map<String, String>>()
        every { dotenv() } returns mockDotenv

        val result = configModule.dotenv.get
        assertEquals(mockDotenv, result)
        verify(exactly = 1) { dotenv() }
    }

    @Test
    fun testConfigInitialization() {
        mockkStatic(ConfigFactory::class)
        val mockConfig = mockk<Config>()
        every { ConfigFactory.load() } returns mockConfig

        val mockOverrides = mapOf("key1" to "value1")
        val mockDotenv = mapOf("key2" to "value2")

        val testConfigModule = object : ConfigModule() {
            override val overrides by bean { mockOverrides }
            override val dotenv by bean { mockDotenv }
        }

        val result = testConfigModule.config.get

        assertEquals(mockConfig, result)
        verify(exactly = 1) { ConfigFactory.load() }

        // Verify System properties were set
        assertEquals("value1", System.getProperty("key1"))
        assertEquals("value2", System.getProperty("key2"))
    }

    @Test
    fun testConfigInitializationWithEmptyMaps() {
        mockkStatic(ConfigFactory::class)
        val mockConfig = mockk<Config>()
        every { ConfigFactory.load() } returns mockConfig

        val result = configModule.config.get

        assertEquals(mockConfig, result)
        verify(exactly = 1) { ConfigFactory.load() }
    }

    @Test
    fun testConfigInitializationWithNullValues() {
        mockkStatic(ConfigFactory::class)
        val mockConfig = mockk<Config>()
        every { ConfigFactory.load() } returns mockConfig

        val mockOverrides = mapOf("key1" to null)
        val mockDotenv = mapOf("key2" to null)

        val testConfigModule = object : ConfigModule() {
            override val overrides by bean { mockOverrides }
            override val dotenv by bean { mockDotenv }
        }

        assertThrows<NullPointerException> {
            testConfigModule.config.get
        }
    }

    @Test
    fun testConfigInitializationWithDuplicateKeys() {
        mockkStatic(ConfigFactory::class)
        val mockConfig = mockk<Config>()
        every { ConfigFactory.load() } returns mockConfig

        val mockOverrides = mapOf("key" to "overrideValue")
        val mockDotenv = mapOf("key" to "dotenvValue")

        val testConfigModule = object : ConfigModule() {
            override val overrides by bean { mockOverrides }
            override val dotenv by bean { mockDotenv }
        }

        val result = testConfigModule.config.get

        assertEquals(mockConfig, result)
        verify(exactly = 1) { ConfigFactory.load() }

        // Verify the overrides value takes precedence
        assertEquals("overrideValue", System.getProperty("key"))
    }

    @Test
    fun testBeanInitialization() {
        val overridesBean = configModule.overrides
        val dotenvBean = configModule.dotenv
        val configBean = configModule.config

        assertNotNull(overridesBean)
        assertNotNull(dotenvBean)
        assertNotNull(configBean)
    }
}