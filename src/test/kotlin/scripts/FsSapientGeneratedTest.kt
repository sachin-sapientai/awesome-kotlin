
package link.kotlin.scripts.utils

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FsSapientGeneratedTest {

    @TempDir
    lateinit var tempDir: Path

    @BeforeEach
    fun setup() {
        mockkStatic(Files::class)
        mockkStatic(Paths::class)
    }

    @Test
    fun `writeFile with String path should write data to file`() {
        val path = "test.txt"
        val data = "Test data"
        val mockPath = mockk<Path>()

        every { Paths.get(path) } returns mockPath
        every { Files.write(any(), any<ByteArray>(), any(), any()) } returns mockPath

        writeFile(path, data)

        verify {
            Paths.get(path)
            Files.write(
                mockPath,
                data.toByteArray(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        }
    }

    @Test
    fun `writeFile with Path should write data to file`() {
        val path = tempDir.resolve("test.txt")
        val data = "Test data"

        every { Files.write(any(), any<ByteArray>(), any(), any()) } returns path

        writeFile(path, data)

        verify {
            Files.write(
                path,
                data.toByteArray(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "Some data", "Multi\nline\ndata"])
    fun `writeFile should handle different data contents`(data: String) {
        val path = tempDir.resolve("test.txt")

        every { Files.write(any(), any<ByteArray>(), any(), any()) } returns path

        writeFile(path, data)

        verify {
            Files.write(
                path,
                data.toByteArray(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        }
    }

    @Test
    fun `writeFile should handle null path`() {
        val path: String? = null
        val data = "Test data"

        assertFailsWith<NullPointerException> {
            writeFile(path!!, data)
        }
    }

    @Test
    fun `writeFile should handle empty path`() {
        val path = ""
        val data = "Test data"
        val mockPath = mockk<Path>()

        every { Paths.get(path) } returns mockPath
        every { Files.write(any(), any<ByteArray>(), any(), any()) } returns mockPath

        writeFile(path, data)

        verify {
            Paths.get(path)
            Files.write(
                mockPath,
                data.toByteArray(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        }
    }

    @Test
    fun `writeFile should handle file system errors`() {
        val path = tempDir.resolve("test.txt")
        val data = "Test data"

        every { Files.write(any(), any<ByteArray>(), any(), any()) } throws IllegalArgumentException("Mock file system error")

        assertFailsWith<IllegalArgumentException> {
            writeFile(path, data)
        }
    }

    @Test
    fun `writeFile should overwrite existing file`() {
        val path = tempDir.resolve("existing.txt")
        val initialData = "Initial data"
        val newData = "New data"

        Files.write(path, initialData.toByteArray())
        every { Files.write(any(), any<ByteArray>(), any(), any()) } returns path

        writeFile(path, newData)

        verify {
            Files.write(
                path,
                newData.toByteArray(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        }
    }

    @Test
    fun `writeFile should create new file if it doesn't exist`() {
        val path = tempDir.resolve("newfile.txt")
        val data = "New file data"

        every { Files.write(any(), any<ByteArray>(), any(), any()) } returns path

        writeFile(path, data)

        verify {
            Files.write(
                path,
                data.toByteArray(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        }
    }
}
