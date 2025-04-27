package com.noom.interview.fullstack.sleep.util

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.get
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

class NullMessageException : RuntimeException() {
    override fun getLocalizedMessage(): String? = null
}

@WebMvcTest
@Import(GlobalExceptionHandlerTest.TestConfiguration::class)
class GlobalExceptionHandlerTest(@Autowired val mockMvc: MockMvc) {

    @org.springframework.context.annotation.Configuration
    class TestConfiguration {
        @org.springframework.context.annotation.Bean
        fun testController() = TestController()

        @org.springframework.context.annotation.Bean
        fun globalExceptionHandler() = GlobalExceptionHandler()
    }

    @RestController
    @RequestMapping("/test-exceptions")
    class TestController {

        @GetMapping("/illegal-argument")
        fun illegalArgument(): String {
            throw IllegalArgumentException("Invalid input for testing")
        }

        @GetMapping("/illegal-argument-null")
        fun throwIllegalArgumentNull(): Nothing {
            throw object : IllegalArgumentException() {
                override fun getLocalizedMessage(): String? = null
            }
        }

        @GetMapping("/generic-error")
        fun genericError(): String {
            throw RuntimeException("Something went wrong")
        }

        @GetMapping("/unknown")
        fun throwUnknownException(): Nothing {
            throw object : RuntimeException() {
                override fun getLocalizedMessage(): String? = null
            }
        }
    }

    @Test
    fun `should handle IllegalArgumentException with 400`() {
        mockMvc.get("/test-exceptions/illegal-argument")
            .andExpect {
                status { isBadRequest() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
                jsonPath("$.error") { value("Invalid input for testing") }
            }
    }

    @Test
    fun `should handle IllegalArgumentException with null localizedMessage and return default message`() {
        mockMvc.get("/test-exceptions/illegal-argument-null")
            .andExpect {
                status { isBadRequest() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
                jsonPath("$.error") { value("Invalid input") }
            }
    }

    @Test
    fun `should handle generic Exception with 500`() {
        mockMvc.get("/test-exceptions/generic-error")
            .andExpect {
                status { isInternalServerError() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
                jsonPath("$.error") { value("An unexpected error occurred. Please try again later.") }
                jsonPath("$.details") { value("Something went wrong") }
            }
    }

    @Test
    fun `should handle Exception with null localizedMessage and return default details`() {
        mockMvc.get("/test-exceptions/unknown")
            .andExpect {
                status { isInternalServerError() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
                jsonPath("$.error") { value("An unexpected error occurred. Please try again later.") }
                jsonPath("$.details") { value("No additional details") }
            }
    }
}