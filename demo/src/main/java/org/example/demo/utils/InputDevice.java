package org.example.demo.utils;

import org.example.demo.model.CustomException.InvalidNumberException;

import java.util.Scanner;

public class InputDevice {
    private Scanner scanner;

    public InputDevice() {
        this.scanner = new Scanner(System.in);
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public int readInt() throws InvalidNumberException {
        while (true) {
            if (!scanner.hasNextInt()) {
                scanner.next(); // Discard the invalid input
                throw new InvalidNumberException("Invalid input. Please enter an integer:");
            } else {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character left by nextInt()
                return value;
            }
        }
    }

    public String[] getArguments() {
        System.out.println("Enter command and arguments (space-separated):");
        String input = scanner.nextLine();
        return input.split("\\s+");
    }

    public void close() {
        scanner.close();
    }
}