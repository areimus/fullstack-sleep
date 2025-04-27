package com.noom.interview.fullstack.sleep.mapper

import com.noom.interview.fullstack.sleep.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.Database

class UserMapperTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            // In-memory H2 database setup
            Database.connect(
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;",
                driver = "org.h2.Driver"
            )
            transaction {
                create(Users)
            }
        }
    }

    @Test
    fun `should map ResultRow to UserDTO correctly`() {
        val now = LocalDateTime.now()

        val insertedId = transaction {
            Users.insert {
                it[username] = "testuser"
                it[createdAt] = now
            } get Users.id
        }

        val userRow = transaction {
            Users.selectAll().where { Users.id eq insertedId }
                .single()
        }

        val userDTO = userRow.toUserDTO()

        assertEquals(insertedId, userDTO.id)
        assertEquals("testuser", userDTO.username)
        assertEquals(now, userDTO.createdAt)
    }

    @Test
    fun `should create SleepLogDTO from parameters correctly`() {
        val now = LocalDateTime.now()
        val userDTO = toUserDTO(
            id = 456L,
            username = "testuser-2",
            createdAt = now
        )

        assertEquals(456L, userDTO.id)
        assertEquals("testuser-2", userDTO.username)
        assertEquals(now, userDTO.createdAt)
    }
}