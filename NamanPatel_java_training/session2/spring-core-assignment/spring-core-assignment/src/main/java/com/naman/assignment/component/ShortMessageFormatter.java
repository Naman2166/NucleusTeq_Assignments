package com.naman.assignment.component;

import org.springframework.stereotype.Component;

// this class generates short message
// it is registered as a spring bean with name 'SHORT'
@Component("SHORT")
public class ShortMessageFormatter implements MessageFormatter {

    @Override
    public String formatMessage() {
        return "Short message sent";
    }
}

