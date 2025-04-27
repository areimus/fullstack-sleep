package com.noom.interview.fullstack.sleep.repository

import com.noom.interview.fullstack.sleep.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.jetbrains.exposed.sql.Database
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    private val userRepository = UserRepository()

    @BeforeAll
    fun setupDatabase() {
        Database.connect(
            "jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver"
        )

        transaction {
            SchemaUtils.create(Users)
        }
    }

    @BeforeEach
    fun cleanDatabase() {
        transaction {
            Users.deleteAll()
        }
    }

    @Test
    fun `should create a user successfully`() {
        val username = "testuser-repository-test1"
        val result = userRepository.createUser(username)

        assertTrue(result.isSuccess)
        val user = result.getOrNull()
        assertNotNull(user)
        assertEquals(username, user?.username)
    }

    @Test
    fun `should find user by username`() {
        val username = "testuser-repository-test2"
        transaction {
            Users.insert {
                it[Users.username] = username
                it[createdAt] = LocalDateTime.now()
            }
        }

        val user = userRepository.findByUsername(username)

        assertNotNull(user)
        assertEquals(username, user?.username)
    }

    @Test
    fun `should fail to create duplicate user`() {
        val username = "test-user-repository-test3"

        val firstResult = userRepository.createUser(username)
        val secondResult = userRepository.createUser(username)

        assertTrue(firstResult.isSuccess)
        assertTrue(secondResult.isFailure)

        val exception = secondResult.exceptionOrNull()
        assertNotNull(exception)
    }

    @Test
    fun `should return null if user not found`() {
        val user = userRepository.findByUsername("testuser-repostory-doesnotexist")
        assertNull(user)
    }
}