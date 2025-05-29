package com.databases;

import java.sql.SQLException;

import com.databases.connection.Connecting;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connecting connection = null;
        try {
            connection = new Connecting();
            System.out.println("SSH Tunnel established and connected to the database.\n");
        } catch (Exception e) {
            System.err.println("Error establishing connection: " + e.getMessage());
        }

        if (connection == null)
            return;

        while (true) { 
            
        }
    }
}