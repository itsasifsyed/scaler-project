# Product Catalog Service - Comprehensive Project Documentation

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

The Product Catalog Service is a Spring Boot-based microservice that provides a comprehensive product catalog management system. It implements a dual-service architecture where products can be fetched from either a local database or an external FakeStore API, demonstrating the Strategy Pattern and Service Locator Pattern. The service includes advanced features like Redis caching, search functionality with pagination and sorting, and microservice discovery integration.

### Key Features

* **Dual Data Source Support**: Integrates with both local MySQL database and external FakeStore API
* **Strategy Pattern**: Dynamic selection between storage and external API services
* **Redis Caching**: Implements caching layer for improved performance
* **Advanced Search**: Full-text search with pagination and multi-field sorting
* **Microservice Architecture**: Eureka client integration for service discovery
* **RESTful API**: Clean, stateless API design with proper error handling
* **JPA Integration**: Full CRUD operations with MySQL database

## Architecture Overview

The project follows a **layered architecture** with clear separation of concerns and implements several design patterns:

```
┌─────────────────────────────────────────────────────────────────┐
│                    Presentation Layer                           │
│  ┌─────────────────┐  ┌─────────────────────────────────────┐  │
│  │ ProductController│  │         SearchController           │  │
│  └─────────────────┘  └─────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│                     Service Layer                              │
│  ┌─────────────────┐  ┌─────────────────────────────────────┐  │
│  │ IProductService │  │         ISearchService              │  │
│  │   (Interface)   │  │         (Interface)                 │  │
│  └─────────────────┘  └─────────────────────────────────────┘  │
│  ┌─────────────────┐  ┌─────────────────────────────────────┐  │
│  │StorageProduct   │  │      FakeStoreProductService        │  │
│  │Service          │  │                                      │  │
│  └─────────────────┘  └─────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│                   Repository Layer                             │
│  ┌─────────────────┐  ┌─────────────────────────────────────┐  │
│  │   ProductRepo   │  │         CategoryRepo                │  │
│  └─────────────────┘  └─────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────────┐
│                   Data Layer                                  │
│  ┌─────────────────┐  ┌─────────────────────────────────────┐  │
│  │      MySQL      │  │              Redis                   │  │
│  │   Database      │  │            Cache                     │  │
│  └─────────────────┘  └─────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Technologies Used

### Core Technologies
* **Java 17**: Modern Java with enhanced features
* **Spring Boot 3.4.5**: Latest Spring Boot version with Spring 6 support
* **Spring Data JPA**: Object-relational mapping and data access
* **Spring Web**: RESTful web services and MVC framework

### Database & Caching
* **MySQL 9.2.0**: Primary relational database
* **Redis**: In-memory caching for performance optimization
* **Hibernate**: JPA implementation for ORM

### Microservices & Integration
* **Spring Cloud Netflix Eureka**: Service discovery client
* **RestTemplate**: HTTP client for external API integration
* **FakeStore API**: External product data source

### Development Tools
* **Maven**: Build automation and dependency management
* **Lombok**: Reduces boilerplate code
* **Spring Boot DevTools**: Development-time enhancements

### Testing
* **Spring Boot Test**: Testing framework for Spring Boot applications
* **JUnit**: Unit testing framework

## Project Structure

```
src/main/java/com/example/productcatalogservice/
├── config/
│   └── RedisConfig.java                    # Redis configuration
├── controllers/
│   ├── ProductController.java              # Product CRUD operations
│   ├── SearchController.java               # Search functionality
│   └── ControllerAdvisor.java              # Global exception handling
├── dtos/
│   ├── ProductDto.java                     # Product data transfer object
│   ├── CategoryDto.java                    # Category data transfer object
│   ├── SearchRequestDto.java               # Search request DTO
│   ├── SortParam.java                      # Sorting parameters
│   └── SortType.java                       # Sorting direction enum
├── modals/                                 # Domain models
│   ├── BaseModal.java                      # Abstract base entity
│   ├── Product.java                        # Product entity
│   ├── Category.java                       # Category entity
│   ├── State.java                          # Entity state enum
│   └── TestModal.java                      # Test entity
├── repos/                                  # Data access layer
│   ├── ProductRepo.java                    # Product repository
│   └── CategoryRepo.java                   # Category repository
├── services/                               # Business logic layer
│   ├── IProductService.java                # Product service interface
│   ├── ISearchService.java                 # Search service interface
│   ├── StorageProductService.java          # Local database service
│   ├── FakeStoreProductService.java        # External API service
│   └── JpaSearchService.java               # JPA-based search service
└── ProductCatalogServiceApplication.java    # Main application class
```

## Database Schema

### Entity Relationships

The database schema follows a normalized design with proper relationships:

```
┌─────────────────┐         ┌─────────────────┐
│    Product      │         │    Category     │
├─────────────────┤         ├─────────────────┤
│ id (PK)         │◄────────┤ id (PK)         │
│ name            │         │ name            │
│ description     │         │ description     │
│ imageUrl        │         │ products        │
│ price           │         │ created_at      │
│ isPrime         │         │ updated_at      │
│ category_id (FK)│         │ state           │
│ created_at      │         └─────────────────┘
│ updated_at      │
│ state           │
└─────────────────┘
```

### Base Entity Inheritance

All entities inherit from `BaseModal` which provides common fields:

```java
@MappedSuperclass
public abstract class BaseModal {
    @Id
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private State state;
}
```

### Database Migration

The project uses Flyway for database versioning with the initial migration:

```sql
CREATE TABLE test_modal
(
    id         BIGINT NOT NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    state      SMALLINT NULL,
    CONSTRAINT pk_testmodal PRIMARY KEY (id)
);
```

## Design Patterns Implemented

### 1. Strategy Pattern

**Purpose**: Allows dynamic selection between different product service implementations.

**Implementation**: 
- `IProductService` interface defines the contract
- `StorageProductService` implements local database operations
- `FakeStoreProductService` implements external API operations
- Service selection is controlled by Spring's `@Qualifier` annotation

```java
@Service("storage-product-service")
@Primary
public class StorageProductService implements IProductService { ... }

