package de.arjmandi.navvistask.numberdatasource.data.remote

import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkError
import de.arjmandi.navvistask.numberdatasource.domain.model.NumbersResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

class ApiClient {

    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000 // 10s timeout
            connectTimeoutMillis = 5_000  // 5s connect timeout
            socketTimeoutMillis = 5_000   // 5s socket timeout
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
    }

    suspend fun fetchNumbers(): NumbersResponse {
        return try {
            val response: HttpResponse = client.get("https://navvis.com/numbers.json") {
                headers {
                    append(HttpHeaders.Accept, "application/json")
                }
            }

            when (response.status) {
                HttpStatusCode.OK -> response.body<NumbersResponse>()
                HttpStatusCode.BadRequest -> throw BadRequestException("Bad request: ${response.status}")
                HttpStatusCode.Unauthorized -> throw UnauthorizedException("Unauthorized access: ${response.status}")
                HttpStatusCode.RequestTimeout -> throw TimeOutException("Request Timeout: ${response.status}")
                HttpStatusCode.NotFound -> throw NotFoundException("Not found: ${response.status}")
                else -> throw ApiException("API error: ${response.status}")
            }

        } catch (e: ConnectTimeoutException) {
            NumbersResponse(emptyList(), error = NetworkError.Timeout())
        } catch (e: IOException) {
            NumbersResponse(emptyList(), error = NetworkError.NoConnection())
        } catch (e: SerializationException) {
            NumbersResponse(emptyList(), error = NetworkError.MalformedResponse())
        } catch (e: ResponseException) {
            NumbersResponse(emptyList(), error = NetworkError.UnknownError(e.message ?: "Unknown API error"))
        }  catch (e: Exception) {
            NumbersResponse(emptyList(), error = NetworkError.UnknownError(e.message ?: "Unknown error"))
        }
    }
}

class BadRequestException(message: String) : Exception(message)
class UnauthorizedException(message: String) : Exception(message)
class TimeOutException(message: String) : Exception(message)
class NotFoundException(message: String) : Exception(message)
class ApiException(message: String) : Exception(message)

