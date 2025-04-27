package com.noom.interview.fullstack.sleep.data

import com.noom.interview.fullstack.sleep.util.MorningFeeling
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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