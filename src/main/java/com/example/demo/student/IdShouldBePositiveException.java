package com.example.demo.student;

public class IdShouldBePositiveException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Student ID must be a positive number";
    }
}
