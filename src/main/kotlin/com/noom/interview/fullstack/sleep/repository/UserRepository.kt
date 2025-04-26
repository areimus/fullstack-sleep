package com.noom.interview.fullstack.sleep.repository

import com.noom.interview.fullstack.sleep.models.Users
import com.noom.interview.fullstack.sleep.data.UserDTO
import com.noom.interview.fullstack.sleep.mapper.toUserDTO

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import java.time.LocalDateTime

class UserRepository {
    fun createUser(username: String): Result<UserDTO> {
        return runCatching {
            transaction {
                val now = LocalDateTime.now()
                val insertedId = Users.insert {
                    it[Users.username] = username
                    it[createdAt] = now
                } get Users.id

                toUserDTO(insertedId, username, now)
            }
        }
    }

    fun findByUsername(username: String): UserDTO? =  transaction {
        Users.selectAll().where { Users.username eq username }
            .singleOrNull()
            ?.toUserDTO()
    }
}