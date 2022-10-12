package com.example.remindetti.locationreminders.data.local

import com.example.remindetti.locationreminders.data.ReminderDataSource
import com.example.remindetti.locationreminders.data.dto.ReminderDTO
import com.example.remindetti.locationreminders.data.dto.Result

class FakeDataSource : ReminderDataSource {

    private val reminders = mutableListOf<ReminderDTO>()

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return Result.Success(reminders)
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