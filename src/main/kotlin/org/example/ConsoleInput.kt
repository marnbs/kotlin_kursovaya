package org.example

import kotlin.system.exitProcess

private fun readConsoleLine(prompt: String): String? {
    print(prompt)
    return readlnOrNull()?.trim()
}

private fun exitOnClosedInput(): Nothing {
    println()
    println("Ввод закрыт. Запустите приложение в интерактивной консоли: .\\gradlew.bat run")
    exitProcess(0)
}

fun readInt(prompt: String): Int {
    while (true) {
        val input = readConsoleLine(prompt) ?: exitOnClosedInput()
        val value = input.toIntOrNull()
        if (value != null) return value
        println("Ошибка: введите целое число.")
    }
}

fun readNonNegativeInt(prompt: String): Int {
    while (true) {
        val value = readInt(prompt)
        if (value >= 0) return value
        println("Ошибка: значение не может быть отрицательным.")
    }
}

fun readPositiveDouble(prompt: String): Double {
    while (true) {
        val input = readConsoleLine(prompt) ?: exitOnClosedInput()
        val value = input.replace(',', '.').toDoubleOrNull()
        if (value != null && value >= 0.0) return value
        println("Ошибка: введите неотрицательное число, например 350.50.")
    }
}

fun readNonEmptyString(prompt: String): String {
    while (true) {
        val value = readConsoleLine(prompt) ?: exitOnClosedInput()
        if (value.isNotEmpty()) return value
        println("Ошибка: строка не может быть пустой.")
    }
}

fun readOptionalString(prompt: String, currentValue: String): String {
    return readConsoleLine("$prompt [$currentValue]: ")?.takeIf { it.isNotEmpty() } ?: currentValue
}

fun readOptionalInt(prompt: String, currentValue: Int): Int {
    while (true) {
        val input = readConsoleLine("$prompt [$currentValue]: ")
        if (input.isNullOrEmpty()) return currentValue
        val value = input.toIntOrNull()
        if (value != null && value >= 0) return value
        println("Ошибка: введите неотрицательное целое число или оставьте поле пустым.")
    }
}

fun readOptionalDouble(prompt: String, currentValue: Double): Double {
    while (true) {
        val input = readConsoleLine("$prompt [$currentValue]: ")?.replace(',', '.')
        if (input.isNullOrEmpty()) return currentValue
        val value = input.toDoubleOrNull()
        if (value != null && value >= 0.0) return value
        println("Ошибка: введите неотрицательное число или оставьте поле пустым.")
    }
}

fun readFilename(prompt: String, defaultName: String): String {
    return readConsoleLine("$prompt [$defaultName, Enter - по умолчанию]: ")
        ?.takeIf { it.isNotEmpty() }
        ?: defaultName
}
