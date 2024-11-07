package com.example.test

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariDataSource
import io.mockk.*
import kotlinx.serialization.hocon.Hocon
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class JdbcModuleSapientGeneratedTest {

    private lateinit var configModule: ConfigModule
    private lateinit var jdbcModule: JdbcModule

    @BeforeEach
    fun setup() {
        configModule = mockk()
        every { configModule.config.get } returns ConfigFactory.parseString("""
            jdbc {
                host = "localhost"
                port = "5432"
            }
        """.trimIndent())
        jdbcModule = JdbcModule(configModule)
    }

    @Test
    fun `test jdbcConfig initialization`() {
        val config = jdbcModule.jdbcConfig.get
        assertEquals("localhost", config.host)
        assertEquals("5432", config.port)
    }

    @Test
    fun `test dataSource initialization`() {
        val dataSource = jdbcModule.dataSource.get
        assertNotNull(dataSource)
        assertTrue(dataSource is HikariDataSource)
        
        with(dataSource as HikariDataSource) {
            assertEquals("org.postgresql.ds.PGSimpleDataSource", dataSourceClassName)
            assertEquals("awesome_kotlin", username)
            assertEquals("awesome_kotlin", password)
            assertEquals("awesome_kotlin", dataSourceProperties["databaseName"])
            assertEquals("localhost", dataSourceProperties["serverName"])
            assertEquals("5432", dataSourceProperties["portNumber"])
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["host1", "host2", "127.0.0.1"])
    fun `test jdbcConfig with different hosts`(host: String) {
        every { configModule.config.get } returns ConfigFactory.parseString("""
            jdbc {
                host = "$host"
                port = "5432"
            }
        """.trimIndent())
        
        val newJdbcModule = JdbcModule(configModule)
        assertEquals(host, newJdbcModule.jdbcConfig.get.host)
    }

    @ParameterizedTest
    @ValueSource(strings = ["1234", "5678", "9999"])
    fun `test jdbcConfig with different ports`(port: String) {
        every { configModule.config.get } returns ConfigFactory.parseString("""
            jdbc {
                host = "localhost"
                port = "$port"
            }
        """.trimIndent())
        
        val newJdbcModule = JdbcModule(configModule)
        assertEquals(port, newJdbcModule.jdbcConfig.get.port)
    }

    @Test
    fun `test close method when dataSource is initialized`() {
        val mockDataSource = mockk<HikariDataSource>(relaxed = true)
        every { jdbcModule.dataSource.isInitialized } returns true
        every { jdbcModule.dataSource.get } returns mockDataSource

        jdbcModule.close()

        verify { mockDataSource.close() }
    }

    @Test
    fun `test close method when dataSource is not initialized`() {
        every { jdbcModule.dataSource.isInitialized } returns false

        jdbcModule.close()

        verify(exactly = 0) { any<HikariDataSource>().close() }
    }

    @Test
    fun `test JdbcConfig data class`() {
        val config = JdbcModule.JdbcConfig("testhost", "1234")
        assertEquals("testhost", config.host)
        assertEquals("1234", config.port)

        val (host, port) = config
        assertEquals("testhost", host)
        assertEquals("1234", port)

        val copy = config.copy(port = "5678")
        assertEquals("testhost", copy.host)
        assertEquals("5678", copy.port)
    }

    @Test
    fun `test null safety of JdbcConfig`() {
        assertThrows<IllegalArgumentException> {
            JdbcModule.JdbcConfig(host = "", port = "")
        }
    }
}