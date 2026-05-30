package org.example

fun consoleMenuOps() {
    var choice: Int
    do {
        println(
            """

            Меню отображения автобусных рейсов:
            1. Отобразить все рейсы
            2. Отсортировать рейсы
            3. Отфильтровать рейсы
            4. Вернуться в главное меню
            """.trimIndent()
        )

        choice = readInt("Выберите пункт: ")
        when (choice) {
            1 -> BusRouteManager.showRoutes()
            2 -> BusRouteManager.sortRoutesInteractive()
            3 -> BusRouteManager.filterRoutesInteractive()
            4 -> println("Возврат в главное меню.")
            else -> println("Ошибка: неизвестный пункт меню.")
        }
    } while (choice != 4)
}
