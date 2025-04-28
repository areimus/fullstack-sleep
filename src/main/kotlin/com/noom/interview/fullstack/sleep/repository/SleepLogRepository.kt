package com.noom.interview.fullstack.sleep.repository

import com.noom.interview.fullstack.sleep.data.SleepLogAveragesDTO
import com.noom.interview.fullstack.sleep.models.SleepLogs
import com.noom.interview.fullstack.sleep.data.SleepLogDTO
import com.noom.interview.fullstack.sleep.mapper.toSleepLogDTO
import com.noom.interview.fullstack.sleep.util.MorningFeeling

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.sql.SQLException

class SleepLogRepository {
    private val logger = LoggerFactory.getLogger(SleepLogRepository::class.java)

    fun createSleepLog(
        userId: Long,
        entryDate: LocalDate,
        bedTime: LocalTime,
        wakeTime: LocalTime,
        morningFeeling: MorningFeeling
    ): Result<SleepLogDTO> {
        return try {
            transaction {
                val totalTimeInBed = ChronoUnit.SECONDS.between(bedTime, wakeTime).toInt()
                val now = LocalDateTime.now()
                val insertedId = SleepLogs.insert {
                    it[SleepLogs.userId] = userId
                    it[SleepLogs.entryDate] = entryDate
                    it[SleepLogs.bedTime] = bedTime
                    it[SleepLogs.wakeTime] = wakeTime
                    it[SleepLogs.totalTimeInBed] = totalTimeInBed
                    it[SleepLogs.morningFeeling] = morningFeeling.toString()
                    it[createdAt] = now
                    it[updatedAt] = now
                } get SleepLogs.id

                Result.success(
                    toSleepLogDTO(
                        insertedId, userId, entryDate, bedTime, wakeTime, totalTimeInBed,
                        morningFeeling.toString(), now, now
                    )
                )
            }
        } catch (ex: SQLException) {
            if (ex.sqlState == "23505") {
                logger.debug("Duplicate sleep log creation attempt for userId=$userId, entryDate=$entryDate", ex)
                Result.failure(IllegalArgumentException("A sleep log for this user and date already exists."))
            } else {
                logger.debug("SQLException sqlState=${ex.sqlState} encountered for input -> userId=$userId, entryDate=$entryDate", ex)
                Result.failure(ex)
            }
        } catch (ex: Exception) {
            logger.debug("Exception encountered for input -> userId=$userId, entryDate=$entryDate", ex)
            Result.failure(ex)
        }
    }

    fun getSleepLog(userId: Long, entryDate: LocalDate): SleepLogDTO? = transaction {
        SleepLogs.selectAll()
            .where {
                (SleepLogs.userId eq userId) and (SleepLogs.entryDate eq entryDate)
            }
            .singleOrNull()
            ?.toSleepLogDTO()
    }

    fun getSleepLogs(userId: Long, startDate: LocalDate, endDate: LocalDate): List<SleepLogDTO> = transaction {
        SleepLogs.selectAll()
            .where { (SleepLogs.userId eq userId) and (SleepLogs.entryDate.between(startDate, endDate)) }
            .orderBy(SleepLogs.entryDate to SortOrder.ASC)
            .map { row -> row.toSleepLogDTO() }
    }

    fun getSleepLogReport(userId: Long, reportDays: Long): SleepLogAveragesDTO? {
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(reportDays)

        val logs = getSleepLogs(userId, startDate, endDate)

        if (logs.isEmpty()) return null

        val avgTotalTimeInBed = logs.map { it.totalTimeInBed }.average().toInt()

        val avgBedTimeSeconds = logs.map { it.bedTime.toSecondOfDay() }.average().toInt()
        val avgWakeTimeSeconds = logs.map { it.wakeTime.toSecondOfDay() }.average().toInt()

        val avgBedTime = LocalTime.ofSecondOfDay(avgBedTimeSeconds.toLong())
        val avgWakeTime = LocalTime.ofSecondOfDay(avgWakeTimeSeconds.toLong())

        val feelingFrequencies = logs.groupingBy { it.morningFeeling }
            .eachCount()

        return SleepLogAveragesDTO(
            startDate = startDate,
            endDate = endDate,
            averageTotalTimeInBedSeconds = avgTotalTimeInBed,
            averageBedTime = avgBedTime,
            averageWakeTime = avgWakeTime,
            morningFeelingFrequencies = feelingFrequencies
        )
    }
}