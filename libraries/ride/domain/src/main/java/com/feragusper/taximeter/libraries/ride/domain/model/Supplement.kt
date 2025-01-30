package com.feragusper.taximeter.libraries.ride.domain.model

import java.util.UUID

/**
 * Represents an additional service or feature that can be added to a ride.
 *
 * Supplements can include items such as extra baggage, baby seats, or special ride options.
 *
 * @property id A unique identifier for the supplement.
 * @property name The name of the supplement (e.g., "Extra Baggage", "Baby Seat").
 * @property price The cost of the supplement in the ride currency.
 */
data class Supplement(
    val id: UUID,  // Unique identifier for the supplement
    val name: String,  // Descriptive name of the supplement
    val price: Double  // Cost of the supplement
)
