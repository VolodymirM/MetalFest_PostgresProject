package com.databases;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;

import com.databases.database.Connecting;
import com.databases.tables.Bands;

public class Main {
    private static Connection connection = null;
    private static org.jline.reader.LineReader reader;
    public static void main(String[] args) throws SQLException {
        
        reader = LineReaderBuilder.builder().build();

        try {
            connection = new Connecting().getConnection();
        } catch (EndOfFileException | UserInterruptException e) {
            System.err.println("Error establishing connection: " + e.getMessage());
        }

        if (connection == null)
            System.exit(1);

        connection.setAutoCommit(false);

        String input;
        
        clearConsole();
        System.out.println("SSH Tunnel established and connected to the database.\n");
        reader.readLine("Press Enter to continue...");
        
        while (true) { 
            printMenu();

            input = reader.readLine("Choose an option: ");
            switch (input) {
                case "1" -> {
                    // Call method to display registered bands
                    Bands bands = new Bands();
                    bands.start();
                }
                case "2" -> {
                    clearConsole();
                    System.out.println("Stages' information:");
                    reader.readLine("Press Enter to continue...");
                    // Call method to display stages' information
                }
                case "3" -> {
                    clearConsole();
                    System.exit(0);
                }
                default -> {
                    clearConsole();
                }
            }
        }
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error clearing console: " + e.getMessage());
        }
    }

    public static void printMenu() {
        clearConsole();
        System.out.println("1. Registred Bands");
        System.out.println("2. Stages' information");
        System.out.println("3. Exit\n");
    }

    public static Connection getConnection() {
        return connection;
    }

    public static org.jline.reader.LineReader getReader() {
        return reader;
    }
}