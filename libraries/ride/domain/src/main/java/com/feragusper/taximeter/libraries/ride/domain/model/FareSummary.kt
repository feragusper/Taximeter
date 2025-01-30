package com.feragusper.taximeter.libraries.ride.domain.model

/**
 * Represents the fare summary for a ride.
 *
 * A [FareSummary] consists of multiple [Concept] items, each with an associated cost.
 * The total amount is calculated as the sum of all concept prices.
 *
 * @property concepts A list of individual charge items associated with the ride.
 */
data class FareSummary(
    val concepts: List<Concept>
) {
    /**
     * Total amount of the fare summary.
     *
     * @return The sum of all concept prices.
     */
    val total: Double
        get() = concepts.sumOf { it.price }

    /**
     * Represents a charge item associated with a ride.
     *
     * @property name A descriptive name for the charge (e.g., "Base Fare", "Extra Baggage").
     * @property price The monetary value of the charge.
     */
    data class Concept(
        val name: String,
        val price: Double
    )
}
