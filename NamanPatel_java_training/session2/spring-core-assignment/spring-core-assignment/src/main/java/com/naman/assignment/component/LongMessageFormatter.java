package com.naman.assignment.component;

import org.springframework.stereotype.Component;

// this class generates long message
// it is registered as a spring bean with name 'LONG'
@Component("LONG")
public class LongMessageFormatter implements MessageFormatter {

    @Override
    public String formatMessage() {
        return "Long message sent successfully with dynamic formatting";
    }
}

