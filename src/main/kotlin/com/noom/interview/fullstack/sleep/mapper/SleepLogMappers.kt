package com.noom.interview.fullstack.sleep.mapper

import com.noom.interview.fullstack.sleep.data.SleepLogDTO
import com.noom.interview.fullstack.sleep.models.SleepLog
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Helper function for generating a SleepLogDTO from a ResultRow
 */
fun ResultRow.toSleepLogDTO() = SleepLogDTO(
    id = this[SleepLog.id],
    userId = this[SleepLog.userId],
    entryDate = this[SleepLog.entryDate],
    bedTime = this[SleepLog.bedTime],
    wakeTime = this[SleepLog.wakeTime],
    totalTimeInBed = this[SleepLog.totalTimeInBed],
    morningFeeling = this[SleepLog.morningFeeling],
    createdAt = this[SleepLog.createdAt],
    updatedAt = this[SleepLog.updatedAt]
)

/**
 * Helper function to generate a SleepLogDTO from the corresponding set of data represented
 */
fun toSleepLogDTO(
    id: Long,
    userId: Long,
    entryDate: LocalDate,
    bedTime: LocalTime,
    wakeTime: LocalTime,
    totalTimeInBed: Int,
    morningFeeling: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime
) = SleepLogDTO(
    id = id,
    userId = userId,
    entryDate = entryDate,
    bedTime = bedTime,
    wakeTime = wakeTime,
    totalTimeInBed = totalTimeInBed,
    morningFeeling = morningFeeling,
    createdAt = createdAt,
    updatedAt = updatedAt
)