@Service("fake-store-service")
public class FakeStoreProductService implements IProductService { ... }

@Autowired
@Qualifier("storage-product-service")
private IProductService productService;
```

**Benefits**:
- Easy to switch between data sources
- New service implementations can be added without changing existing code
- Runtime service selection capability

### 2. Repository Pattern

**Purpose**: Abstracts data access logic and provides a consistent interface for data operations.

**Implementation**: 
- `ProductRepo` and `CategoryRepo` extend `JpaRepository`
- Provides CRUD operations and custom query methods
- Separates business logic from data access logic

```java
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findProductByOrderByPriceDesc();
    List<Product> findByNameEquals(String name, Pageable pageable);
    
    @Query("select p.name from Product p where p.id=?1")
    String findProductNameById(Long id);
}
```

### 3. DTO Pattern

**Purpose**: Separates internal domain models from external API representations.

**Implementation**:
- `ProductDto` for API requests/responses
- `CategoryDto` for category information
- `SearchRequestDto` for search parameters
- Mapping methods convert between DTOs and domain models

```java
private ProductDto from(Product product) {
    ProductDto productDto = new ProductDto();
    productDto.setId(product.getId());
    productDto.setName(product.getName());
    // ... other mappings
    return productDto;
}
```

### 4. Template Method Pattern

**Purpose**: The `BaseModal` class provides a template for all entities.

**Implementation**: All entities inherit common fields and behavior from `BaseModal`.

### 5. Factory Pattern (Conceptual)

**Purpose**: Spring's dependency injection acts as a factory for creating service instances.

## MVC Pattern Implementation

The project implements the **Model-View-Controller (MVC)** pattern as follows:

### Controller Layer (View)

**Files**: `ProductController.java`, `SearchController.java`

**Responsibilities**:
- Handle HTTP requests and responses
- Validate input data
- Call appropriate service methods
- Transform data between DTOs and domain models
- Handle exceptions and return appropriate HTTP status codes

```java
@RestController
@RequestMapping("/products")
public class ProductController {
    @GetMapping("/{id}")
    public ProductDto getProductDetails(@PathVariable Long id) {
        if(id < 0) {
            throw new IllegalArgumentException("Please pass productId greater than 0");
        }
        Product product = productService.getProductById(id);
        return from(product);
    }
}
```

### Service Layer (Model)

**Files**: `StorageProductService.java`, `FakeStoreProductService.java`, `JpaSearchService.java`

**Responsibilities**:
- Implement business logic
- Coordinate between different components
- Handle data transformation
- Manage caching strategies
- Integrate with external services

```java
@Service("storage-product-service")
@Primary
public class StorageProductService implements IProductService {
    @Override
    public Product getProductById(Long id) {
        // Check cache first
        Product product = (Product)redisTemplate.opsForHash().get("products",id);
        if(product == null) {
            // Fallback to database
            Optional<Product> optionalProduct = productRepo.findById(id);
            if(optionalProduct.isPresent()) {
                // Cache the result
                redisTemplate.opsForHash().put("products", id, optionalProduct.get());
                return optionalProduct.get();
            }
            return null;
        }
        return product;
    }
}
```

### Data Layer (Model)

**Files**: `ProductRepo.java`, `CategoryRepo.java`, Entity classes

**Responsibilities**:
- Define data structure and relationships
- Provide data access methods
- Handle database operations
- Manage entity lifecycle

## Module Connections

### 1. Request Flow

```
Client Request → Controller → Service → Repository → Database
                                    ↓
                              Redis Cache
