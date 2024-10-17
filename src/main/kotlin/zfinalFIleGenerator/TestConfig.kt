package org.example.zfinalFIleGenerator

data class TestConfig(
    val sourceFileName: String,
    val testFileName: String,
    val sourceCode: String,
    val testFramework: String = "JUnit 5"
)
