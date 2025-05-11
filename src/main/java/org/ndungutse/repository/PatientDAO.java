package org.ndungutse.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.ndungutse.database.Database;
import org.ndungutse.model.Patient;

public class PatientDAO {

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

}
