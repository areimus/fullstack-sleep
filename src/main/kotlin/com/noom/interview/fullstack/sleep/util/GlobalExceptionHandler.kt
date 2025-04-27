import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus

@RestControllerAdvice
class GlobalAPIExceptionHandler {

    // Already existing generic handler
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf(
                "error" to "An unexpected error occurred. Please try again later.",
                "details" to (ex.localizedMessage ?: "No additional details")
            ))
    }

    // 🆕 New clean handler for IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf(
                "error" to (ex.localizedMessage ?: "Invalid input")
            ))
    }
}