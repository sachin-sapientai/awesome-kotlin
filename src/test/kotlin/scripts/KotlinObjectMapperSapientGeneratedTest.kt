
package link.kotlin.scripts.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.LocalDate

class KotlinObjectMapperSapientGeneratedTest {

    @Test
    fun `KotlinObjectMapper should return an instance of ObjectMapper`() {
        val mapper = KotlinObjectMapper()
        mapper.shouldBeInstanceOf<ObjectMapper>()
    }

    @Test
    fun `KotlinObjectMapper should include kotlinModule`() {
        val mapper = KotlinObjectMapper()
        assertDoesNotThrow {
            mapper.writeValueAsString(TestDataClass("test", 42))
        }
    }

    @Test
    fun `KotlinObjectMapper should include JavaTimeModule`() {
        val mapper = KotlinObjectMapper()
        val date = LocalDate.of(2023, 1, 1)
        val json = mapper.writeValueAsString(date)
        val deserializedDate = mapper.readValue(json, LocalDate::class.java)
        deserializedDate shouldBe date
    }

    @Test
    fun `KotlinObjectMapper should correctly serialize and deserialize complex objects`() {
        val mapper = KotlinObjectMapper()
        val testObject = ComplexTestObject(
            string = "test",
            number = 42,
            date = LocalDate.of(2023, 1, 1),
            nullableField = null,
            list = listOf(1, 2, 3),
            nestedObject = NestedObject("nested")
        )

        val json = mapper.writeValueAsString(testObject)
        val deserializedObject = mapper.readValue(json, ComplexTestObject::class.java)

        deserializedObject shouldBe testObject
    }

    @Test
    fun `KotlinObjectMapper should handle null values correctly`() {
        val mapper = KotlinObjectMapper()
        val testObject = TestDataClass(null, 42)

        val json = mapper.writeValueAsString(testObject)
        val deserializedObject = mapper.readValue(json, TestDataClass::class.java)

        deserializedObject shouldBe testObject
    }

    private data class TestDataClass(val name: String?, val age: Int)

    private data class ComplexTestObject(
        val string: String,
        val number: Int,
        val date: LocalDate,
        val nullableField: String?,
        val list: List<Int>,
        val nestedObject: NestedObject
    )

    private data class NestedObject(val value: String)
}
