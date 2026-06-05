package org.example

data class RouteAggregates(
    val averageTicketPrice: Double,
    val totalTicketPrice: Double,
    val minTicketPrice: Double,
    val maxTicketPrice: Double,
    val averageTravelDurationMinutes: Double
)

object BusRouteManager {
    fun sortByRouteNumber(routes: List<BusRoute>): List<BusRoute> =
        routes.sortedBy { it.routeNumber.lowercase() }

    fun sortByTicketPrice(routes: List<BusRoute>): List<BusRoute> =
        routes.sortedBy { it.ticketPrice }

    fun sortByTravelDuration(routes: List<BusRoute>): List<BusRoute> =
        routes.sortedBy { it.travelDurationMinutes }

    fun filterByDepartureCity(routes: List<BusRoute>, city: String): List<BusRoute> =
        routes.filter { it.departureCity.equals(city, ignoreCase = true) }

    fun filterByMaxTicketPrice(routes: List<BusRoute>, maxPrice: Double): List<BusRoute> =
        routes.filter { it.ticketPrice <= maxPrice }

    fun filterWithAvailableSeats(routes: List<BusRoute>): List<BusRoute> =
        routes.filter { it.availableSeats > 0 }

    fun findRoutes(routes: List<BusRoute>, query: String): List<BusRoute> =
        routes.filter {
            it.routeNumber.contains(query, ignoreCase = true) ||
                it.departureCity.contains(query, ignoreCase = true) ||
                it.arrivalCity.contains(query, ignoreCase = true)
        }

    fun calculateAggregates(routes: List<BusRoute>): RouteAggregates? {
        if (routes.isEmpty()) return null

        val prices = routes.map { it.ticketPrice }
        return RouteAggregates(
            averageTicketPrice = prices.average(),
            totalTicketPrice = prices.sum(),
            minTicketPrice = prices.minOrNull() ?: 0.0,
            maxTicketPrice = prices.maxOrNull() ?: 0.0,
            averageTravelDurationMinutes = routes.map { it.travelDurationMinutes }.average()
        )
    }

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
            1 -> showRoutes(sortByRouteNumber(RouteStorage.routes))
            2 -> showRoutes(sortByTicketPrice(RouteStorage.routes))
            3 -> showRoutes(sortByTravelDuration(RouteStorage.routes))
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
                filterByDepartureCity(RouteStorage.routes, city)
            }
            2 -> {
                val maxPrice = readPositiveDouble("Введите максимальную стоимость: ")
                filterByMaxTicketPrice(RouteStorage.routes, maxPrice)
            }
            3 -> filterWithAvailableSeats(RouteStorage.routes)
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
        val found = findRoutes(RouteStorage.routes, query)

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

        val aggregates = calculateAggregates(routes) ?: return

        println("Средняя стоимость билета: ${"%.2f".format(aggregates.averageTicketPrice)}")
        println("Общая стоимость всех билетов: ${"%.2f".format(aggregates.totalTicketPrice)}")
        println("Минимальная стоимость билета: ${"%.2f".format(aggregates.minTicketPrice)}")
        println("Максимальная стоимость билета: ${"%.2f".format(aggregates.maxTicketPrice)}")
        println("Среднее время в пути: ${"%.2f".format(aggregates.averageTravelDurationMinutes)} мин.")
    }

    private fun readUniqueId(): Int {
        while (true) {
            val id = readInt("ID рейса: ")
            if (RouteStorage.routes.none { it.id == id }) return id
            println("Ошибка: рейс с таким ID уже существует.")
        }
    }
}
