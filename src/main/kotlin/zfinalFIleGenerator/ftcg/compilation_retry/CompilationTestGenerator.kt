package zfinalFIleGenerator.ftcg.compilation_retry

import zfinalFIleGenerator.ftcg.common.LlmApiClient
import java.io.File

class CompilationTestGenerator(private val llmClient: LlmApiClient, private val promptGenerator: CompilationPromptGenerator) {
    fun generateAndSaveTest(config: CompilationTestConfig, outputPath: String) {
        val prompt = promptGenerator.generatePrompt(config)
        val generatedTest = llmClient.generateTest(prompt)
        File(outputPath).writeText(generatedTest)
    }
}
