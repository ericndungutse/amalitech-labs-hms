package org.ndungutse;

import org.ndungutse.database.Database;
import org.ndungutse.exceptions.ResourceNotFoundRuntimeException;
import org.ndungutse.model.Patient;
import org.ndungutse.repository.PatientDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {
        try {
            setupDatabase();
        } catch (SQLException | IOException | URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }

        // List<Patient> patients = PatientDAO.getAllPatients();

        // for (Patient patient : patients) {
        // System.out.println(patient);
        // }

    }

    public static void setupDatabase() throws SQLException, IOException, URISyntaxException {
        URL path = Main.class.getClassLoader().getResource("create-schema.sql");
        if (path == null) {
            throw new ResourceNotFoundRuntimeException("Required resource 'create-schemas.sql' not found");
        }
        Path file = Paths.get(path.toURI());
        logger.info("Starting database setup using script at: {}", file);
        Database.initializeDatabase(file);
        logger.info("Database setup completed successfully.");
    }
}
