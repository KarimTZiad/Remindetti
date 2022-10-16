package com.udacity.project4.locationreminders.reminderslist

import android.os.Bundle
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.RemindApp
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.runner.RunWith
import org.koin.android.ext.android.getKoin
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@ExtendWith(InstantExecutorExtension::class, MainCoroutineExtension::class)
@MediumTest
class RemindersFragmentTest : TestCase(){

    private lateinit var repository: ReminderDataSource

    @BeforeEach
    fun setUpRepository() {
        val app = RemindApp()
        repository = app.getKoin().get()
    }

    @AfterEach
    fun resetRepository() = runBlocking {
        repository.deleteAllReminders()
    }

    //    TODO: test the navigation of the fragments.
    @Test
    fun clickingFAB_navigatesToSaveReminderFragment () {
        val scenario = launchFragmentInContainer<RemindersFragment>(Bundle(), R.style.Theme_Remindetti)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        onView(withId(R.id.addReminderFAB)).perform(click())
        verify(navController).navigate(RemindersFragmentDirections.toSaveReminder())
    }

    //    TODO: test the displayed data on the UI.
    @Test
    fun remindersList_DisplayedInUI(): Unit = runBlocking {
        val rem1 = ReminderDTO("Rem1","Reminder 1", "Null", 12.0, 12.0)
        val rem2 = ReminderDTO("Rem2","Reminder 2", "None", -12.0, -12.0)
        repository.saveReminder(rem1)
        repository.saveReminder(rem2)

        launchFragmentInContainer<RemindersFragment>(Bundle(), R.style.Theme_Remindetti)

        onView(withId(R.id.reminderssRecyclerView))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText(rem1.title))
                )
            )
        onView(withId(R.id.reminderssRecyclerView))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText(rem2.title))
                )
            )
    }

}

class InstantExecutorExtension : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance()
            .setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                override fun postToMainThread(runnable: Runnable) = runnable.run()

                override fun isMainThread(): Boolean = true
            })
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

}

@ExperimentalCoroutinesApi
class MainCoroutineExtension(private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()):
    BeforeEachCallback, AfterEachCallback, TestCoroutineScope by TestCoroutineScope(dispatcher) {

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}