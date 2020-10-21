package app;

import http.AddressEvaluator;
import http.Method;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Core {
    private String studentIdHeaderValue;
    private final Scanner scanner;

    public Core() {
        scanner = new Scanner(System.in);
        System.out.println("Type <help> for commands set.");
    }

    public void start() {
        String input;
        while (true) {
            System.out.print("HTTP CLI>");
            input = scanner.nextLine();
            boolean exit = processInput(input);
            if (exit) break;
        }
    }

    /**
     * @param input taken from user
     * @return exit state of program
     */
    public boolean processInput(String input) {
        if (input.equals("exit"))
            return true;

        switch (input) {
            case "set-student-id-header":
                System.out.print("Enter Student Id:");
                studentIdHeaderValue = scanner.nextLine().trim();
                break;

            case "remove-student-id-header":
                System.out.println("Student Id header removed");
                studentIdHeaderValue = null;
                break;
            case "help":
                printHelpManual();
                break;
            default: // handle http address
                handleHttpAddress(input);
        }

        return false;
    }

    private void handleHttpAddress(String httpAddress) {
        boolean valid = AddressEvaluator.evaluate(httpAddress);
        if (!valid) System.out.println("Http Address is Invalid! try again.");
        else {
            Method method = getHttpMethodFromUser();
            System.out.printf("Sending Request to %s\n" , httpAddress);
            RequestTransmitter.send(httpAddress , method , studentIdHeaderValue);
        }
    }

    private Method getHttpMethodFromUser() {
        System.out.println("Available Methods" + Method.presentAll());
        Method method = null;

        while (method == null) {
            System.out.print("Enter Method: ");
            try {
                method = Method.valueOf(scanner.nextLine().trim());
            } catch (IllegalArgumentException e) {
                System.out.println("You may misspelled or typed a method that isn't available! try again.");
            }
        }

        return method;
    }

    private void printHelpManual() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("help.txt");
        if (stream == null)
            throw new NullPointerException("Help.txt Stream was Null!");
        Scanner scanner = new Scanner(stream);
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) builder.append(scanner.nextLine()).append("\n");
        System.out.print(builder);
    }

}
