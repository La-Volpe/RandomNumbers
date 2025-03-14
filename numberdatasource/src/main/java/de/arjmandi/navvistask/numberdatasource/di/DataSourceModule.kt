package de.arjmandi.navvistask.numberdatasource.di

import de.arjmandi.navvistask.numberdatasource.NumberDataSource
import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkSimulator
import de.arjmandi.navvistask.numberdatasource.data.remote.ApiClient
import de.arjmandi.navvistask.numberdatasource.data.repository.NumbersRepositoryImpl
import de.arjmandi.navvistask.numberdatasource.domain.parser.NumberParser
import de.arjmandi.navvistask.numberdatasource.domain.repository.NumbersRepository
import de.arjmandi.navvistask.numberdatasource.parser.NumberParserImpl
import org.koin.dsl.module

val appModule = module {
    // Data sources
    single { ApiClient() }
    single { NetworkSimulator() }

    // Repository
    single<NumbersRepository> { NumbersRepositoryImpl(get(), get()) }

    // Parser
    single<NumberParser> { NumberParserImpl() }

    // Facade
    single { NumberDataSource(get(), get()) }
}
