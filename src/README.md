

# Sleep Logger API

This project provides the backend API for a sleep logging application. It is designed to record, retrieve, and manage sleep data, including sleep sessions, time in bed, and sleep quality metrics.

## Tech Stack
- Kotlin (1.9.22)  
  -- with Spring Boot  (3.4.5)
- Exposed (0.61.0)
- PostgreSQL
- Flyway (9.22.3)

## Testing

#### Users

#### Create user
``  
curl -X POST "http://localhost:8080/users/create?username=testuser1" -H "Content-Type: application/json"  
``

#### Get user
``  
curl -X GET "http://localhost:8080/users/findByUsername?username=testuser2" -H "Content-Type: application/json"  
``

### Sleep Logs

#### Create Log
``  
curl -X POST "http://localhost:8080/users/1/logs" \  
  -H "Content-Type: application/json" \  
  -d "entryDate=2025-04-24&bedTime=22:30:00&wakeTime=06:30:00&morningFeeling=GOOD"  
``

#### Get Log By Date
``  
curl -X GET "http://localhost:8080/users/1/logs/2025-04-24" -H "Content-Type: application/json"  
``

#### Get Logs By Date Range
``  
curl -X GET "http://localhost:8080/users/1/logs?startDate=2025-04-01&endDate=2025-04-30" -H "Content-Type: application/json"  
``

