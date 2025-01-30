package com.feragusper.taximeter.libraries.ride.data.datasource.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceConfigurationEntity(
    // The price per km is calculated with the distance between the origin and the destination.
    @SerialName("price_per_km")
    val pricePerKm: Double,
    // Every seconds is passed has to by multiplied by the price per second.
    @SerialName("price_per_second")
    val pricePerSecond: Double,
)