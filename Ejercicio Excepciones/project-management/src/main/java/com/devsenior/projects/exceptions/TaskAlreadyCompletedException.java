package com.devsenior.projects.exceptions;

public class TaskAlreadyCompletedException extends Exception {
    public TaskAlreadyCompletedException(String message) {
        super(message);
    }
}
