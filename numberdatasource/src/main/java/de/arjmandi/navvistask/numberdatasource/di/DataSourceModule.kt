package de.arjmandi.navvistask.numberdatasource.di

import de.arjmandi.navvistask.numberdatasource.NumberDataSource
import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkSimulator
import de.arjmandi.navvistask.numberdatasource.data.mock.RandomSimulatorImpl
import de.arjmandi.navvistask.numberdatasource.data.remote.ApiClient
import de.arjmandi.navvistask.numberdatasource.data.repository.NumbersRepositoryImpl
import de.arjmandi.navvistask.numberdatasource.domain.mock.RandomSimulator
import de.arjmandi.navvistask.numberdatasource.domain.parser.NumberParser
import de.arjmandi.navvistask.numberdatasource.domain.repository.NumbersRepository
import de.arjmandi.navvistask.numberdatasource.parser.NumberParserImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import org.koin.dsl.module

val dataSourceModule = module {
    single<HttpClient> {
        HttpClient(CIO) {
            install(HttpTimeout) {
                requestTimeoutMillis = 10_000
                connectTimeoutMillis = 5_000
                socketTimeoutMillis = 5_000
            }
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.BODY
            }
        }
    }
    single { ApiClient(get()) }
    single<RandomSimulator> { RandomSimulatorImpl() }
    single { NetworkSimulator(get()) }

    single<NumbersRepository> { NumbersRepositoryImpl(get(), get()) }

    single<NumberParser> { NumberParserImpl() }

    single { NumberDataSource(get(), get()) }
}
