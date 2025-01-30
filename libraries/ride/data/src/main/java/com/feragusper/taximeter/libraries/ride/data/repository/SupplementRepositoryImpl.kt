package com.feragusper.taximeter.libraries.ride.data.repository

import com.feragusper.taximeter.libraries.ride.data.datasource.SupplementDataSource
import com.feragusper.taximeter.libraries.ride.domain.repository.SupplementRepository

/**
 * Implementation of [SupplementRepository] that handles supplement-related operations.
 *
 * This repository acts as an intermediary between the data source and the domain layer,
 * providing access to supplement data.
 *
 * @param supplementDataSource The data source that provides supplement-related data.
 */
class SupplementRepositoryImpl(
    private val supplementDataSource: SupplementDataSource
) : SupplementRepository {

    /**
     * Retrieves the list of available supplements from the data source.
     *
     * @return A list of available supplements.
     */
    override fun getSupplements() = supplementDataSource.getSupplements()
}
