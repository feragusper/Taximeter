package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.ride.domain.repository.PriceRepository
import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import com.feragusper.taximeter.libraries.ride.domain.usecase.StartRideUseCase
import java.util.UUID

/**
 * Implementation of [StartRideUseCase] responsible for initiating a new ride.
 *
 * @param rideRepository The repository responsible for managing ride operations.
 * @param priceRepository The repository providing pricing configurations.
 */
class StartRideUseCaseImpl(
    private val rideRepository: RideRepository,
    private val priceRepository: PriceRepository,
) : StartRideUseCase {

    /**
     * Starts a ride with the provided supplements.
     *
     * @param supplements The list of selected supplements for the ride.
     * @return A [Result] indicating success or failure.
     */
    override suspend operator fun invoke(supplements: List<UUID>): Result<Unit> =
        runCatching {
            val pricingConfig = priceRepository.getPriceConfiguration()
            rideRepository.startRide(pricingConfig, supplements)
        }.mapCatching {
            Result.success(Unit)
        }.getOrElse { exception ->
            Result.failure(exception)
        }
}
