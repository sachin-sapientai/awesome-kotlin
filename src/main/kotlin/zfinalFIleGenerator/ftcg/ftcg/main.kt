package zfinalFIleGenerator.ftcg.ftcg


import zfinalFIleGenerator.ftcg.common.LlmApiClient
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val baseSourcePath = "/Users/sachinkumar/Desktop/kotlin_llm_poc/awesome-kotlin/src/main/kotlin/link/kotlin/scripts"
    val baseTestPath = "/Users/sachinkumar/Desktop/kotlin_llm_poc/awesome-kotlin/src/test/kotlin/link/kotlin/scripts"
    val packageName = ""

    val sourcePath = Paths.get(baseSourcePath, packageName)
    val testPath = Paths.get(baseTestPath, packageName)

    // Ensure the test directory exists
    Files.createDirectories(testPath)

    val bearerToken = "xxx"
    val llmClient = LlmApiClient(bearerToken)
    val promptGenerator = PromptGenerator()
    val testGenerator = TestGenerator(llmClient, promptGenerator)

    // Get all Kotlin files in the specified package
    val kotlinFiles = Files.walk(sourcePath)
        .filter { it.toString().endsWith(".kt") }
        .toList()

    // Read the DSL.kt file content once before the loop
    val dslFilePath = Paths.get("/Users/sachinkumar/Desktop/kotlin_llm_poc/awesome-kotlin/src/main/kotlin/link/kotlin/scripts/dsl/DSL.kt")
    val dslContent = Files.readString(dslFilePath)
    val additionalContextContent = "<additional_context>${dslContent}</additional_context>"

    kotlinFiles.forEach { file ->
        val sourceFileName = file.fileName.toString()
        val testFileName = "${sourceFileName.removeSuffix(".kt")}SapientGeneratedTest.kt"
        val sourceCode = Files.readString(file) + "\n" + additionalContextContent

        val config = TestConfig(
            sourceFileName = sourceFileName,
            testFileName = testFileName,
            sourceCode = sourceCode,
            testFramework = "JUnit 5"
        )

        val outputPath = testPath.resolve(testFileName).toString()

        try {
            testGenerator.generateAndSaveTest(config, outputPath)
            println("Test file has been generated and saved to: $outputPath")
        } catch (e: Exception) {
            println("Error generating test for $sourceFileName: ${e.message}")
            e.printStackTrace()
        }
    }
}
