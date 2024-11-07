package link.kotlin.scripts.utils

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

class CopyTaskSapientGeneratedTest : FunSpec({

    context("copyResources function") {
        test("should copy single file successfully") {
            mockkStatic(Files::class, Paths::class)
            
            val fromPath = Paths.get("source.txt")
            val toPath = Paths.get("destination.txt")
            
            every { Paths.get("source.txt") } returns fromPath
            every { Paths.get("destination.txt") } returns toPath
            every { Files.copy(fromPath, toPath, REPLACE_EXISTING) } returns toPath
            
            copyResources("source.txt" to "destination.txt")
            
            verify(exactly = 1) { Files.copy(fromPath, toPath, REPLACE_EXISTING) }
        }
        
        test("should copy multiple files successfully") {
            mockkStatic(Files::class, Paths::class)
            
            val fromPath1 = Paths.get("source1.txt")
            val toPath1 = Paths.get("destination1.txt")
            val fromPath2 = Paths.get("source2.txt")
            val toPath2 = Paths.get("destination2.txt")
            
            every { Paths.get("source1.txt") } returns fromPath1
            every { Paths.get("destination1.txt") } returns toPath1
            every { Paths.get("source2.txt") } returns fromPath2
            every { Paths.get("destination2.txt") } returns toPath2
            every { Files.copy(fromPath1, toPath1, REPLACE_EXISTING) } returns toPath1
            every { Files.copy(fromPath2, toPath2, REPLACE_EXISTING) } returns toPath2
            
            copyResources(
                "source1.txt" to "destination1.txt",
                "source2.txt" to "destination2.txt"
            )
            
            verify(exactly = 1) { Files.copy(fromPath1, toPath1, REPLACE_EXISTING) }
            verify(exactly = 1) { Files.copy(fromPath2, toPath2, REPLACE_EXISTING) }
        }
        
        test("should throw NoSuchFileException when source file doesn't exist") {
            mockkStatic(Files::class, Paths::class)
            
            val fromPath = Paths.get("nonexistent.txt")
            val toPath = Paths.get("destination.txt")
            
            every { Paths.get("nonexistent.txt") } returns fromPath
            every { Paths.get("destination.txt") } returns toPath
            every { Files.copy(fromPath, toPath, REPLACE_EXISTING) } throws NoSuchFileException("nonexistent.txt")
            
            shouldThrow<NoSuchFileException> {
                copyResources("nonexistent.txt" to "destination.txt")
            }
        }
        
        test("should handle empty vararg parameter") {
            copyResources()
        }
        
        test("should replace existing files") {
            mockkStatic(Files::class, Paths::class)
            
            val fromPath = Paths.get("source.txt")
            val toPath = Paths.get("existing.txt")
            
            every { Paths.get("source.txt") } returns fromPath
            every { Paths.get("existing.txt") } returns toPath
            every { Files.copy(fromPath, toPath, REPLACE_EXISTING) } returns toPath
            
            copyResources("source.txt" to "existing.txt")
            
            verify(exactly = 1) { Files.copy(fromPath, toPath, REPLACE_EXISTING) }
        }
    }
})