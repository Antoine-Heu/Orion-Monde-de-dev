# MDD API - Back-end

REST API for the MDD (Monde de Dév) social network, built with Spring Boot.

## Technologies

- **Java**: 11
- **Spring Boot**: 2.7.18
- **Spring Security**: JWT Authentication
- **Spring Data JPA**: Data access
- **MySQL**: Database
- **Maven**: Dependency management
- **Lombok**: Boilerplate code reduction

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/your-username/mdd-project.git
cd mdd-project/back
```

### 2. Configure the database

Create a MySQL database:

```sql
CREATE DATABASE mdd_db;
```

Configure connection parameters in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mdd_db?serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Install dependencies and run the application

```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## Main Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/auth/me` - Get current user
- `POST /api/auth/logout` - User logout

### Users
- `GET /api/user` - User information
- `PUT /api/user` - Update profile

### Posts
- `GET /api/posts` - List all posts
- `GET /api/posts/{id}` - Get post details
- `POST /api/posts` - Create a post

### Topics
- `GET /api/topics` - List all topics
- `POST /api/topics/{id}/subscribe` - Subscribe to a topic
- `DELETE /api/topics/{id}/unsubscribe` - Unsubscribe from a topic

### Comments
- `GET /api/comments/post/{postId}` - Get post comments
- `POST /api/comments` - Add a comment

## Architecture

```
back/
├── src/main/java/com/openclassrooms/mddapi/
│   ├── Controllers/        # REST endpoints
│   ├── Services/           # Business logic
│   ├── Repositories/       # Data access
│   ├── Models/             # JPA entities
│   ├── Dto/                # Data Transfer Objects
│   └── Config/             # Spring configuration
└── src/main/resources/
    └── application.properties
```

## SOLID Principles

The architecture follows SOLID principles:
- **S**ingle Responsibility: Each class has a single responsibility
- **O**pen/Closed: Use of interfaces for extension
- **L**iskov Substitution: Interchangeable implementations
- **I**nterface Segregation: Specific interfaces (UserService, PostService)
- **D**ependency Inversion: Spring dependency injection

## Security

- JWT authentication with HttpOnly cookies
- CSRF protection enabled
- Data validation with Bean Validation
- BCrypt password hashing
