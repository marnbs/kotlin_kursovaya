package org.example

data class BusRoute(
    val id: Int,
    val routeNumber: String,
    val departureCity: String,
    val arrivalCity: String,
    val departureTime: String,
    val travelDurationMinutes: Int,
    val ticketPrice: Double,
    val availableSeats: Int
)
