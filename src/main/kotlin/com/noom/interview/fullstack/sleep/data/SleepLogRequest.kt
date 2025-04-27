package com.noom.interview.fullstack.sleep.data

import java.time.LocalDate
import java.time.LocalTime

data class SleepLogRequest(
    val entryDate: LocalDate,
    val bedTime: LocalTime,
    val wakeTime: LocalTime,
    val morningFeeling: String
)