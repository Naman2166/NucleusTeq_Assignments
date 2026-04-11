package com.naman.assignment.controller;

import com.naman.assignment.service.NotificationService;
import org.springframework.web.bind.annotation.*;


// This class handles all REST API requests related to Notification
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    //constructor injection
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // API to trigger notification
    @GetMapping
    public String sendNotification() {
        return notificationService.triggerNotification();
    }
}