# Spring Web Application with Clean Architecture

## 🚀 Quick Start

```bash
# Build and run the application
./gradlew bootRun

# Run tests
./gradlew test

# Build for production
./gradlew build
```

## 🎯 Features

- **Clean Architecture** implementation
- **Spring Boot 4** with Java 25
- **JWT Authentication** with Spring Security
- **Role-Based Access Control** (Admin/User roles)
- **Validation** with proper HTTP status codes
- **Admin User Initialization** on startup

## 📋 API Endpoints

### Authentication

```http
POST /api/auth/login
{
    "email": "admin@example.com",
    "password": "admin123"
}
```

### Users (Admin Only)

- `GET /api/users` - List all users
- `POST /api/users` - Create new user
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

## 🔐 Default Admin User

- **Email**: `admin@example.com`
- **Password**: `admin123`
- **Role**: ADMIN

⚠️ **You can change this secured password**

## 🛡️ Security

- **JWT Tokens** for stateless authentication
- **BCrypt** password hashing
- **Role-based authorization**
- **CSRF protection** disabled for API

## 🧪 Testing

- **Unit tests** for business logic
- **Integration tests** for controllers
- **Security tests** for authentication/authorization

## 📦 Architecture

```
Presentation Layer → Application Layer → Domain Layer → Infrastructure Layer
```

- **Presentation**: Controllers, DTOs, API
- **Application**: Services, use cases
- **Domain**: Models, factories, repositories
- **Infrastructure**: Database, security, configuration

## 🔧 Configuration

Edit `application.properties` for:

```properties
# Admin user
app.admin.enabled=true
app.admin.username=admin
app.admin.email=admin@example.com

# JWT settings
app.jwt.secret=yourSecureSecretKeyHere12345678901234567890123456789012
app.jwt.expiration=86400000
```

## 🚀 Deployment

### Development
```bash
./gradlew bootRun
```

### Production
```bash
./gradlew build
java -jar build/libs/spring-web-0.0.1-SNAPSHOT.jar
```

## 📚 Documentation

- [Clean Architecture Guide](ARCHITECTURE.md)
- [Security Implementation](SECURITY.md)
- [Admin User Initialization](ADMIN_USER_INITIALIZATION.md)

## 🛠️ Built With

- Spring Boot 4
- Spring Security with JWT
- Jakarta Validation
- JUnit 5
- Mockito

## ✅ Checklist

- [ ] Change default admin password
- [ ] Configure proper JWT secret
- [ ] Set up HTTPS in production
- [ ] Implement proper logging
- [ ] Configure monitoring

---

**Clean Architecture + Spring Boot = Maintainable & Scalable Applications** 🚀