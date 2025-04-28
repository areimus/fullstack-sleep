package com.noom.interview.fullstack.sleep.repository

import com.noom.interview.fullstack.sleep.data.SleepLogAveragesDTO
import com.noom.interview.fullstack.sleep.models.SleepLog
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

/**
 * A repository containing the functionality required to create new sleep logs and
 * fetch individual or sets of sleep logs.
 */
class SleepLogRepository {
    private val logger = LoggerFactory.getLogger(SleepLogRepository::class.java)

    /**
     * Create a new sleep log entry
     *
     * @param userId Long The User associated with the sleep log
     * @param entryDate LocalDate The date the sleep log contains data for
     * @param bedTime LocalTime The recorded bedtime
     * @param wakeTime LocalTime The record wake-up time
     * @param morningFeeling MorningFeeling One of the valid MorningFeeling enum values
     * @throws Exception When attempting to create a duplicate entry on the userId-entryDate unique index
     * @return SleepLogDTO
     */
    fun createSleepLog(
        userId: Long,
        entryDate: LocalDate,
        bedTime: LocalTime,
        wakeTime: LocalTime,
        morningFeeling: MorningFeeling
    ): Result<SleepLogDTO> {
        return try {
            transaction {
                val bedDateTime = entryDate.atTime(bedTime)
                val wakeDateTime = if (wakeTime.isBefore(bedTime)) {
                    entryDate.plusDays(1).atTime(wakeTime)
                } else {
                    entryDate.atTime(wakeTime)
                }

                val totalTimeInBed = ChronoUnit.SECONDS.between(bedDateTime, wakeDateTime).toInt()
                val now = LocalDateTime.now()
                val insertedId = SleepLog.insert {
                    it[SleepLog.userId] = userId
                    it[SleepLog.entryDate] = entryDate
                    it[SleepLog.bedTime] = bedTime
                    it[SleepLog.wakeTime] = wakeTime
                    it[SleepLog.totalTimeInBed] = totalTimeInBed
                    it[SleepLog.morningFeeling] = morningFeeling.toString()
                    it[createdAt] = now
                    it[updatedAt] = now
                } get SleepLog.id

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

    /**
     * Return a single sleep log entry
     *
     * @param userId Long The User associated with the sleep log
     * @param entryDate LocalDate The date of the sleep log to retrieve
     * @return SleepLogDTO or null if no sleep log record exists
     */
    fun getSleepLog(userId: Long, entryDate: LocalDate): SleepLogDTO? = transaction {
        SleepLog.selectAll()
            .where {
                (SleepLog.userId eq userId) and (SleepLog.entryDate eq entryDate)
            }
            .singleOrNull()
            ?.toSleepLogDTO()
    }

    /**
     * Get a set of sleep log entries for a given time range
     *
     * @param userId Long The User associated with the sleep log
     * @param startDate LocalDate The start date of the range to return sleep log records for
     * @param endDate LocalDate The end date of the range to return sleep log records for
     * @return List<SleepLogDTO>
     */
    fun getSleepLogs(userId: Long, startDate: LocalDate, endDate: LocalDate): List<SleepLogDTO> = transaction {
        SleepLog.selectAll()
            .where { (SleepLog.userId eq userId) and (SleepLog.entryDate.between(startDate, endDate)) }
            .orderBy(SleepLog.entryDate to SortOrder.ASC)
            .map { row -> row.toSleepLogDTO() }
    }

    /**
     * Return an N-Day sleep log report containing averages for bed/wake time, total time in
     * bed and morning feeling frequencies.
     *
     *  @param userId Long The User associated with the sleep log
     *  @param reportDays Long The maximum number of days to include in the report
     *  @return SleepLogAveragesDTO or null if no records are found in the requested timeframe
     */
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