```

### 2. Component Dependencies

```
ProductController
    ↓ (depends on)
IProductService (interface)
    ↓ (implemented by)
StorageProductService / FakeStoreProductService
    ↓ (depends on)
ProductRepo / RestTemplate
    ↓ (depends on)
MySQL Database / External API
```

### 3. Search Flow

```
SearchController → ISearchService → JpaSearchService → ProductRepo → Database
```

### 4. Caching Strategy

```
Service → Redis Cache (check)
    ↓ (if not found)
Service → Database → Redis Cache (store) → Return
```

## API Endpoints

### 1. Product Management

#### Get All Products
- **Endpoint**: `GET /products`
- **Response**: List of all products
- **Cache**: No caching implemented

#### Get Product by ID
- **Endpoint**: `GET /products/{id}`
- **Parameters**: `id` (Long)
- **Response**: `ProductDto` with category information
- **Cache**: Redis caching with fallback to database
- **Validation**: ID must be positive

#### Create Product
- **Endpoint**: `POST /products`
- **Request Body**: `ProductDto`
- **Response**: Created `ProductDto`
- **Logic**: Checks if product exists, creates if not

#### Update Product
- **Endpoint**: `PUT /products/{id}`
- **Parameters**: `id` (Long)
- **Request Body**: `Product` entity
- **Response**: Updated `Product` entity
- **Validation**: ID must be non-negative

#### Delete Product
- **Endpoint**: `DELETE /products/{id}`
- **Parameters**: `id` (Long)
- **Response**: Boolean indicating success
- **Status**: Not implemented (returns null)

### 2. Search Functionality

#### Search Products
- **Endpoint**: `POST /search`
- **Request Body**: `SearchRequestDto`
- **Response**: `Page<Product>` with pagination and sorting
- **Features**: 
  - Full-text search by product name
  - Pagination support
  - Multi-field sorting (ASC/DESC)
  - Configurable page size

### 3. Request/Response DTOs

#### ProductDto
```json
{
    "id": 1,
    "name": "Product Name",
    "description": "Product Description",
    "imageUrl": "https://example.com/image.jpg",
    "price": 99.99,
    "category": {
        "id": 1,
        "name": "Electronics",
        "description": "Electronic products"
    }
}
```

#### SearchRequestDto
```json
{
    "query": "laptop",
    "pageSize": 10,
    "pageNumber": 0,
    "sortParams": [
        {
            "paramName": "price",
            "sortType": "DESC"
        }
    ]
}
```

## Configuration

### Application Properties

```properties
spring.application.name=ProductCatalogService
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/productcatalogservice
spring.datasource.username=root
spring.datasource.password=Hell0There!
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql: true
server.port=8080

# Redis Configuration
spring.data.redis.host=localhost
spring.data.redis.port=6379

# Service Discovery
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

### Configuration Classes

* **RedisConfig**: Creates and configures Redis template bean
* **Spring Boot Auto-configuration**: Automatically configures JPA, Redis, and web components

