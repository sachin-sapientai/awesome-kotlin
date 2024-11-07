package link.kotlin.scripts.scripting

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromClassContext
import kotlin.script.experimental.jvm.jvm

class AwesomeScriptSapientGeneratedTest : FunSpec({

    test("AwesomeScript class should be abstract") {
        AwesomeScript::class.isAbstract shouldBe true
    }

    test("AwesomeScript should have KotlinScript annotation") {
        val annotation = AwesomeScript::class.annotations.firstOrNull { it is KotlinScript }
        annotation.shouldBeInstanceOf<KotlinScript>()
        with(annotation as KotlinScript) {
            displayName shouldBe "Awesome Kotlin Executor"
            fileExtension shouldBe "awesome.kts"
            compilationConfiguration shouldBe LinkScriptCompilationConfiguration::class
        }
    }

    test("LinkScriptCompilationConfiguration should be an object") {
        LinkScriptCompilationConfiguration::class.objectInstance shouldBe LinkScriptCompilationConfiguration
    }

    test("LinkScriptCompilationConfiguration should inherit from ScriptCompilationConfiguration") {
        LinkScriptCompilationConfiguration shouldBeInstanceOf ScriptCompilationConfiguration::class
    }

    test("LinkScriptCompilationConfiguration should have correct default imports") {
        val configuration = LinkScriptCompilationConfiguration
        val defaultImports = configuration[ScriptCompilationConfiguration.defaultImports]
        defaultImports shouldBe listOf(
            "link.kotlin.scripts.dsl.*",
            "link.kotlin.scripts.dsl.PlatformType.*",
            "link.kotlin.scripts.dsl.PlatformType.ANDROID",
            "link.kotlin.scripts.dsl.PlatformType.COMMON",
            "link.kotlin.scripts.dsl.PlatformType.IOS",
            "link.kotlin.scripts.dsl.PlatformType.JS",
            "link.kotlin.scripts.dsl.PlatformType.JVM",
            "link.kotlin.scripts.dsl.PlatformType.NATIVE",
            "link.kotlin.scripts.dsl.PlatformType.WASM"
        )
    }

    test("LinkScriptCompilationConfiguration should have correct JVM configuration") {
        val configuration = LinkScriptCompilationConfiguration
        val jvmConfiguration = configuration[ScriptCompilationConfiguration.jvm]
        jvmConfiguration.shouldBeInstanceOf<JvmScriptCompilationConfigurationBuilder>()

        val dependenciesFromContext = jvmConfiguration.dependenciesFromClassContext
        dependenciesFromContext shouldBe listOf(
            AwesomeScript::class,
            "awesome-kotlin",
            "kotlin-stdlib"
        )

        val compilerOptions = jvmConfiguration.compilerOptions
        compilerOptions shouldBe listOf("-Xskip-prerelease-check", "-Xallow-unstable-dependencies")
    }

    test("LinkScriptCompilationConfiguration should have correct IDE configuration") {
        val configuration = LinkScriptCompilationConfiguration
        val ideConfiguration = configuration[ScriptCompilationConfiguration.ide]
        ideConfiguration.shouldBeInstanceOf<IdeScriptCompilationConfigurationBuilder>()

        val acceptedLocations = ideConfiguration.acceptedLocations
        acceptedLocations shouldBe listOf(ScriptAcceptedLocation.Project)
    }
})