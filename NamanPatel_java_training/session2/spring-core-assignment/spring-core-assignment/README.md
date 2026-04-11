# Spring Core Assignment

## Overview
This is a Spring Boot REST API project built using:
- Java 17
- Maven
- Layered Architecture (Controller → Service → Repository)
- In-memory data (no database)

---

## Base URL  `http://localhost:8080`

---

## API Endpoints

### 1. Employee Management

- #### Get All Employees
  **GET** `/employees`  
  Returns list of all employees

- #### Create Employee
  **POST** `/employees`  
  Creates a new employee

**Sample Body:**
```json
{
  "firstName": "Naman",
  "lastName": "Patel",
  "email": "naman@gmail.com",
  "department": "IT"
}
```

- #### Get Employee by ID
  **GET** `/employees/{id}`  
  Returns employee based on ID

- #### Delete Employee by ID
  **DELETE** `/employees/{id}`  
  Deletes employee based on ID

- #### Update Employee by ID
  **PUT** `/employees/{id}`  
  Updates employee based on ID

**Sample Body:**
```json
{
  "firstName": "Naman",
  "lastName": "Patel",
  "email": "naman@gmail.com",
  "department": "IT"
}
```

---

### 2. Notification System

#### Send Notification
**GET** `/notification`  
Returns: "Notification Sent Successfully"

---

### 3. Message Formatter

#### Short Message
**GET** `/message?type=SHORT`  
Returns short message

#### Long Message
**GET** `/message?type=LONG`  
Returns detailed message

---

## Exception Handling
- Global exception handling implemented using `@ControllerAdvice`
- Handles common exceptions such as:
    - Employee not found
    - Invalid input request
    - General exceptions
- Returns meaningful error messages with appropriate HTTP status codes

---

## Validation
- Input validation implemented using `@Valid`
- Ensures required fields are not null or empty
- Includes validations like:
    - Email format validation
    - Mandatory field checks
- Returns structured validation error responses

---

## How to Run
```bash
mvn spring-boot:run
```

---

## Note
- Uses constructor injection
- No database (dummy data used)
- Follows clean layered architecture
- Proper comments added in code

---