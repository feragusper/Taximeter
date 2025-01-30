package com.feragusper.taximeter.libraries.ride.domain.model

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.milliseconds

/**
 * Represents a ride with route, timing, supplements, and pricing configuration.
 *
 * @property route List of location points defining the ride path.
 * @property startTime The start time of the ride in milliseconds.
 * @property endTime The end time of the ride in milliseconds (nullable).
 * @property supplements List of additional services chosen for the ride.
 * @property status The current status of the ride (Started/Ended).
 * @property priceConfiguration The configuration used to calculate ride cost.
 */
data class Ride(
    val route: List<LocationPoint>,
    val startTime: Long,
    val endTime: Long?,
    val supplements: List<Supplement>,
    val status: RideStatus,
    val priceConfiguration: PriceConfiguration,
    val updatedAt: Long
) {

    /**
     * Defines the possible states of a ride.
     */
    sealed interface RideStatus {
        data object Started : RideStatus
        data object Ended : RideStatus
    }

    /**
     * Calculates the fare summary for the ride based on the configured rates and selected supplements.
     */
    val fareSummary: FareSummary
        get() = FareSummary(
            concepts = listOf(
                FareSummary.Concept(
                    name = "Distance",
                    price = route.calculateTotalDistance() * priceConfiguration.pricePerKm
                ),
                FareSummary.Concept(
                    name = "Time",
                    price = timeInSeconds * priceConfiguration.pricePerSecond
                ),
                FareSummary.Concept(
                    name = "Supplements",
                    price = supplements.sumOf { it.price }
                )
            )
        )

    /**
     * Calculates the total duration of the ride in seconds.
     */
    val timeInSeconds: Long
        get() = ((endTime ?: System.currentTimeMillis()) - startTime).milliseconds.inWholeSeconds

    /**
     * Computes the total ride price based on configured rates and selected supplements.
     */
    val totalPrice: Double
        get() {
            val distanceCost = route.calculateTotalDistance() * priceConfiguration.pricePerKm
            val timeCost = timeInSeconds * priceConfiguration.pricePerSecond
            val supplementCost = supplements.sumOf { it.price }

            return distanceCost + timeCost + supplementCost
        }

    val distance: Double
        get() = route.calculateTotalDistance()
}

/**
 * Extension function to convert degrees to radians.
 */
private fun Double.toRadians(): Double = Math.toRadians(this)

/**
 * Calculates the distance between two [LocationPoint] instances using the Haversine formula.
 *
 * @param target The destination location point.
 * @return The distance between the two points in kilometers.
 */
fun LocationPoint.distanceTo(target: LocationPoint): Double {
    val earthRadiusKm = 6371.0

    val lat1 = this.lat.toRadians()
    val lon1 = this.lon.toRadians()
    val lat2 = target.lat.toRadians()
    val lon2 = target.lon.toRadians()

    val dLat = lat2 - lat1
    val dLon = lon2 - lon1

    val a = sin(dLat / 2).pow(2) +
            cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadiusKm * c
}

/**
 * Calculates the total distance covered by a list of [LocationPoint] instances.
 *
 * @return The total distance in kilometers.
 */
fun List<LocationPoint>.calculateTotalDistance(): Double {
    var totalDistance = 0.0
    for (i in 0 until size - 1) {
        totalDistance += this[i].distanceTo(this[i + 1])
    }
    return totalDistance
}
