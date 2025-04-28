package com.noom.interview.fullstack.sleep.data

import java.time.LocalDateTime

/**
 * Data class representing a user.
 *
 * @property id The unique identifier associated with the user
 * @property username The unique human friendly identifier associated with the user
 * @property createdAt Creation time of the user in the application's persistent storage
 */
data class UserDTO(
    val id: Long,
    val username: String,
    val createdAt: LocalDateTime
)