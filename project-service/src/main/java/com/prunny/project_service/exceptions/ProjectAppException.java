package com.prunny.project_service.exceptions;

public abstract class ProjectAppException extends RuntimeException {
    public ProjectAppException(String message) {
        super(message);
    }
}
