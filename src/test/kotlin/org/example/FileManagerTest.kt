package org.example

import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FileManagerTest {
    private val routes = listOf(
        BusRoute(1, "No101", "Moscow", "Tver", "08:00", 180, 650.0, 20),
        BusRoute(2, "No,202", "City \"A\"", "City B", "09:30", 240, 850.5, 5)
    )

    @Test
    fun saveAndLoadJsonPreservesRoutes() {
        val file = Files.createTempFile("bus-routes-", ".json")

        saveToJson(routes, file.toString())
        val loaded = loadFromJson(file.toString())

        assertEquals(routes, loaded)
        assertTrue(Files.size(file) > 0)
    }

    @Test
    fun saveAndLoadCsvPreservesRoutesWithEscapedCells() {
        val file = Files.createTempFile("bus-routes-", ".csv")

        saveToCsv(routes, file.toString())
        val loaded = loadFromCsv(file.toString())

        assertEquals(routes, loaded)
        assertTrue(Files.readString(file).contains("\"No,202\""))
        assertTrue(Files.readString(file).contains("\"City \"\"A\"\"\""))
    }

    @Test
    fun loadMissingFilesReturnsEmptyList() {
        val missingFile = Files.createTempDirectory("bus-routes-missing-").resolve("missing.json")

        assertEquals(emptyList(), loadFromJson(missingFile.toString()))
        assertEquals(emptyList(), loadFromCsv(missingFile.toString()))
    }
}
