
# Spring Boot Authentication System (JWT + Email Verification + Password Reset)

A functional authentication backend built with Spring Boot.  
It includes user signup, email verification, JWT-based login, and a secure forgot/reset password flow using hashed one-time tokens.

---

## Features

### 1. User Registration
- Creates a new user with a hashed password  
- Generates a verification code  
- Sends verification email  
- User must verify before logging in  

### 2. Email Verification
- User submits email + verification code  
- Verifies the code and activates the account  

### 3. Secure Login (JWT)
- Validates credentials  
- Only verified users can log in  
- Issues JWT token with expiry time  
- Token is used to access protected routes  

### 4. Forgot Password
- Accepts email (silent if email doesn’t exist)  
- Marks old tokens as used  
- Generates a new secure reset token  
- Stores only the **hashed** token in the database  
- Sends the raw token to the user's email  

### 5. Reset Password
- User submits reset token + new password  
- Token is verified (expiry, used, matching hash)  
- Saves new encoded password  
- Marks token as used  
- Invalidates other unused tokens for safety  

---

## Tech Stack

- Spring Boot 3  
- Spring Security  
- JWT (JJWT)  
- JavaMailSender  
- PostgreSQL  
- JPA / Hibernate  

---

## Project Structure

```

src/main/java
└── org.example.springjwt
├── controller        // Auth endpoints
├── service           // Business logic
├── entity            // User + PasswordResetToken
├── repository        // JPA repositories
├── config            // Security configuration + filters
└── utils             // Token utilities

```

---

## Authentication Flow

### Signup → Verify → Login
```

Client → /auth/signup
↓  (email sent)
Client → /auth/verify
↓
Client → /auth/login
↓
JWT returned

```

### Forgot → Reset Password
```

Client → /auth/forgot-password
↓  (reset token emailed)
Client → /auth/reset-password
↓
Password updated

```

---

## Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/signup` | Register a new user |
| POST | `/auth/verify` | Verify account using OTP |
| POST | `/auth/login` | Login and receive JWT |
| POST | `/auth/resend` | Resend verification code |
| POST | `/auth/forgot-password` | Send password reset instructions |
| POST | `/auth/reset-password` | Reset password using token |

---

## Security Highlights

- Password reset tokens are hashed before storing  
- Silent email checks prevent user enumeration  
- One-time use tokens  
- Expiry enforcement  
- Transactional operations  
- No raw secrets logged or stored  

---

## Status

This is a **functional prototype (MVP)** created for learning, testing, and understanding Spring Security fundamentals.  
It follows secure patterns but is not production-hardened.

---

## Future Improvements

- Proper Exception Handling
- Rate limiting  
- JWT blacklist or token versioning  
- Multi-device session tracking  
- Activity/audit logs  
- Strong password validation  
- Frontend integration  

---

