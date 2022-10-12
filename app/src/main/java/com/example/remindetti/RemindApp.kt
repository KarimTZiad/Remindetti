package com.example.remindetti

import android.app.Application
import com.example.remindetti.locationreminders.data.ReminderDataSource
import com.example.remindetti.locationreminders.data.local.LocalDB
import com.example.remindetti.locationreminders.data.local.RemindersLocalRepository
import com.example.remindetti.locationreminders.reminderslist.RemindersListViewModel
import com.example.remindetti.locationreminders.savereminder.SaveReminderViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class RemindApp : Application() {
    override fun onCreate() {
        super.onCreate()

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
            viewModel {
                RemindersListViewModel(
                    get(),
                    get() as ReminderDataSource
                )
            }
            //Declare singleton definitions to be later injected using by inject()
            single {
                //This view model is declared singleton to be used across multiple fragments
                SaveReminderViewModel(
                    get(),
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(this@RemindApp) }
        }

        startKoin {
            androidContext(this@RemindApp)
            modules(listOf(myModule))
        }
    }
}