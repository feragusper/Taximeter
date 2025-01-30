package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import com.feragusper.taximeter.libraries.ride.domain.usecase.UpdateRideSupplementsUseCase
import java.util.UUID

/**
 * Implementation of [UpdateRideSupplementsUseCase] responsible for updating ride supplements.
 *
 * @param rideRepository The repository responsible for ride-related operations.
 */
class UpdateRideSupplementsUseCaseImpl(
    private val rideRepository: RideRepository
) : UpdateRideSupplementsUseCase {

    /**
     * Updates the supplements for the current ride.
     *
     * @param supplements A list of [Supplement] to be applied to the current ride.
     * @return A [Result] indicating success or failure.
     */
    override suspend operator fun invoke(supplements: List<UUID>): Result<Unit> =
        runCatching {
            rideRepository.updateRideSupplements(supplements)
        }.mapCatching {
            Result.success(Unit)
        }.getOrElse { exception ->
            Result.failure(exception)
        }
}
