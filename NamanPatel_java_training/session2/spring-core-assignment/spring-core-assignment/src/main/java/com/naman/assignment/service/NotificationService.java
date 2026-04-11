package com.naman.assignment.service;

import com.naman.assignment.component.NotificationComponent;
import org.springframework.stereotype.Service;


// This class handles business logic for notification
@Service
public class NotificationService {

    private final NotificationComponent notificationComponent;

    public NotificationService(NotificationComponent notificationComponent) {
        this.notificationComponent = notificationComponent;
    }

    // method to trigger notification
    public String triggerNotification() {
        return notificationComponent.sendNotification();
    }
}