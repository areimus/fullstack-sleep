package com.noom.interview.fullstack.sleep.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.noom.interview.fullstack.sleep.data.UserDTO
import com.noom.interview.fullstack.sleep.repository.UserRepository
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime

@WebMvcTest(UserController::class)
@Import(UserControllerTest.TestConfig::class)
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @TestConfiguration
    class TestConfig {
        @Bean
        fun userRepository(): UserRepository = mock(UserRepository::class.java)
    }

    @Test
    fun `should create user successfully`() {
        val username = "testuser-controller1"
        val userDTO = UserDTO(1L, username, LocalDateTime.now())

        `when`(userRepository.createUser(username)).thenReturn(Result.success(userDTO))

        mockMvc.post("/users/create") {
            param("username", username)
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(1) }
                jsonPath("$.username") { value(username) }
            }
    }

    @Test
    fun `should return bad request if user creation fails`() {
        val username = "baduser"

        `when`(userRepository.createUser(username))
            .thenReturn(Result.failure(IllegalArgumentException("Username already exists")))

        mockMvc.post("/users/create") {
            param("username", username)
        }
            .andExpect {
                status { isBadRequest() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
                content { string(containsString("Failed to create user: Username already exists")) }
            }
    }

    @Test
    fun `should return user by username`() {
        val username = "testuser-controller2"
        val userDTO = UserDTO(2L, username, LocalDateTime.now())

        `when`(userRepository.findByUsername(username)).thenReturn(userDTO)

        mockMvc.get("/users/findByUsername") {
            param("username", username)
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(2) }
                jsonPath("$.username") { value(username) }
            }
    }

    @Test
    fun `should return 404 if user not found`() {
        val username = "nonexistent"

        `when`(userRepository.findByUsername(username)).thenReturn(null)

        mockMvc.get("/users/findByUsername") {
            param("username", username)
        }
            .andExpect {
                status { isNotFound() }
            }
    }
}