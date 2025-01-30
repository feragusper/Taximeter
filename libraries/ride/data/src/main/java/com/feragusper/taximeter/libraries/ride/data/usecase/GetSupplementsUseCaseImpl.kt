package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import com.feragusper.taximeter.libraries.ride.domain.repository.SupplementRepository
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetSupplementsUseCase

/**
 * Implementation of [GetSupplementsUseCase] that retrieves the list of available supplements.
 *
 * @param supplementRepository The repository that provides supplement-related operations.
 */
class GetSupplementsUseCaseImpl(
    private val supplementRepository: SupplementRepository
) : GetSupplementsUseCase {

    /**
     * Fetches the list of available supplements from the repository.
     *
     * @return A [Result] containing the list of supplements on success, or a failure result if an error occurs.
     */
    override suspend operator fun invoke(): Result<List<Supplement>> =
        runCatching {
            supplementRepository.getSupplements()
        }.mapCatching { supplements ->
            Result.success(supplements)
        }.getOrElse { exception ->
            Result.failure(exception)
        }
}
