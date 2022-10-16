package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.savereminder.InstantExecutorExtension
import com.udacity.project4.locationreminders.savereminder.MainCoroutineExtension
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class, MainCoroutineExtension::class)
@RunWith(AndroidJUnit4::class)
class RemindersListViewModelTest {
    private lateinit var repository : FakeDataSource
    private lateinit var viewModel : RemindersListViewModel

    @BeforeEach
    fun setUpRepositoryAndViewModel(){
        repository = FakeDataSource()
        viewModel = RemindersListViewModel(Application(), repository)
    }

    @AfterEach
    fun clearKoin() {
        stopKoin()
    }

    //TODO: provide testing to the RemindersListViewModel and its live data objects
    @Test
    fun loadRemindersFromRepository_shouldPopulateViewModelList() = runBlocking {
        val rem1 = ReminderDTO("Rem1","Reminder 1", "Null", 12.0, 12.0)
        val rem2 = ReminderDTO("Rem2","Reminder 2", "None", -12.0, -12.0)
        repository.saveReminder(rem1)
        repository.saveReminder(rem2)

        viewModel.loadReminders()

        assertNotEquals(0, viewModel.remindersList.value!!.size)
        assertEquals(2, viewModel.remindersList.value!!.size)
    }

    @Test
    fun shouldReturnError_displaysSnackBar() {
        repository.shouldReturnError = true

        viewModel.loadReminders()
        assertEquals("An error has occured!", viewModel.showSnackBar.value)
    }

}