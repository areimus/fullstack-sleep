package com.noom.interview.fullstack.sleep.data

import java.time.LocalDate
import java.time.LocalTime

data class SleepLogAveragesDTO(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val averageTotalTimeInBedSeconds: Int,
    val averageBedTime: LocalTime,
    val averageWakeTime: LocalTime,
    val morningFeelingFrequencies: Map<String, Int>
)