package com.prunny.Task_Service.exception;

public class NotLeaderOfProjectException extends Exception {

    public NotLeaderOfProjectException(String message) {
        super(message);
    }
}
