package com.noom.interview.fullstack.sleep.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.time

object SleepLogs : Table("sleep_logs") {
    val id = long("id").autoIncrement()
    val userId = long("user_id").references(Users.id)
    val entryDate = date("entry_date")
    val bedTime = time("bed_time")
    val wakeTime = time("wake_time")
    val totalTimeInBed = integer("total_time_in_bed")
    val morningFeeling = varchar("morning_feeling", 4)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(userId, entryDate)
    }
}