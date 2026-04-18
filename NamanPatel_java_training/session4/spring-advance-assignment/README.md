# Todo App (Spring Boot Advance Assignment)

A Spring Boot REST API for managing Todo tasks.  
It demonstrates concepts such as layered architecture, DTO separation, validation, and global exception handling.

The application follows a structured layered architecture:

#### Client → Controller → Service → Repository → DB

---

## Technologies Used
- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven

---

## Project Structure
````
com.naman.todo
│
├── controller
│ └── TodoController.java            # Handles API requests
│
├── service
│ └── TodoService.java               # Contains business logic
│
├── repository
│ └── TodoRepository.java            # Handles database operations
│
├── entity
│ └── Todo.java                      # Todo entity class
│
├── dto
│ ├── TodoRequestDTO.java            # Request DTO
│ └── TodoResponseDTO.java           # Response DTO
│
├── exception
│ ├── TodoNotFoundException.java     # Custom exception
│ ├── InvalidStatusException.java    # Custom exception
│ └── GlobalExceptionHandler.java    # Global exception handler
│
├── enums
│  └── TodoStatus.java                # Enum for status
│
└──  SpringAdvanceAssignmentApplication.java

````

---

## Features
- Create, update, delete, and fetch todos
- DTO separation (Request & Response)
- Input validation using `@Valid`
- Global exception handling
- Status transition validation (PENDING ↔ COMPLETED)
- Clean and scalable architecture

---

## API Endpoints

#### Base URL: `http://localhost:8080/todos`

---

### 1. Create Todo
**POST** `/todos`

#### Request Body :
```
{
  "title": "Learn Spring Boot",
  "description": "Practice DTO and exception handling",
  "Status": "PENDING"
}
```

#### Request Body :
```
{
"id": 1,
"title": "Learn Spring Boot",
"description": "Practice DTO and exception handling",
"status": "PENDING",
"createdAt": "2026-04-18T21:30:00"
}
```

### 2. Get All Todo
**GET** `/todos`

#### Request Body :
```
[
  {
    "id": 1,
    "title": "Learn Spring Boot",
    "description": "Practice DTO",
    "status": "PENDING",
    "createdAt": "2026-04-18T21:30:00"
  },
]
```

### 3. Get Todo by ID
**GET** `/todos/{id}`  

#### Example : `GET /todos/1`

#### Request Body :
```
{
  "id": 1,
  "title": "Learn Spring Boot",
  "description": "Practice DTO",
  "status": "PENDING",
  "createdAt": "2026-04-18T21:30:00"
}
```

### 4. Update Todo
**PUT** `/todos/{id}`  

#### Example : `PUT /todos/1`

#### Request Body :
```
{
  "title": "Updated Title",
  "description": "Updated Description",
  "status": "COMPLETED"
}
```
#### Response:
```
{
"id": 1,
"title": "Learn Spring Boot",
"description": "Practice DTO and exception handling",
"status": "PENDING",
"createdAt": "2026-04-18T21:30:00"
}
```

### 5. Update Todo
**PUT** `/todos/{id}`

#### Example : `PUT /todos/1`

#### Request Body :
```
{
  "title": "Updated Title",
  "description": "Updated Description",
  "status": "COMPLETED"
}
```
#### Response Body:
```
{
"id": 1,
"title": "Updated Title",
"description": "Updated Description",
"status": "COMPLETED",
"createdAt": "2026-04-18T21:30:00"
}
```

---

### 6. Delete Todo
**DELETE** `/todos/{id}`

#### Example : `DELETE /todos/1`

#### Response Body:
```
Todo deleted successfully
```

---

##  Validation Rules

| Field       | Rule                           |
|------------|--------------------------------|
| title       | Required, minimum 3 characters |
| description | Optional                       |
| status      | Must be PENDING or COMPLETED   |

---

##  Key Concepts Implemented
- DTO Pattern (Request vs Response)
- Layered Architecture (Controller → Service → Repository)
- Exception Handling using `@RestControllerAdvice`
- Input Validation
- Enum-based status management

---

##  How to Run

1. Clone the repository
 ````
   git clone <your-repo-url>
````
2. Navigate to project folder
 ````
   cd spring-advance-assignment
````
3. Run the application
 ````
   mvn spring-boot:run
````
4. Access API
 ````
   http://localhost:8080/todos
````

---

## Author  
Naman Patel

---

## Notes  
- Uses H2 in-memory database
- Data resets on application restart