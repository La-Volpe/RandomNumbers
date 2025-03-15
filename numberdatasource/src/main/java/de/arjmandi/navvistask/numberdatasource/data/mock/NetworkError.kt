package de.arjmandi.navvistask.numberdatasource.data.mock

sealed class NetworkError {
    object None : NetworkError()

    data class Timeout(
        val message: String = "Request timed out",
    ) : NetworkError()

    data class NoConnection(
        val message: String = "No internet connection",
    ) : NetworkError()

    data class MalformedResponse(
        val message: String = "Malformed JSON received",
    ) : NetworkError()

    data class FlakyResponse(
        val message: String = "Flaky network connection",
    ) : NetworkError()

    data class UnknownError(
        val message: String = "Unknown Network Error",
    ) : NetworkError()
}
