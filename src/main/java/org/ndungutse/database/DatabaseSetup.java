package org.ndungutse.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseSetup {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSetup.class);
    private static final String URL = "jdbc:postgresql://localhost:5432/hms";
    private static final String USER = "postgres";
    private static final String PASSWORD = "eric";

    // Method to execute SQL script for database setup
    public static void executeSqlScript(Path scriptFilePath) throws SQLException, IOException {
        logger.info("Attempting to connect to the database at {}", URL);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            logger.info("Connection established successfully to database: {}", URL);

            // Read the SQL script from file
            logger.info("Reading SQL script from file: {}", scriptFilePath);
            StringBuilder scriptContent = new StringBuilder();
            try (BufferedReader br = Files.newBufferedReader(scriptFilePath)) {
                String line;
                while ((line = br.readLine()) != null) {
                    scriptContent.append(line).append("\n");
                }
            }

            // Create a Statement to execute the script
            logger.info("Executing SQL script...");
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(scriptContent.toString());
                logger.info("Database schema created successfully.");
            }

        }
    }

}
