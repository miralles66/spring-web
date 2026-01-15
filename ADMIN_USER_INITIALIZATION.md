# Admin User Initialization

## 🎯 Overview

Automatically creates an admin user on application startup if one doesn't exist.

## 🔧 Configuration

```properties
# application.properties
app.admin.enabled=true
app.admin.username=admin
app.admin.email=admin@example.com
```

## 🚀 Usage

1. **Default Admin**: User is created automatically on first run
2. **Credentials**: Default password is `admin123` (logged on startup)
3. **Security**: Password is automatically hashed using BCrypt

## ⚠️ Production Notes

- **Change the default password immediately**
- **Use environment variables** for sensitive data
- **Disable after first run** if not needed: `app.admin.enabled=false`

## 🛠️ Customization

Override default credentials:
```properties
app.admin.username=customAdmin
app.admin.email=custom@example.com
```

## 📋 How It Works

1. Application starts → AdminUserInitializer runs
2. Checks if admin user exists in database
3. Creates admin user if missing
4. Sets admin flag and default password
5. Logs creation details

## ✅ Best Practices

- **Idempotent**: Safe to run multiple times
- **Configurable**: All settings in application.properties
- **Secure**: Passwords are hashed before storage
- **Logged**: Admin creation is logged for audit

**Note**: In production, implement proper password management and consider using a more secure initialization method.