package de.arjmandi.navvistask.numberdatasource.domain.mock

interface RandomSimulator {
    fun randomBoolean(): Boolean

    fun randomDelay(): Long

    fun randomListLength(): Int

    fun randomValidNumber(): Int

    fun randomInvalidNumber(): Int
}
