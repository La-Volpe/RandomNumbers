package de.arjmandi.random_numbers.numberdatasource.data.mock

/**
 * Represents the mode of the network simulation.
 */
enum class NetworkMode(stringValue: String) {
    STABLE(stringValue = "Stable Connection"),
    STABLE_WITH_MALFORMED(stringValue = "Stable With Malformed Response"),
    NO_CONNECTION(stringValue = "No Connection"),
    FLAKY("Flaky Connection"),
}
