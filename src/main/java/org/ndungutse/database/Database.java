package org.ndungutse.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.ndungutse.Main;
import org.ndungutse.exceptions.ResourceNotFoundRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private static final String URL = "jdbc:postgresql://localhost:5432/hms";
    private static final String USER = "postgres";
    private static final String PASSWORD = "eric";

    // Method to execute SQL script for database setup
    public static void initializeDatabase() throws SQLException, IOException, URISyntaxException {

        URL path = Main.class.getClassLoader().getResource("create-schema.sql");
        if (path == null) {
            throw new ResourceNotFoundRuntimeException("Required resource 'create-schemas.sql' not found");
        }

        Path scriptFilePath = Paths.get(path.toURI());

        try (Connection connection = Database.getConnection();) {
            logger.info("Connection established successfully to database: {}", URL);
            logger.info("Starting database setup using script at: {}", scriptFilePath);

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

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
