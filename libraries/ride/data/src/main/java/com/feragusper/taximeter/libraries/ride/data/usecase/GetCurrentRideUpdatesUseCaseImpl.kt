package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetCurrentRideUpdatesUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Implementation of [GetCurrentRideUpdatesUseCase] that provides real-time updates
 * for the current ride by observing ride updates from the repository.
 *
 * @param rideRepository The repository that provides ride-related operations.
 */
class GetCurrentRideUpdatesUseCaseImpl(
    private val rideRepository: RideRepository
) : GetCurrentRideUpdatesUseCase {

    /**
     * Retrieves ride updates as a flow of results.
     *
     * @return A [Flow] that emits ride updates wrapped in a [Result] object.
     * If an error occurs, it emits a failure result.
     */
    override suspend operator fun invoke() =
        rideRepository.rideUpdates
            .map { ride ->
                Result.success(ride)
            }
            .catch { exception ->
                emit(Result.failure(exception))
            }
}
