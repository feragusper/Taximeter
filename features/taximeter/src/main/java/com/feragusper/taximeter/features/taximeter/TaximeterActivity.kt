package com.feragusper.taximeter.features.taximeter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.feragusper.taximeter.libraries.design.ui.theme.TaximeterTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Entry point for the Taximeter feature.
 */
@AndroidEntryPoint
class TaximeterActivity : ComponentActivity() {

    // ViewModel injected via Hilt for dependency management and lifecycle awareness
    private val viewModel: TaximeterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TaximeterTheme {
                // Main UI content for the Taximeter feature
                Taximeter(viewModel)
            }
        }
    }
}
