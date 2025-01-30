package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.ride.domain.model.FareSummary
import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetCurrentRideFareSummaryUseCase

/**
 * Implementation of [GetCurrentRideFareSummaryUseCase] that retrieves the fare summary of the current ride.
 *
 * @param rideRepository The repository that handles ride-related operations.
 */
class GetCurrentRideFareSummaryUseCaseImpl(
    private val rideRepository: RideRepository
) : GetCurrentRideFareSummaryUseCase {

    /**
     * Retrieves the fare summary of the current ride if available.
     *
     * @return [Result] containing the fare summary if a ride is in progress, otherwise an error.
     */
    override suspend operator fun invoke(): Result<FareSummary> = runCatching {
        rideRepository.currentRide?.let { ride ->
            Result.success(ride.fareSummary)
        } ?: Result.failure(Throwable("No ride in progress"))
    }.getOrElse { exception ->
        Result.failure(exception)
    }
}
