package com.noom.interview.fullstack.sleep.mapper

import com.noom.interview.fullstack.sleep.data.SleepLogDTO
import com.noom.interview.fullstack.sleep.models.SleepLogs
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun ResultRow.toSleepLogDTO() = SleepLogDTO(
    id = this[SleepLogs.id],
    userId = this[SleepLogs.userId],
    entryDate = this[SleepLogs.entryDate],
    bedTime = this[SleepLogs.bedTime],
    wakeTime = this[SleepLogs.wakeTime],
    totalTimeInBed = this[SleepLogs.totalTimeInBed],
    morningFeeling = this[SleepLogs.morningFeeling],
    createdAt = this[SleepLogs.createdAt],
    updatedAt = this[SleepLogs.updatedAt]
)

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