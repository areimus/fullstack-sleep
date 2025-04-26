package com.noom.interview.fullstack.sleep.config

import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.sql.Database
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class DatabaseConfiguration(
    private val dataSource: DataSource
) {

    @PostConstruct
    fun initialize() {
        Database.connect(dataSource)
    }
}