package de.arjmandi.random_numbers.di

import de.arjmandi.random_numbers.numberdatasource.NumberDataSource
import de.arjmandi.random_numbers.ui.number_list.NumberViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        single { NumberDataSource(get(), get()) }
        viewModel { NumberViewModel(get()) }
    }
