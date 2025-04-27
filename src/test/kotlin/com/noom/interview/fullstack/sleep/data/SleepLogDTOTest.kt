package com.noom.interview.fullstack.sleep.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class SleepLogDTOTest {

    @Test
    fun `should create SleepLogDTO with correct values`() {
        val now = LocalDateTime.now()
        val sleepLog = SleepLogDTO(
            id = 1L,
            userId = 1L,
            entryDate = LocalDate.of(2025, 4, 24),
            bedTime = LocalTime.of(23, 0),
            wakeTime = LocalTime.of(7, 30),
            totalTimeInBed = 8 * 60 * 60,
            morningFeeling = "GOOD",
            createdAt = now,
            updatedAt = now
        )

        assertEquals(1L, sleepLog.id)
        assertEquals(1L, sleepLog.userId)
        assertEquals(LocalDate.of(2025, 4, 24), sleepLog.entryDate)
        assertEquals(LocalTime.of(23, 0), sleepLog.bedTime)
        assertEquals(LocalTime.of(7, 30), sleepLog.wakeTime)
        assertEquals(8 * 60 * 60, sleepLog.totalTimeInBed)
        assertEquals("GOOD", sleepLog.morningFeeling)
        assertEquals(now, sleepLog.createdAt)
        assertEquals(now, sleepLog.updatedAt)
    }
}