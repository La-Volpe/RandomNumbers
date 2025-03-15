package de.arjmandi.navvistask.numberdatasource.data.mock

import de.arjmandi.navvistask.numberdatasource.domain.mock.RandomSimulator
import kotlin.random.Random

class RandomSimulatorImpl : RandomSimulator {
    override fun randomBoolean(): Boolean = Random.nextBoolean()

    override fun randomDelay(): Long = Random.nextLong(100, 1500)

    override fun randomListLength(): Int = Random.nextInt(1, 20)

    override fun randomValidNumber(): Int = Random.nextInt(0, 256)

    override fun randomInvalidNumber(): Int = Random.nextInt(-100, 356)

    override fun randomOneThirdChance(): Int = Random.nextInt(3)
}
