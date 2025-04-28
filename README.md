# **Sleep Logger API**



A backend REST API for recording, retrieving, and analyzing sleep logs.

Designed to manage users, track nightly sleep sessions, and calculate sleep quality metrics like time in bed and morning feelings.

----------


## **Tech Stack**

-   **Language**: [Kotlin 1.9.22](https://kotlinlang.org/)
-   **Framework**: [Spring Boot 3.4.5](https://spring.io/projects/spring-boot)
-   **ORM**: [Exposed 0.61.0](https://github.com/JetBrains/Exposed)
-   **Database**: [PostgreSQL](https://www.postgresql.org/)
-   **Database Migrations**: [Flyway 9.22.3](https://flywaydb.org/)

----------

## **Pre-Requisites**
- Docker

## **Quick Start**
``
git clone https://github.com/areimus/fullstack-sleep.git
``

``
cd fullstack-sleep
``

``
docker-compose up --build
``

## **Features**

- Create and retrieve **Users**.
- Record nightly **Sleep Logs**.
  - Retrieve single or multiple **Sleep Log entries**.

- Generate **N-day Sleep Reports** with:
  - Average total time in bed.
  - Average bedtimes and wake times.
  - Frequency distribution of morning feelings (“GOOD”, “OK”, “BAD”).

-   Built-in **global exception handling** for consistent API error responses.
-   Fully covered with **unit and controller tests**.


----------

## **API Testing with Postman**
You can import the API Collection into Postman:

- [Download Collection](./postman/sleep-api.postman_collection.json)
  Or manually by importing the file into Postman (**File → Import → Upload Files**).
- An environment called 'Local Server Testing' is provided which configures Postman to point at the locally running API server.


> The collection includes pre-configured examples for creating users, logging sleep data, retrieving single day and N-Day reports

----------

## **Running Tests**
```
./gradlew test 
./gradlew jacocoTestReport
```

**HTML coverage reports will be generated at:**
>build/reports/jacoco/test/html/index.html