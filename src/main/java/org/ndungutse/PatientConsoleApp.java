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
                    case 3 -> updatePatient();
                    case 4 -> deletePatient();
                    case 5 -> getPatientByNumber();
                    case 6 -> {
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
        System.out.println("5. Get Patient by Patient Number");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private void getPatientByNumber() {
        System.out.println("\n--- Get Patient by Patient Number ---");
        System.out.print("Enter Patient Number: ");
        String patientNumber = scanner.nextLine();

        try {
            Patient patient = PatientDAO.getPatientByNumber(patientNumber);

            if (patient == null) {
                logger.info("No patient found with patient number: {}", patientNumber);
                System.out.println("No patient found with the provided patient number.");
            } else {
                System.out.println("\nPatient Details:");
                System.out.println("Patient Number: " + patient.getPatientNumber());
                System.out.println("Surname: " + patient.getSurname());
                System.out.println("First Name: " + patient.getFirstName());
                System.out.println("Address: " + patient.getAddress());
                System.out.println("Phone Number: " + patient.getPhoneNumber());
            }
        } catch (SQLException e) {
            logger.error("Error retrieving patient by number: {}", e.getMessage());
            System.out.println("An error occurred while retrieving the patient.");
        }
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

    // Update Patient
    private void updatePatient() {
        System.out.println("\n--- Update Patient ---");

        // Ask the user for the patient number to update
        System.out.print("Enter the Patient Number of the patient you want to update: ");
        String patientNumber = scanner.nextLine();

        try {
            Patient existingPatient = PatientDAO.getPatientByNumber(patientNumber);

            if (existingPatient == null) {
                logger.warn("No patient found with the provided patient number: {}", patientNumber);
                System.out.println("No patient found with the provided patient number.");
                return;
            }

            System.out.println("Current details for patient " + patientNumber + ":");
            System.out.println("Surname: " + existingPatient.getSurname());
            System.out.println("First Name: " + existingPatient.getFirstName());
            System.out.println("Address: " + existingPatient.getAddress());
            System.out.println("Phone Number: " + existingPatient.getPhoneNumber());

            // Input new values
            System.out.print("Enter new Surname (leave blank to keep current): ");
            String newSurname = scanner.nextLine();
            if (!newSurname.isEmpty()) {
                existingPatient.setSurname(newSurname);
            }

            System.out.print("Enter new First Name (leave blank to keep current): ");
            String newFirstName = scanner.nextLine();
            if (!newFirstName.isEmpty()) {
                existingPatient.setFirstName(newFirstName);
            }

            System.out.print("Enter new Address (leave blank to keep current): ");
            String newAddress = scanner.nextLine();
            if (!newAddress.isEmpty()) {
                existingPatient.setAddress(newAddress);
            }

            System.out.print("Enter new Phone Number (leave blank to keep current): ");
            String newPhoneNumber = scanner.nextLine();
            if (!newPhoneNumber.isEmpty()) {
                existingPatient.setPhoneNumber(newPhoneNumber);
            }

            // Update the patient in the database
            PatientDAO.updatePatient(existingPatient);
            logger.info("Patient updated successfully: {}", existingPatient);
            System.out.println("Patient details updated successfully.");

        } catch (SQLException e) {
            logger.error("Failed to update patient: {}", e.getMessage());
            System.out.println("An error occurred while updating the patient.");
        }
    }

    // Delete Patient
    private void deletePatient() {
        System.out.println("\n--- Delete Patient ---");

        // Ask the user for the patient number to delete
        System.out.print("Enter the Patient Number of the patient you want to delete: ");
        String patientNumber = scanner.nextLine();

        try {
            Patient existingPatient = PatientDAO.getPatientByNumber(patientNumber);

            if (existingPatient == null) {
                logger.warn("No patient found with the provided patient number: {}", patientNumber);
                System.out.println("No patient found with the provided patient number.");
                return;
            }

            // Confirm deletion
            System.out.print("Are you sure you want to delete the patient? (yes/no): ");
            String confirmation = scanner.nextLine();

            if ("yes".equalsIgnoreCase(confirmation)) {
                PatientDAO.deletePatient(existingPatient.getPatientId());
                logger.info("Patient deleted successfully: {}", existingPatient);
                System.out.println("Patient deleted successfully.");
            } else {
                System.out.println("Patient deletion canceled.");
            }
        } catch (SQLException e) {
            logger.error("Failed to delete patient: {}", e.getMessage());
            System.out.println("An error occurred while deleting the patient.");
        }
    }
}
