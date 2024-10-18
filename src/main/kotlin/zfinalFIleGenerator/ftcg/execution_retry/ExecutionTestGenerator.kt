package zfinalFIleGenerator.ftcg.execution_retry

import zfinalFIleGenerator.ftcg.common.LlmApiClient
import java.io.File

class ExecutionTestGenerator(private val llmClient: LlmApiClient, private val promptGenerator: ExecutionPromptGenerator) {
    fun generateAndSaveTest(config: ExecutionTestConfig, outputPath: String) {
        val prompt = promptGenerator.generatePrompt(config)
        val generatedTest = llmClient.generateTest(prompt)
        File(outputPath).writeText(generatedTest)
    }
}
