package de.arjmandi.random_numbers.numberdatasource.domain.mock

interface RandomSimulator {
    fun randomBoolean(): Boolean

    fun randomDelay(): Long

    fun randomListLength(): Int

    fun randomValidNumber(): Int

    fun randomInvalidNumber(): Int

    fun randomOneThirdChance(): Int
}
