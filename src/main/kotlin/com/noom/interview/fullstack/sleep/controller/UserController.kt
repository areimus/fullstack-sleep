package com.noom.interview.fullstack.sleep.controller

import com.noom.interview.fullstack.sleep.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userRepository: UserRepository = UserRepository()
) {

    @PostMapping("/create")
    fun createUser(@RequestParam username: String): ResponseEntity<Any> {
        return userRepository.createUser(username)
            .fold(
                onSuccess = { user -> ResponseEntity.ok(user) },
                onFailure = { error ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(mapOf("error" to "Failed to create user: ${error.localizedMessage}"))
                }
            )
    }

    @GetMapping("/findByUsername")
    fun findByUsername(@RequestParam username: String): ResponseEntity<Any> {
        val user = userRepository.findByUsername(username)
        return if (user != null) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to "User not found with username: $username"))
        }
    }
}