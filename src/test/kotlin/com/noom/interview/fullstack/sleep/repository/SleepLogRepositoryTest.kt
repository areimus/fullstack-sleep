package com.noom.interview.fullstack.sleep.repository

import com.noom.interview.fullstack.sleep.models.SleepLogs
import com.noom.interview.fullstack.sleep.models.Users
import com.noom.interview.fullstack.sleep.util.MorningFeeling
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SleepLogRepositoryTest {

    private val sleepLogRepository = SleepLogRepository()

    @BeforeAll
    fun setupDatabase() {
        Database.connect(
            "jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver"
        )
        transaction {
            SchemaUtils.create(Users, SleepLogs)
        }
    }

    @BeforeEach
    fun cleanDatabase() {
        transaction {
            SleepLogs.deleteAll()
            Users.deleteAll()
        }
    }

    private fun insertTestUser(username: String = "testuser-sleeplogrepository"): Long {
        return transaction {
            Users.insert {
                it[Users.username] = username
                it[createdAt] = LocalDateTime.now()
            } get Users.id
        }
    }

    private fun Throwable.findSQLException(): java.sql.SQLException? {
        var current: Throwable? = this
        while (current != null) {
            if (current is java.sql.SQLException) {
                return current
            }
            current = current.cause
        }
        return null
    }

    @Test
    fun `should create a sleep log successfully`() {
        val userId = insertTestUser()
        val entryDate = LocalDate.now()
        val bedTime = LocalTime.of(22, 0)
        val wakeTime = LocalTime.of(6, 0)

        val result = sleepLogRepository.createSleepLog(
            userId, entryDate, bedTime, wakeTime, MorningFeeling.BAD
        )

        assertTrue(result.isSuccess)
        val sleepLog = result.getOrNull()
        assertNotNull(sleepLog)
        assertEquals(userId, sleepLog?.userId)
        assertEquals(entryDate, sleepLog?.entryDate)
        assertEquals(bedTime, sleepLog?.bedTime)
        assertEquals(wakeTime, sleepLog?.wakeTime)
    }

    @Test
    fun `should retrieve sleep log by userId and entryDate`() {
        val userId = insertTestUser()
        val entryDate = LocalDate.now()
        val bedTime = LocalTime.of(23, 0)
        val wakeTime = LocalTime.of(7, 0)

        sleepLogRepository.createSleepLog(userId, entryDate, bedTime, wakeTime, MorningFeeling.OK)

        val retrieved = sleepLogRepository.getSleepLog(userId, entryDate)

        assertNotNull(retrieved)
        assertEquals(userId, retrieved?.userId)
        assertEquals(entryDate, retrieved?.entryDate)
    }

    @Test
    fun `should retrieve sleep logs for date range`() {
        val userId = insertTestUser()

        val day1 = LocalDate.now().minusDays(2)
        val day2 = LocalDate.now().minusDays(1)
        val day3 = LocalDate.now()

        sleepLogRepository.createSleepLog(userId, day1, LocalTime.of(22, 0), LocalTime.of(6, 0), MorningFeeling.GOOD)
        sleepLogRepository.createSleepLog(userId, day2, LocalTime.of(22, 30), LocalTime.of(6, 30), MorningFeeling.OK)
        sleepLogRepository.createSleepLog(userId, day3, LocalTime.of(23, 0), LocalTime.of(7, 0), MorningFeeling.BAD)

        val logs = sleepLogRepository.getSleepLogs(
            userId,
            day1,
            day3
        )

        assertEquals(3, logs.size)
    }

    @Test
    fun `should return null when sleep log does not exist`() {
        val userId = insertTestUser()
        val nonExistentDate = LocalDate.now().plusDays(10)

        val result = sleepLogRepository.getSleepLog(userId, nonExistentDate)

        assertNull(result)
    }

    @Test
    fun `should fail with duplicate sleep log for same user and entryDate`() {
        val userId = insertTestUser()
        val entryDate = LocalDate.now()
        val bedTime = LocalTime.of(22, 0)
        val wakeTime = LocalTime.of(6, 0)

        val firstResult = sleepLogRepository.createSleepLog(
            userId, entryDate, bedTime, wakeTime, MorningFeeling.GOOD
        )
        assertTrue(firstResult.isSuccess)

        // Second insert with same (userId, entryDate) should fail
        val secondResult = sleepLogRepository.createSleepLog(
            userId, entryDate, bedTime, wakeTime, MorningFeeling.GOOD
        )

        assertTrue(secondResult.isFailure)

        val exception = secondResult.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is IllegalArgumentException)
        assertEquals("A sleep log for this user and date already exists.", exception?.message)
    }
}