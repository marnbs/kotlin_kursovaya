package org.example

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BusRouteManagerTest {
    private val routes = listOf(
        BusRoute(1, "No202", "Moscow", "Tver", "09:30", 240, 850.0, 5),
        BusRoute(2, "No101", "Moscow", "Yaroslavl", "08:00", 180, 650.0, 20),
        BusRoute(3, "No303", "Tver", "Saint Petersburg", "11:00", 480, 1200.0, 0)
    )

    @Test
    fun sortByRouteNumberOrdersRoutesAlphabetically() {
        val sorted = BusRouteManager.sortByRouteNumber(routes)

        assertEquals(listOf("No101", "No202", "No303"), sorted.map { it.routeNumber })
    }

    @Test
    fun sortByTicketPriceOrdersRoutesByNumericPrice() {
        val sorted = BusRouteManager.sortByTicketPrice(routes)

        assertEquals(listOf(650.0, 850.0, 1200.0), sorted.map { it.ticketPrice })
    }

    @Test
    fun filterByDepartureCityIgnoresCase() {
        val filtered = BusRouteManager.filterByDepartureCity(routes, "moscow")

        assertEquals(listOf(1, 2), filtered.map { it.id })
    }

    @Test
    fun filterByMaxTicketPriceKeepsCheapRoutesOnly() {
        val filtered = BusRouteManager.filterByMaxTicketPrice(routes, 900.0)

        assertEquals(listOf(1, 2), filtered.map { it.id })
    }

    @Test
    fun filterWithAvailableSeatsRemovesFullRoutes() {
        val filtered = BusRouteManager.filterWithAvailableSeats(routes)

        assertEquals(listOf(1, 2), filtered.map { it.id })
    }

    @Test
    fun findRoutesSearchesRouteNumberAndCities() {
        val byNumber = BusRouteManager.findRoutes(routes, "101")
        val byCity = BusRouteManager.findRoutes(routes, "petersburg")

        assertEquals(listOf(2), byNumber.map { it.id })
        assertEquals(listOf(3), byCity.map { it.id })
    }

    @Test
    fun calculateAggregatesReturnsNumericSummary() {
        val aggregates = BusRouteManager.calculateAggregates(routes)

        assertEquals(900.0, aggregates?.averageTicketPrice)
        assertEquals(2700.0, aggregates?.totalTicketPrice)
        assertEquals(650.0, aggregates?.minTicketPrice)
        assertEquals(1200.0, aggregates?.maxTicketPrice)
        assertEquals(300.0, aggregates?.averageTravelDurationMinutes)
    }

    @Test
    fun calculateAggregatesReturnsNullForEmptyList() {
        assertNull(BusRouteManager.calculateAggregates(emptyList()))
    }
}
