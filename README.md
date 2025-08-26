# Payment Service - Comprehensive Project Documentation

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

The Payment Service is a Spring Boot-based microservice that provides a unified interface for processing payments through multiple payment gateways (Razorpay and Stripe). It implements a strategy pattern to dynamically select the best payment gateway and provides RESTful APIs for payment processing and webhook handling.

### Key Features
- **Multi-Gateway Support**: Integrates with Razorpay and Stripe payment gateways
- **Strategy Pattern**: Dynamic selection of payment gateways
- **Webhook Support**: Handles payment events from Stripe
- **RESTful API**: Clean, stateless API design
- **Configurable**: Environment-based configuration for API keys

## Architecture Overview

The project follows a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│  ┌─────────────────┐  ┌─────────────────────────────────┐  │
│  │ PaymentController│  │     StripeWebhookController    │  │
│  └─────────────────┘  └─────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                     Service Layer                          │
│  ┌─────────────────────────────────────────────────────┐  │
│  │                PaymentService                        │  │
│  └─────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                   Gateway Layer                            │
│  ┌─────────────────┐  ┌─────────────────────────────────┐  │
│  │ RazorpayGateway │  │        StripeGateway            │  │
│  └─────────────────┘  └─────────────────────────────────┘  │
│  ┌─────────────────────────────────────────────────────┐  │
│  │           PaymentGatewaySelectorStrategy            │  │
│  └─────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                   Configuration Layer                       │
│  ┌─────────────────────────────────────────────────────┐  │
│  │                RazorpayConfig                       │  │
│  └─────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Technologies Used

### Core Technologies
- **Java 17**: Modern Java with enhanced features
- **Spring Boot 3.5.5**: Latest stable version with Spring Framework 6
- **Maven**: Dependency management and build tool

### Spring Framework Components
- **Spring Web**: RESTful web services
- **Spring Boot DevTools**: Development utilities
- **Spring Boot Test**: Testing framework

### Payment Gateway SDKs
- **Razorpay Java SDK (1.4.8)**: Integration with Razorpay payment gateway
- **Stripe Java SDK (29.0.0)**: Integration with Stripe payment gateway

### Development Tools
- **Lombok**: Reduces boilerplate code
- **Spring Boot DevTools**: Hot reload and development utilities

## Project Structure

```
PaymentService/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/paymentservice/
│   │   │       ├── config/                    # Configuration classes
│   │   │       │   └── RazorpayConfig.java    # Razorpay client configuration
│   │   │       ├── controllers/               # REST controllers (MVC View)
│   │   │       │   ├── PaymentController.java # Payment processing endpoints
│   │   │       │   └── StripeWebhookController.java # Webhook handling
│   │   │       ├── dtos/                      # Data Transfer Objects
│   │   │       │   └── PaymentRequestDto.java # Payment request structure
│   │   │       ├── paymentGateways/           # Payment gateway implementations
│   │   │       │   ├── IPaymentGateway.java   # Gateway interface
│   │   │       │   ├── RazorpayPaymentGateway.java # Razorpay implementation
│   │   │       │   ├── StripePaymentGateway.java   # Stripe implementation
│   │   │       │   └── PaymentGatewaySelectorStrategy.java # Strategy selector
│   │   │       ├── services/                  # Business logic layer (MVC Model)
│   │   │       │   └── PaymentService.java    # Core payment service
│   │   │       └── PaymentServiceApplication.java # Main application class
│   │   └── resources/
│   │       ├── application.properties         # Configuration properties
│   │       ├── static/                        # Static resources
│   │       └── templates/                     # Template files
│   └── test/                                  # Test classes
├── pom.xml                                     # Maven configuration
├── mvnw                                        # Maven wrapper script
└── mvnw.cmd                                   # Maven wrapper script (Windows)
```

## Database Schema

**Note**: This project currently does not implement a database layer. It's designed as a stateless service that processes payments and returns payment links. However, here's what a typical database schema would look like for a production payment service:

### Conceptual Database Schema

```sql
-- Users table
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Orders table
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id VARCHAR(255) UNIQUE NOT NULL,
    user_id BIGINT,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    status ENUM('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Payments table
CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT,
    payment_gateway ENUM('RAZORPAY', 'STRIPE') NOT NULL,
    gateway_transaction_id VARCHAR(255),
    amount DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING', 'SUCCESS', 'FAILED') DEFAULT 'PENDING',
    payment_link TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- Payment webhooks table
CREATE TABLE payment_webhooks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    payment_id BIGINT,
    webhook_data JSON,
    processed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payment_id) REFERENCES payments(id)
);
```

