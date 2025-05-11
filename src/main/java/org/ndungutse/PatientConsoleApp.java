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
                    case 2 -> addPatient();
                    case 5 -> {
                        logger.info("User exited the application. 👋");
                        return;
                    }
                    default -> {
                        logger.warn("Invalid menu choice entered: {}", choice);
                    }
                }
            } catch (SQLException e) {
                logger.error("Database error occurred: {}", e.getMessage(), e);
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
        } else {
            // Fixed column widths
            int addressColumnWidth = 80;
            int patientNumberColumnWidth = 15;
            int surnameColumnWidth = 15;
            int firstNameColumnWidth = 20;
            int phoneColumnWidth = 20;

            // Table Header with borders (no column separators)
            String header = String.format("%-15s %-15s %-20s %-80s %-20s",
                    "Patient Number", "Surname", "First Name", "Address", "Phone Number");

            String border = "-".repeat(patientNumberColumnWidth) + "-" +
                    "-".repeat(surnameColumnWidth) + "-" +
                    "-".repeat(firstNameColumnWidth) + "-" +
                    "-".repeat(addressColumnWidth) + "-" +
                    "-".repeat(phoneColumnWidth);

            // Print the border and header
            System.out.println(border);
            System.out.println(header);
            System.out.println(border);

            // Printing each patient's details in a tabular format (no column separators)
            for (Patient patient : patients) {
                String patientRow = String.format("%-15s %-15s %-20s %-80s %-20s",
                        patient.getPatientNumber(),
                        patient.getSurname(),
                        patient.getFirstName(),
                        patient.getAddress(),
                        patient.getPhoneNumber());
                System.out.println(patientRow);
            }

            // Print bottom border
            System.out.println(border);

        }
    }

    // Add Patient
    private void addPatient() {
        System.out.println("\n--- Add New Patient ---");

        // Input patient details from the user
        System.out.println("Enter Patient Number: ");
        String patientNumber = scanner.nextLine();

        System.out.println("Enter Surname: ");
        String surname = scanner.nextLine();

        System.out.println("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.println("Enter Address: ");
        String address = scanner.nextLine();

        System.out.println("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();

        // Create patient object
        Patient newPatient = new Patient(0, patientNumber, surname, firstName, address, phoneNumber);

        try {
            // Add patient to the database
            PatientDAO.createPatient(newPatient);
            logger.info("New patient added successfully: {}", newPatient);
        } catch (SQLException e) {
            logger.error("Failed to add patient: {}", e.getMessage());
        }
    }
}
