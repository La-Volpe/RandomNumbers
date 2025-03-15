package de.arjmandi.navvistask.di

import de.arjmandi.navvistask.numberdatasource.NumberDataSource
import de.arjmandi.navvistask.ui.number_list.NumberViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { NumberDataSource(get(), get()) }
    viewModel { NumberViewModel(get()) }
}