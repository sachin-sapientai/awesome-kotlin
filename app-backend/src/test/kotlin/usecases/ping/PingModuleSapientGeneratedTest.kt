package usecases.ping

import di.bean
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PingModuleSapientGeneratedTest {

    private lateinit var pingModule: PingModule

    @BeforeEach
    fun setUp() {
        pingModule = PingModule()
    }

    @Test
    fun `route property should return PingRoute instance`() {
        val result = pingModule.route
        assertNotNull(result)
        assert(result is PingRoute)
    }

    @Test
    fun `route property should use bean delegate`() {
        mockkObject(di.bean)
        every { bean<PingRoute>(any()) } returns PingRoute()

        pingModule.route

        verify { bean<PingRoute>(any()) }
    }

    @Test
    fun `route property should be lazy initialized`() {
        mockkObject(di.bean)
        var initializationCount = 0
        every { bean<PingRoute>(any()) } answers {
            initializationCount++
            PingRoute()
        }

        // Access the property multiple times
        repeat(3) {
            pingModule.route
        }

        // Verify that the initialization happened only once
        assert(initializationCount == 1)
    }

    @Test
    fun `route property should throw exception if PingRoute constructor fails`() {
        mockkObject(di.bean)
        every { bean<PingRoute>(any()) } throws RuntimeException("PingRoute initialization failed")

        assertThrows<RuntimeException> {
            pingModule.route
        }
    }
}