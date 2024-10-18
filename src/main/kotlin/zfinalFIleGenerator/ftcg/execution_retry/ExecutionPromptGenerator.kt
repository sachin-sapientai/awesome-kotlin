package zfinalFIleGenerator.ftcg.execution_retry

import zfinalFIleGenerator.ftcg.common.LlmApiClient


class ExecutionPromptGenerator {
    fun generatePrompt(config: ExecutionTestConfig): List<LlmApiClient.Message> {
        return listOf(
            LlmApiClient.Message(
                role = "user",
                content = "You are an expert senior Kotlin developer specializing in resolving test execution failures in unit test code."
            ),
            LlmApiClient.Message(
                role = "assistant",
                content = " Could you please provide the source code, test code files, and the specific execution failures you're encountering?"
            ),
            LlmApiClient.Message(
                role = "user",
                content = createInstructions(config)
            ),
            LlmApiClient.Message(
                role = "assistant",
                content = "Thank you for providing the context. To proceed with fixing the execution failures, I'll need the following:\\n\\n1. The complete source code \\n2. The full content of the test file \\n3. A list of the specific execution failures you're encountering\\n\\nCould you please provide these details?"
            ),
            LlmApiClient.Message(
                role = "user",
                content = createSourceCodeContent(config)
            )
        )
    }

    private fun createInstructions(config: ExecutionTestConfig): String {
        return buildString {
            append("""
            I need help fixing execution failures in a Kotlin unit test file. Here are the details:

            <file_details>
            <source_file_name>${config.sourceFileName}</source_file_name>
            <test_file_name>${config.testFileName}</test_file_name>
            </file_details>

            <instructions>
            <guidelines>
            1. Focus exclusively on the disabled tests that are failing. These tests are likely marked with @Disabled, @Ignore, or @Suppress("UNUSED") annotations in Kotlin.
            2. Analyze the execution failures deeply and propose fixes based on the specific issues encountered.
            3. Resolve all execution failures for the disabled tests while maintaining their original intent and coverage.
            4. Ensure all assertions use proper Kotlin testing assertions (e.g., assertEquals, assertThat, shouldBe, etc.).
            5. Check for Kotlin-specific issues like:
               - Proper handling of nullable types and safe calls
               - Correct usage of coroutines in tests (if applicable)
               - Appropriate use of backing fields and property access
               - Proper implementation of extension functions
               - Correct usage of companion objects and object declarations
            6. Verify proper usage of Kotlin test frameworks (JUnit5, KotlinTest, or Kotest) specific features.
            </guidelines>

            Please provide the corrected <test_file_name>${config.testFileName}</test_file_name> file that addresses the execution failures in the disabled tests.
            </instructions>

            <output_format>
            Your response should contain the complete, updated test file content, including both the fixed and unchanged tests. Do not include any additional explanations or text outside of the file content, inline comments, and the summary comment at the end of the file.
            Format your entire response as a single code block, starting with ``` and ending with ```.
            </output_format>
        """.trimIndent())
        }
    }

    private fun createSourceCodeContent(config: ExecutionTestConfig): String {
        return """
        <source_code>
        <file_name>${config.sourceFileName}</file_name>
        <content>
        ${config.sourceCode}
        </content>
        </source_code>

        <test_file_with_execution_issues>
        <file_name>${config.testFileName}</file_name>
        <content>
        ${config.testCode}
        </content>
        </test_file_with_execution_issues>

        <execution_issues_list>
        ${config.executionIssues}
        </execution_issues_list>
    """.trimIndent()
    }
}
