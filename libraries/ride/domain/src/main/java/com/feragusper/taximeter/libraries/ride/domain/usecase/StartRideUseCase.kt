package com.feragusper.taximeter.libraries.ride.domain.usecase

import java.util.UUID

/**
 * This use case is used to start the ride.
 */
interface StartRideUseCase {

    /**
     * This method is used to start the ride.
     *
     * @param supplements The list of supplements to be added to the ride.
     */
    suspend operator fun invoke(supplements: List<UUID>): Result<Unit>
}