package com.example.remindetti.locationreminders.savereminder

import android.app.Application
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.remindetti.locationreminders.data.dto.ReminderDTO
import com.example.remindetti.locationreminders.data.local.FakeDataSource
import com.example.remindetti.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import com.example.remindetti.locationreminders.data.dto.Result

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class, MainCoroutineExtension::class)
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest{

    private lateinit var repository : FakeDataSource
    private lateinit var viewModel : SaveReminderViewModel

    @BeforeEach
    fun setUpRepositoryAndViewModel(){
        repository = FakeDataSource()
        viewModel = SaveReminderViewModel(Application(), repository)
    }

    @AfterEach
    fun clearKoin() {
        stopKoin()
    }

    @Test
    fun validateAndSaveReminder_shouldSaveReminderInRepository() = runBlocking {
        val reminder = ReminderDataItem("Rem1","Reminder 1", "Null", 12.0, 12.0)
        val reminderDTO = ReminderDTO("Rem1","Reminder 1", "Null", 12.0, 12.0, id=reminder.id)
        viewModel.validateAndSaveReminder(reminder)
        assertEquals(Result.Success(reminderDTO), repository.getReminder(reminder.id))
    }

    @Test
    fun emptyTitle_shouldReturnError() = runBlocking {
        val reminder = ReminderDataItem("","Reminder 1", "Null", 12.0, 12.0)
        assertFalse(viewModel.validateEnteredData(reminder))
    }

    @Test
    fun unsetLocation_shouldReturnError() = runBlocking {
        val reminder = ReminderDataItem("Rem1","Reminder 1", null, 12.0, 12.0)
        assertFalse(viewModel.validateEnteredData(reminder))
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