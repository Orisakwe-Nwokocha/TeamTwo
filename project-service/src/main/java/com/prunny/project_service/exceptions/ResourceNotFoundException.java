package com.prunny.project_service.exceptions;

public class ResourceNotFoundException extends ProjectAppException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
