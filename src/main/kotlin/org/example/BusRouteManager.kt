package org.example

object BusRouteManager {
    fun addRouteInteractive() {
        println("\nДобавление нового рейса")
        val route = BusRoute(
            id = readUniqueId(),
            routeNumber = readNonEmptyString("Номер маршрута: "),
            departureCity = readNonEmptyString("Город отправления: "),
            arrivalCity = readNonEmptyString("Город прибытия: "),
            departureTime = readNonEmptyString("Время отправления (например, 08:30): "),
            travelDurationMinutes = readNonNegativeInt("Продолжительность в пути, минут: "),
            ticketPrice = readPositiveDouble("Стоимость билета: "),
            availableSeats = readNonNegativeInt("Количество свободных мест: ")
        )
        RouteStorage.routes.add(route)
        println("Рейс добавлен.")
    }

    fun showRoutes(routes: List<BusRoute> = RouteStorage.routes) {
        if (routes.isEmpty()) {
            println("Рейсы отсутствуют.")
            return
        }

        routes.forEach { route ->
            println(
                "ID: ${route.id}; маршрут: ${route.routeNumber}; " +
                    "${route.departureCity} -> ${route.arrivalCity}; " +
                    "отправление: ${route.departureTime}; путь: ${route.travelDurationMinutes} мин.; " +
                    "цена: ${"%.2f".format(route.ticketPrice)}; мест: ${route.availableSeats}"
            )
        }
    }

    fun sortRoutesInteractive() {
        println(
            """

            Сортировка рейсов:
            1. По номеру маршрута
            2. По стоимости билета
            3. По времени в пути
            4. Назад
            """.trimIndent()
        )

        when (readInt("Выберите пункт: ")) {
            1 -> showRoutes(RouteStorage.routes.sortedBy { it.routeNumber.lowercase() })
            2 -> showRoutes(RouteStorage.routes.sortedBy { it.ticketPrice })
            3 -> showRoutes(RouteStorage.routes.sortedBy { it.travelDurationMinutes })
            4 -> return
            else -> println("Ошибка: неизвестный пункт меню.")
        }
    }

    fun filterRoutesInteractive() {
        println(
            """

            Фильтрация рейсов:
            1. По городу отправления
            2. По максимальной стоимости билета
            3. Только рейсы со свободными местами
            4. Назад
            """.trimIndent()
        )

        val filtered = when (readInt("Выберите пункт: ")) {
            1 -> {
                val city = readNonEmptyString("Введите город отправления: ")
                RouteStorage.routes.filter { it.departureCity.equals(city, ignoreCase = true) }
            }
            2 -> {
                val maxPrice = readPositiveDouble("Введите максимальную стоимость: ")
                RouteStorage.routes.filter { it.ticketPrice <= maxPrice }
            }
            3 -> RouteStorage.routes.filter { it.availableSeats > 0 }
            4 -> return
            else -> {
                println("Ошибка: неизвестный пункт меню.")
                return
            }
        }

        showRoutes(filtered)
    }

    fun findRoutesInteractive() {
        val query = readNonEmptyString("Введите номер маршрута, город отправления или город прибытия: ")
        val found = RouteStorage.routes.filter {
            it.routeNumber.contains(query, ignoreCase = true) ||
                it.departureCity.contains(query, ignoreCase = true) ||
                it.arrivalCity.contains(query, ignoreCase = true)
        }

        if (found.isEmpty()) {
            println("Не найдено.")
        } else {
            showRoutes(found)
        }
    }

    fun editRouteInteractive() {
        val id = readInt("Введите ID рейса для редактирования: ")
        val index = RouteStorage.routes.indexOfFirst { it.id == id }
        if (index == -1) {
            println("Рейс не найден.")
            return
        }

        val oldRoute = RouteStorage.routes[index]
        println("Оставьте поле пустым, чтобы сохранить старое значение.")
        val newRoute = oldRoute.copy(
            routeNumber = readOptionalString("Номер маршрута", oldRoute.routeNumber),
            departureCity = readOptionalString("Город отправления", oldRoute.departureCity),
            arrivalCity = readOptionalString("Город прибытия", oldRoute.arrivalCity),
            departureTime = readOptionalString("Время отправления", oldRoute.departureTime),
            travelDurationMinutes = readOptionalInt("Продолжительность в пути, минут", oldRoute.travelDurationMinutes),
            ticketPrice = readOptionalDouble("Стоимость билета", oldRoute.ticketPrice),
            availableSeats = readOptionalInt("Количество свободных мест", oldRoute.availableSeats)
        )

        RouteStorage.routes[index] = newRoute
        println("Рейс обновлен.")
    }

    fun deleteRouteInteractive() {
        val id = readInt("Введите ID рейса для удаления: ")
        val removed = RouteStorage.routes.removeIf { it.id == id }
        if (removed) {
            println("Рейс удален.")
        } else {
            println("Рейс не найден.")
        }
    }

    fun showAggregates() {
        val routes = RouteStorage.routes
        if (routes.isEmpty()) {
            println("Рейсы отсутствуют. Агрегированные показатели недоступны.")
            return
        }

        val prices = routes.map { it.ticketPrice }
        val minPrice = prices.minOrNull() ?: 0.0
        val maxPrice = prices.maxOrNull() ?: 0.0
        val averageDuration = routes.map { it.travelDurationMinutes }.average()

        println("Средняя стоимость билета: ${"%.2f".format(prices.average())}")
        println("Общая стоимость всех билетов: ${"%.2f".format(prices.sum())}")
        println("Минимальная стоимость билета: ${"%.2f".format(minPrice)}")
        println("Максимальная стоимость билета: ${"%.2f".format(maxPrice)}")
        println("Среднее время в пути: ${"%.2f".format(averageDuration)} мин.")
    }

    private fun readUniqueId(): Int {
        while (true) {
            val id = readInt("ID рейса: ")
            if (RouteStorage.routes.none { it.id == id }) return id
            println("Ошибка: рейс с таким ID уже существует.")
        }
    }
}
