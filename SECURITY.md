# Spring Boot Security Implementation Guide

## 🔒 Security Overview

This guide documents the security implementation in the Spring Web application, which uses **JWT (JSON Web Tokens)** for stateless authentication and **Spring Security** for authorization.

## 🎯 Security Features

### 1. **JWT Authentication**
- Stateless authentication using JSON Web Tokens
- Secure token generation and validation
- Configurable token expiration
- Bearer token authentication

### 2. **Password Security**
- BCrypt password hashing
- Secure password storage
- Automatic password encoding

### 3. **Role-Based Access Control**
- Admin and User roles
- Fine-grained endpoint authorization
- Role-based access to resources

### 4. **Secure Endpoints**
- CSRF protection (disabled for API, enabled for web forms)
- Stateless sessions
- Secure HTTP headers

## 🔧 Configuration

### JWT Configuration

Add the following to `application.properties`:

```properties
# JWT configuration
app.jwt.secret=yourSecureSecretKeyHere12345678901234567890123456789012
app.jwt.expiration=86400000  # 24 hours in milliseconds
```

**Important:** The JWT secret should be:
- At least 32 characters long
- Random and unpredictable
- Stored securely (consider using environment variables in production)

### Security Endpoints

```properties
# Public endpoints (no authentication required)
/api/auth/**
/swagger-ui/**
/v3/api-docs/**

# Admin-only endpoints
/api/users/**
/actuator/**

# All other endpoints require authentication
/**
```

## 🚀 Authentication Flow

### 1. User Login

```mermaid
graph TD
    A[Client] -->|POST /api/auth/login| B[AuthController]
    B -->|Authenticate| C[AuthenticationManager]
    C -->|Validate Credentials| D[UserDetailsService]
    D -->|Load User| E[UserRepository]
    E -->|Return User| D
    D -->|Return UserDetails| C
    C -->|Authentication Success| B
    B -->|Generate JWT| F[JwtUtil]
    F -->|Return Token| B
    B -->|Return AuthResponseDTO| A
```

### 2. Protected Requests

```mermaid
graph TD
    A[Client] -->|Request with Authorization Header| B[JwtAuthenticationFilter]
    B -->|Extract JWT Token| C[JwtUtil]
    C -->|Validate Token| D[JwtUtil]
    D -->|Extract Username| E[JwtUtil]
    E -->|Load UserDetails| F[UserDetailsService]
    F -->|Return UserDetails| B
    B -->|Set Security Context| G[SecurityContextHolder]
    G -->|Continue Request| H[Controller]
    H -->|Return Response| A
```

## 📋 API Endpoints

### Authentication Endpoints

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "securePassword123!"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Health Check
```http
POST /api/auth/health
```

**Response:**
```json
"Auth service is healthy"
```

### Protected Endpoints

All endpoints under `/api/users/**` require:
- Valid JWT token in `Authorization: Bearer <token>` header
- User with `ADMIN` role

Example:
```http
GET /api/users
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 🔐 Security Best Practices

### 1. **Password Management**
- Always use strong, randomly generated passwords
- Never store plain text passwords
- Use BCrypt or other secure hashing algorithms
- Enforce password complexity requirements

### 2. **JWT Security**
- Use strong, random secrets for JWT signing
- Set appropriate token expiration times
- Store tokens securely on the client side
- Implement token refresh mechanisms for long sessions

### 3. **API Security**
- Always use HTTPS in production
- Implement proper CORS configuration
- Use security headers (CSP, XSS protection, etc.)
- Rate limit authentication endpoints

### 4. **Admin Security**
- Change the default admin password immediately
- Use strong passwords for admin accounts
- Limit admin access to trusted networks
- Implement multi-factor authentication for admins

## 🛠 Customization

### Changing Admin Credentials

To change the default admin credentials, modify `application.properties`:

```properties
app.admin.username=newAdminUsername
app.admin.email=newAdminEmail@example.com
```

### Adding Custom Roles

To add custom roles, extend the `CustomUserDetailsService`:

```java
private Collection<? extends GrantedAuthority> getAuthorities(User user) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    
    // Add roles based on your business logic
    if (user.hasRole("ADMIN")) {
        authorities.add(() -> "ROLE_ADMIN");
    }
    if (user.hasRole("MANAGER")) {
        authorities.add(() -> "ROLE_MANAGER");
    }
    authorities.add(() -> "ROLE_USER"); // All users have USER role
    
    return authorities;
}
```

### Customizing Security Configuration

Modify `SecurityConfig.java` to change security rules:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/public/**").permitAll()
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/manager/**").hasAnyRole("ADMIN", "MANAGER")
            .anyRequest().authenticated()
        )
        // ... other configuration
    
    return http.build();
}
```

## 🧪 Testing Security

### Testing Authentication

```java
@Test
void testLoginWithValidCredentials() {
    AuthRequestDTO request = new AuthRequestDTO("admin@example.com", "admin123");
    
    ResponseEntity<AuthResponseDTO> response = restTemplate.postForEntity(
        "/api/auth/login", 
        request, 
        AuthResponseDTO.class
    );
    
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody().token());
}
```

### Testing Protected Endpoints

```java
@Test
void testProtectedEndpointRequiresAuthentication() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + getValidToken());
    
    HttpEntity<String> entity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange(
        "/api/users", 
        HttpMethod.GET, 
        entity, 
        String.class
    );
    
    assertEquals(HttpStatus.OK, response.getStatusCode());
}
```

## ⚠️ Production Considerations

### 1. **Environment Variables**
Use environment variables for sensitive configuration:

```bash
export JWT_SECRET=yourVerySecureSecretKeyHere
export ADMIN_PASSWORD=secureAdminPassword123!
```

### 2. **HTTPS Configuration**
Always use HTTPS in production:

```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=yourPassword
server.ssl.key-store-type=PKCS12
```

### 3. **Database Security**
- Use strong database credentials
- Implement proper database permissions
- Use connection pooling with SSL

### 4. **Monitoring and Logging**
- Implement security event logging
- Monitor failed login attempts
- Set up alerts for suspicious activity

### 5. **Regular Updates**
- Keep dependencies updated
- Regularly update Spring Security
- Monitor for security vulnerabilities

## 🔗 Related Documentation

- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/)
- [JWT.io](https://jwt.io/)
- [OWASP Security Guidelines](https://owasp.org/)
- [Spring Boot Security Guide](https://spring.io/guides/gs/securing-web/)

## ✅ Security Checklist

- [ ] JWT secret is strong and secure
- [ ] Passwords are properly hashed
- [ ] HTTPS is enabled in production
- [ ] Default admin password has been changed
- [ ] Security headers are configured
- [ ] CORS is properly configured
- [ ] Rate limiting is implemented for auth endpoints
- [ ] Monitoring and logging are in place
- [ ] Regular security audits are performed

The security implementation provides a solid foundation for building secure Spring Boot applications with JWT authentication and role-based authorization.