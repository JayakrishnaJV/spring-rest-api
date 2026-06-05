# Spring REST API

A Spring Boot REST API application for managing employees and departments with JWT authentication and Swagger documentation.

## Features

- **Employee Management**: Create, read, update, and delete employee records
- **Department Management**: Manage departments with summaries
- **JWT Authentication**: Secure endpoints with JWT token-based authentication
- **Swagger/OpenAPI Documentation**: Interactive API documentation available at `/swagger-ui.html`
- **Global Exception Handling**: Centralized error handling with custom error responses
- **Advanced Search & Filtering**: Employee search with specification-based queries
- **H2 Database**: In-memory database for development (easily switchable to production DB)
- **Comprehensive Logging**: Configurable logging with file output support

## Project Structure

```
spring-rest-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/RestApi/
│   │   │   ├── config/              # Swagger configuration
│   │   │   ├── controller/          # REST endpoints
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── exception/           # Custom exceptions & global handler
│   │   │   ├── model/               # JPA entities
│   │   │   ├── repository/          # Data access layer
│   │   │   ├── security/            # JWT & Spring Security config
│   │   │   ├── service/             # Business logic
│   │   │   └── specification/       # JPA specifications for filtering
│   │   └── resources/
│   │       └── application.properties  # Configuration
│   └── test/
│       └── java/com/example/RestApi/   # Unit tests
├── .env                  # Environment variables (keep secret, don't commit)
├── .env.template         # Template for .env configuration
└── pom.xml              # Maven dependencies
```

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- Git

## Setup & Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd spring-rest-api
```

### 2. Configure Environment Variables
```bash
# Copy the template .env file
cp .env.template .env

# Edit .env with your settings (if needed)
# Default values work for development
```

### 3. Build the Project
```bash
./mvnw clean package
# or on Windows:
mvnw.cmd clean package
```

### 4. Run the Application
```bash
./mvnw spring-boot:run
# or
java -jar target/spring-rest-api-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## Environment Configuration

Create a `.env` file in the project root (use `.env.template` as a template):

```properties
# Database Configuration
DB_URL=jdbc:h2:mem:user_management
DB_USER=sa
DB_PASSWORD=

# Database Options (for production databases)
# DB_URL=jdbc:mysql://localhost:3306/rest_api
# DB_DRIVER=com.mysql.cj.jdbc.Driver
# DB_USER=root
# DB_PASSWORD=yourpassword

# JPA/Hibernate
JPA_DDL_AUTO=update  # Options: validate, update, create, create-drop

# Logging
LOG_LEVEL_ROOT=INFO
LOG_LEVEL_APP=DEBUG
LOG_FILE_PATH=./logs
```

**Important**: Do NOT commit `.env` file to Git. It's in `.gitignore` to prevent accidental exposure of sensitive data.

## API Endpoints

### Authentication
- `POST /auth/login` - Login with credentials
- `POST /auth/register` - Register new user

### Employees
- `GET /api/employees` - Get all employees (with filtering & pagination)
- `GET /api/employees/{id}` - Get employee by ID
- `POST /api/employees` - Create new employee
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Delete employee

### Departments
- `GET /api/departments` - Get all departments
- `GET /api/departments/{id}` - Get department by ID
- `POST /api/departments` - Create new department
- `PUT /api/departments/{id}` - Update department
- `DELETE /api/departments/{id}` - Delete department

## Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/v3/api-docs
- **H2 Console** (dev only): http://localhost:8080/h2-console

## Running Tests

```bash
./mvnw test
```

Tests included:
- Unit tests for services
- Integration tests for controllers
- Application startup test

## Database

### Development (Default)
- **Database**: H2 (in-memory)
- **URL**: `jdbc:h2:mem:user_management`
- **User**: `sa` (System Administrator)
- **Console**: http://localhost:8080/h2-console

### Production
Update `.env` with your production database credentials:

**MySQL Example**:
```properties
DB_URL=jdbc:mysql://localhost:3306/rest_api
DB_DRIVER=com.mysql.cj.jdbc.Driver
DB_USER=root
DB_PASSWORD=secretpassword
JPA_DIALECT=org.hibernate.dialect.MySQLDialect
```

**PostgreSQL Example**:
```properties
DB_URL=jdbc:postgresql://localhost:5432/rest_api
DB_DRIVER=org.postgresql.Driver
DB_USER=postgres
DB_PASSWORD=secretpassword
JPA_DIALECT=org.hibernate.dialect.PostgreSQLDialect
```

## Key Technologies

- **Spring Boot 2.x** - Application framework
- **Spring Security** - Authentication & authorization
- **JWT (JSON Web Tokens)** - Stateless authentication
- **Spring Data JPA** - Data access layer
- **H2 Database** - Development database
- **Swagger/OpenAPI 3.0** - API documentation
- **Maven** - Build tool
- **JUnit 5** - Testing framework

## Logging

Logs are configured in `application.properties`:

```properties
logging.level.root=INFO                    # Root logging level
logging.level.com.example.RestApi.service=DEBUG  # App package level
logging.file.name=logs/app.log            # Log file path
```

Logs are written to `./logs/app.log` by default.

## Security Considerations

1. **Never commit `.env` file** - Contains sensitive credentials
2. **Keep JWT secret safe** - Store in environment variables
3. **Use HTTPS in production** - Ensure encrypted communication
4. **Validate inputs** - Use DTO validation annotations
5. **Set strong database passwords** - Especially for production

## Troubleshooting

### Application won't start
- Check Java version: `java -version` (must be 11+)
- Verify port 8080 is available
- Check logs: `tail -f logs/app.log`

### Database connection errors
- Verify `.env` file exists and is configured correctly
- Check database is running (for non-H2 databases)
- Review `application.properties` placeholders

### JWT authentication failures
- Ensure `JwtFilter` is configured in `SecurityConfig`
- Check token format: `Bearer <token>`
- Verify token hasn't expired

## Contributing

1. Create a feature branch
2. Make your changes
3. Write/update tests
4. Commit with clear messages
5. Push and create a Pull Request

## License

This project is licensed under the MIT License - see LICENSE file for details.

## Contact

For questions or support, please open an issue in the repository.
