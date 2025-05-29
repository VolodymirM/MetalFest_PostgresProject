package com.databases.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Connecting {
    String sshUser = "vlmo0142";
    private String sshHost = "uosis.mif.vu.lt";
    private int sshPort = 22;

    private String remoteHost = "pgsql3.mif";
    private int remotePort = 5432;
    private int localPort = 5433;

    private String dbName = "studentu";
    private String dbUser = "vlmo0142";
    private String dbPassword = "Izjah6dxx0";

    private Session session = null;
    private Connection conn = null;

    @SuppressWarnings("ConvertToTryWithResources")
    public Connecting() {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sshUser, sshHost, sshPort);

            session.setPassword("Izjah6dxx0");

            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            session.setPortForwardingL(localPort, remoteHost, remotePort);

            String jdbcUrl = "jdbc:postgresql://localhost:" + localPort + "/" + dbName;
            conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
            
        } catch (JSchException | SQLException e) {
            throw new RuntimeException("Failed to establish SSH tunnel or connect to the database", e);
        }
    }

    public void disconnect() throws SQLException {
        if (session != null && session.isConnected() && conn != null && !conn.isClosed()) {
            conn.close();
            System.out.println("Connection to PostgreSQL closed.");
            session.disconnect();
            System.out.println("SSH Tunnel closed.");
        }
    }
}
