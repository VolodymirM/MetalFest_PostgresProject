package com.databases.tables;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import com.databases.Main;

public class Bands {
    private Connection connection;
    private Statement statement;
    private ResultSet tableSearchResult;
    private ResultSet bandSearchResult;
    private ResultSet membersSearchResult;
    private final String viewBands;

    public Bands() {
        this.connection = Main.getConnection();
        try {
            this.statement = connection.createStatement();
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Error creating SQL statement: " + e.getMessage(), e);
        }
        if (this.connection == null) {
            throw new RuntimeException("Connection to the database is not established.");
        }

        String bandsContent = "";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("requests/select_bands.sql")) {
            if (is == null) {
                throw new RuntimeException("SQL file not found!");
            }
            bandsContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error initializing SQL requests: " + e.getMessage());
        }
        this.viewBands = bandsContent;
    }

    public void start() {
        String input;

        while (true) {
            displayTable();
            displayTableActions();
            
            input = Main.getReader().readLine("\nChoose an option: ");
            switch (input) {
                case "1" -> {
                    displayTable();
                    chooseBand();
                }
                case "2" -> {
                    Main.clearConsole();
                    return;
                }
            }
        }


    }

    private void displayTable() {
        Main.clearConsole();
        System.out.println("Registered Bands:");
        
        try {
            tableSearchResult = statement.executeQuery(viewBands);
            viewTable();
            
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Error executing SQL statement: " + e.getMessage(), e);
        }
    }

    private void displayTableActions() {
        System.out.println("\n1. View band details");
        System.out.println("2. Return to main menu");
    }

    private void chooseBand() {
        Band band;

        String inputBand;

        System.out.print("Enter band ID to view details: ");
        String bandId = Main.getReader().readLine();
        
        while (true) {
            Main.clearConsole();
            
            try {
                bandSearchResult = statement.executeQuery("SELECT * FROM Band WHERE ID = " + bandId);
                
                if (bandSearchResult.next()) {
                    
                    band = new Band(
                            bandSearchResult.getInt("ID"),
                            bandSearchResult.getString("Name"),
                            bandSearchResult.getString("Country"),
                            bandSearchResult.getString("Biography")
                    );

                    bandSearchResult.close();
                    band.display();

                    membersSearchResult = statement.executeQuery("SELECT * FROM Member WHERE Band_id = " + bandId);
                    displayMembers();

                    System.out.println("\n1. Add new member");
                    System.out.println("2. Change member's details");
                    System.out.println("3. Remove member");
                    System.out.println("4. Return");

                    inputBand = Main.getReader().readLine("Choose an option: ");

                    switch (inputBand) {
                        case "1" -> {
                            Main.clearConsole();
                            System.out.print("Enter member's first name: ");
                            String firstName = Main.getReader().readLine();
                            System.out.print("Enter member's last name: ");
                            String lastName = Main.getReader().readLine();
                            System.out.print("Enter member's age: ");
                            int age;
                            try {
                                age = Integer.parseInt(Main.getReader().readLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid age input. Please enter a valid number.");
                                Main.getReader().readLine("Press Enter to continue...");
                                continue;
                            }
                            addMember(band.getId(), firstName, lastName, age);
                            Main.getReader().readLine("Press Enter to continue...");
                        }
                        case "2" -> {
                            Main.clearConsole();
                            band.display();
                            membersSearchResult = statement.executeQuery("SELECT * FROM Member WHERE Band_id = " + bandId);
                            displayMembers();
                            System.out.print("\nEnter member ID to change details: ");
                            String memberId = Main.getReader().readLine();
                            System.out.print("Enter new first name: ");
                            String newFirstName = Main.getReader().readLine();
                            System.out.print("Enter new last name: ");
                            String newLastName = Main.getReader().readLine();
                            System.out.print("Enter new age: ");
                            int newAge;
                            try {
                                newAge = Integer.parseInt(Main.getReader().readLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid age input. Please enter a valid number.");
                                Main.getReader().readLine("Press Enter to continue...");
                                continue;
                            }
                            updateMember(Integer.parseInt(memberId), newFirstName, newLastName, newAge);
                            Main.getReader().readLine("Press Enter to continue...");
                        }
                        case "3" -> {
                            Main.clearConsole();
                            band.display();
                            membersSearchResult = statement.executeQuery("SELECT * FROM Member WHERE Band_id = " + bandId);
                            displayMembers();
                            System.out.print("\nEnter member ID to remove: ");
                            String memberId = Main.getReader().readLine();
                            removeMember(Integer.parseInt(memberId));
                            Main.getReader().readLine("Press Enter to continue...");
                        }
                        case "4" -> {
                            Main.clearConsole();
                            return;
                        }
                    }

                    Main.clearConsole();
                } else {
                    System.out.println("No band found with ID: " + bandId);
                    Main.getReader().readLine("Press Enter to try again...");
                    Main.clearConsole();
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving band details: " + e.getMessage());
            }
        }
    }

    private void displayMembers() {
        System.out.printf("%s %s %s %s%n", 
                            "ID", "FirstName", "LastName", "Age");

        try {
            while (membersSearchResult.next()) {
                System.out.printf("%d. %s %s, %d%n",
                        membersSearchResult.getInt("ID"),
                        membersSearchResult.getString("FirstName"),
                        membersSearchResult.getString("LastName"),
                        membersSearchResult.getInt("Age"));
            }
        } catch (SQLException e) {
            System.err.println("Error displaying members: " + e.getMessage());
        }
    }

    private void addMember(int bandId, String firstName, String lastName, int age) {
        String insertMemberSQL = "INSERT INTO Member (Band_id, FirstName, LastName, Age) VALUES (?, ?, ?, ?)";
        try {
            var preparedStatement = connection.prepareStatement(insertMemberSQL);
            preparedStatement.setInt(1, bandId);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setInt(4, age);
            preparedStatement.executeUpdate();
            System.out.println("Member added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding member: " + e.getMessage());
        }
    }

    private void updateMember(int memberId, String newFirstName, String newLastName, int newAge) {
        String updateMemberSQL = "UPDATE Member SET FirstName = ?, LastName = ?, Age = ? WHERE ID = ?";
        try {
            var preparedStatement = connection.prepareStatement(updateMemberSQL);
            preparedStatement.setString(1, newFirstName);
            preparedStatement.setString(2, newLastName);
            preparedStatement.setInt(3, newAge);
            preparedStatement.setInt(4, memberId);
            preparedStatement.executeUpdate();
            System.out.println("Member updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
        }
    }

    private void removeMember(int memberId) {
        String deleteMemberSQL = "DELETE FROM Member WHERE ID = ?";
        try {
            var preparedStatement = connection.prepareStatement(deleteMemberSQL);
            preparedStatement.setInt(1, memberId);
            preparedStatement.executeUpdate();
            System.out.println("Member removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error removing member: " + e.getMessage());
        }
    }

    public String getViewBands() {
        return viewBands;
    }

    private void viewTable() throws SQLException {
        System.out.printf("%-5s %-20s %-15s %-20s %-12s %-10s %-10s%n",
                "ID", "Band Name", "Country", "Stage", "Date", "Start", "End");
        System.out.println("------------------------------------------------------------------------------------------");

        while (tableSearchResult.next()) {
            int bandId = tableSearchResult.getInt("BandID");
            String bandName = tableSearchResult.getString("BandName");
            String bandCountry = tableSearchResult.getString("BandCountry");
            String stageName = tableSearchResult.getString("StageName");
            Date performanceDate = tableSearchResult.getDate("PerformanceDate");
            Time start = tableSearchResult.getTime("PerformanceStart");
            Time end = tableSearchResult.getTime("PerformanceEnd");

            System.out.printf("%-5d %-20s %-15s %-20s %-12s %-10s %-10s%n",
                    bandId, bandName, bandCountry, stageName, performanceDate, start, end);
        }
    }
}
