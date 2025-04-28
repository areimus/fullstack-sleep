package com.noom.interview.fullstack.sleep.data

import java.time.LocalDate
import java.time.LocalTime

/**
 * A data class for representing the derived data used by the N-Day log reports.
 *
 * @property startDate The start Date of the logs used to calculate averages
 * @property endDate The end Date of the logs used to calculate averages
 * @property averageTotalTimeInBedSeconds The average total time in bed, in seconds
 * @property averageBedTime The average time a bedtime was recorded, calculated across the included logs
 * @property averageWakeTime The average time waking up that was calculated across the included logs
 * @property morningFeelingFrequencies The frequency of each MorningFeeling for the included logs
 */
data class SleepLogAveragesDTO(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val averageTotalTimeInBedSeconds: Int,
    val averageBedTime: LocalTime,
    val averageWakeTime: LocalTime,
    val morningFeelingFrequencies: Map<String, Int>
)