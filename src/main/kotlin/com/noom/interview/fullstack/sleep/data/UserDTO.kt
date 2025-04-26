package com.noom.interview.fullstack.sleep.data

import java.time.LocalDateTime

data class UserDTO(
    val id: Long,
    val username: String,
    val createdAt: LocalDateTime
)