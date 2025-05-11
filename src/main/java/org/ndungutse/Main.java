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
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {
        // Step 1: Optionally set up DB
        // try {
        // setupDatabase();
        // } catch (SQLException | IOException | URISyntaxException e) {
        // logger.error(e.getMessage(), e);
        // }

        // Step 2: Test patient creation (optional, can uncomment if needed)
        // logger.info("Create Patient Object.");
        // Patient newPatient = new Patient(
        // 0,
        // "P001",
        // "Ndungutse",
        // "Eric",
        // "Kigali",
        // "0788123456");

        // try {
        // PatientDAO.createPatient(newPatient);
        // logger.info("New patient added successfully.");
        // } catch (SQLException e) {
        // logger.error("Failed to add patient: {}", e.getMessage(), e);
        // }

        // Step 3: Fetch and print all patients before update
        List<Patient> patients = PatientDAO.getAllPatients();
        logger.info("Before update:");
        for (Patient patient : patients) {
            System.out.println(patient);
        }

        // Step 4: Update a patient (change values to match an existing patient number)
        Patient updatedPatient = new Patient(
                10,
                "PN0010",
                "UpdatedSurname",
                "UpdatedFirstName",
                "UpdatedAddress",
                "0788999999");

        try {
            PatientDAO.updatePatient(updatedPatient);
            logger.info("Patient updated successfully.");
        } catch (SQLException e) {
            logger.error("Failed to update patient: {}", e.getMessage(), e);
        }

        // Step 5: Fetch and print all patients after update
        patients = PatientDAO.getAllPatients();
        logger.info("After update:");
        for (Patient patient : patients) {
            System.out.println(patient);
        }

        // Optional Step: Test delete
        // PatientDAO.deletePatient(11); // Adjust ID as needed
        // logger.info("After delete:");
        // patients = PatientDAO.getAllPatients();
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
