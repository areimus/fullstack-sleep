-- Users table
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Sleep logs table
CREATE TABLE sleep_logs (
                            id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT NOT NULL REFERENCES users(id),
                            entry_date DATE NOT NULL,
                            bed_time TIME NOT NULL,
                            wake_time TIME NOT NULL,
                            total_time_in_bed INT NOT NULL,
                            morning_feeling VARCHAR(4) NOT NULL CHECK (morning_feeling IN ('BAD', 'OK', 'GOOD')),
                            created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                            UNIQUE (user_id, entry_date)
);

-- Index for querying sleep logs by date range
CREATE INDEX idx_sleep_logs_user_date ON sleep_logs(user_id, entry_date);