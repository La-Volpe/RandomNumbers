package de.arjmandi.navvistask.numberdatasource.data.repository


import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkError
import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkMode
import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkSimulator
import de.arjmandi.navvistask.numberdatasource.data.remote.ApiClient
import de.arjmandi.navvistask.numberdatasource.domain.model.NumbersResponse
import de.arjmandi.navvistask.numberdatasource.domain.repository.NumbersRepository
import io.ktor.client.network.sockets.ConnectTimeoutException

import kotlinx.io.IOException
import kotlinx.serialization.SerializationException


class NumbersRepositoryImpl(
    private val apiClient: ApiClient,
    private val networkSimulator: NetworkSimulator
) : NumbersRepository {

    override suspend fun fetchNumbers(): NumbersResponse {
        return try {
            apiClient.fetchNumbers()
        } catch (e: ConnectTimeoutException) {
            // Return a timeout error response
            NumbersResponse(emptyList(), error = NetworkError.Timeout())
        } catch (e: IOException) {
            // Return a simulated offline response
            networkSimulator.simulateNetworkResponse(NetworkMode.NO_CONNECTION).toNumbersResponse()
        } catch (e: SerializationException) {
            // Return a malformed response from simulator
            networkSimulator.simulateNetworkResponse(NetworkMode.STABLE_WITH_MALFORMED).toNumbersResponse()
        } catch (e: Exception) {
            // Generic error handling
            NumbersResponse(emptyList(), error = NetworkError.UnknownError(e.message ?: "Unknown error"))
        }
    }
}