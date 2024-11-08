package com.Midterm.model;

public class CustomException {

    public static class InvalidNumberException extends Exception {
        public InvalidNumberException(String message) {
            super(message);
        }

        public InvalidNumberException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // You can define more nested exception classes here if needed
}