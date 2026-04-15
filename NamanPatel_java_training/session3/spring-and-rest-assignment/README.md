# Spring And Rest Assignment (Session 3)

A Spring Boot REST API for managing users using an in-memory data store.  
It demonstrates concepts such as Inversion of Control (IoC), constructor-based Dependency Injection, layered architecture, and RESTful API design.

The application follows a structured layered architecture:

#### Controller → Service → Repository

---

## 🛠️ Technologies Used
- Java 17
- Spring Boot
- Maven
- REST APIs

---

## 📂 Project Structure
```
com.naman.assignment
│
├── controller
│   └── UserController.java            # Handles API requests
│
├── service
│   └── UserService.java               # Contains business logic
│
├── repository
│   └── UserRepository.java            # Manages in-memory data
│
├── entity
│   └── User.java                      # User Entity class
│
├── exception
│   ├── BadRequestException.java       # Custom exception
│   └── GlobalExceptionHandler.java    # Handles exceptions globally
│ 
└── SpringAndRestAssignmentApplication.java
```

---

## ⚙️ Features
- Search users with optional filters
- Submit new user (with validation)
- Delete user with confirmation
- Global exception handling

---

##  API Endpoints

### ✅ 1. Search Users
**GET** `/users/search`

#### Query Parameters :
- `name` (String)
- `age` (Integer)
- `role` (String)

#### Examples:
```
GET /users/search
GET /users/search?name=Naman
GET /users/search?age=30
GET /users/search?role=USER
GET /users/search?age=30&role=user
```

---

### ✅ 2. Submit User
**POST** `/users/submit`

#### Request Body:
```json
{
  "name": "Naman",
  "age": 21,
  "role": "USER"
}
```

#### Responses:
- **201 Created** → User submitted successfully
- **400 Bad Request** → Invalid input

---

### ✅ 3. Delete User
**DELETE** `/users/{id}`

#### Parameters:
- `id` → User ID
- `confirm` → true/false

#### Examples:
```
DELETE /users/1
DELETE /users/1/confirm=true
DELETE /users/1/confirm=false
```

#### Behavior:
- `confirm=false` → "Confirmation required"
- `confirm=true` → User deleted

---

## 🔒 Validation Rules

| Field | Rule              |
|------|-------------------|
| name | Must not be empty |
| age  | Must not be null  |
| role | Must not be empty |

---

## 📈 Key Concepts Implemented
- Constructor-based Dependency Injection
- Inversion of Control(IOC) - Spring manages object creation
- Layered Architecture (Controller → Service → Repository)
- Manual Validation in Service Layer
- Exception Handling using `@RestControllerAdvice`
- In-memory data handling (no database used)

---

## 🧑‍💻 Author
### Naman Patel

---

## 📌 Notes
- No database is used (dummy data only)
- Data will reset on application restart
