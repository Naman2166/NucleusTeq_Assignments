package com.naman.assignment.controller;

import com.naman.assignment.service.MessageService;
import org.springframework.web.bind.annotation.*;

// this class handles API related to message formatting
@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    //constructor injection
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // API to get dynamically formatted message
    // example :- /message?type=SHORT
    @GetMapping
    public String getMessage(@RequestParam String type) {       //it reads values from query parameters
        return messageService.getMessage(type);
    }
}