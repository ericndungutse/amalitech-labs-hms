package org.ndungutse;

import org.ndungutse.model.Patient;
import org.ndungutse.repository.PatientDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PatientConsoleApp {
    private static final Logger logger = LoggerFactory.getLogger(PatientConsoleApp.class);
    private final Scanner scanner = new Scanner(System.in);

    // Start Application
    public void run() {
        logger.info("Starting Patient Console Application...");
        while (true) {
            showMenu();
            int choice = getUserChoice();
            try {
                switch (choice) {
                    case 1 -> viewAllPatients();
                    case 5 -> {
                        logger.info("User exited the application.");
                        System.out.println("Exiting application. Goodbye!");
                        return;
                    }
                    default -> {
                        System.out.println("Invalid choice. Please try again.");
                        logger.warn("Invalid menu choice entered: {}", choice);
                    }
                }
            } catch (SQLException e) {
                logger.error("Database error occurred: {}", e.getMessage(), e);
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private void showMenu() {
        System.out.println("\n====== Patient Management System ======");
        System.out.println("1. View All Patients");
        System.out.println("2. Add Patient");
        System.out.println("3. Update Patient");
        System.out.println("4. Delete Patient");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            logger.warn("Failed to parse user input to integer.", e);
            return -1;
        }
    }

    private void viewAllPatients() throws SQLException {
        logger.info("User selected: View All Patients");
        List<Patient> patients = PatientDAO.getAllPatients();
        if (patients.isEmpty()) {
            logger.info("No patients found in the database.");
            System.out.println("No patients found.");
        } else {
            logger.info("Displaying {} patients.", patients.size());
            patients.forEach(System.out::println);
        }
    }
}
