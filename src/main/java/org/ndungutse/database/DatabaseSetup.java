package org.ndungutse.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    private static final String URL = "jdbc:postgresql://localhost:5432/hms";
    private static final String USER = "postgres";
    private static final String PASSWORD = "eric";

    // Method to execute SQL script for database setup
    public static void executeSqlScript(String scriptFilePath) throws SQLException, IOException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Read the SQL script from file
            StringBuilder scriptContent = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(scriptFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    scriptContent.append(line).append("\n");
                }
            }

            // Create a Statement to execute the script
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(scriptContent.toString());
                System.out.println("Database schema created successfully.");
            }
        }
    }

    // Method to establish database connection (optional)
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
