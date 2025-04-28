package com.noom.interview.fullstack.sleep.mapper

import com.noom.interview.fullstack.sleep.data.UserDTO
import com.noom.interview.fullstack.sleep.models.User
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDateTime

/**
 * Helper function to generate a UserDTO from a ResultRow
 */
fun ResultRow.toUserDTO() = UserDTO(
    id = this[User.id],
    username = this[User.username],
    createdAt = this[User.createdAt]
)

/**
 * Helper function to generate a UserDTO from the corresponding set of data required
 */
fun toUserDTO(id: Long, username: String, createdAt: LocalDateTime) = UserDTO(
    id = id,
    username = username,
    createdAt = createdAt
)