package com.feragusper.taximeter.libraries.ride.domain.model

/**
 * Represents the price configuration for a ride, including distance and time-based pricing.
 *
 * @property pricePerKm The cost per kilometer traveled.
 * @property pricePerSecond The cost per second of ride duration.
 */
data class PriceConfiguration(
    val pricePerKm: Double,
    val pricePerSecond: Double
)
