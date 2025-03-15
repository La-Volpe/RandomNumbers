package de.arjmandi.navvistask

import android.app.Application
import de.arjmandi.navvistask.di.appModule
import de.arjmandi.navvistask.numberdatasource.di.dataSourceModule
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            modules(listOf(appModule, dataSourceModule))
        }
    }
}