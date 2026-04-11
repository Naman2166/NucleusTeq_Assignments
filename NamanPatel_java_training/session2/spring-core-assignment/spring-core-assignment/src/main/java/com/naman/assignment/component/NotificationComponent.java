package com.naman.assignment.component;

import org.springframework.stereotype.Component;

// this class is used to create notification message
@Component
public class NotificationComponent {

    public String sendNotification() {
        return "Notification sent Successfully";
    }
}