package org.ndungutse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.ndungutse.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException, IOException, URISyntaxException {
        try {
            // Initialize Database
            Database.initializeDatabase();
        } catch (SQLException e) {
            logger.error("Database error occurred: {}", e.getMessage());
            logger.debug("Stack trace: ", e);
            return;
        } catch (IOException | URISyntaxException e) {
            logger.error("Failed to read or access SQL script file: {}", e.getMessage());
            logger.debug("Stack trace: ", e);
            return;
        } finally {
            // Start the application
            try {
                logger.info("🚀 Starting Applications... 🚀");
                new PatientConsoleApp().run();
            } catch (Exception e) {
                logger.error("Application error: {}", e.getMessage());
                logger.debug("Stack trace: ", e);
            }
        }

    }
}
