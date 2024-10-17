
package link.kotlin.scripts.scripting

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import link.kotlin.scripts.utils.Cache
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.reflect.KClass
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

class ScriptEvaluatorSapientGeneratedTest {

    private lateinit var scriptingHost: BasicJvmScriptingHost
    private lateinit var cache: Cache
    private lateinit var scriptEvaluator: ScriptEvaluator

    @BeforeEach
    fun setup() {
        scriptingHost = mockk()
        cache = mockk()
        scriptEvaluator = ScriptEvaluator.default(scriptingHost, cache)
    }
//
//    @Test
//    fun `test ScriptingScriptEvaluator eval`() {
//        val source = "println(\"Hello, World!\")"
//        val name = "test.kts"
//        val type = String::class
//
//        val mockResult = mockk<ResultValue.Value>()
//        every { mockResult.value } returns "Hello, World!"
//
//        every {
//            scriptingHost.evalWithTemplate<AwesomeScript>(any(), any<ScriptEvaluationConfiguration>())
//        } returns mockk {
//            every { valueOrThrow().returnValue } returns mockResult
//        }
//
//        val result = (scriptEvaluator as CachingScriptEvaluator).eval(source, name, type)
//
//        assertEquals("Hello, World!", result)
//        verify {
//            scriptingHost.evalWithTemplate<AwesomeScript>(any(), any<ScriptEvaluationConfiguration>())
//        }
//    }
//
//    @Test
//    fun `test CachingScriptEvaluator eval with cache miss`() {
//        val source = "2 + 2"
//        val name = "math.kts"
//        val type = Int::class
//        val cacheKey = Cache.cacheKey(source)
//
//        every { cache.get(cacheKey, type) } returns null
//        every { cache.put(cacheKey, 4) } returns Unit
//
//        val mockInnerEvaluator = mockk<ScriptEvaluator>()
//        every { mockInnerEvaluator.eval(source, name, type) } returns 4
//
//        val cachingEvaluator = CachingScriptEvaluator(cache, mockInnerEvaluator)
//        val result = cachingEvaluator.eval(source, name, type)
//
//        assertEquals(4, result)
//        verify {
//            cache.get(cacheKey, type)
//            mockInnerEvaluator.eval(source, name, type)
//            cache.put(cacheKey, 4)
//        }
//    }
//
//    @Test
//    fun `test CachingScriptEvaluator eval with cache hit`() {
//        val source = "3 * 3"
//        val name = "math.kts"
//        val type = Int::class
//        val cacheKey = Cache.cacheKey(source)
//
//        every { cache.get(cacheKey, type) } returns 9
//
//        val mockInnerEvaluator = mockk<ScriptEvaluator>()
//
//        val cachingEvaluator = CachingScriptEvaluator(cache, mockInnerEvaluator)
//        val result = cachingEvaluator.eval(source, name, type)
//
//        assertEquals(9, result)
//        verify {
//            cache.get(cacheKey, type)
//        }
//        verify(exactly = 0) {
//            mockInnerEvaluator.eval(any(), any(), any())
//        }
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = ["", "   ", "\n\t"])
//    fun `test eval with empty or blank source`(source: String) {
//        val name = "empty.kts"
//        val type = String::class
//
//        every { cache.get(any(), any()) } returns null
//        every { cache.put(any(), any()) } returns Unit
//
//        every {
//            scriptingHost.evalWithTemplate<AwesomeScript>(any(), any<ScriptEvaluationConfiguration>())
//        } returns mockk {
//            every { valueOrThrow().returnValue } returns ResultValue.Value("")
//        }
//
//        val result = scriptEvaluator.eval(source, name, type)
//
//        assertEquals("", result)
//    }
//
//    @Test
//    fun `test eval with null safety`() {
//        val source = "val nullableString: String? = null; nullableString"
//        val name = "nullable.kts"
//        val type = String::class
//
//        every { cache.get(any(), any()) } returns null
//        every { cache.put(any(), any()) } returns Unit
//
//        every {
//            scriptingHost.evalWithTemplate<AwesomeScript>(any(), any<ScriptEvaluationConfiguration>())
//        } returns mockk {
//            every { valueOrThrow().returnValue } returns ResultValue.Value(null)
//        }
//
//        val result = scriptEvaluator.eval(source, name, type)
//
//        assertNull(result)
//    }
//
//    @Test
//    fun `test eval with exception`() {
//        val source = "throw IllegalArgumentException(\"Test exception\")"
//        val name = "exception.kts"
//        val type = Any::class
//
//        every { cache.get(any(), any()) } returns null
//
//        every {
//            scriptingHost.evalWithTemplate<AwesomeScript>(any(), any<ScriptEvaluationConfiguration>())
//        } throws IllegalArgumentException("Test exception")
//
//        assertThrows(IllegalArgumentException::class.java) {
//            scriptEvaluator.eval(source, name, type)
//        }
//    }
//
//    @Test
//    fun `test eval with custom type`() {
//        data class CustomType(val value: String)
//
//        val source = "CustomType(\"Test\")"
//        val name = "custom.kts"
//        val type = CustomType::class
//
//        every { cache.get(any(), any()) } returns null
//        every { cache.put(any(), any()) } returns Unit
//
//        every {
//            scriptingHost.evalWithTemplate<AwesomeScript>(any(), any<ScriptEvaluationConfiguration>())
//        } returns mockk {
//            every { valueOrThrow().returnValue } returns ResultValue.Value(CustomType("Test"))
//        }
//
//        val result = scriptEvaluator.eval(source, name, type)
//
//        assertEquals(CustomType("Test"), result)
//    }
//
//    @Test
//    fun `test default ScriptEvaluator creation`() {
//        val defaultEvaluator = ScriptEvaluator.default(scriptingHost, cache)
//        assertNotNull(defaultEvaluator)
//        assertTrue(defaultEvaluator is CachingScriptEvaluator)
//    }
}
