package com.noom.interview.fullstack.sleep.util

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalAPIExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalAPIExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception): ResponseEntity<Map<String, String>> {
        logger.error("Unhandled exception caught: ", ex)

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf(
                "error" to "An unexpected error occurred. Please try again later.",
                "details" to (ex.localizedMessage ?: "No additional details")
            ))
    }
}