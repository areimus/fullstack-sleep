package com.noom.interview.fullstack.sleep.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * Object presentation of the table "users" available in the persistent storage.
 */
object User : Table("users") {
    /**
     * Unique identifier for the user
     */
    val id = long("id").autoIncrement()

    /**
     * Unique, human-friendly, identifier for the user
     */
    val username = varchar("username", 255).uniqueIndex()

    /**
     * The Date the user record was created
     */
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}