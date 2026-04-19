package com.naman.todo.service;

import com.naman.todo.dto.TodoRequestDTO;
import com.naman.todo.dto.TodoResponseDTO;
import com.naman.todo.entity.Todo;
import com.naman.todo.enums.TodoStatus;
import com.naman.todo.exception.InvalidStatusException;
import com.naman.todo.exception.TodoNotFoundException;
import com.naman.todo.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


// this class contains business logic for Todo operations
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    private final NotificationServiceClient notificationServiceClient;

    // Constructor Injection
    public TodoService(TodoRepository todoRepository, NotificationServiceClient notificationServiceClient) {
        this.todoRepository = todoRepository;
        this.notificationServiceClient = notificationServiceClient;
    }

    //Logger for TodoService class
    private static final Logger logger = LoggerFactory.getLogger(TodoService.class);



    // method to create new todo
    public TodoResponseDTO createTodo(TodoRequestDTO dto) {

        //log message before creating todo
        logger.info("Creating todo: {}", dto.getTitle());

        Todo todo = new Todo();

        //mapping Dto to entity
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        todo.setCreatedAt(LocalDateTime.now());        //creation time set automatically

        // applying validation
        if (dto.getStatus() == null) {                //default status will be pending
            todo.setStatus(TodoStatus.PENDING);
        }
        else {
            todo.setStatus(dto.getStatus());
        }

        Todo saved = todoRepository.save(todo);      //save to repo

        //log message after saving todo
        logger.info("Todo created with id: {}", saved.getId());

        //sending notification when new todo is created
        notificationServiceClient.sendNotification("New TODO created with id: " + saved.getId());

        return mapToResponseDTO(saved);                  //return dto after mapping saved entity to dto
    }



    // method to get all todos
    public List<TodoResponseDTO> getAllTodos() {

        logger.info("Fetching all todos");              //log message for fetching all todos

        return todoRepository.findAll()
                .stream()
                .map(todo -> mapToResponseDTO(todo))    //mapping each todo into Dto
                .collect(Collectors.toList());                //return list
    }



    // method to get todo by id
    public TodoResponseDTO getTodoById(Long id) {

        logger.info("Fetching todo with id: {}", id);         // log entry for fetching todo by id

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo not found with id: " + id ));      //throw exception if todo not found

        return mapToResponseDTO(todo);
    }



    // method to update todo
    public TodoResponseDTO updateTodo(Long id, TodoRequestDTO dto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo not found with id: " + id ));

        //logging message before updating todo
        logger.info("Updating todo with id: {}", id);

        //mapping dto to entity
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());

        // Status update with validation
        if (dto.getStatus() != null) {
            TodoStatus currentStatus = todo.getStatus();     //stored status
            TodoStatus newStatus = dto.getStatus();          // new status from client

            //if status are same then no transition
            if (currentStatus == newStatus) {
                throw new InvalidStatusException("Status value is already " + currentStatus);
            }

            // allow only valid transitions
            if ((currentStatus == TodoStatus.PENDING && newStatus == TodoStatus.COMPLETED) ||
                    (currentStatus == TodoStatus.COMPLETED && newStatus == TodoStatus.PENDING)) {
                todo.setStatus(newStatus);
            }
            else {
                throw new InvalidStatusException("Invalid status transition");
            }
        }

        Todo updated = todoRepository.save(todo);

        //log message after updating todo
        logger.info("Todo updated with id: {}", updated.getId());

        return mapToResponseDTO(updated);
    }



    // method to delete todo
    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo not found with id: " + id ));

        //log message before deletion
        logger.info("Deleting todo with id: {}", id);

        todoRepository.delete(todo);

        //log message after deletion
        logger.info("todo deleted with id: {}", id);
    }



    // mapping Entity to DTO
    private TodoResponseDTO mapToResponseDTO(Todo todo) {
        return new TodoResponseDTO(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getStatus(),
                todo.getCreatedAt()
        );
    }
}