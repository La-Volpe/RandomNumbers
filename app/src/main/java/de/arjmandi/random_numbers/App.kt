package de.arjmandi.random_numbers

import android.app.Application
import de.arjmandi.random_numbers.di.appModule
import de.arjmandi.random_numbers.numberdatasource.di.dataSourceModule
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            modules(listOf(appModule, dataSourceModule))
        }
    }
}
