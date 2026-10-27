package com.example.checkscam.component;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component // Make it a Spring component
public class LocalizationUtils {
    // Basic placeholder, replace with your actual localization logic (e.g., using MessageSource)
    public String getLocalizedMessage(String key, Object... args) {
        String message = key; // Default to key if not found
        // Example: if (key.equals(MessageKeys.REPORT_NOT_FOUND)) message = "Report not found";
        if (args != null && args.length > 0) {
            // Simple formatting, replace with MessageFormat if needed
            for (int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}", String.valueOf(args[i]));
            }
        }
        // For the provided message format in InvalidParamException
        // "Cannot add %d image(s) to product %d. It already has %d image(s). Maximum allowed is %d. (Key: %s)"
        // This simple replacement won't work. You'd typically use String.format or MessageFormat here,
        // or ensure your InvalidParamException's message is already formatted.
        // For now, we'll assume the message from the exception is pre-formatted or the key itself is sufficient.
        return String.format("Localized: %s with args %s", key, Arrays.toString(args)); // Basic placeholder
    }
}