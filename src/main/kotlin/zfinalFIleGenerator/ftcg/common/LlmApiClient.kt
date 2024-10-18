package zfinalFIleGenerator.ftcg.common

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class LlmApiClient(private val bearerToken: String) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()

    private val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
    private val mediaType = "application/json".toMediaType()

    companion object {
        private const val API_URL = "https://us-east5-aiplatform.googleapis.com/v1/projects/prod-it-406207/locations/us-east5/publishers/anthropic/models/claude-3-5-sonnet@20240620:rawPredict"
    }

    data class Message(
        val role: String,
        val content: String
    )

    data class RequestBody(
        val anthropic_version: String = "vertex-2023-10-16",
        val messages: List<Message>,
        val max_tokens: Int = 8192,
        val stream: Boolean = false
    )

    fun generateTest(prompt: List<Message>): String {
        val requestBody = RequestBody(messages = prompt)
        val jsonBody = mapper.writeValueAsString(requestBody)

        val request = Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer $bearerToken")
            .addHeader("Content-Type", "application/json")
            .post(jsonBody.toRequestBody(mediaType))
            .build()

        return client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val errorBody = response.body.string()
                throw RuntimeException("API call failed with code: ${response.code}. Error: $errorBody")
            }

            val responseBody = response.body.string()
            parseResponse(responseBody)
        }
    }

    private fun parseResponse(responseBody: String): String {
        try {
            val responseJson: JsonNode = mapper.readTree(responseBody)
            val contentArray = responseJson.path("content")

            val extractedText = StringBuilder()
            for (contentNode in contentArray) {
                if (contentNode.path("type").asText() == "text") {
                    extractedText.append(contentNode.path("text").asText())
                }
            }

            val resultText = extractedText.toString()
                .replace("```kotlin", "")
                .replace("```", "")

            return resultText.ifEmpty {
                throw RuntimeException("No text content found in response")
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to parse response: ${e.message}\nResponse body: $responseBody", e)
        }
    }

}
