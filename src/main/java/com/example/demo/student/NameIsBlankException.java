package com.example.demo.student;

public class NameIsBlankException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Name should not be blank";
    }
}
