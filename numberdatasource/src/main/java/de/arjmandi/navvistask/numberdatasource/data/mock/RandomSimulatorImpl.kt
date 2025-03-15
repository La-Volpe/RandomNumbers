package de.arjmandi.navvistask.numberdatasource.data.mock

import de.arjmandi.navvistask.numberdatasource.domain.mock.RandomSimulator
import kotlin.random.Random

class RandomSimulatorImpl: RandomSimulator {
    override fun randomBoolean(): Boolean {
        return Random.nextBoolean()
    }

    override fun randomDelay(): Long {
        return Random.nextLong(100, 1500)
    }

    override fun randomListLength(): Int {
        return Random.nextInt(1, 20)
    }

    override fun randomValidNumber(): Int {
        return Random.nextInt(0, 256)
    }

    override fun randomInvalidNumber(): Int {
        return Random.nextInt(-100, 356)
    }
}