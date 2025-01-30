package com.feragusper.taximeter.features.taximeter

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class TaximeterScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testViewState = TaximeterViewState(
        fare = "€40.05",
        elapsedTime = "00:15:00",
        distance = "1.25 km",
        inProgress = false,
        supplements = listOf(
            TaximeterViewState.Supplement(UUID.randomUUID(), "Baby Seat", 0),
            TaximeterViewState.Supplement(UUID.randomUUID(), "Extra Baggage", 0)
        ),
        priceBreakdown = TaximeterViewState.PriceBreakdown(
            items = listOf(
                TaximeterViewState.PriceBreakdown.Item("Base Fare", "€5.0"),
                TaximeterViewState.PriceBreakdown.Item("Time Fare", "€10.0"),
            ),
            total = "€40.05"
        )
    )

    @Test
    fun whenSupplementsAreAdded_shouldIncreaseCount() {
        var state = testViewState
        composeTestRule.setContent {
            TaximeterContent(state) {
                state = state.copy(
                    supplements = state.supplements.map { it.copy(count = it.count + 1) }
                )
            }
        }

        composeTestRule.waitForIdle()

        // Given - Baby Seat supplement is displayed
        composeTestRule.onNodeWithText("Baby Seat").assertIsDisplayed()

        // When - Click the increment button
        composeTestRule.onAllNodesWithText("+")[0].performClick()

        // Then - Verify the count increases to 1
        composeTestRule.onAllNodesWithText("1")[0].assertIsDisplayed()
    }

    @Test
    fun whenTaximeterScreenIsLoaded_fareAndDistanceShouldBeDisplayed() {
        composeTestRule.setContent {
            TaximeterContent(testViewState) {}
        }

        composeTestRule.waitForIdle()

        // Then - Validate formatted values
        composeTestRule.onNodeWithTag(TestTag.TOTAL_FARE_LABEL).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTag.TOTAL_FARE_VALUE)
            .assert(hasText("€40.05"))

        composeTestRule.onNodeWithTag(TestTag.DISTANCE_LABEL).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTag.DISTANCE_VALUE)
            .assert(hasText("1.25 km"))
    }

    @Test
    fun whenTaximeterScreenIsLoaded_timeShouldBeFormattedCorrectly() {
        composeTestRule.setContent {
            TaximeterContent(testViewState) {}
        }

        composeTestRule.waitForIdle()

        // Then - Validate elapsed time formatted correctly
        composeTestRule.onNodeWithTag(TestTag.DISTANCE_LABEL).assertIsDisplayed()
        composeTestRule.onNodeWithText("00:15:00").assertIsDisplayed()
    }
}
