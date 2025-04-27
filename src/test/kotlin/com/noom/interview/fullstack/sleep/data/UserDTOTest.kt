package com.noom.interview.fullstack.sleep.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class UserDTOTest {

    @Test
    fun `should create UserDTO with correct values`() {
        val now = LocalDateTime.now()
        val user = UserDTO(
            id = 1L,
            username = "testuser",
            createdAt = now
        )

        assertEquals(1L, user.id)
        assertEquals("testuser", user.username)
        assertEquals(now, user.createdAt)
    }
}