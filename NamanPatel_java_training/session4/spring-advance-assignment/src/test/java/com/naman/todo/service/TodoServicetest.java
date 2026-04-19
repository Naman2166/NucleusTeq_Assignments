package com.naman.todo.service;

import com.naman.todo.dto.TodoRequestDTO;
import com.naman.todo.dto.TodoResponseDTO;
import com.naman.todo.entity.Todo;
import com.naman.todo.enums.TodoStatus;
import com.naman.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

//unit test class for todoService
class TodoServiceTest {

    @Mock                                       //creates a fake dependency or object of TodoRepository for testing
    private TodoRepository todoRepository;

    @Mock
    private NotificationServiceClient notificationServiceClient;

    @InjectMocks                                //creates original dependency or object of TodoService but injects mock dependencies inside it
    private TodoService todoService;

    //constructor
    //it initializes all annotation belongs to mockito
    public TodoServiceTest(){
        MockitoAnnotations.openMocks(this);
    }



    // Test for createTodo method
    @Test
    void testCreateTodo() {

        // creates fake input
        TodoRequestDTO dto = new TodoRequestDTO("Test Title", "Test Description", TodoStatus.PENDING);

        //creates fake output
        Todo savedTodo = new Todo();
        savedTodo.setId(1L);
        savedTodo.setTitle("Test Title");
        savedTodo.setDescription("Test Description");
        savedTodo.setStatus(TodoStatus.PENDING);

        // set behaviour for fake repository save operation by providing both input and output
        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        // Call createTodo method from service
        TodoResponseDTO result = todoService.createTodo(dto);

        // checking the results
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());

        // Verify repository save method and notification sent methods are called once only
        verify(todoRepository, times(1)).save(any(Todo.class));
        verify(notificationServiceClient, times(1)).sendNotification(anyString());
    }



    // Test for getTodoById method
    @Test
    void testGetTodoById() {

        // create fake input id
        Long id = 1L;

        // creates fake output object
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Title");
        todo.setDescription("Test Description");
        todo.setStatus(TodoStatus.PENDING);

        // set behaviour for repository findById operation
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        // call service class method
        TodoResponseDTO result = todoService.getTodoById(id);

        // checking and verifying the results
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(todoRepository, times(1)).findById(id);
    }



    // Test for getAllTodos method
    @Test
    void testGetAllTodos() {

        // create fake output todo list
        Todo todo1 = new Todo();
        todo1.setId(1L);
        todo1.setTitle("Task 1");
        todo1.setDescription("Description 1");
        todo1.setStatus(TodoStatus.PENDING);

        Todo todo2 = new Todo();
        todo2.setId(2L);
        todo2.setTitle("Task 2");
        todo2.setDescription("Description 2");
        todo2.setStatus(TodoStatus.COMPLETED);

        List<Todo> todoList = List.of(todo1, todo2);

        // set behaviour
        when(todoRepository.findAll()).thenReturn(todoList);

        // call getAllTodos method
        List<TodoResponseDTO> result = todoService.getAllTodos();

        // verify result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        verify(todoRepository, times(1)).findAll();
    }



    // Test for updateTodo method
    @Test
    void testUpdateTodo() {

        // existing todo in database
        Long id = 1L;

        Todo existingTodo = new Todo();
        existingTodo.setId(1L);
        existingTodo.setTitle("Old Title");
        existingTodo.setDescription("Old Description");
        existingTodo.setStatus(TodoStatus.PENDING);

        // new data to update
        TodoRequestDTO dto = new TodoRequestDTO("New Title", "New Description", TodoStatus.COMPLETED);

        // set repository behavior
        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(existingTodo);

        // call update method
        var result = todoService.updateTodo(id, dto);

        // verify result
        assertNotNull(result);
        assertEquals("New Title", result.getTitle());

        verify(todoRepository, times(1)).findById(id);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }



    // Test for deleteTodos method
    @Test
    void testDeleteTodo() {

        // fake input data
        Long id = 1L;

        Todo todo = new Todo();
        todo.setId(id);

        // set behaviour
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        // call delete method from service class
        todoService.deleteTodo(id);

        // verify result
        verify(todoRepository, times(1)).delete(todo);
    }


}
