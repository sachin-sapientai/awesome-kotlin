package zfinalFIleGenerator.ftcg.compilation_retry

data class CompilationTestConfig(
    val sourceFileName: String,
    val testFileName: String,
    val sourceCode: String,
    val testCode: String,
    val compilationIssues: String,
    val testFramework: String
)
