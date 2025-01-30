package com.feragusper.taximeter.libraries.mvi

/**
 * Represents the state of the UI at any given point in time.
 * This should contain all the data required to render the UI.
 */
interface ViewState

/**
 * Represents side effects or one-time events that should be handled,
 * such as navigation, showing a toast message, or analytics tracking.
 */
interface SideEffect

/**
 * Represents user-initiated actions that intend to modify the state.
 * Examples could include button clicks, input changes, or screen load events.
 */
interface UserIntent

/**
 * Represents the resulting actions after processing user intents.
 * These actions are passed through reducers to update the ViewState.
 */
interface UserIntentAction
