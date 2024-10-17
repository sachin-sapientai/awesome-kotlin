package org.example.zfinalFIleGenerator

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

    val bearerToken = "ya29.a0AcM612wb3x4rs8OAJfTs1BiZu1uG17Yirh_HkH7xHDYygs06NF3f5-tSCRQ5ansopTcrOSOmTti9-7r_-1yrcWH04U_cBt23ta9cLtTRCURXnEG01yoDuPlO4HcgRGXdrYn4Q5RYThaUV-R3fLPNPsa7WXc8tyZ6xH73F-ChHBoXDLc4aCgYKAfUSARISFQHGX2Mi4bseyvdofRDDxlhIHpq7pA0183"
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
