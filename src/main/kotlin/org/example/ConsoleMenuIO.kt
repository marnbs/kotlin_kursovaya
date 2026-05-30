package org.example

fun consoleMenuIO() {
    var choice: Int
    do {
        println(
            """

            Меню ввода/вывода автобусных рейсов:
            1. Добавить новый рейс
            2. Загрузить данные из JSON-файла
            3. Загрузить данные из CSV-файла
            4. Сохранить данные в JSON-файл
            5. Сохранить данные в CSV-файл
            6. Вернуться в главное меню
            """.trimIndent()
        )

        choice = readInt("Выберите пункт: ")
        when (choice) {
            1 -> BusRouteManager.addRouteInteractive()
            2 -> replaceRoutes(loadFromJson(readFilename("Имя JSON-файла", "bus_routes.json")))
            3 -> replaceRoutes(loadFromCsv(readFilename("Имя CSV-файла", "bus_routes.csv")))
            4 -> {
                saveToJson(RouteStorage.routes, readFilename("Имя JSON-файла", "bus_routes.json"))
                println("Выберите следующий пункт меню или 6, чтобы вернуться в главное меню.")
            }
            5 -> {
                saveToCsv(RouteStorage.routes, readFilename("Имя CSV-файла", "bus_routes.csv"))
                println("Выберите следующий пункт меню или 6, чтобы вернуться в главное меню.")
            }
            6 -> println("Возврат в главное меню.")
            else -> println("Ошибка: неизвестный пункт меню.")
        }
    } while (choice != 6)
}

private fun replaceRoutes(newRoutes: MutableList<BusRoute>) {
    RouteStorage.routes.clear()
    RouteStorage.routes.addAll(newRoutes)
    println("Загружено рейсов: ${RouteStorage.routes.size}.")
}
