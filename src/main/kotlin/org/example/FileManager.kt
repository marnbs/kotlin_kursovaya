package org.example

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

private val gson = GsonBuilder().setPrettyPrinting().create()

fun saveToJson(routes: List<BusRoute>, filename: String = "bus_routes.json") {
    try {
        val file = File(filename).absoluteFile
        file.writeText(gson.toJson(routes), Charsets.UTF_8)
        println("Данные сохранены в ${file.path}.")
    } catch (ex: Exception) {
        println("Ошибка сохранения JSON: ${ex.message}")
    }
}

fun loadFromJson(filename: String = "bus_routes.json"): MutableList<BusRoute> {
    val file = File(filename)
    if (!file.exists()) {
        println("Файл $filename не найден.")
        return mutableListOf()
    }

    return try {
        val type = object : TypeToken<MutableList<BusRoute>>() {}.type
        gson.fromJson<MutableList<BusRoute>>(file.readText(Charsets.UTF_8), type) ?: mutableListOf()
    } catch (ex: Exception) {
        println("Ошибка чтения JSON: ${ex.message}")
        mutableListOf()
    }
}

fun saveToCsv(routes: List<BusRoute>, filename: String = "bus_routes.csv") {
    try {
        val content = buildString {
            appendLine("id,routeNumber,departureCity,arrivalCity,departureTime,travelDurationMinutes,ticketPrice,availableSeats")
            routes.forEach { route ->
                appendLine(
                    listOf(
                        route.id.toString(),
                        route.routeNumber,
                        route.departureCity,
                        route.arrivalCity,
                        route.departureTime,
                        route.travelDurationMinutes.toString(),
                        route.ticketPrice.toString(),
                        route.availableSeats.toString()
                    ).joinToString(",") { it.toCsvCell() }
                )
            }
        }

        val file = File(filename).absoluteFile
        file.writeText(content, Charsets.UTF_8)
        println("Данные сохранены в ${file.path}.")
    } catch (ex: Exception) {
        println("Ошибка сохранения CSV: ${ex.message}")
    }
}

fun loadFromCsv(filename: String = "bus_routes.csv"): MutableList<BusRoute> {
    val file = File(filename)
    if (!file.exists()) {
        println("Файл $filename не найден.")
        return mutableListOf()
    }

    return try {
        file.readLines(Charsets.UTF_8)
            .drop(1)
            .mapNotNull { line -> parseCsvRoute(line) }
            .toMutableList()
    } catch (ex: Exception) {
        println("Ошибка чтения CSV: ${ex.message}")
        mutableListOf()
    }
}

private fun parseCsvRoute(line: String): BusRoute? {
    val parts = parseCsvLine(line)
    if (parts.size != 8) return null

    return BusRoute(
        id = parts[0].toIntOrNull() ?: return null,
        routeNumber = parts[1],
        departureCity = parts[2],
        arrivalCity = parts[3],
        departureTime = parts[4],
        travelDurationMinutes = parts[5].toIntOrNull() ?: return null,
        ticketPrice = parts[6].replace(',', '.').toDoubleOrNull() ?: return null,
        availableSeats = parts[7].toIntOrNull() ?: return null
    )
}

private fun String.toCsvCell(): String {
    val shouldQuote = any { it == ',' || it == '"' || it == '\n' || it == '\r' }
    val escaped = replace("\"", "\"\"")
    return if (shouldQuote) "\"$escaped\"" else escaped
}

private fun parseCsvLine(line: String): List<String> {
    val result = mutableListOf<String>()
    val current = StringBuilder()
    var inQuotes = false
    var index = 0

    while (index < line.length) {
        val char = line[index]
        when {
            char == '"' && inQuotes && index + 1 < line.length && line[index + 1] == '"' -> {
                current.append('"')
                index++
            }
            char == '"' -> inQuotes = !inQuotes
            char == ',' && !inQuotes -> {
                result.add(current.toString())
                current.clear()
            }
            else -> current.append(char)
        }
        index++
    }

    result.add(current.toString())
    return result
}
