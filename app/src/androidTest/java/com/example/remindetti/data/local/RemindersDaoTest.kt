package com.example.remindetti.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.remindetti.locationreminders.data.dto.ReminderDTO
import com.example.remindetti.locationreminders.data.local.RemindersDatabase
import com.example.remindetti.locationreminders.reminderslist.MainCoroutineExtension
import com.example.remindetti.locationreminders.reminderslist.InstantExecutorExtension
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@ExtendWith(InstantExecutorExtension::class, MainCoroutineExtension::class)
class RemindersDaoTest : TestCase(){

    private lateinit var database: RemindersDatabase

    @BeforeEach
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @AfterEach
    fun closeDatabase() = database.close()

    @Test
    fun getSavedReminder_findsAndReturnsCorrectData() = runBlockingTest {
        val reminder = ReminderDTO("Rem1","Reminder 1", "Null", 12.0, 12.0)
        database.reminderDao().saveReminder(reminder)
        var result = database.reminderDao().getReminderById(reminder.id)

        assertNotNull(result)
        result = result!!
        assertEquals(reminder.id, result.id)
        assertEquals(reminder.title, result.title)
        assertEquals(reminder.description, result.description)
        assertEquals(reminder.location, result.location)
        assertEquals(reminder.latitude, result.latitude)
        assertEquals(reminder.longitude, result.longitude)
    }

    @Test
    fun getAllReminders_returnsAllReminders() = runBlockingTest {
        val reminder1 = ReminderDTO("Rem1","Reminder 1", "Null", 12.0, 12.0)
        val reminder2 = ReminderDTO("Rem2","Reminder 2", "None", -12.0, -12.0)
        val reminder3 = ReminderDTO("Rem3","Reminder 3", "Void", 0.0, 0.0)

        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)
        database.reminderDao().saveReminder(reminder3)

        val remindersList = database.reminderDao().getReminders()
        val databaseIdsList = mutableListOf<String>()
        remindersList.forEach {
            databaseIdsList.add(it.id)
        }
        val remindersIdsList = listOf(reminder1.id, reminder2.id, reminder3.id).sorted()

        assertNotNull(remindersList)
        assertEquals(remindersIdsList, databaseIdsList.sorted())
    }

    @Test
    fun deleteAllReminders_emptiesTheDatabase() = runBlockingTest {
        val reminder1 = ReminderDTO("Rem1","Reminder 1", "Null", 12.0, 12.0)
        val reminder2 = ReminderDTO("Rem2","Reminder 2", "None", -12.0, -12.0)
        val reminder3 = ReminderDTO("Rem3","Reminder 3", "Void", 0.0, 0.0)

        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)
        database.reminderDao().saveReminder(reminder3)

        database.reminderDao().deleteAllReminders()

        val remindersList = database.reminderDao().getReminders()

        assertEquals(emptyList<ReminderDTO>(), remindersList)
    }

}