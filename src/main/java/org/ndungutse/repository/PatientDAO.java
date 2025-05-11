package org.ndungutse.repository;

import java.sql.Connection;
import java.sql.DriverManager;
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

    public static List<Patient> getAllPatients() throws SQLException {
        String sql = "SELECT u.user_id, u.surname, u.first_name, u.address, u.telephone_number, p.patient_number " +
                "FROM users u JOIN patients p ON u.user_id = p.patient_id";

        List<Patient> patients = new ArrayList<>();

        try (
                Connection connection = Database.getConnection();
                Statement stmt = connection.createStatement();) {

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Patient p = new Patient(rs.getInt("user_id"), rs.getString("patient_number"), rs.getString("surname"),
                        rs.getString("first_name"), rs.getString("address"), rs.getString("telephone_number"));
                patients.add(p);
            }

        }
        return patients;
    }

    // Add new Patient
    public static void createPatient(Patient patient) throws SQLException {
        String insertUserSQL = "INSERT INTO users (surname, first_name, role, address, telephone_number) " +
                "VALUES (?, ?, 'Patient', ?, ?)";
        String insertPatientSQL = "INSERT INTO patients (patient_id, patient_number) " +
                "VALUES (currval('users_user_id_seq'), ?)";

        try (
                Connection connection = Database.getConnection();
                PreparedStatement userStmt = connection.prepareStatement(insertUserSQL);
                PreparedStatement patientStmt = connection.prepareStatement(insertPatientSQL)) {
            // Start Transaction
            connection.setAutoCommit(false);

            // Insert into users
            userStmt.setString(1, patient.getSurname());
            userStmt.setString(2, patient.getFirstName());
            userStmt.setString(3, patient.getAddress());
            userStmt.setString(4, patient.getPhoneNumber());
            userStmt.executeUpdate();

            // Insert into patients using currval of users_user_id_seq
            patientStmt.setString(1, patient.getPatientNumber());
            patientStmt.executeUpdate();

            // Commit if both succeed
            connection.commit();

        }
    }

    // Delete Patient
    public static void deletePatient(int patientId) throws SQLException {
        String deletePatientSQL = "DELETE FROM patients WHERE patient_id = ?";
        String deleteUserSQL = "DELETE FROM users WHERE user_id = ?";

        try (
                Connection connection = Database.getConnection();
                PreparedStatement patientStmt = connection.prepareStatement(deletePatientSQL);
                PreparedStatement userStmt = connection.prepareStatement(deleteUserSQL)) {

            // Start Transaction
            connection.setAutoCommit(false);

            // Delete from patients
            patientStmt.setInt(1, patientId);
            patientStmt.executeUpdate();

            // Delete from users
            userStmt.setInt(1, patientId);
            userStmt.executeUpdate();

            // Commit if both succeed
            connection.commit();
        }
    }

    // Update Patient
    public static void updatePatient(Patient patient) throws SQLException {
        String updateUserSQL = "UPDATE users SET surname = ?, first_name = ?, address = ?, telephone_number = ? " +
                "WHERE user_id = (SELECT patient_id FROM patients WHERE patient_number = ?)";

        try (
                Connection connection = Database.getConnection();
                PreparedStatement userStmt = connection.prepareStatement(updateUserSQL)) {

            // Start Transaction
            connection.setAutoCommit(false);

            // Set parameters for the update query
            userStmt.setString(1, patient.getSurname());
            userStmt.setString(2, patient.getFirstName());
            userStmt.setString(3, patient.getAddress());
            userStmt.setString(4, patient.getPhoneNumber());
            userStmt.setString(5, patient.getPatientNumber());

            // Execute the update statement
            int affectedRows = userStmt.executeUpdate();

            if (affectedRows > 0) {
                // Commit transaction if the update is successful
                connection.commit();
            } else {
                // Rollback transaction if no patient was found with the given patient number
                connection.rollback();
                logger.warn(
                        "Attempted to update patient, but no patient found with number: " + patient.getPatientNumber());

            }

        }
    }

    public static Patient getPatientByNumber(String patientNumber) throws SQLException {
        String query = "SELECT u.user_id, u.surname, u.first_name, u.address, u.telephone_number " +
                "FROM users u " +
                "JOIN patients p ON u.user_id = p.patient_id " +
                "WHERE p.patient_number = ?";

        Patient patient = null;

        try (Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, patientNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    String surname = resultSet.getString("surname");
                    String firstName = resultSet.getString("first_name");
                    String address = resultSet.getString("address");
                    String phoneNumber = resultSet.getString("telephone_number");

                    patient = new Patient(
                            resultSet.getInt("user_id"),
                            patientNumber,
                            surname,
                            firstName,
                            address,
                            phoneNumber);
                }
            }
        }

        return patient;
    }

}
