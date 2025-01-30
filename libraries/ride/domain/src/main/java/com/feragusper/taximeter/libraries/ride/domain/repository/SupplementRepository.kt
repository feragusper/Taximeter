package com.feragusper.taximeter.libraries.ride.domain.repository

import com.feragusper.taximeter.libraries.ride.domain.model.Supplement

/**
 * This repository is used to get the list of available supplements.
 */
interface SupplementRepository {

    /**
     * This method is used to get the list of available supplements.
     *
     * @return The list of available supplements.
     */
    fun getSupplements(): List<Supplement>
}