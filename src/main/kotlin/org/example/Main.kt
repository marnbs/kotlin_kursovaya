package org.example

fun main() {
    addSampleRoutes()

    var choice: Int
    do {
        println(
            """

            Главное меню:
            1. Ввод/вывод автобусных рейсов
            2. Отобразить автобусные рейсы
            3. Найти автобусный рейс
            4. Редактировать автобусный рейс
            5. Удалить автобусный рейс
            6. Вычислить агрегированные показатели
            7. Выход
            """.trimIndent()
        )

        choice = readInt("Выберите пункт: ")
        when (choice) {
            1 -> consoleMenuIO()
            2 -> consoleMenuOps()
            3 -> BusRouteManager.findRoutesInteractive()
            4 -> BusRouteManager.editRouteInteractive()
            5 -> BusRouteManager.deleteRouteInteractive()
            6 -> BusRouteManager.showAggregates()
            7 -> println("Программа завершена.")
            else -> println("Ошибка: неизвестный пункт меню.")
        }
    } while (choice != 7)
}

private fun addSampleRoutes() {
    RouteStorage.routes.addAll(
        listOf(
            BusRoute(1, "№101", "Москва", "Тверь", "08:00", 180, 650.0, 20),
            BusRoute(2, "№202", "Москва", "Ярославль", "09:30", 240, 850.0, 5),
            BusRoute(3, "№303", "Тверь", "Санкт-Петербург", "11:00", 480, 1200.0, 0)
        )
    )
}
