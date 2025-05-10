package org.ndungutse;

import org.ndungutse.database.DatabaseSetup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException, IOException {

        Path file = Paths.get("E:\\Dev Env 1\\amalitech\\Labs\\week 6\\hospital_managment_system\\src\\main\\resources\\create-schema.sql");

        DatabaseSetup.executeSqlScript(file.toString());
    }
}