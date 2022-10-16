package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

class FakeDataSource : ReminderDataSource {

    private val reminders = mutableListOf<ReminderDTO>()
    var shouldReturnError: Boolean = false

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return  if(!shouldReturnError) Result.Success(reminders)
        else Result.Error("An error has occured!")
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        reminders.forEach { reminder ->
            if(reminder.id == id)
                return Result.Success(reminder)
        }
        return Result.Error("No such reminder!")
    }

    override suspend fun deleteAllReminders() {
        reminders.clear()
    }
}