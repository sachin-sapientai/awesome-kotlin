package org.example.zfinalFIleGenerator


class PromptGenerator {
    fun generatePrompt(config: TestConfig): List<LlmApiClient.Message> {
        return listOf(
            LlmApiClient.Message(
                role = "user",
                content = "You are an expert senior Kotlin developer with expertise in generating comprehensive Kotlin unit tests from provided source code."
            ),
            LlmApiClient.Message(
                role = "assistant",
                content = "Can you assist me with guidelines for generating tests and provide the corresponding code for which the tests should be generated?"
            ),
            LlmApiClient.Message(
                role = "user",
                content = createInstructions(config)
            ),
            LlmApiClient.Message(
                role = "assistant",
                content = "Could you please provide the source code for which the test file should be generated?"
            ),
            LlmApiClient.Message(
                role = "user",
                content = createSourceCodeContent(config)
            )
        )
    }

    private fun createInstructions(config: TestConfig): String {
        return """
            <instructions>
            You will be provided with Source file: <source_file_name>${config.sourceFileName}</source_file_name> Generate a ${config.testFramework} test file in Kotlin with <test_file_name>${config.testFileName}</test_file_name>

            Your task is to generate a comprehensive, fully functional unit test file that addresses the following objectives:

            <objectives>
            1. Completeness: Generate a complete, compilable unit test file with all necessary imports, no TODOs, fully implemented tests that all pass, covering various scenarios including edge cases.
            2. Strictly Ensure all necessary imports are included for the code to run, such as imports for the required classes, annotations (e.g., @Test, @ParameterizedTest), and any methods or utilities from kotlinx.test or other Kotlin testing libraries that are used in the code.
            3. Coverage: Ensure 100% branch coverage of the source code, including all functions and edge cases.
            4. Passing tests: Self review and correct to Ensure that ALL tests in the generated file pass successfully.
            5. Kotlin-specific features: Thoroughly test Kotlin-specific features such as null safety, data classes, and extension functions.
            </objectives>

            <guidelines>
            1. Use ${config.testFramework} for Kotlin or kotlinx.test: Utilize appropriate test annotations and lifecycle management for Kotlin.
            2. Assertions: Use Kotlin-specific assertions from kotlinx.test, Strikt, or Kotest for more idiomatic and expressive assertions.
            3. Mocking: Use MockK for mocking dependencies and verifying interactions where appropriate.
            4. Parameterized Testing: Utilize Kotlin's parameterized testing capabilities for testing multiple scenarios efficiently.
            5. Null Safety Testing: Include tests for both null and non-null scenarios to verify Kotlin's null safety features.
            6. Extension Function Testing: If testing extension functions, ensure they are tested with various receivers.
            7. Inline Function Testing: For inline functions, test their behavior in different contexts.
            8. Private Function Testing: Test private functions indirectly through public functions that call them.
            9. Exception Handling: Test exception scenarios using Kotlin's exception handling syntax and appropriate assertion functions.
            10. Naming Convention: Use descriptive test function names that clearly indicate the scenario being tested and the expected outcome, following Kotlin naming conventions.
            11. Test Isolation: Ensure each test is independent and does not rely on the state from other tests. Use appropriate setup and teardown functions for Kotlin tests.
            12. Boundary Value Analysis: Include tests for boundary values, especially for numerical operations.
            </guidelines>

            <self_review_guidelines>
            After generating the test code, perform a self-review and fix common errors based on the following guidelines:
            1. Test function visibility:
               - Don't use private classes, methods, variables in tests.
               - Don't use private constructors or companion object methods for creating objects, as they aren't accessible in the test module.
            2. Exception handling:
               - Use Kotlin's exception handling syntax and appropriate assertion functions for testing exceptions.
            3. Coroutine usage:
               - Suspension functions can only be called within a coroutine body.
            4. Imports:
               - Ensure that all necessary imports are included to prevent compilation errors.

            After applying these guidelines, update the code to ensure there are no compilation issues.
            </self_review_guidelines>

            <output_format>
            Generate a complete, updated test file in your response. Provide ONLY the Kotlin code for the test file, without any explanations, comments, or additional text. The response should start with the package declaration and end with the closing brace of the test class. Format your entire response as a single code block, starting with ```kotlin and ending with ```.
            - Perform a self-review based on the <self_review_guidelines> and make necessary adjustments.
            </output_format>
            </instructions>
        """.trimIndent()
    }

    private fun createSourceCodeContent(config: TestConfig): String {
        return """
            <source_code>
            <file_name>${config.sourceFileName}</file_name>
            <content>
            ${config.sourceCode}
            </content>
            </source_code>
        """.trimIndent()
    }
}
