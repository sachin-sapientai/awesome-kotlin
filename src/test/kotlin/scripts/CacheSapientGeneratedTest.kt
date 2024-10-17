//
//package link.kotlin.scripts.utils
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import io.mockk.*
//import kotlinx.coroutines.runBlocking
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.io.TempDir
//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.ValueSource
//import strikt.api.expectThat
//import strikt.assertions.*
//import java.math.BigInteger
//import java.nio.file.Files
//import java.nio.file.Path
//import java.security.MessageDigest
//import kotlin.reflect.KClass
//import link.kotlin.scripts.model.ApplicationConfiguration
//
//class CacheSapientGeneratedTest {
//
//    @TempDir
//    lateinit var tempDir: Path
//
//    private lateinit var objectMapper: ObjectMapper
//    private lateinit var fileCache: FileCache
//    private lateinit var disableCache: DisableCache
//    private lateinit var applicationConfiguration: ApplicationConfiguration
//
//    @BeforeEach
//    fun setup() {
//        objectMapper = mockk()
//        fileCache = FileCache(tempDir, objectMapper)
//        applicationConfiguration = mockk()
//        disableCache = DisableCache(fileCache, applicationConfiguration)
//    }
//
//    @AfterEach
//    fun tearDown() {
//        clearAllMocks()
//    }
//
//    @Test
//    fun `cacheKey should generate correct MD5 hash`() {
//        val input = "test data"
//        val expectedHash = "eb733a00c0c9d336e65691a37ab54293"
//
//        val result = Cache.cacheKey(input)
//
//        expectThat(result).isEqualTo(expectedHash)
//    }
//
//    @Test
//    fun `FileCache put should write value to file`() {
//        val key = "testKey"
//        val value = "testValue"
//        val expectedPath = tempDir.resolve(key)
//
//        every { objectMapper.writeValueAsString(value) } returns value
//
//        fileCache.put(key, value)
//
//        verify { objectMapper.writeValueAsString(value) }
//        expectThat(Files.exists(expectedPath)).isTrue()
//        expectThat(Files.readString(expectedPath)).isEqualTo(value)
//    }
//
//    @Test
//    fun `FileCache get should return value when file exists`() {
//        val key = "testKey"
//        val value = "testValue"
//        val type: KClass<String> = String::class
//        val path = tempDir.resolve(key)
//
//        Files.write(path, value.toByteArray())
//
//        every { objectMapper.readValue(any<ByteArray>(), String::class.java) } returns value
//
//        val result = fileCache.get(key, type)
//
//        verify { objectMapper.readValue(any<ByteArray>(), String::class.java) }
//        expectThat(result).isEqualTo(value)
//    }
//
//    @Test
//    fun `FileCache get should return null when file does not exist`() {
//        val key = "nonExistentKey"
//        val type: KClass<String> = String::class
//
//        val result = fileCache.get(key, type)
//
//        expectThat(result).isNull()
//    }
//
//    @Test
//    fun `FileCache get should delete invalid cache entry and return null`() {
//        val key = "invalidKey"
//        val type: KClass<String> = String::class
//        val path = tempDir.resolve(key)
//
//        Files.write(path, "invalid data".toByteArray())
//
//        every { objectMapper.readValue(any<ByteArray>(), String::class.java) } throws RuntimeException("Invalid data")
//
//        val result = fileCache.get(key, type)
//
//        verify { objectMapper.readValue(any<ByteArray>(), String::class.java) }
//        expectThat(result).isNull()
//        expectThat(Files.exists(path)).isFalse()
//    }
//
//    @ParameterizedTest
//    @ValueSource(booleans = [true, false])
//    fun `DisableCache put should delegate to cache when enabled`(cacheEnabled: Boolean) {
//        val key = "testKey"
//        val value = "testValue"
//
//        every { applicationConfiguration.cacheEnabled } returns cacheEnabled
//
//        disableCache.put(key, value)
//
//        if (cacheEnabled) {
//            verify { fileCache.put(key, value) }
//        } else {
//            verify(exactly = 0) { fileCache.put(any(), any()) }
//        }
//    }
//
//    @ParameterizedTest
//    @ValueSource(booleans = [true, false])
//    fun `DisableCache get should delegate to cache when enabled`(cacheEnabled: Boolean) {
//        val key = "testKey"
//        val type: KClass<String> = String::class
//
//        every { applicationConfiguration.cacheEnabled } returns cacheEnabled
//        every { fileCache.get(key, type) } returns "testValue"
//
//        val result = disableCache.get(key, type)
//
//        if (cacheEnabled) {
//            verify { fileCache.get(key, type) }
//            expectThat(result).isEqualTo("testValue")
//        } else {
//            verify(exactly = 0) { fileCache.get(any(), any()) }
//            expectThat(result).isNull()
//        }
//    }
//
//    @Test
//    fun `default cache creation`() {
//        val folder = tempDir
//        val mapper = mockk<ObjectMapper>()
//        val configuration = mockk<ApplicationConfiguration>()
//
//        val cache = Cache.default(folder, mapper, configuration)
//
//        expectThat(cache).isA<DisableCache>()
//        expectThat(Files.exists(folder)).isTrue()
//    }
//}
