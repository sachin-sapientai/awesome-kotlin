package com.example.test

import di.bean
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MetricsModuleSapientGeneratedTest {

    private lateinit var metricsModule: MetricsModule

    @BeforeEach
    fun setup() {
        metricsModule = MetricsModule()
    }

    @Test
    fun `test meterRegistry initialization`() {
        val meterRegistry = metricsModule.meterRegistry

        assertNotNull(meterRegistry)
        assert(meterRegistry is PrometheusMeterRegistry)
    }

    @Test
    fun `test meterRegistry uses PrometheusConfig DEFAULT`() {
        mockkObject(PrometheusConfig)
        every { PrometheusConfig.DEFAULT } returns PrometheusConfig.DEFAULT

        metricsModule.meterRegistry

        verify(exactly = 1) { PrometheusConfig.DEFAULT }
    }

    @Test
    fun `test bean delegate is used for meterRegistry`() {
        mockkObject(::bean)
        every { bean<PrometheusMeterRegistry>(any()) } returns PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

        metricsModule.meterRegistry

        verify(exactly = 1) { bean<PrometheusMeterRegistry>(any()) }
    }
}