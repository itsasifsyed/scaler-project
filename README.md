# Project Microservices Repository

Welcome to the centralized repository for our microservices-based application. This repository brings together all the individual services and necessary components for the project, providing a complete solution for an e-commerce platform back-end.

---

## Folder Structure

The repository is organized into two main directories, `services` and `discovery`, to keep the components logically separated.

```text
.
├── 📂 discovery/
│   └── 🗺️ scaler-service-discovery/
│
├── 📂 services/
│   ├── 🛍️ scaler-products-catalog-service/
│   ├── 💳 scaler-payment-service/
│   └── 👤 scaler-user-service/
│
└── 📄 README.md

````
---

## Project Components

Each component is a self-contained application with its own dependencies and documentation. For detailed information about any specific service, please refer to the `README.md` file located within its respective directory.

### 🗺️ Service Discovery (`discovery/scaler-service-discovery`)

* **Description:** This project implements a Service Discovery mechanism using Eureka Server, which is a crucial component in modern microservices architectures. It acts as a centralized registry where all other microservices can register themselves and discover others, allowing them to communicate without hardcoding network locations.
* **Technologies:** Java 17, Spring Boot 3.x, Spring Cloud Netflix Eureka, Maven.
* **More Info:** For detailed setup and usage instructions, please see the `README.md` inside the `/discovery/scaler-service-discovery` directory.

---

### 🛍️ Product Catalog Service (`services/scaler-products-catalog-service`)

* **Description:** This service provides a comprehensive product catalog management system. It features a dual-service architecture, using a Strategy Pattern to fetch products from either a local MySQL database or an external FakeStore API. It includes advanced features like Redis caching, search functionality with pagination and sorting, and service discovery integration.
* **Technologies:** Java, Spring Boot, MySQL, Redis, Eureka, JPA, Maven.
* **More Info:** For detailed setup and API documentation, please see the `README.md` inside the `/services/scaler-products-catalog-service` directory.

### 💳 Payment Service (`services/scaler-payment-service`)

* **Description:** This microservice provides a unified interface for processing payments through multiple payment gateways, specifically Razorpay and Stripe. It uses a Strategy Pattern to dynamically select the appropriate payment gateway and offers RESTful APIs for processing payments and handling webhooks.
* **Technologies:** Java 17, Spring Boot, Razorpay Java SDK, Stripe Java SDK, Maven.
* **More Info:** For detailed setup and API documentation, please see the `README.md` inside the `/services/scaler-payment-service` directory.

### 👤 User Service (`services/scaler-user-service`)

* **Description:** This service is responsible for user-centric functionalities, including user authentication (signup/login), token generation for secure API access, and management of user profiles.
* **Technologies:** (Likely includes Java, Spring Boot, Spring Security, and a database like MySQL).
* **More Info:** For detailed setup and API documentation, please see the `README.md` inside the `/services/scaler-user-service` directory.

---

## Getting Started

Follow these instructions to get the entire application running on your local machine.

### Prerequisites

* Java 17+
* Maven 3.8+
* MySQL Server
* Redis
* (Any other global prerequisites you have)

### Running the Application

1.  **Start the Service Discovery:**
    * Navigate to the `discovery/scaler-service-discovery` directory and follow the instructions in its `README.md`. The service discovery must be running before any other services are started.

2.  **Start the Microservices:**
    * For each service in the `services/` directory, navigate to its folder and follow the startup instructions in its local `README.md`. It is recommended to start them one by one after the service discovery is up and running.
