package org.example

import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AnnotationUsageTest {
    private val testSources = listOf(
        Path.of("src/test/kotlin/org/example/BusRouteManagerTest.kt"),
        Path.of("src/test/kotlin/org/example/FileManagerTest.kt")
    )

    @Test
    fun testSourceFilesImportTestAnnotation() {
        testSources.forEach { file ->
            val source = Files.readString(file)

            assertTrue(
                source.contains("import kotlin.test.Test"),
                "Файл $file должен импортировать аннотацию kotlin.test.Test"
            )
        }
    }

    @Test
    fun eachTestMethodHasTestAnnotation() {
        val missingAnnotations = testSources.flatMap { file ->
            findTestMethodsWithoutAnnotation(file)
        }

        assertEquals(emptyList(), missingAnnotations)
    }

    private fun findTestMethodsWithoutAnnotation(file: Path): List<String> {
        val lines = Files.readAllLines(file)
        return lines.mapIndexedNotNull { index, line ->
            val trimmedLine = line.trimStart()
            if (!trimmedLine.startsWith("fun ") || !trimmedLine.endsWith(") {")) {
                return@mapIndexedNotNull null
            }

            val previousMeaningfulLine = lines
                .take(index)
                .lastOrNull { it.isNotBlank() }
                ?.trim()

            if (previousMeaningfulLine == "@Test") {
                null
            } else {
                "$file:${index + 1} $trimmedLine"
            }
        }
    }
}
