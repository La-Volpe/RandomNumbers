package de.arjmandi.navvistask.data.mock

import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkError
import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkMode
import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkResponse
import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkSimulator
import de.arjmandi.navvistask.numberdatasource.domain.mock.RandomSimulator
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import org.junit.After
import org.junit.Before
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkSimulatorTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private lateinit var mockRandom: RandomSimulator
    private lateinit var networkSimulator: NetworkSimulator

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Set test dispatcher
        mockRandom = mockk<RandomSimulator>()
        networkSimulator = NetworkSimulator(mockRandom)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `simulate STABLE mode returns valid response`() = runTest(testScheduler) {

        every { mockRandom.randomBoolean() } returns true
        every { mockRandom.randomDelay() } returns 200L
        every { mockRandom.randomListLength() } returns 10
        every { mockRandom.randomValidNumber() } returns 10
        every { mockRandom.randomInvalidNumber()} returns -12

        val response = networkSimulator.simulateNetworkResponse(NetworkMode.STABLE)

        assertTrue(response is NetworkResponse.StableConnection)
        assertTrue((response as NetworkResponse.StableConnection).numbers.isNotEmpty())
    }

    @Test
    fun `simulate STABLE_WITH_MALFORMED mode returns valid or throws exception`() = runTest(testScheduler) {
        every { mockRandom.randomBoolean() } returns true
        every { mockRandom.randomDelay() } returns 300L
        every { mockRandom.randomListLength() } returns 5
        every { mockRandom.randomValidNumber() } returns 10
        every { mockRandom.randomInvalidNumber()} returns -12

        assertTrue(networkSimulator.simulateNetworkResponse(NetworkMode.STABLE_WITH_MALFORMED) is NetworkResponse.StableConnectionWithMalformedResponse)

    }

    @Test
    fun `simulate STABLE_WITH_MALFORMED mode throws exception`() = runTest(testScheduler) {
        every { mockRandom.randomBoolean() } returns false // Second run throws exception
        every { mockRandom.randomDelay() } returns 300L
        every { mockRandom.randomListLength() } returns 5
        every { mockRandom.randomValidNumber() } returns 10
        every { mockRandom.randomInvalidNumber()} returns -12

        assertFailsWith<SerializationException> {
            networkSimulator.simulateNetworkResponse(NetworkMode.STABLE_WITH_MALFORMED)
        }
    }

    @Test
    fun `simulate FLAKY mode throws timeout`() = runTest(testScheduler) {
        every { mockRandom.randomBoolean() } returns true // First run should timeout
        every { mockRandom.randomDelay() } returns 10000
        every { mockRandom.randomListLength() } returns 17
        every { mockRandom.randomValidNumber() } returns 10
        every { mockRandom.randomInvalidNumber()} returns -12

        assertFailsWith<ConnectTimeoutException> {
            assertTrue(networkSimulator
                .simulateNetworkResponse(NetworkMode.FLAKY)
                .toNumbersResponse()
                .error
                    is NetworkError.FlakyResponse)
        }
    }

    @Test
    fun `simulate FLAKY mode returns incomplete response`() = runTest(testScheduler) {
        every { mockRandom.randomBoolean() } returns false // First run should timeout
        every { mockRandom.randomDelay() } returns 100L
        every { mockRandom.randomListLength() } returns 10
        every { mockRandom.randomValidNumber() } returns 10
        every { mockRandom.randomInvalidNumber()} returns -12

        assertFailsWith<SerializationException> {
            assertTrue(networkSimulator.simulateNetworkResponse(NetworkMode.FLAKY).toNumbersResponse().error is NetworkError.FlakyResponse)
        }
    }

    @Test
    fun `simulate NO_CONNECTION mode throws IOException`() = runTest(testScheduler) {
        every { mockRandom.randomBoolean() } returns true // First run should time out
        every { mockRandom.randomDelay() } returns 1000
        every { mockRandom.randomListLength() } returns 13
        every { mockRandom.randomValidNumber() } returns 10
        every { mockRandom.randomInvalidNumber()} returns -12
        assertFailsWith<IOException>{
            assertTrue(networkSimulator.simulateNetworkResponse(NetworkMode.NO_CONNECTION) is NetworkResponse.NoConnection)
        }
    }

    @Test
    fun `simulate NO_CONNECTION returns empty list`() = runTest(testScheduler) {
        every { mockRandom.randomBoolean() } returns false // Second run returns empty response
        every { mockRandom.randomDelay() } returns 1000
        every { mockRandom.randomListLength() } returns 13
        every { mockRandom.randomValidNumber() } returns 10
        every { mockRandom.randomInvalidNumber()} returns -12
        assertTrue(networkSimulator.simulateNetworkResponse(NetworkMode.NO_CONNECTION) is NetworkResponse.NoConnection)
        assertTrue(networkSimulator.simulateNetworkResponse(NetworkMode.NO_CONNECTION).toNumbersResponse().numbers.isEmpty())
    }
}
