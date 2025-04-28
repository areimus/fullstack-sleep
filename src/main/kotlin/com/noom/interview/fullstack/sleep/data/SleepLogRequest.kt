package com.noom.interview.fullstack.sleep.data

import java.time.LocalDate
import java.time.LocalTime

/**
 * A data class representing the complex Request object used to create
 * a new sleep log via the API.
 *
 * @property entryDate The date the entry log holds data for
 * @property bedTime The recorded bedtime for the sleep log
 * @property wakeTime The record time of waking up
 * @property morningFeeling A string, containing one of the valid MorningFeeling values
 */
data class SleepLogRequest(
    val entryDate: LocalDate,
    val bedTime: LocalTime,
    val wakeTime: LocalTime,
    val morningFeeling: String
)