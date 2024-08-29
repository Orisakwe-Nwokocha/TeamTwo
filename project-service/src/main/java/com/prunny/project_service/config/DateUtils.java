package com.prunny.project_service.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd-MMMM-yyyy 'at' h:mm a");
        return dateTime.format(formatter)
                .replace("AM", "am")
                .replace("PM", "pm");
    }

}
