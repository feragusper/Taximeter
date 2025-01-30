package com.feragusper.taximeter.libraries.ride.domain.repository

import com.feragusper.taximeter.libraries.ride.domain.model.PriceConfiguration

/**
 * This repository is used to get the price configuration.
 */
interface PriceRepository {

    /**
     * This method is used to get the price configuration.
     *
     * @return The price configuration.
     */
    suspend fun getPriceConfiguration(): PriceConfiguration
}