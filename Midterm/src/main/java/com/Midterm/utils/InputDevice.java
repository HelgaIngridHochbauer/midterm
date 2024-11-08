package com.Midterm.utils;

import com.Midterm.model.CustomException.InvalidNumberException;
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
        while (!scanner.hasNextInt()) {
            scanner.next(); // Discard invalid input
            throw new InvalidNumberException("Invalid input. Please enter an integer:");
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character left by nextInt()
        return value;
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