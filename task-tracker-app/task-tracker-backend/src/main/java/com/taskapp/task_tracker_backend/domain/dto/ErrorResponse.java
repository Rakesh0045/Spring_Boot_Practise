package com.taskapp.task_tracker_backend.domain.dto;

public record ErrorResponse(
        int status,
        String message,
        String details
) {
}
