package com.feragusper.taximeter.libraries.ride.domain.usecase

import com.feragusper.taximeter.libraries.ride.domain.model.FareSummary

/**
 * This use case is used to get the current ride fare summary.
 */
interface GetCurrentRideFareSummaryUseCase {
    suspend operator fun invoke(): Result<FareSummary>
}