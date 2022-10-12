package com.example.remindetti.locationreminders.data

import com.example.remindetti.locationreminders.data.dto.ReminderDTO
import com.example.remindetti.locationreminders.data.dto.Result

/**
 * Main entry point for accessing reminders data.
 */
interface ReminderDataSource {
    suspend fun getReminders(): com.example.remindetti.locationreminders.data.dto.Result<List<ReminderDTO>>
    suspend fun saveReminder(reminder: ReminderDTO)
    suspend fun getReminder(id: String): Result<ReminderDTO>
    suspend fun deleteAllReminders()
}