package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.RemindApp
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.koin.android.ext.android.getKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    private lateinit var repository: ReminderDataSource

    @BeforeEach
    fun setUpRepository() {
        val app = RemindApp()
        repository = app.getKoin().get()
    }

    @AfterEach
    fun clearRepository() = runBlocking {
        repository.deleteAllReminders()
    }

    //    TODO: Add testing implementation to the RemindersLocalRepository.kt
    @Test
    fun deleteAllReminders_returnsEmptyList() = runBlocking {
        val rem1 = ReminderDTO("Rem1","Reminder 1", "Null", 12.0, 12.0)
        val rem2 = ReminderDTO("Rem2","Reminder 2", "None", -12.0, -12.0)
        repository.saveReminder(rem1)
        repository.saveReminder(rem2)

        repository.deleteAllReminders()

        val res = repository.getReminders() as Result.Success<List<ReminderDTO>>
        assertEquals(0, res.data.size)
    }

}