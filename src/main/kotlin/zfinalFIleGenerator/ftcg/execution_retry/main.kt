package zfinalFIleGenerator.ftcg.execution_retry

import zfinalFIleGenerator.ftcg.common.LlmApiClient
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    // User provided paths and configurations
    val sourceFilePath = "/Users/sachinkumar/Desktop/kotlin_llm_poc/forked_kotlin_poc/awesome-kotlin/src/main/kotlin/link/kotlin/scripts/ArticlesProcessor.kt"
    val testFilePath = "/Users/sachinkumar/Desktop/kotlin_llm_poc/forked_kotlin_poc/awesome-kotlin/src/test/kotlin/scripts/ArticlesProcessorSapientGeneratedTest.kt"


    val executionIssuesPath = "/Users/sachinkumar/Desktop/kotlin_llm_poc/forked_kotlin_poc/awesome-kotlin/src/main/kotlin/zfinalFIleGenerator/ftcg/execution_retry/execution_issues.txt"
    val dslFilePath = "/Users/sachinkumar/Desktop/kotlin_llm_poc/awesome-kotlin/src/main/kotlin/link/kotlin/scripts/dsl/DSL.kt"

    // Initialize clients and generators
    val bearerToken = "ya29.a0AcM612zy-KeqJ4wm1qRYqr1H72rJyNJNJ01706DmWH_VuWwk5jvEuk0MyAJ1HJyh9Cf9I1y8xZM9rfnugZumeP0o_MJfcGMM8BWwUy5yhUCsI1nXJ2OKdtVrUmTIhZakTSn08VsFKr5OiDLSGhy2s87hCRalASrASueaOFuo4vNsQdhOaCgYKAfcSARISFQHGX2MilO36gOgto29c-88zudIeFg0183"
    val llmClient = LlmApiClient(bearerToken)
    val promptGenerator = ExecutionPromptGenerator()
    val testGenerator = ExecutionTestGenerator(llmClient, promptGenerator)

    try {
        // Read necessary file contents
        val sourceCode = Files.readString(Paths.get(sourceFilePath))
        val testFileCode = if (Files.exists(Paths.get(testFilePath))) {
            Files.readString(Paths.get(testFilePath))
        } else {
            "" // Empty string if test file doesn't exist yet
        }
        val dslContent = Files.readString(Paths.get(dslFilePath))
        val compilationIssues = Files.readString(Paths.get(executionIssuesPath))

        // Prepare the additional context
        val sourceCodeWithContext = """
            $sourceCode
            
            <additional_context>
            $dslContent
            </additional_context>
        """.trimIndent()

        // Extract file names from paths
        val sourceFileName = Paths.get(sourceFilePath).fileName.toString()
        val testFileName = Paths.get(testFilePath).fileName.toString()

        // Create configuration
        val config = ExecutionTestConfig(
            sourceFileName = sourceFileName,
            testFileName = testFileName,
            sourceCode = sourceCodeWithContext,
            testCode = testFileCode,
            executionIssues = compilationIssues,
            testFramework = "JUnit 5"
        )

        // Generate and save the test
        testGenerator.generateAndSaveTest(config, testFilePath)
        println("Test file has been generated and saved to: $testFilePath")

    } catch (e: Exception) {
        println("Error generating test: ${e.message}")
        e.printStackTrace()
    }
}
