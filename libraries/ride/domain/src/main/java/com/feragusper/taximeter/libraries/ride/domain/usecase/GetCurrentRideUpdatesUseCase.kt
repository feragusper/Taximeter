package com.feragusper.taximeter.libraries.ride.domain.usecase

import com.feragusper.taximeter.libraries.ride.domain.model.Ride
import kotlinx.coroutines.flow.Flow

/**
 * This use case is used to get the current ride updates.
 */
interface GetCurrentRideUpdatesUseCase {
    suspend operator fun invoke(): Flow<Result<Ride?>>
}