### Entity Relationship Diagram

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│    Users    │    │    Orders   │    │   Payments  │
├─────────────┤    ├─────────────┤    ├─────────────┤
│ id (PK)     │    │ id (PK)     │    │ id (PK)     │
│ name        │    │ order_id    │    │ order_id    │
│ email       │    │ user_id (FK)│    │ gateway     │
│ phone       │    │ amount      │    │ amount      │
│ created_at  │    │ currency    │    │ status      │
│ updated_at  │    │ status      │    │ payment_link│
└─────────────┘    │ created_at  │    │ created_at  │
       │           └─────────────┘    │ updated_at  │
       │                    │         └─────────────┘
       │                    │                 │
       └────────────────────┼─────────────────┘
                            │
                   ┌────────▼────────┐
                   │ PaymentWebhooks │
                   ├─────────────────┤
                   │ id (PK)         │
                   │ payment_id (FK) │
                   │ webhook_data    │
                   │ processed       │
                   │ created_at      │
                   └─────────────────┘
```

## Design Patterns Implemented

### 1. Strategy Pattern
**Purpose**: Allows the system to dynamically select payment gateways at runtime.

**Implementation**: 
- `IPaymentGateway` interface defines the contract
- `RazorpayPaymentGateway` and `StripePaymentGateway` implement the strategy
- `PaymentGatewaySelectorStrategy` selects the appropriate strategy

```java
// Strategy Interface
public interface IPaymentGateway {
    String getPaymentLink(Long amount, String orderId, String phoneNumber, String name, String email);
}

// Concrete Strategies
@Component
public class RazorpayPaymentGateway implements IPaymentGateway { ... }

@Component
public class StripePaymentGateway implements IPaymentGateway { ... }

// Strategy Selector
@Component
public class PaymentGatewaySelectorStrategy {
    public IPaymentGateway getBestPaymentGateway() {
        // Currently returns Stripe, but can be made dynamic
        return stripePaymentGateway;
    }
}
```

**Benefits**:
- Easy to add new payment gateways
- Runtime gateway selection
- Loose coupling between gateway implementations

### 2. Dependency Injection Pattern
**Purpose**: Spring Framework manages object creation and dependencies.

**Implementation**: Uses `@Autowired` annotations for automatic dependency injection.

```java
@Service
public class PaymentService {
    @Autowired
    private PaymentGatewaySelectorStrategy paymentGatewaySelectorStrategy;
    // Spring automatically injects the dependency
}
```

**Benefits**:
- Loose coupling between components
- Easier testing through mock injection
- Automatic lifecycle management

### 3. Factory Pattern (Conceptual)
**Purpose**: The `PaymentGatewaySelectorStrategy` acts as a factory for creating payment gateway instances.

**Implementation**: 
```java
@Component
public class PaymentGatewaySelectorStrategy {
    @Autowired
    private RazorpayPaymentGateway razorpayPaymentGateway;
    @Autowired
    private StripePaymentGateway stripePaymentGateway;

    public IPaymentGateway getBestPaymentGateway() {
        // Factory logic to select appropriate gateway
        return stripePaymentGateway;
    }
}
```

### 4. Template Method Pattern
**Purpose**: The `IPaymentGateway` interface defines a template for payment processing.

**Implementation**: All payment gateways must implement the same method signature, ensuring consistency.

## MVC Pattern Implementation

The project implements the **Model-View-Controller (MVC)** pattern as follows:

### Controller Layer (View)
**Files**: `PaymentController.java`, `StripeWebhookController.java`

**Responsibilities**:
- Handle HTTP requests
- Validate input data
- Call appropriate service methods
- Return HTTP responses

```java
@RestController
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment")
    public String initiatePayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        return paymentService.getPaymentLink(
            paymentRequestDto.getAmount(), 
            paymentRequestDto.getOrderId(), 
            paymentRequestDto.getPhoneNumber(), 
            paymentRequestDto.getName(), 
            paymentRequestDto.getEmail()
        );
    }
}
```

### Service Layer (Model)
**Files**: `PaymentService.java`

**Responsibilities**:
- Implement business logic
- Coordinate between different components
- Handle data transformation
- Manage transaction boundaries

```java
@Service
public class PaymentService {
    @Autowired
    private PaymentGatewaySelectorStrategy paymentGatewaySelectorStrategy;

    public String getPaymentLink(Long amount, String orderId, String phoneNumber, String name, String email) {
        IPaymentGateway paymentGateway = paymentGatewaySelectorStrategy.getBestPaymentGateway();
        return paymentGateway.getPaymentLink(amount, orderId, phoneNumber, name, email);
    }
}
```

### Data Transfer Objects (DTOs)
**Files**: `PaymentRequestDto.java`

**Responsibilities**:
- Define data structure for API requests/responses
- Encapsulate data transfer between layers
- Provide data validation

```java
@Getter
@Setter
public class PaymentRequestDto {
    Long amount;
    String orderId;
    String phoneNumber;
    String name;
    String email;
}
```

## Module Connections

### 1. Request Flow
```
Client Request → PaymentController → PaymentService → PaymentGatewaySelectorStrategy → Concrete Gateway → Payment Gateway API
```

### 2. Component Dependencies
```
PaymentController
    ↓ (depends on)
