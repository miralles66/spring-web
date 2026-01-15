# Clean Architecture Implementation

## 🎯 Overview

This project implements **Clean Architecture** principles with Spring Boot, separating concerns into distinct layers for better maintainability and testability.

## 📦 Layer Structure

```
Presentation Layer → Application Layer → Domain Layer → Infrastructure Layer
```

### 1. Presentation Layer
**Controllers, DTOs, API Endpoints**
- Handles HTTP requests/responses
- Validates input using `@Valid`
- Maps between DTOs and domain models

### 2. Application Layer
**Services, Use Cases, Business Logic**
- Contains application-specific business rules
- Orchestrates domain objects
- Implements use cases

### 3. Domain Layer
**Models, Factories, Repositories (Interfaces)**
- Core business logic and entities
- Pure Java - no framework dependencies
- Defines repository interfaces

### 4. Infrastructure Layer
**Database, Security, External Services**
- Implements repository interfaces
- Handles persistence (JPA, etc.)
- Configures security, logging, etc.

## 🔧 Key Components

### User Factory Pattern
```java
// Domain Layer Interface
public interface UserFactory {
    User createUser(String username, String email);
    User createAdminUser(String username, String email);
}

// Infrastructure Layer Implementation
@Service
public class DefaultUserFactory implements UserFactory {
    // Concrete implementation
}
```

### Dependency Flow
```
Presentation → Application → Domain ← Infrastructure
```

## ✅ Benefits

- **Testability**: Easy to mock dependencies
- **Maintainability**: Clear separation of concerns
- **Flexibility**: Easy to swap implementations
- **Framework Independence**: Domain layer has no Spring dependencies

## 🛠️ Implementation Tips

1. **Dependency Rule**: Inner layers don't know about outer layers
2. **Interface Segregation**: Keep interfaces focused
3. **Single Responsibility**: Each class has one clear purpose
4. **Open/Closed Principle**: Extend behavior without modifying existing code

## 📚 Related

- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

**Clean Architecture = Maintainable & Testable Code** 🚀