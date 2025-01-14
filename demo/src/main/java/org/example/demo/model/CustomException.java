package org.example.demo.model;

public class CustomException {

    public static class InvalidNumberException extends Exception {
        public InvalidNumberException(String message) {
            super(message);
        }

    }
    // Custom exception for post-related errors
    public static class PostCreationException extends Exception {
        public PostCreationException(String message) {
            super(message);
        }
    }


    // You can define more nested exception classes here if needed
}
