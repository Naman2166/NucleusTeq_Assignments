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

}
