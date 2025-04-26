package com.noom.interview.fullstack.sleep.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object Users : Table("users") {
    val id = long("id").autoIncrement()
    val username = varchar("username", 255).uniqueIndex()
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}