### Environment Support

The application supports both local development and AWS deployment:
- **Local**: MySQL on localhost:3306, Redis on localhost:6379
- **AWS**: RDS MySQL instance, Eureka server on localhost:8761

## How to Run

### Prerequisites

* Java 17 or higher
* Maven 3.6 or higher
* MySQL 8.0 or higher
* Redis 6.0 or higher
* Eureka Server (for service discovery)

### Setup Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd scaler-products-catalog-service
   ```

2. **Configure Database**
   - Create MySQL database: `productcatalogservice`
   - Update `application.properties` with your database credentials

3. **Start Redis Server**
   ```bash
   redis-server
   ```

4. **Start Eureka Server** (optional, for microservice discovery)
   - Run Eureka server on port 8761

5. **Run the application**
   ```bash
   # Using Maven wrapper
   ./mvnw spring-boot:run
   
   # Or using Maven directly
   mvn spring-boot:run
   ```

6. **Access the application**
   - Application runs on `http://localhost:8080`
   - Product endpoints: `http://localhost:8080/products`
   - Search endpoint: `http://localhost:8080/search`

### Testing the API

#### Test product endpoints
```bash
# Get all products
curl -X GET http://localhost:8080/products

# Get product by ID
curl -X GET http://localhost:8080/products/1

# Create product
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "Test Product",
    "description": "Test Description",
    "price": 99.99,
    "imageUrl": "https://example.com/image.jpg"
  }'

# Search products
curl -X POST http://localhost:8080/search \
  -H "Content-Type: application/json" \
  -d '{
    "query": "laptop",
    "pageSize": 10,
    "pageNumber": 0,
    "sortParams": [
      {
        "paramName": "price",
        "sortType": "DESC"
      }
    ]
  }'
```

## Testing

### Test Structure

* **Unit Tests**: Test individual components in isolation
* **Integration Tests**: Test component interactions
* **API Tests**: Test REST endpoints

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ProductControllerTest

# Run with coverage
./mvnw test jacoco:report
```

### Test Coverage Areas

* Product controller request handling
* Product service business logic
* Repository data access methods
* Search service functionality
* Exception handling and validation

## Future Enhancements

### 1. Database Integration Improvements

* Implement proper database migrations with Flyway
* Add database connection pooling configuration
* Implement audit logging and soft deletes
* Add database indexing for search optimization

### 2. Security Improvements

* JWT-based authentication and authorization
* API rate limiting and throttling
* Input validation and sanitization
* CORS configuration

### 3. Performance Enhancements

* Implement connection pooling for Redis
* Add database query optimization
* Implement async processing for bulk operations
* Add health checks and monitoring

### 4. Additional Features

* Product image upload and management
* Product reviews and ratings
* Inventory management
* Product recommendations
* Export functionality (CSV, PDF)

### 5. Monitoring and Observability

* Logging framework (SLF4J + Logback)
* Metrics collection (Micrometer)
* Distributed tracing
* Application performance monitoring

### 6. Error Handling

* Global exception handler with proper error responses
* Retry mechanisms for external API calls
* Circuit breaker pattern for fault tolerance
* Structured error logging

## Conclusion

This Product Catalog Service project demonstrates several important software engineering concepts:

1. **Clean Architecture**: Clear separation of concerns with layered design
2. **Design Patterns**: Strategic use of Strategy, Repository, DTO, and Template Method patterns
3. **MVC Implementation**: Proper separation of presentation, business logic, and data layers
4. **Microservice Architecture**: Service discovery integration and external API integration
5. **Performance Optimization**: Redis caching and efficient data access patterns
6. **Modern Java**: Uses Java 17 and Spring Boot 3.4.5 with best practices
7. **Extensibility**: Easy to add new data sources, services, and features

The project serves as an excellent foundation for understanding:
- Microservice architecture and design
- Spring Boot development with JPA and Redis
- Design patterns in practice
- API design and RESTful services
- Database design and optimization
- Caching strategies and performance tuning

This implementation can be extended with additional features like security, monitoring, and advanced search capabilities to create a production-ready product catalog service.

---

**Note**: This project is designed for educational purposes and demonstrates architectural concepts. For production use, additional security, error handling, monitoring, and testing features should be implemented.
