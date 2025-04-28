package com.noom.interview.fullstack.sleep.repository

import com.noom.interview.fullstack.sleep.models.User
import com.noom.interview.fullstack.sleep.data.UserDTO
import com.noom.interview.fullstack.sleep.mapper.toUserDTO

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import java.time.LocalDateTime

/**
 * A repository containing the functionality for creating and retrieving users
 * from a persistent storage.
 */
class UserRepository {
    /**
     * Create a new user in the persistent storage and return a UserDTO
     *
     * @param username String
     * @throws Exception On attempt to create a duplicate user
     * @return UserDTO
     */
    fun createUser(username: String): Result<UserDTO> {
        return runCatching {
            transaction {
                val now = LocalDateTime.now()
                val insertedId = User.insert {
                    it[User.username] = username
                    it[createdAt] = now
                } get User.id

                toUserDTO(insertedId, username, now)
            }
        }
    }

    /**
     * Return a UserDTO representing an existing user.
     *
     * @return UserDTO or null if the user record does not exist
     */
    fun findByUsername(username: String): UserDTO? =  transaction {
        User.selectAll().where { User.username eq username }
            .singleOrNull()
            ?.toUserDTO()
    }
}