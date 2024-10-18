package zfinalFIleGenerator.ftcg.compilation_retry

import zfinalFIleGenerator.ftcg.common.LlmApiClient
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    // User provided paths and configurations
    val sourceFilePath = "/Users/sachinkumar/Desktop/kotlin_llm_poc/forked_kotlin_poc/awesome-kotlin/src/main/kotlin/link/kotlin/scripts/GithubTrending.kt"
    val testFilePath = "/Users/sachinkumar/Desktop/kotlin_llm_poc/forked_kotlin_poc/awesome-kotlin/src/test/kotlin/scripts/GithubTrendingSapientGeneratedTest.kt"


    val compilationIssuesPath = "/Users/sachinkumar/Desktop/kotlin_llm_poc/forked_kotlin_poc/awesome-kotlin/src/main/kotlin/zfinalFIleGenerator/ftcg/compilation_retry/compilation_issues.txt"
    val dslFilePath = "/Users/sachinkumar/Desktop/kotlin_llm_poc/awesome-kotlin/src/main/kotlin/link/kotlin/scripts/dsl/DSL.kt"

    // Initialize clients and generators
    val bearerToken = "ya29.a0AcM612whnOyOjEO1SRSdCEitiq-jtyzjeczbF7CQ3Agcp_f2r3vZYMCKMLuuFps85iyaiW9IaicWgk-fts-c7hzKjAITppQJBdngE2Sr_FlLha33h93gHJW_PMYp3lDbdLvB9NDL_MDoJlA0E2eXHs0btKxD0hSuguGvxLkso4mm6TVAaCgYKAfASARISFQHGX2MiVxsGlYtC10HmeEJmPhe4xQ0183"
    val llmClient = LlmApiClient(bearerToken)
    val promptGenerator = CompilationPromptGenerator()
    val testGenerator = CompilationTestGenerator(llmClient, promptGenerator)

    try {
        // Read necessary file contents
        val sourceCode = Files.readString(Paths.get(sourceFilePath))
        val testFileCode = if (Files.exists(Paths.get(testFilePath))) {
            Files.readString(Paths.get(testFilePath))
        } else {
            "" // Empty string if test file doesn't exist yet
        }
        val dslContent = Files.readString(Paths.get(dslFilePath))
        val compilationIssues = Files.readString(Paths.get(compilationIssuesPath))

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
        val config = CompilationTestConfig(
            sourceFileName = sourceFileName,
            testFileName = testFileName,
            sourceCode = sourceCodeWithContext,
            testCode = testFileCode,
            compilationIssues = compilationIssues,
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
