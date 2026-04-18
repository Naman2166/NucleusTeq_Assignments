package com.naman.todo.service;

import com.naman.todo.dto.TodoDTO;
import com.naman.todo.entity.Todo;
import com.naman.todo.enums.TodoStatus;
import com.naman.todo.exception.InvalidStatusException;
import com.naman.todo.exception.TodoNotFoundException;
import com.naman.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


// this class contains business logic for Todo operations
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    // Constructor Injection
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }



    // method to create new todo
    public TodoDTO createTodo(TodoDTO dto) {

        Todo todo = new Todo();

        //mapping Dto to entity
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());

        // applying validation
        if (dto.getStatus() == null) {              //default status will be pending
            todo.setStatus(TodoStatus.PENDING);
        }
        else {
            todo.setStatus(dto.getStatus());
        }

        Todo saved = todoRepository.save(todo);      //save to repo

        return mapToDTO(saved);                  //return dto after mapping saved entity to dto
    }



    // method to get all todos
    public List<TodoDTO> getAllTodos() {
        return todoRepository.findAll()
                .stream()
                .map(todo -> mapToDTO(todo))        //mapping each todo into Dto
                .collect(Collectors.toList());            //return list
    }



    // method to get todo by id
    public TodoDTO getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo not found with id: " + id ));      //throw exception if todo not found

        return mapToDTO(todo);
    }



    // method to update todo
    public TodoDTO updateTodo(Long id, TodoDTO dto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo not found with id: " + id ));

        //mapping dto to entity
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());

        // Status update with validation
        if (dto.getStatus() != null) {
            TodoStatus currentStatus = todo.getStatus();     //stored status
            TodoStatus newStatus = dto.getStatus();          // new status from client

            // allow only valid transitions
            if ((currentStatus == TodoStatus.PENDING && newStatus == TodoStatus.COMPLETED) ||
               (currentStatus == TodoStatus.COMPLETED && newStatus == TodoStatus.PENDING)) {
                todo.setStatus(newStatus);
            }
            else if (currentStatus != newStatus) {
                throw new InvalidStatusException("Invalid status transition");
            }
        }

        Todo updated = todoRepository.save(todo);

        return mapToDTO(updated);
    }



    // method to delete todo
    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo not found with id: " + id ));

        todoRepository.delete(todo);
    }



    // mapping Entity to DTO
    private TodoDTO mapToDTO(Todo todo) {
        return new TodoDTO(
                todo.getTitle(),
                todo.getDescription(),
                todo.getStatus()
        );
    }
}