package org.example.zfinalFIleGenerator

import java.io.File

class TestGenerator(private val llmClient: LlmApiClient, private val promptGenerator: PromptGenerator) {
    fun generateAndSaveTest(config: TestConfig, outputPath: String) {
        val prompt = promptGenerator.generatePrompt(config)
        val generatedTest = llmClient.generateTest(prompt)
        File(outputPath).writeText(generatedTest)
    }
}