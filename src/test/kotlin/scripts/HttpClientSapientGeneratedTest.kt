//
//package link.kotlin.scripts.utils
//
//import io.mockk.*
//import kotlinx.coroutines.runBlocking
//import org.apache.http.HttpResponse
//import org.apache.http.client.methods.HttpUriRequest
//import org.apache.http.concurrent.FutureCallback
//import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
//import org.apache.http.nio.client.HttpAsyncClient
//import org.junit.jupiter.api.*
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.ValueSource
//import java.io.ByteArrayInputStream
//import java.io.InputStream
//import java.util.concurrent.Future
//
//class HttpClientSapientGeneratedTest {
//
//    @Test
//    fun `test HttpResponse body extension function`() {
//        val mockResponse = mockk<HttpResponse>()
//        val mockEntity = mockk<org.apache.http.HttpEntity>()
//        val mockContent = ByteArrayInputStream("Test content".toByteArray())
//
//        every { mockResponse.entity } returns mockEntity
//        every { mockEntity.content } returns mockContent
//
//        val result = mockResponse.body()
//
//        assertEquals("Test content", result)
//        verify { mockResponse.entity }
//        verify { mockEntity.content }
//    }
//
//    @Test
//    fun `test DefaultHttpClient execute`() = runBlocking {
//        val mockClient = mockk<HttpAsyncClient>()
//        val mockRequest = mockk<HttpUriRequest>()
//        val mockResponse = mockk<HttpResponse>()
//
//        coEvery { mockClient.execute(any()) } returns mockResponse
//
//        val httpClient = DefaultHttpClient(mockClient)
//        val result = httpClient.execute(mockRequest)
//
//        assertEquals(mockResponse, result)
//        coVerify { mockClient.execute(mockRequest) }
//    }
//
//    @Test
//    fun `test HttpAsyncClient execute extension function`() = runBlocking {
//        val mockClient = mockk<HttpAsyncClient>()
//        val mockRequest = mockk<HttpUriRequest>()
//        val mockResponse = mockk<HttpResponse>()
//        val mockFuture = mockk<Future<HttpResponse>>()
//
//        every {
//            mockClient.execute(any(), any<FutureCallback<HttpResponse>>())
//        } answers {
//            val callback = secondArg<FutureCallback<HttpResponse>>()
//            callback.completed(mockResponse)
//            mockFuture
//        }
//
//        val result = mockClient.execute(mockRequest)
//
//        assertEquals(mockResponse, result)
//        verify { mockClient.execute(any(), any<FutureCallback<HttpResponse>>()) }
//    }
//
//    @Test
//    fun `test HttpAsyncClient execute extension function with cancellation`() = runBlocking {
//        val mockClient = mockk<HttpAsyncClient>()
//        val mockRequest = mockk<HttpUriRequest>()
//        val mockFuture = mockk<Future<HttpResponse>>()
//
//        every {
//            mockClient.execute(any(), any<FutureCallback<HttpResponse>>())
//        } answers {
//            val callback = secondArg<FutureCallback<HttpResponse>>()
//            callback.cancelled()
//            mockFuture
//        }
//
//        assertThrows<kotlinx.coroutines.CancellationException> {
//            mockClient.execute(mockRequest)
//        }
//
//        verify { mockClient.execute(any(), any<FutureCallback<HttpResponse>>()) }
//    }
//
//    @Test
//    fun `test HttpAsyncClient execute extension function with failure`() = runBlocking {
//        val mockClient = mockk<HttpAsyncClient>()
//        val mockRequest = mockk<HttpUriRequest>()
//        val mockFuture = mockk<Future<HttpResponse>>()
//        val testException = RuntimeException("Test exception")
//
//        every {
//            mockClient.execute(any(), any<FutureCallback<HttpResponse>>())
//        } answers {
//            val callback = secondArg<FutureCallback<HttpResponse>>()
//            callback.failed(testException)
//            mockFuture
//        }
//
//        val exception = assertThrows<RuntimeException> {
//            mockClient.execute(mockRequest)
//        }
//
//        assertEquals("Test exception", exception.message)
//        verify { mockClient.execute(any(), any<FutureCallback<HttpResponse>>()) }
//    }
//
//    @Test
//    fun `test HttpClient Companion default function`() {
//        val httpClient = HttpClient.default()
//
//        assertNotNull(httpClient)
//        assertTrue(httpClient is DefaultHttpClient)
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = ["", "Test content", "Lorem ipsum dolor sit amet"])
//    fun `test HttpResponse body extension function with different content`(content: String) {
//        val mockResponse = mockk<HttpResponse>()
//        val mockEntity = mockk<org.apache.http.HttpEntity>()
//        val mockContent: InputStream = ByteArrayInputStream(content.toByteArray())
//
//        every { mockResponse.entity } returns mockEntity
//        every { mockEntity.content } returns mockContent
//
//        val result = mockResponse.body()
//
//        assertEquals(content, result)
//        verify { mockResponse.entity }
//        verify { mockEntity.content }
//    }
//
//    @Test
//    fun `test HttpClient Companion default function creates and starts HttpAsyncClient`() {
//        val httpClientField = HttpClient.default()::class.java.getDeclaredField("client")
//        httpClientField.isAccessible = true
//        val asyncClient = httpClientField.get(httpClient) as CloseableHttpAsyncClient
//
//        assertTrue(asyncClient.isRunning)
//    }
//
//    @Test
//    fun `test HttpClient Companion default function sets correct user agent`() {
//        val httpClientField = HttpClient.default()::class.java.getDeclaredField("client")
//        httpClientField.isAccessible = true
//        val asyncClient = httpClientField.get(httpClient) as CloseableHttpAsyncClient
//
//        val userAgentField = asyncClient.javaClass.getDeclaredField("userAgent")
//        userAgentField.isAccessible = true
//        val userAgent = userAgentField.get(asyncClient) as String
//
//        assertEquals("Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:74.0) Gecko/20100101 Firefox/74.0", userAgent)
//    }
//
//    @Test
//    fun `test HttpClient Companion default function sets correct max connections`() {
//        val httpClientField = HttpClient.default()::class.java.getDeclaredField("client")
//        httpClientField.isAccessible = true
//        val asyncClient = httpClientField.get(httpClient) as CloseableHttpAsyncClient
//
//        val connManagerField = asyncClient.javaClass.getDeclaredField("connManager")
//        connManagerField.isAccessible = true
//        val connManager = connManagerField.get(asyncClient)
//
//        val maxTotalField = connManager.javaClass.getDeclaredField("maxTotal")
//        maxTotalField.isAccessible = true
//        val maxTotal = maxTotalField.getInt(connManager)
//
//        val defaultMaxPerRouteField = connManager.javaClass.getDeclaredField("defaultMaxPerRoute")
//        defaultMaxPerRouteField.isAccessible = true
//        val defaultMaxPerRoute = defaultMaxPerRouteField.getInt(connManager)
//
//        assertEquals(100, maxTotal)
//        assertEquals(10, defaultMaxPerRoute)
//    }
//}
