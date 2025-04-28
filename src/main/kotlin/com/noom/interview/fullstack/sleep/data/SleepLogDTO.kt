package com.noom.interview.fullstack.sleep.data

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Data class representing a single SleepLog.
 *
 * @property id The unique id generated for the sleep log entry
 * @property userId The user (as defined by UserDTO) that is associated with the sleep log
 * @property entryDate The Date the sleep log holds information on -- This is not necessarily
 *                     the same as the creation date of the record in the persistent storage
 * @property bedTime The recorded bedtime for the sleep log
 * @property wakeTime The record time of waking up
 * @property totalTimeInBed The time spent in bed, in seconds
 * @property morningFeeling A string, containing one of the valid MorningFeeling values
 * @property createdAt The date the log entry was created in the persistent storage
 * @property updatedAt The date the log entry was last modified
 */
data class SleepLogDTO(
    val id: Long,
    val userId: Long,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val entryDate: LocalDate,
    @DateTimeFormat(pattern = "HH:mm:ss")
    val bedTime: LocalTime,
    @DateTimeFormat(pattern = "HH:mm:ss")
    val wakeTime: LocalTime,
    val totalTimeInBed: Int,
    val morningFeeling: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)