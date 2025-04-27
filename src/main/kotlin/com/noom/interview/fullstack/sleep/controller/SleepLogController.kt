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
}