PaymentService
    ↓ (depends on)
PaymentGatewaySelectorStrategy
    ↓ (depends on)
IPaymentGateway implementations (Razorpay/Stripe)
    ↓ (depends on)
External Payment Gateway APIs
```

### 3. Configuration Dependencies
```
RazorpayConfig → RazorpayClient → RazorpayPaymentGateway
application.properties → StripePaymentGateway
```

### 4. Webhook Flow
```
Stripe Webhook → StripeWebhookController → Business Logic (to be implemented)
```

## API Endpoints

### 1. Payment Processing
**Endpoint**: `POST /payment`

**Request Body**:
```json
{
    "amount": 1000,
    "orderId": "ORD123456",
    "phoneNumber": "+1234567890",
    "name": "John Doe",
    "email": "john.doe@example.com"
}
```

**Response**: Payment link URL (String)

**Flow**:
1. Controller receives payment request
2. Service processes the request
3. Strategy selects payment gateway
4. Gateway generates payment link
5. Link returned to client

### 2. Stripe Webhook
**Endpoint**: `POST /stripeWebhook`

**Purpose**: Handle payment events from Stripe (success, failure, etc.)

**Current Implementation**: Logs the event (placeholder for future business logic)

## Configuration

### Application Properties
```properties
spring.application.name=PaymentService
razorpay.key.id=000
razorpay.key.secret=000
stripe.apiKey=000
```

### Configuration Classes
- **RazorpayConfig**: Creates and configures Razorpay client bean
- **Environment Variables**: API keys should be set via environment variables in production

### Security Considerations
- API keys are currently placeholder values
- In production, use environment variables or secure configuration management
- Implement proper authentication and authorization

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Valid Razorpay and Stripe API keys

### Setup Steps
1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd PaymentService
   ```

2. **Configure API keys**
   - Update `src/main/resources/application.properties`
   - Set valid Razorpay and Stripe API keys

3. **Run the application**
   ```bash
   # Using Maven wrapper
   ./mvnw spring-boot:run
   
   # Or using Maven directly
   mvn spring-boot:run
   ```

4. **Access the application**
   - Application runs on `http://localhost:8080`
   - Payment endpoint: `POST http://localhost:8080/payment`
   - Webhook endpoint: `POST http://localhost:8080/stripeWebhook`

### Testing the API
```bash
# Test payment endpoint
curl -X POST http://localhost:8080/payment \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 1000,
    "orderId": "TEST123",
    "phoneNumber": "+1234567890",
    "name": "Test User",
    "email": "test@example.com"
  }'
```

## Testing

### Test Structure
- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions
- **API Tests**: Test REST endpoints

### Running Tests
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=PaymentServiceTest

# Run with coverage
./mvnw test jacoco:report
```

### Test Coverage Areas
- Payment service business logic
- Payment gateway implementations
- Controller request handling
- Configuration validation

## Future Enhancements

### 1. Database Integration
- Implement JPA entities for payment tracking
- Add transaction management
- Implement audit logging

### 2. Security Improvements
- JWT-based authentication
- API rate limiting
- Input validation and sanitization

### 3. Additional Features
- Payment status tracking
- Refund processing
- Multi-currency support
- Payment analytics and reporting

### 4. Monitoring and Observability
- Logging framework (SLF4J + Logback)
- Metrics collection (Micrometer)
- Health checks
- Distributed tracing

### 5. Error Handling
- Global exception handler
- Proper error responses
- Retry mechanisms
- Circuit breaker pattern

## Conclusion

This Payment Service project demonstrates several important software engineering concepts:

1. **Clean Architecture**: Clear separation of concerns with layered design
2. **Design Patterns**: Strategic use of Strategy, Dependency Injection, and Factory patterns
3. **MVC Implementation**: Proper separation of presentation, business logic, and data layers
4. **Extensibility**: Easy to add new payment gateways and features
5. **Modern Java**: Uses Java 17 and Spring Boot 3.5.5 with best practices

The project serves as an excellent foundation for understanding microservice architecture, design patterns, and Spring Boot development. It can be extended with additional features like database integration, security, and monitoring to create a production-ready payment service.

---

**Note**: This project is designed for educational purposes and demonstrates architectural concepts. For production use, additional security, error handling, and monitoring features should be implemented.
