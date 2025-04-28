package com.noom.interview.fullstack.sleep.controller

import com.noom.interview.fullstack.sleep.repository.SleepLogRepository
import com.noom.interview.fullstack.sleep.data.SleepLogDTO
import com.noom.interview.fullstack.sleep.data.SleepLogRequest
import com.noom.interview.fullstack.sleep.util.MorningFeeling
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/users/{userId}/logs")
class SleepLogController(
    private val sleepLogRepository: SleepLogRepository = SleepLogRepository()

) {

    /**
     * Create a new sleep log entry
     */
    @PostMapping
    fun createSleepLog(
        @PathVariable userId: Long,
        @RequestBody request: SleepLogRequest
    ): ResponseEntity<Any> {
        val result = sleepLogRepository.createSleepLog(
            userId,
            request.entryDate,
            request.bedTime,
            request.wakeTime,
            MorningFeeling.valueOf(request.morningFeeling)
        )

        return result.fold(
            onSuccess = { ResponseEntity.ok(it) },
            onFailure = { ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to it.message)) }
        )
    }

    /**
     * Retrieve a single sleep log for the given user and date
     */
    @GetMapping("/{entryDate}")
    fun getSleepLog(
        @PathVariable userId: Long,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) entryDate: LocalDate
    ): ResponseEntity<Any> {
        val log = sleepLogRepository.getSleepLog(userId, entryDate)
        return if (log != null) {
            ResponseEntity.ok(log)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to "Sleep log not found for userId $userId on $entryDate"))
        }
    }

    /**
     * Retrieve last night's sleep log. The log created on the current date is assumed to be
     * the last night's log
     */
    @GetMapping("/lastNight")
    fun getLastNightSleepLog(
        @PathVariable userId: Long
    ): ResponseEntity<Any> {
        val entryDate = LocalDate.now()
        val log = sleepLogRepository.getSleepLog(userId, entryDate)
        return if (log != null) {
            ResponseEntity.ok(log)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to "Sleep log not found for userId $userId on $entryDate"))
        }
    }


    /**
     * Get a set of sleep logs for a given user and date range
     */
    @GetMapping
    fun getSleepLogs(
        @PathVariable userId: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): ResponseEntity<List<SleepLogDTO>> {
        val logs = sleepLogRepository.getSleepLogs(
            userId,
            startDate,
            endDate
        )
        return ResponseEntity.ok(logs)
    }

    /**
     * Set a sleep log averages report for a given number of days, defaults to 30.
     */
    @GetMapping("/report")
    fun getSleepLogReport(
        @PathVariable userId: Long,
        @RequestParam(required = false, defaultValue = "30") days: Long
    ): ResponseEntity<Any> {
        val report = sleepLogRepository.getSleepLogReport(userId, days)
        return if (report != null) {
            ResponseEntity.ok(report)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to "No sleep logs found for userId $userId in the last $days days"))
        }
    }
}