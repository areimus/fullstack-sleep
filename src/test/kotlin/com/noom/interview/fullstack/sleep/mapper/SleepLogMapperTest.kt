package com.noom.interview.fullstack.sleep.mapper

import com.noom.interview.fullstack.sleep.models.SleepLog
import com.noom.interview.fullstack.sleep.models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import org.jetbrains.exposed.sql.Database

class SleepLogMapperTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            Database.connect(
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
                driver = "org.h2.Driver"
            )
            transaction {
                create(SleepLog)
            }
        }
    }

    @Test
    fun `should map ResultRow to SleepLogDTO correctly`() {
        val now = LocalDateTime.now()
        val entryDate = LocalDate.of(2025, 4, 24)
        val bedTime = LocalTime.of(23, 0)
        val wakeTime = LocalTime.of(7, 30)

        val insertedUserId = transaction {
            // ✅ Insert a dummy user first
            User.insert {
                it[username] = "testuser-forsleeplog"
                it[createdAt] = now
            } get User.id
        }

        val insertedId = transaction {
            SleepLog.insert {
                it[userId] = 1L
                it[SleepLog.entryDate] = entryDate
                it[SleepLog.bedTime] = bedTime
                it[SleepLog.wakeTime] = wakeTime
                it[totalTimeInBed] = 8 * 60 * 60
                it[morningFeeling] = "GOOD"
                it[createdAt] = now
                it[updatedAt] = now
            } get SleepLog.id
        }

        val sleepLogRow = transaction {
            SleepLog.selectAll().where { SleepLog.id eq insertedId }
                .single()
        }

        val sleepLogDTO = sleepLogRow.toSleepLogDTO()

        assertEquals(insertedId, sleepLogDTO.id)
        assertEquals(1L, sleepLogDTO.userId)
        assertEquals(entryDate, sleepLogDTO.entryDate)
        assertEquals(bedTime, sleepLogDTO.bedTime)
        assertEquals(wakeTime, sleepLogDTO.wakeTime)
        assertEquals(8 * 60 * 60, sleepLogDTO.totalTimeInBed)
        assertEquals("GOOD", sleepLogDTO.morningFeeling)
        assertEquals(now, sleepLogDTO.createdAt)
        assertEquals(now, sleepLogDTO.updatedAt)
    }

    @Test
    fun `should create SleepLogDTO from parameters correctly`() {
        val now = LocalDateTime.now()
        val entryDate = LocalDate.of(2025, 4, 25)
        val bedTime = LocalTime.of(22, 30)
        val wakeTime = LocalTime.of(6, 30)
        val totalTimeInBed = 8 * 60 * 60
        val morningFeeling = "OK"

        val sleepLogDTO = toSleepLogDTO(
            id = 123L,
            userId = 1L,
            entryDate = entryDate,
            bedTime = bedTime,
            wakeTime = wakeTime,
            totalTimeInBed = totalTimeInBed,
            morningFeeling = morningFeeling,
            createdAt = now,
            updatedAt = now
        )

        assertEquals(123L, sleepLogDTO.id)
        assertEquals(1L, sleepLogDTO.userId)
        assertEquals(entryDate, sleepLogDTO.entryDate)
        assertEquals(bedTime, sleepLogDTO.bedTime)
        assertEquals(wakeTime, sleepLogDTO.wakeTime)
        assertEquals(totalTimeInBed, sleepLogDTO.totalTimeInBed)
        assertEquals(morningFeeling, sleepLogDTO.morningFeeling)
        assertEquals(now, sleepLogDTO.createdAt)
        assertEquals(now, sleepLogDTO.updatedAt)
    }
}