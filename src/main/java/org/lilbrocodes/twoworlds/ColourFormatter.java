package org.lilbrocodes.twoworlds;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ColourFormatter extends Formatter {
    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String CUSTOM_COLOR = "\u001B[38;2;125;18;255m";  // RGB for #7d12ff

    @Override
    public String format(LogRecord record) {
        String color = RESET;
        String message = formatMessage(record);

        // Check if the message matches the specific text
        if (message.contains("░")) {
            color = CUSTOM_COLOR;
        }

        return color + message + RESET + "\n";
    }
}
