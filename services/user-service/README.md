# User Authentication Service - Comprehensive Project Documentation

## Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture Overview](#architecture-overview)
3. [Technologies Used](#technologies-used)
4. [Project Structure](#project-structure)
5. [Database Schema](#database-schema)
6. [Design Patterns Implemented](#design-patterns-implemented)
7. [MVC Pattern Implementation](#mvc-pattern-implementation)
8. [Module Connections](#module-connections)
9. [API Endpoints](#api-endpoints)
10. [Configuration](#configuration)
11. [How to Run](#how-to-run)
12. [Testing](#testing)
13. [Future Enhancements](#future-enhancements)

## Project Overview

The User Authentication Service is a Spring Boot-based microservice that provides comprehensive user authentication and authorization capabilities. It implements a secure, scalable authentication system with JWT token management, password encryption, and integration with external services like Kafka for event-driven communication. The service follows microservice architecture principles and integrates with Eureka for service discovery.

### Key Features

* **Secure Authentication**: JWT-based token authentication with configurable expiration
* **Password Security**: BCrypt password hashing for secure password storage
* **User Management**: Complete user lifecycle management (signup, login, logout, validation)
* **Event-Driven Architecture**: Kafka integration for asynchronous communication
* **Microservice Architecture**: Eureka client integration for service discovery
* **RESTful API**: Clean, stateless API design with proper error handling
* **JPA Integration**: Full CRUD operations with H2 database (configurable for MySQL)
* **Security Framework**: Spring Security integration with customizable security policies

## Architecture Overview

The project follows a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│                    (Controllers)                           │
├─────────────────────────────────────────────────────────────┤
│                    Business Logic Layer                     │
│                    (Services)                              │
├─────────────────────────────────────────────────────────────┤
│                    Data Access Layer                       │
│                    (Repositories)                          │
├─────────────────────────────────────────────────────────────┤
│                    Data Storage Layer                       │
│                    (Database + External Services)           │
└─────────────────────────────────────────────────────────────┘
```

### System Architecture Diagram

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Client App    │───▶│  Auth Controller │───▶│  Auth Service   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │                        │
                                ▼                        ▼
                       ┌──────────────────┐    ┌─────────────────┐
                       │   User Repo      │    │ Kafka Producer  │
                       └──────────────────┘    └─────────────────┘
                                │                        │
                                ▼                        ▼
                       ┌──────────────────┐    ┌─────────────────┐
                       │   H2 Database    │    │   Kafka Topic   │
                       └──────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌──────────────────┐
                       │   Eureka Server  │
                       └──────────────────┘
```

## Technologies Used

### Core Technologies
* **Java 17**: Modern Java features and performance improvements
* **Spring Boot 3.5.3**: Rapid application development framework
* **Spring Security 3.5.3**: Comprehensive security framework
* **Spring Data JPA**: Data access abstraction layer
* **Spring Kafka 3.3.4**: Apache Kafka integration
* **Spring Cloud Netflix Eureka Client 4.2.1**: Service discovery

### Database & Storage
* **H2 Database**: In-memory database for development (configurable for MySQL)
* **Hibernate**: JPA implementation for ORM
* **JPA**: Java Persistence API for database operations

### Security & Authentication
* **JWT (JSON Web Tokens)**: Stateless authentication mechanism
* **BCrypt**: Secure password hashing algorithm
* **Spring Security**: Authentication and authorization framework

### Development Tools
* **Maven**: Build automation and dependency management
* **Lombok**: Reduces boilerplate code
* **Spring Boot DevTools**: Development-time enhancements

### External Integrations
* **Apache Kafka**: Event streaming platform for asynchronous communication
* **Eureka Server**: Service discovery and registration

## Project Structure

```
UserAuthenticationService/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/userauthenticationservice/
│   │   │       ├── clients/           # External service clients
│   │   │       │   └── KafkaProducerClient.java
│   │   │       ├── config/            # Configuration classes
│   │   │       │   ├── OAuthConfig.java
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── controllers/       # REST API controllers
│   │   │       │   └── AuthController.java
│   │   │       ├── dtos/              # Data Transfer Objects
│   │   │       │   ├── EmailDto.java
│   │   │       │   ├── LoginRequestDto.java
│   │   │       │   ├── LogoutRequestDto.java
│   │   │       │   ├── SignupRequestDto.java
│   │   │       │   ├── UserDto.java
│   │   │       │   └── ValidateTokenDto.java
│   │   │       ├── exceptions/        # Custom exception classes
│   │   │       │   ├── IncorrectPassword.java
│   │   │       │   ├── UserAlreadyExists.java
│   │   │       │   └── UserNotFound.java
│   │   │       ├── models/            # Entity models
│   │   │       │   ├── BaseModel.java
│   │   │       │   ├── State.java
│   │   │       │   └── User.java
│   │   │       ├── repos/             # Data access layer
│   │   │       │   └── UserRepo.java
│   │   │       ├── security/          # Security-related classes
│   │   │       │   ├── CustomUserDetails.java
│   │   │       │   └── CustomUserDetailsService.java
│   │   │       ├── services/          # Business logic layer
│   │   │       │   ├── AuthService.java
│   │   │       │   └── IAuthService.java
│   │   │       └── UserAuthenticationServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                          # Test classes
└── pom.xml                            # Maven configuration
```

## Database Schema

### Entity Relationships

```
┌─────────────────────────────────────────────────────────────┐
│                        BaseModel                           │
├─────────────────────────────────────────────────────────────┤
│ + id: Long (Primary Key)                                  │
│ + createdAt: Date                                         │
│ + lastUpdatedAt: Date                                     │
│ + state: State (ACTIVE/INACTIVE)                          │
└─────────────────────────────────────────────────────────────┘
                                ▲
                                │ (inherits)
                                │
┌─────────────────────────────────────────────────────────────┐
│                           User                             │
├─────────────────────────────────────────────────────────────┤
│ + emailId: String (Unique)                                │
│ + password: String (Encrypted)                            │
└─────────────────────────────────────────────────────────────┘
```

### Database Schema Details

#### BaseModel (Abstract Class)
- **id**: Auto-generated primary key using IDENTITY strategy
- **createdAt**: Timestamp when the entity was created
- **lastUpdatedAt**: Timestamp when the entity was last modified
- **state**: Enum value indicating if the entity is ACTIVE or INACTIVE

#### User Entity
- **emailId**: Unique email address for user identification
- **password**: BCrypt-encrypted password for security
- **Inherits**: All fields from BaseModel (id, timestamps, state)

#### State Enum
- **ACTIVE**: User account is active and can be used
- **INACTIVE**: User account is deactivated

### Database Configuration
- **Database**: H2 in-memory database (configurable for MySQL)
- **JPA Strategy**: Update mode for automatic schema generation
- **Connection**: JDBC with H2 driver
- **Console**: H2 console available at `/h2-console`

## Design Patterns Implemented

### 1. **Repository Pattern**
- **Purpose**: Abstracts data access logic from business logic
- **Implementation**: `UserRepo` interface with Spring Data JPA
- **Benefits**: 
  - Decouples business logic from data access
  - Easier testing with mock repositories
  - Consistent data access interface

### 2. **Strategy Pattern**
- **Purpose**: Allows interchangeable algorithms at runtime
- **Implementation**: Service interfaces (`IAuthService`) with multiple implementations
- **Benefits**: 
  - Easy to switch between different authentication strategies
  - Extensible for new authentication methods
  - Loose coupling between strategy and context

### 3. **DTO Pattern (Data Transfer Object)**
- **Purpose**: Separates API contracts from internal models
- **Implementation**: Separate DTOs for request/response (`LoginRequestDto`, `UserDto`)
- **Benefits**: 
  - API versioning and backward compatibility
  - Input validation and sanitization
  - Hides internal implementation details

### 4. **Factory Pattern**
- **Purpose**: Creates objects without specifying exact classes
- **Implementation**: Spring's dependency injection container
- **Benefits**: 
  - Automatic object creation and lifecycle management
  - Configuration-based object instantiation
  - Easy testing with mock objects

### 5. **Template Method Pattern**
- **Purpose**: Defines algorithm skeleton with customizable steps
- **Implementation**: Spring Security's `SecurityFilterChain`
- **Benefits**: 
  - Consistent security configuration
  - Customizable security policies
  - Reusable security patterns

### 6. **Observer Pattern**
- **Purpose**: Notifies multiple objects when state changes
- **Implementation**: Kafka event publishing for user actions
- **Benefits**: 
  - Loose coupling between services
  - Asynchronous processing
  - Scalable event handling

## MVC Pattern Implementation

### Model Layer
- **Entities**: `User`, `BaseModel`, `State`
- **Repositories**: `UserRepo` for data access
- **Services**: `AuthService` for business logic

### View Layer
- **REST Controllers**: `AuthController` handles HTTP requests
- **Response Entities**: Structured JSON responses
- **Error Handling**: HTTP status codes and error messages

### Controller Layer
- **Request Mapping**: RESTful endpoint definitions
- **Input Validation**: DTO validation and processing
- **Response Generation**: HTTP response creation with appropriate status codes

### MVC Flow Example
```
1. Client sends POST /auth/login
2. AuthController receives request
3. Controller validates LoginRequestDto
4. Controller calls AuthService.login()
5. Service performs business logic (user lookup, password verification)
6. Service generates JWT token
7. Controller creates ResponseEntity with token in headers
8. Client receives response with user data and authentication token
```

## Module Connections

### 1. **Request Flow Architecture**

```
Client Request → AuthController → AuthService → UserRepo → H2 Database
                                    ↓
                              KafkaProducerClient → Kafka Topic
                                    ↓
                              Eureka Client → Eureka Server
```

### 2. **Component Dependencies**

```
AuthController
    ↓ (depends on)
IAuthService (interface)
    ↓ (implemented by)
AuthService
    ↓ (depends on)
UserRepo, BCryptPasswordEncoder, SecretKey, KafkaProducerClient
    ↓ (depends on)
H2 Database, Kafka Broker, Eureka Server
```

### 3. **Authentication Flow**

```
Login Request → Controller → Service → Repository → Database
                                    ↓
                              Password Verification (BCrypt)
                                    ↓
                              JWT Token Generation
                                    ↓
                              Response with Token + User Data
```

### 4. **Event Publishing Flow**

```
User Action → Service → KafkaProducerClient → Kafka Topic
                                    ↓
                              Other Services (Email Service, etc.)
```

### 5. **Service Discovery Flow**

```
Application Startup → Eureka Client → Eureka Server
                                    ↓
                              Service Registration
                                    ↓
                              Health Checks and Monitoring
```

## API Endpoints

### 1. **Authentication Endpoints**

#### User Signup
- **Endpoint**: `POST /auth/signup`
- **Request Body**: `SignupRequestDto`
- **Response**: `UserDto` with user information
- **Features**: 
  - Email uniqueness validation
  - Password encryption
  - Welcome email notification via Kafka
  - User state management

#### User Login
- **Endpoint**: `POST /auth/login`
- **Request Body**: `LoginRequestDto`
- **Response**: `UserDto` with JWT token in Set-Cookie header
- **Features**: 
  - User authentication
  - Password verification
  - JWT token generation
  - HTTP status codes for different scenarios

#### User Logout
- **Endpoint**: `POST /auth/logout`
- **Request Body**: `LogoutRequestDto`
- **Response**: Boolean indicating success
- **Features**: 
  - Token invalidation (placeholder implementation)
  - Session cleanup

#### Token Validation
- **Endpoint**: `POST /auth/validateToken`
- **Request Body**: `ValidateTokenDto`
- **Response**: Boolean indicating token validity
- **Features**: 
  - JWT token verification
  - Expiration checking
  - User ID validation

### 2. **Request/Response DTOs**

#### SignupRequestDto
```json
{
    "emailId": "user@example.com",
    "password": "securePassword123"
}
```

#### LoginRequestDto
```json
{
    "emailId": "user@example.com",
    "password": "securePassword123"
}
```

#### UserDto
```json
{
    "id": 1,
    "emailId": "user@example.com"
}
```

#### ValidateTokenDto
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 1
}
```

### 3. **HTTP Status Codes**

- **200 OK**: Successful operation
- **201 Created**: User successfully created/logged in
- **400 Bad Request**: Invalid input data
- **401 Unauthorized**: Incorrect password
- **404 Not Found**: User not found
- **500 Internal Server Error**: Server-side errors

## Configuration

### Application Properties

```properties
# Application Configuration
spring.application.name=UserAuthenticationService
server.port=9000

# Database Configuration (H2)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:userdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# Security Configuration
logging.level.org.springframework.security=trace

# Service Discovery
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

### Configuration Classes

#### SecurityConfig
- **Purpose**: Configures Spring Security and authentication
- **Features**: 
  - CSRF protection disabled
  - BCrypt password encoder
  - JWT secret key generation
  - Security filter chain configuration

#### OAuthConfig
- **Purpose**: OAuth2 authorization server configuration
- **Features**: 
  - OAuth2 server setup
  - Authorization endpoints
  - Token management

### Environment Support

The application supports multiple deployment scenarios:

- **Local Development**: H2 in-memory database, local Kafka, local Eureka
- **Production**: MySQL database, Kafka cluster, Eureka cluster
- **Cloud Deployment**: Cloud-native database, managed Kafka, service mesh

## How to Run

### Prerequisites

- **Java 17** or higher
- **Maven 3.6** or higher
- **Apache Kafka** 2.8 or higher
- **Eureka Server** (for service discovery)
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code)

### Setup Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd UserAuthenticationService
   ```

2. **Start Kafka Server**
   ```bash
   # Start Zookeeper
   bin/zookeeper-server-start.sh config/zookeeper.properties
   
   # Start Kafka
   bin/kafka-server-start.sh config/server.properties
   ```

3. **Start Eureka Server** (optional, for microservice discovery)
   - Run Eureka server on port 8761
   - Or use standalone mode without Eureka

4. **Run the application**
   ```bash
   # Using Maven wrapper
   ./mvnw spring-boot:run
   
   # Or using Maven directly
   mvn spring-boot:run
   ```

5. **Access the application**
   - Application runs on `http://localhost:9000`
   - H2 Console: `http://localhost:9000/h2-console`
   - Auth endpoints: `http://localhost:9000/auth/*`

### Testing the API

#### Test user signup
```bash
curl -X POST http://localhost:9000/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "emailId": "test@example.com",
    "password": "password123"
  }'
```

#### Test user login
```bash
curl -X POST http://localhost:9000/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "emailId": "test@example.com",
    "password": "password123"
  }'
```

#### Test token validation
```bash
curl -X POST http://localhost:9000/auth/validateToken \
  -H "Content-Type: application/json" \
  -d '{
    "token": "your_jwt_token_here",
    "userId": 1
  }'
```

## Testing

### Test Structure

- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions
- **API Tests**: Test REST endpoints
- **Security Tests**: Test authentication and authorization

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=AuthControllerTest

# Run with coverage
./mvnw test jacoco:report
```

### Test Coverage Areas

- Authentication service business logic
- Controller request handling and validation
- Repository data access methods
- Security configuration and JWT handling
- Exception handling and error responses
- Kafka integration testing

## Future Enhancements

### 1. **Security Improvements**

- **JWT Token Management**: Implement token refresh mechanism
- **Role-Based Access Control**: Add user roles and permissions
- **API Rate Limiting**: Implement request throttling
- **Input Validation**: Add comprehensive input sanitization
- **Audit Logging**: Track user actions and security events

### 2. **Database Enhancements**

- **Database Migration**: Implement Flyway for schema management
- **Connection Pooling**: Configure HikariCP for performance
- **Data Encryption**: Encrypt sensitive data at rest
- **Backup Strategy**: Implement automated backup procedures
- **Performance Optimization**: Add database indexing and query optimization

### 3. **Performance Enhancements**

- **Caching Layer**: Implement Redis for session caching
- **Async Processing**: Add async operations for non-critical tasks
- **Load Balancing**: Implement horizontal scaling
- **Health Checks**: Add comprehensive health monitoring
- **Metrics Collection**: Implement Micrometer for observability

### 4. **Additional Features**

- **Password Reset**: Implement secure password recovery
- **Multi-Factor Authentication**: Add 2FA support
- **Social Login**: Integrate with OAuth providers
- **User Profile Management**: Extended user information
- **Session Management**: Track and manage active sessions

### 5. **Monitoring and Observability**

- **Logging Framework**: Implement structured logging with SLF4J
- **Distributed Tracing**: Add tracing for microservice communication
- **Application Performance Monitoring**: Monitor service performance
- **Alerting**: Set up automated alerts for issues
- **Dashboard**: Create monitoring dashboards

### 6. **Error Handling and Resilience**

- **Circuit Breaker**: Implement fault tolerance patterns
- **Retry Mechanisms**: Add retry logic for external service calls
- **Fallback Strategies**: Implement graceful degradation
- **Error Tracking**: Integrate with error tracking services
- **User-Friendly Error Messages**: Improve error communication

## Conclusion

This User Authentication Service project demonstrates several important software engineering concepts:

1. **Clean Architecture**: Clear separation of concerns with layered design
2. **Design Patterns**: Strategic use of Repository, Strategy, DTO, and Observer patterns
3. **MVC Implementation**: Proper separation of presentation, business logic, and data layers
4. **Microservice Architecture**: Service discovery integration and event-driven communication
5. **Security Best Practices**: JWT authentication, password encryption, and Spring Security
6. **Modern Java**: Uses Java 17 and Spring Boot 3.5.3 with best practices
7. **Extensibility**: Easy to add new authentication methods, security features, and integrations

The project serves as an excellent foundation for understanding:

- Microservice architecture and design
- Spring Boot development with security and JPA
- Design patterns in practice
- API design and RESTful services
- Database design and security
- Event-driven architecture with Kafka
- Service discovery and microservice communication

This implementation can be extended with additional features like advanced security, monitoring, and integration capabilities to create a production-ready authentication service.

---

**Note**: This project is designed for educational purposes and demonstrates architectural concepts. For production use, additional security, error handling, monitoring, and testing features should be implemented.
