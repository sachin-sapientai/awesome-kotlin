package zfinalFIleGenerator.ftcg.execution_retry

data class ExecutionTestConfig(
    val sourceFileName: String,
    val testFileName: String,
    val sourceCode: String,
    val testCode: String,
    val executionIssues: String,
    val testFramework: String
)
