package com.noom.interview.fullstack.sleep.util

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus

@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * Catch all exception handler for the API
     */
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf(
                "error" to "An unexpected error occurred. Please try again later.",
                "details" to (ex.localizedMessage ?: "No additional details")
            ))
    }

    /**
     * Handler for the instances of IllegalArgumentException, used when APIs attempt to
     * create duplicate data
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf(
                "error" to (ex.localizedMessage ?: "Invalid input")
            ))
    }
}