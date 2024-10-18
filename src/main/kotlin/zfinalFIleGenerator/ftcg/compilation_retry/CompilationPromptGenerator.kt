package zfinalFIleGenerator.ftcg.compilation_retry

import zfinalFIleGenerator.ftcg.common.LlmApiClient


class CompilationPromptGenerator {
    fun generatePrompt(config: CompilationTestConfig): List<LlmApiClient.Message> {
        return listOf(
            LlmApiClient.Message(
                role = "user",
                content = "You are an expert senior Kotlin developer specializing in resolving compilation issues in unit test code."
            ),
            LlmApiClient.Message(
                role = "assistant",
                content = "Could you please provide the source code, test code files, and the specific compilation issues and guidelines for resolving compilation issues?"
            ),
            LlmApiClient.Message(
                role = "user",
                content = createInstructions(config)
            ),
            LlmApiClient.Message(
                role = "assistant",
                content = "Thank you for providing the context. To proceed with fixing the compilation issues, I'll need the following:\n"+
                    "\n"+
                    "1.Source Code file with additional context \n"+
                    "2. Test file with compilation issue  \n"+
                    "3. list of the compilation errors you're encountering\n"+
                    "\n"+
                    "Could you please provide these details?"
            ),
            LlmApiClient.Message(
                role = "user",
                content = createSourceCodeContent(config)
            )
        )
    }

    private fun createInstructions(config: CompilationTestConfig): String {
        return buildString {
            append("""
            Here are the details:
            <file_details>
            <source_file_name>${config.sourceFileName}</source_file_name>
            <test_file_name>${config.testFileName}</test_file_name>
            </file_details>

            <instructions>
            Any compilation issues will be listed along with the method names and corresponding error messages.
            <guidelines>
            1. Do not modify any tests that do not have compilation issues.
            2. Resolve all compilation errors while maintaining the test coverage and structure.
            3. Ensure all imports are correct and present, including Kotlin-specific imports (e.g., kotlinx.coroutines for suspend functions).
            4. Do not remove any tests unless absolutely necessary to resolve compilation errors.
            5. If error is regarding object instantiation:
               - Consider using object declaration for singletons
               - Use data classes when appropriate
               - Use companion objects for factory methods
               - For mocking, ensure proper mockk/mockito-kotlin setup
            6. For handling exceptions:
               - Use @Throws annotation when necessary
               - Consider using runCatching or try-catch blocks where appropriate
               - For coroutines, use proper exception handling mechanisms (e.g., CoroutineExceptionHandler)
            7. Kotlin-specific considerations:
               - Ensure proper nullability annotations (?, !!, lateinit)
               - Use appropriate scope functions (let, run, with, apply, also)
               - Handle Java interoperability issues (@JvmStatic, @JvmField if needed)
               - Consider using backing properties for custom getters/setters
            </guidelines>

            Please provide the corrected <test_file_name>${config.testFileName}</test_file_name> file that addresses all compilation errors and maintains comprehensive test coverage.
            </instructions>

            <output_format>
            Your response should always contain the full Test class, not partial code, without any additional explanations or text outside of the file content.
            Format your entire response as a single code block, starting with ``` and ending with ```.
            Ensure the response includes required Kotlin file headers and proper package declaration.
            </output_format>
        """.trimIndent())
        }
    }

    private fun createSourceCodeContent(config: CompilationTestConfig): String {
        return """
        <source_code>
        <file_name>${config.sourceFileName}</file_name>
        <content>
        ${config.sourceCode}
        </content>
        </source_code>

        <test_file_with_compilation_issues>
        <file_name>${config.testFileName}</file_name>
        <content>
        ${config.testCode}
        </content>
        </test_file_with_compilation_issues>

        <compilation_issues_list>
        ${config.compilationIssues}
        </compilation_issues_list>
    """.trimIndent()
    }
}
