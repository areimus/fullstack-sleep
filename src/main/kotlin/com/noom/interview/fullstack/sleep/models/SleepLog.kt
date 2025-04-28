package com.noom.interview.fullstack.sleep.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.time

/**
 * Object presentation of the table "sleep_logs" available in the persistent storage.
 */
object SleepLog : Table("sleep_logs") {
    /**
     * Unique identifier for the sleep log entry
     */
    val id = long("id").autoIncrement()

    /**
     * User identifier, from the "Users" object
     */
    val userId = long("user_id").references(User.id)

    /**
     * The Date the sleep log contains data for.  This is not the creation date of the persistent
     * storage, rather the Date the sleep log data pertains to.
     */
    val entryDate = date("entry_date")

    /**
     * The HH:MM representation of a bedtime
     */
    val bedTime = time("bed_time")

    /**
     * The HH::MM representation of a wake up time
     */
    val wakeTime = time("wake_time")

    /**
     * The total time spent in bed, in seconds
     */
    val totalTimeInBed = integer("total_time_in_bed")

    /**
     * One of the valid MorningFeeling enum values associated with the sleep log
     */
    val morningFeeling = varchar("morning_feeling", 4)

    /**
     * The Date the record was created in the persistent storage
     */
    val createdAt = datetime("created_at")

    /**
     * The Date the record was modified in the persistent storage
     */
    val updatedAt = datetime("updated_at")
    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(userId, entryDate)
    }
}