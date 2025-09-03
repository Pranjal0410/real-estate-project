# Real Estate Investment Platform

## Project Overview
A comprehensive Real Estate Investment Platform built with Spring Boot, demonstrating advanced Java concepts and Spring Framework features for the Advanced Programming Concepts course (23CS007).

## Technology Stack

### Backend
- **Java 17+** - Core programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Security** - JWT-based authentication
- **Spring Data JPA** - ORM and database access
- **Hibernate** - JPA implementation
- **Spring AOP** - Aspect-oriented programming
- **MySQL 8+** - Primary database

### Frontend
- **JSP** - Java Server Pages for views
- **JSTL** - JSP Standard Tag Library
- **Bootstrap 5** - CSS framework
- **jQuery** - JavaScript library

### Build & Testing
- **Maven** - Build and dependency management
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework

## Features

### Core Functionality
1. **Property Management**
   - CRUD operations for properties
   - Advanced search and filtering
   - Image management
   - Property categorization

2. **Investment Calculator**
   - ROI calculation
   - Rental yield analysis
   - Cap rate computation
   - Mortgage calculator
   - Cash flow analysis
   - Break-even point calculation

3. **User Management**
   - Multi-role authentication (Admin, Investor, Property Owner)
   - JWT-based security
   - Profile management
   - Password encryption

4. **AI Chatbot**
   - Property inquiry assistance
   - Investment advice
   - OpenAI API integration (configurable)

5. **Advanced Features**
   - Asynchronous processing
   - Caching mechanism
   - AOP for logging and security
   - Functional programming with lambdas
   - Exception handling
   - File operations

## Project Structure
```
src/
├── main/
│   ├── java/com/realestate/
│   │   ├── controller/      # REST & MVC controllers
│   │   ├── service/         # Business logic
│   │   ├── repository/      # Data access layer
│   │   ├── model/
│   │   │   ├── entity/     # JPA entities
│   │   │   └── dto/        # Data transfer objects
│   │   ├── config/         # Spring configurations
│   │   ├── security/       # Security components
│   │   ├── aspect/         # AOP aspects
│   │   ├── exception/      # Custom exceptions
│   │   └── util/          # Utility classes
│   ├── resources/
│   │   ├── static/        # CSS, JS files
│   │   ├── application.yml
│   │   └── data.sql
│   └── webapp/WEB-INF/jsp/ # JSP views
└── test/                    # Test classes
```

## Setup Instructions

### Prerequisites
1. Java 17 or higher
2. Maven 3.6+
3. MySQL 8+
4. Git

### Database Setup
1. Install MySQL and create a database:
```sql
CREATE DATABASE real_estate_db;
```

2. Update database credentials in `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/real_estate_db
    username: your_username
    password: your_password
```

3. Run the SQL scripts:
```bash
mysql -u your_username -p real_estate_db < src/main/resources/schema.sql
mysql -u your_username -p real_estate_db < src/main/resources/data.sql
```

### Build and Run
1. Clone the repository:
```bash
git clone <repository-url>
cd real-estate-project
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. Access the application:
- Web Interface: http://localhost:8080
- API Documentation: http://localhost:8080/swagger-ui.html

### Running Tests
```bash
mvn test
```

## API Endpoints

### Authentication
- POST `/api/auth/login` - User login
- POST `/api/auth/register` - User registration
- GET `/api/auth/current` - Get current user

### Properties
- GET `/api/properties` - List all properties
- GET `/api/properties/{id}` - Get property by ID
- POST `/api/properties` - Create property (Admin/Owner)
- PUT `/api/properties/{id}` - Update property
- DELETE `/api/properties/{id}` - Delete property (Admin)
- POST `/api/properties/search` - Search properties

### Investment Calculator
- POST `/api/investments/calculate` - Calculate investment metrics
- GET `/api/investments/roi` - Calculate ROI
- GET `/api/investments/rental-yield` - Calculate rental yield
- GET `/api/investments/mortgage` - Calculate mortgage payment

### Chatbot
- POST `/api/chatbot/message` - Send message to chatbot
- DELETE `/api/chatbot/session/{id}` - Clear chat session

## Default Credentials
- Admin: `admin / password123`
- User: `john_investor / password123`
- Property Owner: `jane_owner / password123`

## Key Technologies Demonstrated

### Java Features
- Lambda expressions and functional interfaces
- Stream API for data processing
- Optional for null safety
- Concurrent processing with CompletableFuture
- Exception handling with custom exceptions
- Annotations and reflection
- Generics and collections

### Spring Framework
- Dependency Injection (Constructor & Field)
- Spring Boot autoconfiguration
- Spring Security with JWT
- Spring Data JPA with custom queries
- Spring AOP for cross-cutting concerns
- Caching with Spring Cache
- Async processing
- RESTful API design

### Design Patterns
- Repository Pattern
- DTO Pattern
- Service Layer Pattern
- Builder Pattern
- Factory Pattern
- Singleton Pattern (Spring beans)

## Performance Optimizations
- Database indexing
- Query optimization
- Caching frequently accessed data
- Asynchronous processing
- Connection pooling
- Lazy loading

## Security Features
- JWT token authentication
- Password encryption (BCrypt)
- Role-based access control
- Method-level security
- CORS configuration
- Input validation
- SQL injection prevention

## Testing Coverage
- Unit tests for services
- Integration tests for controllers
- Repository layer tests
- Security tests
- Mock testing with Mockito

## Deployment
The application can be deployed as:
1. Standalone JAR with embedded Tomcat
2. WAR file for external servlet containers
3. Docker container (Dockerfile included)

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to the branch
5. Create a Pull Request

## License
This project is developed for educational purposes as part of the Advanced Programming Concepts course.

## Contact
For questions or support, please contact the development team.

## Acknowledgments
- Spring Boot Documentation
- OpenAI API Documentation
- Bootstrap Documentation
- Course instructors and TAs