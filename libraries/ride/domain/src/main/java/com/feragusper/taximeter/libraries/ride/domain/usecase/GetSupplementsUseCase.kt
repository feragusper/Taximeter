package com.feragusper.taximeter.libraries.ride.domain.usecase

import com.feragusper.taximeter.libraries.ride.domain.model.Supplement

/**
 * This use case is used to get the list of available supplements.
 */
interface GetSupplementsUseCase {
    suspend operator fun invoke(): Result<List<Supplement>>
}