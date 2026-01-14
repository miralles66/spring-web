# Clean Architecture Structure - Repository-Domain-Presentation Pattern

This Spring Boot application follows a clean architecture approach with clear separation of concerns using the Repository-Domain-Presentation pattern.

## Folder Structure

```
src/main/java/com/miralles/spring_web/
├── application/              # Application Layer (Use Cases)
│   ├── ports/                # Interfaces (Ports)
│   │   └── UserService.java  # Application service interface
│   └── services/             # Service Implementations
│       └── UserServiceImpl.java
├── domain/                   # Domain Layer (Core Business Logic)
│   ├── models/               # Domain Models/Entities
│   │   └── User.java         # Domain model
│   └── repositories/         # Repository Interfaces (Ports)
│       └── UserRepository.java
├── infrastructure/           # Infrastructure Layer (Adapters)
│   ├── adapters/             # External service adapters
│   ├── config/               # Configuration classes
│   │   └── AppConfig.java    # Application configuration
│   └── persistence/          # Data persistence implementations
│       └── JpaUserRepository.java
└── presentation/             # Presentation Layer (UI/API)
    ├── controllers/          # REST Controllers
    │   └── UserController.java
    └── dtos/                 # Data Transfer Objects
        ├── UserMapper.java   # DTO to Domain model mapper
        ├── UserRequestDTO.java
        └── UserResponseDTO.java
```

## Architecture Layers

### 1. Domain Layer (Core)
- Contains the core business logic and entities
- Defines repository interfaces (ports)
- Independent of frameworks and external concerns
- **Key Principle**: This layer should not depend on any other layer

### 2. Application Layer (Use Cases)
- Contains application-specific business rules
- Orchestrates the flow of data between domain and infrastructure
- Defines service interfaces (ports)
- **Key Principle**: Depends only on the domain layer

### 3. Infrastructure Layer (Adapters)
- Implements the interfaces defined in domain and application layers
- Contains database implementations, external service integrations
- **Key Principle**: Depends on domain and application layers

### 4. Presentation Layer (Interface)
- Handles HTTP requests and responses
- Contains REST controllers and DTOs
- **Key Principle**: Depends on application layer, converts between DTOs and domain models

## Dependency Flow

```
Presentation Layer → Application Layer → Domain Layer
                        ↑
                   Infrastructure Layer
```

## Key Benefits

1. **Separation of Concerns**: Each layer has a single responsibility
2. **Testability**: Easy to test each layer in isolation
3. **Maintainability**: Changes in one layer don't affect others
4. **Flexibility**: Easy to swap implementations (e.g., change database)
5. **Framework Independence**: Domain logic is not tied to Spring or any framework

## Example Flow

1. **HTTP Request** → UserController (Presentation)
2. **Controller** → UserService (Application)
3. **Service** → UserRepository (Domain Interface)
4. **Repository Implementation** → JpaUserRepository (Infrastructure)
5. **Data Flow Back** → Response DTO → HTTP Response

## Testing Strategy

- **Unit Tests**: Test individual components in isolation using mocks
- **Integration Tests**: Test layer interactions
- **Controller Tests**: Test API endpoints with MockMvc

## Best Practices

- Keep domain models simple and focused on business logic
- Use DTOs for API contracts, don't expose domain models directly
- Follow Dependency Inversion Principle (DIP)
- Keep controllers thin, move business logic to services
- Use interfaces for all external dependencies