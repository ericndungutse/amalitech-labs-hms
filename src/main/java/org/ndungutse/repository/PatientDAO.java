package org.ndungutse.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.ndungutse.Main;
import org.ndungutse.database.Database;
import org.ndungutse.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientDAO {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // Get all patients
    public static List<Patient> getAllPatients() throws SQLException {
        String sql = "SELECT u.user_id, u.surname, u.first_name, u.address, u.telephone_number, p.patient_number " +
                "FROM users u JOIN patients p ON u.user_id = p.patient_id";

        List<Patient> patients = new ArrayList<>();
        logger.info("Fetching all patients from the database.");

        try (
                Connection connection = Database.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Patient p = new Patient(
                        rs.getInt("user_id"),
                        rs.getString("patient_number"),
                        rs.getString("surname"),
                        rs.getString("first_name"),
                        rs.getString("address"),
                        rs.getString("telephone_number"));
                patients.add(p);
            }
            logger.info("Fetched {} patients from the database.", patients.size());
        } catch (SQLException e) {
            logger.error("Error fetching patients: {}", e.getMessage(), e);
            throw e;
        }

        return patients;
    }

    // Create a new patient
    public static void createPatient(Patient patient) throws SQLException {
        String insertUserSQL = "INSERT INTO users (surname, first_name, role, address, telephone_number) " +
                "VALUES (?, ?, 'Patient', ?, ?)";
        String insertPatientSQL = "INSERT INTO patients (patient_id, patient_number) " +
                "VALUES (currval('users_user_id_seq'), ?)";

        logger.info("Creating new patient with number: {}", patient.getPatientNumber());

        try (
                Connection connection = Database.getConnection();
                PreparedStatement userStmt = connection.prepareStatement(insertUserSQL);
                PreparedStatement patientStmt = connection.prepareStatement(insertPatientSQL)) {
            connection.setAutoCommit(false);

            userStmt.setString(1, patient.getSurname());
            userStmt.setString(2, patient.getFirstName());
            userStmt.setString(3, patient.getAddress());
            userStmt.setString(4, patient.getPhoneNumber());
            userStmt.executeUpdate();

            patientStmt.setString(1, patient.getPatientNumber());
            patientStmt.executeUpdate();

            connection.commit();
            logger.info("Successfully created patient with number: {}", patient.getPatientNumber());
        } catch (SQLException e) {
            logger.error("Error creating patient: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Delete a patient
    public static void deletePatient(int patientId) throws SQLException {
        String deletePatientSQL = "DELETE FROM patients WHERE patient_id = ?";
        String deleteUserSQL = "DELETE FROM users WHERE user_id = ?";

        logger.info("Deleting patient with ID: {}", patientId);

        try (
                Connection connection = Database.getConnection();
                PreparedStatement patientStmt = connection.prepareStatement(deletePatientSQL);
                PreparedStatement userStmt = connection.prepareStatement(deleteUserSQL)) {
            connection.setAutoCommit(false);

            patientStmt.setInt(1, patientId);
            patientStmt.executeUpdate();

            userStmt.setInt(1, patientId);
            userStmt.executeUpdate();

            connection.commit();
            logger.info("Successfully deleted patient with ID: {}", patientId);
        } catch (SQLException e) {
            logger.error("Error deleting patient: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Update a patient
    public static void updatePatient(Patient patient) throws SQLException {
        String updateUserSQL = "UPDATE users SET surname = ?, first_name = ?, address = ?, telephone_number = ? " +
                "WHERE user_id = (SELECT patient_id FROM patients WHERE patient_number = ?)";

        logger.info("Updating patient with number: {}", patient.getPatientNumber());

        try (
                Connection connection = Database.getConnection();
                PreparedStatement userStmt = connection.prepareStatement(updateUserSQL)) {
            connection.setAutoCommit(false);

            userStmt.setString(1, patient.getSurname());
            userStmt.setString(2, patient.getFirstName());
            userStmt.setString(3, patient.getAddress());
            userStmt.setString(4, patient.getPhoneNumber());
            userStmt.setString(5, patient.getPatientNumber());

            int affectedRows = userStmt.executeUpdate();

            if (affectedRows > 0) {
                connection.commit();
                logger.info("Successfully updated patient with number: {}", patient.getPatientNumber());
            } else {
                connection.rollback();
                logger.warn("Attempted to update patient, but no patient found with number: {}",
                        patient.getPatientNumber());
            }
        } catch (SQLException e) {
            logger.error("Error updating patient: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Get a patient by patient number
    public static Patient getPatientByNumber(String patientNumber) throws SQLException {
        String query = "SELECT u.user_id, u.surname, u.first_name, u.address, u.telephone_number " +
                "FROM users u " +
                "JOIN patients p ON u.user_id = p.patient_id " +
                "WHERE p.patient_number = ?";

        logger.info("Fetching patient with number: {}", patientNumber);

        Patient patient = null;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, patientNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    patient = new Patient(
                            resultSet.getInt("user_id"),
                            patientNumber,
                            resultSet.getString("surname"),
                            resultSet.getString("first_name"),
                            resultSet.getString("address"),
                            resultSet.getString("telephone_number"));
                    logger.info("Patient found with number: {}", patientNumber);
                } else {
                    logger.warn("No patient found with number: {}", patientNumber);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching patient by number: {}", e.getMessage(), e);
            throw e;
        }

        return patient;
    }
}
