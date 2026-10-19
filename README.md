# Hospital Management System — Microservices Architecture

A Java-based Hospital Management System developed as a personal backend engineering project to explore microservices architecture, service-to-service communication, data persistence, asynchronous messaging, caching, and fault tolerance.

## Architecture Overview

The application is organized into independently runnable Spring Boot services.

- **Eureka Server** — Service registration and discovery.
- **API Gateway** — Central entry point for routing incoming requests to backend services.
- **Doctor Service** — Manages doctor-related operations and persists data in MySQL.
- **Patient Service** — Manages patient-related operations, persists patient data in MySQL, and communicates with Doctor Service.
- **Audit Service** — Supports audit-related functionality.

## Technology Stack

| Area | Technologies |
|---|---|
| Language | Java |
| Backend | Spring Boot, Spring MVC |
| Microservices | Spring Cloud, Eureka |
| API Routing | Spring Cloud Gateway |
| Service Communication | Spring Cloud OpenFeign |
| Database | MySQL |
| Persistence | Spring Data JPA, Hibernate |
| Messaging | Apache Kafka |
| Fault Tolerance | Resilience4j Circuit Breaker and Retry |
| Performance | Spring Cache |
| Build | Maven |
| API Testing | Postman |
| API Documentation | SpringDoc OpenAPI, if configured |

## Key Backend Concepts

### 1. Service Discovery
Eureka allows service instances to register and discover one another without requiring every service to use a hard-coded instance address.

### 2. API Gateway
The gateway provides a single entry point for client requests and routes them to the appropriate microservice.

### 3. Inter-Service Communication
Spring Cloud OpenFeign is used for declarative HTTP communication between services, including Patient Service and Doctor Service.

### 4. MySQL Persistence
Spring Data JPA and Hibernate are used to map Java entities to relational database tables and perform persistence operations.

### 5. Kafka Messaging
Apache Kafka is used to explore asynchronous event-driven communication between services. Event producers, consumers, topic configuration, and processing behavior depend on the implemented workflows.

### 6. Circuit Breaker and Retry
Resilience4j helps handle failures during remote service calls. Circuit breakers can temporarily stop calls to an unhealthy dependency, while retry policies can repeat eligible failed calls according to configured limits.

### 7. Caching
Spring Cache is used to reduce repeated data retrieval for eligible operations. Cache eviction must be considered when the underlying data changes.

## Request Flow

1. A client sends a request to the API Gateway.
2. The gateway routes the request to the appropriate backend service.
3. Eureka provides service discovery where configured.
4. The backend service processes the request and interacts with MySQL when persistence is required.
5. Where configured, services communicate through OpenFeign or Kafka.
6. Caching and Resilience4j are applied to the operations for which they have been implemented.

## Project Status

This project is under development. Features described here should be verified against the current source code and configuration.

Appointment and Billing services are future extensions unless explicitly implemented.

## Learning Objectives

- Build independently runnable Spring Boot microservices.
- Understand service discovery and API routing.
- Implement synchronous and asynchronous service communication.
- Integrate relational persistence using MySQL.
- Apply caching and cache invalidation.
- Handle remote-service failures using circuit breakers and retry policies.
- Explore event-driven processing with Kafka.

## Running Locally

Each microservice has its own Maven project and application configuration.

Before running the system:
1. Configure MySQL databases and credentials.
2. Configure Eureka and service ports.
3. Configure API Gateway routes.
4. Start Kafka and configure topics and consumers if required by the enabled workflows.
5. Start the services in the order required by their dependencies.

Detailed setup commands, endpoints, and test instructions will be added after validating the final project configuration.
