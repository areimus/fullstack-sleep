package com.noom.interview.fullstack.sleep.controller

import com.noom.interview.fullstack.sleep.data.SleepLogDTO
import com.noom.interview.fullstack.sleep.repository.SleepLogRepository
import com.noom.interview.fullstack.sleep.util.MorningFeeling
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.`is`
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.get
import org.springframework.http.MediaType
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@WebMvcTest(SleepLogController::class)
@Import(SleepLogControllerTest.TestConfig::class)
class SleepLogControllerTest(@Autowired val mockMvc: MockMvc) {

    companion object {
        val mockSleepLogRepository: SleepLogRepository = mock()
    }

    @TestConfiguration
    class TestConfig {
        @Bean
        fun sleepLogRepository(): SleepLogRepository = mockSleepLogRepository
    }

    @Test
    fun `should create sleep log successfully`() {
        val userId = 1L
        val entryDate = LocalDate.now()
        val bedTime = LocalTime.of(22, 0)
        val wakeTime = LocalTime.of(6, 0)
        val sleepLogDTO = SleepLogDTO(
            id = 1L,
            userId = userId,
            entryDate = entryDate,
            bedTime = bedTime,
            wakeTime = wakeTime,
            totalTimeInBed = 28800,
            morningFeeling = "GOOD",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        whenever(mockSleepLogRepository.createSleepLog(userId, entryDate, bedTime, wakeTime, MorningFeeling.GOOD))
            .thenReturn(Result.success(sleepLogDTO))

        mockMvc.post("/users/$userId/logs") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "entryDate": "$entryDate",
                    "bedTime": "22:00:00",
                    "wakeTime": "06:00:00",
                    "morningFeeling": "GOOD"
                }
            """.trimIndent()
        }.andExpect {
            status { isOk() }
            content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
        }
    }

    @Test
    fun `should fail to create sleep log`() {
        val userId = 1L
        val entryDate = LocalDate.now()
        val bedTime = LocalTime.of(22, 0)
        val wakeTime = LocalTime.of(6, 0)

        whenever(
            mockSleepLogRepository.createSleepLog(userId, entryDate, bedTime, wakeTime, MorningFeeling.GOOD)
        ).thenReturn(Result.failure(IllegalArgumentException("Duplicate log for this date already exists.")))

        mockMvc.post("/users/$userId/logs") {
            contentType = MediaType.APPLICATION_JSON
            content = """
            {
                "entryDate": "$entryDate",
                "bedTime": "22:00:00",
                "wakeTime": "06:00:00",
                "morningFeeling": "GOOD"
            }
        """.trimIndent()
        }.andExpect {
            status { isBadRequest() }
            content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.error", `is`("Duplicate log for this date already exists.")) }
        }
    }

    @Test
    fun `should return sleep log by userId and entryDate`() {
        val userId = 1L
        val entryDate = LocalDate.of(2025, 4, 26)
        val sleepLogDTO = SleepLogDTO(
            id = 1L,
            userId = userId,
            entryDate = entryDate,
            bedTime = LocalTime.of(22, 0),
            wakeTime = LocalTime.of(6, 0),
            totalTimeInBed = 28800, // 8 hours
            morningFeeling = "GOOD",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        whenever(mockSleepLogRepository.getSleepLog(userId, entryDate)).thenReturn(sleepLogDTO)

        mockMvc.get("/users/{userId}/logs/{entryDate}", userId, entryDate) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
            jsonPath("$.userId") { value(userId) }
            jsonPath("$.entryDate") { value(entryDate.toString()) }
        }
    }

    @Test
    fun `should return 404 if sleep log not found`() {
        val userId = 1L
        val entryDate = LocalDate.of(2025, 4, 26)

        whenever(mockSleepLogRepository.getSleepLog(userId, entryDate)).thenReturn(null)

        mockMvc.get("/users/{userId}/logs/{entryDate}", userId, entryDate) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
            content { string(containsString("Sleep log not found")) }
        }
    }

    @Test
    fun `should return list of sleep logs within date range`() {
        val userId = 1L
        val startDate = LocalDate.of(2025, 4, 1)
        val endDate = LocalDate.of(2025, 4, 30)

        val sleepLogs = listOf(
            SleepLogDTO(
                id = 1L,
                userId = userId,
                entryDate = LocalDate.of(2025, 4, 20),
                bedTime = LocalTime.of(22, 0),
                wakeTime = LocalTime.of(6, 0),
                totalTimeInBed = 28800,
                morningFeeling = "GOOD",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            SleepLogDTO(
                id = 2L,
                userId = userId,
                entryDate = LocalDate.of(2025, 4, 21),
                bedTime = LocalTime.of(23, 0),
                wakeTime = LocalTime.of(7, 0),
                totalTimeInBed = 28800,
                morningFeeling = "OK",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        whenever(mockSleepLogRepository.getSleepLogs(userId, startDate, endDate)).thenReturn(sleepLogs)

        mockMvc.get("/users/{userId}/logs", userId) {
            param("startDate", startDate.toString())
            param("endDate", endDate.toString())
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
            jsonPath("$.length()") { value(2) }
        }
    }

    @Test
    fun `should return empty list when no sleep logs found`() {
        val userId = 1L
        val startDate = LocalDate.of(2025, 4, 1)
        val endDate = LocalDate.of(2025, 4, 30)

        whenever(mockSleepLogRepository.getSleepLogs(userId, startDate, endDate))
            .thenReturn(emptyList())

        mockMvc.get("/users/$userId/logs") {
            param("startDate", startDate.toString())
            param("endDate", endDate.toString())
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
            content { json("[]") }
        }
    }
}