package org.ndungutse.repository;

import java.sql.Connection;
import java.sql.DriverManager;
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

}
