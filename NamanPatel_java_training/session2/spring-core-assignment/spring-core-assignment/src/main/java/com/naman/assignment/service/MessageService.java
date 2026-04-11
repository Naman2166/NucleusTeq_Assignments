package com.naman.assignment.service;

import com.naman.assignment.component.MessageFormatter;
import org.springframework.stereotype.Service;

import java.util.Map;

// this class handles message generation based on type
@Service
public class MessageService {

    //map stores bean names and formatter names (eg <SHORT, ShortMessageFormatter>)
    private final Map<String, MessageFormatter> formatters;

    public MessageService(Map<String, MessageFormatter> formatters) {
        this.formatters = formatters;
    }


    //method to return message based on type
    public String getMessage(String type) {

        MessageFormatter formatter = formatters.get(type);

        // return message if no formatter is found for given type
        if (formatter == null) {
            return "Invalid message type";
        }

        return formatter.formatMessage();
    }
}
