package com.feragusper.taximeter.libraries.ride.domain.usecase

import java.util.UUID

/**
 * This use case is used to update the ride supplements.
 */
interface UpdateRideSupplementsUseCase {

    /**
     * This method is used to update the ride supplements.
     *
     * @param supplements The list of supplements to be updated.
     * @return The result of the operation.
     */
    suspend operator fun invoke(supplements: List<UUID>): Result<Unit>
}