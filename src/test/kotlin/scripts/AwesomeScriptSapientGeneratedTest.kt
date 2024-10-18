
package link.kotlin.scripts.scripting

import link.kotlin.scripts.dsl.PlatformType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromClassContext
import kotlin.script.experimental.jvm.jvm

class AwesomeScriptSapientGeneratedTest {

    @Test
    fun `test AwesomeScript class declaration`() {
        val awesomeScript = AwesomeScript::class
        assertTrue(awesomeScript.isAbstract)
        assertEquals("AwesomeScript", awesomeScript.simpleName)
        assertTrue(awesomeScript.annotations.any { it is KotlinScript })
    }

    @Test
    fun `test KotlinScript annotation properties`() {
        val kotlinScriptAnnotation = AwesomeScript::class.annotations.filterIsInstance<KotlinScript>().first()
        assertEquals("Awesome Kotlin Executor", kotlinScriptAnnotation.displayName)
        assertEquals("awesome.kts", kotlinScriptAnnotation.fileExtension)
        assertEquals(LinkScriptCompilationConfiguration::class, kotlinScriptAnnotation.compilationConfiguration)
    }

    @Test
    fun `test LinkScriptCompilationConfiguration default imports`() {
        val defaultImports = LinkScriptCompilationConfiguration[ScriptCompilationConfiguration.defaultImports]

        assertNotNull(defaultImports)
        assertTrue(defaultImports!!.contains("link.kotlin.scripts.dsl.*"))
        assertTrue(defaultImports.contains("link.kotlin.scripts.dsl.PlatformType.*"))
        PlatformType.values().forEach { platformType ->
            assertTrue(defaultImports.contains("link.kotlin.scripts.dsl.PlatformType.$platformType"))
        }
    }

//    @Test
//    fun `test LinkScriptCompilationConfiguration JVM configuration`() {
//        val jvmConfig = LinkScriptCompilationConfiguration[ScriptCompilationConfiguration.jvm]
//
//        assertNotNull(jvmConfig)
//
//        val testConfiguration = ScriptCompilationConfiguration(body = jvmConfig!!)
//
//        val dependenciesFromContext = testConfiguration[ScriptCompilationConfiguration.dependencies]
//            ?.filterIsInstance<ScriptDependenciesFromClassContext>()
//            ?.firstOrNull()
//
//        assertNotNull(dependenciesFromContext)
//        assertEquals(AwesomeScript::class, dependenciesFromContext?.baseClass)
//        assertEquals(listOf("awesome-kotlin", "kotlin-stdlib"), dependenciesFromContext?.classpath)
//
//        val compilerOptions = testConfiguration[ScriptCompilationConfiguration.compilerOptions]
//        assertTrue(compilerOptions?.contains("-Xskip-prerelease-check") == true)
//        assertTrue(compilerOptions?.contains("-Xallow-unstable-dependencies") == true)
//    }
//
//    @Test
//    fun `test LinkScriptCompilationConfiguration IDE configuration`() {
//        val ideConfig = LinkScriptCompilationConfiguration[ScriptCompilationConfiguration.ide]
//
//        assertNotNull(ideConfig)
//
//        val testConfiguration = ScriptCompilationConfiguration(body = ideConfig!!)
//
//        val acceptedLocations = testConfiguration[ScriptCompilationConfiguration.ide]?.get(ScriptCompilationConfiguration.acceptedLocations)
//        assertEquals(listOf(ScriptAcceptedLocation.Project), acceptedLocations)
//    }

    @ParameterizedTest
    @EnumSource(PlatformType::class)
    fun `test PlatformType enum values`(platformType: PlatformType) {
        assertNotNull(platformType)
    }
}
