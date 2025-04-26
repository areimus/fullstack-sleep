package com.noom.interview.fullstack.sleep.mapper

import com.noom.interview.fullstack.sleep.data.UserDTO
import com.noom.interview.fullstack.sleep.models.Users
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDateTime

fun ResultRow.toUserDTO() = UserDTO(
    id = this[Users.id],
    username = this[Users.username],
    createdAt = this[Users.createdAt]
)

fun toUserDTO(id: Long, username: String, createdAt: LocalDateTime) = UserDTO(
    id = id,
    username = username,
    createdAt = createdAt
)