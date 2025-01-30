package com.feragusper.taximeter.libraries.mvi

import androidx.annotation.RestrictTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.*

/**
 * Interface to be implemented by test classes to inject coroutine-related dependencies.
 * Provides a custom coroutine scope and dispatcher for testing asynchronous operations.
 */
@ExtendWith(CoroutineTestHandler::class)
interface CoroutineTestConfig {
    var testCoroutineScope: TestScope
    var testCoroutineDispatcher: TestDispatcher
}

/**
 * JUnit 5 extension that sets up a test environment for coroutine-based testing.
 * It ensures that the main dispatcher is replaced with a test dispatcher during tests.
 *
 * Example usage:
 * ```
 * class SampleTest : CoroutineTestConfig {
 *
 *   override lateinit var testCoroutineScope: TestScope
 *   override lateinit var testCoroutineDispatcher: TestDispatcher
 *
 * }
 * ```
 */
@RestrictTo(RestrictTo.Scope.TESTS)
internal class CoroutineTestHandler :
    TestInstancePostProcessor,
    BeforeAllCallback,
    AfterEachCallback,
    AfterAllCallback {

    // Provides an unconfined dispatcher for test execution, which allows immediate execution of coroutines.
    private val dispatcherUnderTest: TestDispatcher = UnconfinedTestDispatcher()

    // Scope for running coroutine-based tests.
    private val testCoroutineScope = TestScope(dispatcherUnderTest)

    /**
     * Injects test dispatcher and scope into test classes implementing [CoroutineTestConfig].
     */
    override fun postProcessTestInstance(
        testInstance: Any?,
        context: ExtensionContext?,
    ) {
        (testInstance as? CoroutineTestConfig)?.let { testConfig ->
            testConfig.testCoroutineScope = testCoroutineScope
            testConfig.testCoroutineDispatcher = dispatcherUnderTest
        }
    }

    /**
     * Called before all tests to replace the main dispatcher with the test dispatcher.
     */
    override fun beforeAll(context: ExtensionContext?) {
        Dispatchers.setMain(dispatcherUnderTest)
    }

    /**
     * Called after each test execution.
     * Currently does nothing but can be used to clean up resources if needed.
     */
    override fun afterEach(context: ExtensionContext?) = Unit

    /**
     * Called after all tests to reset the main dispatcher to its original state.
     */
    override fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}