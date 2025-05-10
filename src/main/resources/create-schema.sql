-- DROP TABLES in correct order to avoid FK issues
DROP TABLE IF EXISTS doctor_hospitalizations, hospitalization_wards, hospitalizations, patients, rotations, wards, departments, employees, users CASCADE;

-- USERS table (Base)
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    surname VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('Doctor', 'Nurse', 'Patient')),
    address VARCHAR(100),
    telephone_number VARCHAR(20)
);

-- DEPARTMENTS table
CREATE TABLE departments (
    department_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    building VARCHAR(50),
    director_employee_id INTEGER,
    FOREIGN KEY (director_employee_id) REFERENCES users(user_id)
);

-- EMPLOYEES table
CREATE TABLE employees (
    employee_id INTEGER PRIMARY KEY,
    employee_number VARCHAR(20) UNIQUE NOT NULL,
    department_id INTEGER,
    salary NUMERIC(10,2),
    FOREIGN KEY (employee_id) REFERENCES users(user_id),
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

-- WARDS table
CREATE TABLE wards (
    ward_number INTEGER,
    department_id INTEGER,
    number_of_beds INTEGER,
    supervisor_nurse_id INTEGER,
    PRIMARY KEY (ward_number, department_id),
    FOREIGN KEY (department_id) REFERENCES departments(department_id),
    FOREIGN KEY (supervisor_nurse_id) REFERENCES employees(employee_id)
);

-- ROTATIONS table
CREATE TABLE rotations (
    rotation_id SERIAL PRIMARY KEY,
    nurse_id INTEGER NOT NULL,
    rotation_date DATE NOT NULL,
    from_dep_id INTEGER,
    to_dep_id INTEGER,
    FOREIGN KEY (nurse_id) REFERENCES employees(employee_id),
    FOREIGN KEY (from_dep_id) REFERENCES departments(department_id),
    FOREIGN KEY (to_dep_id) REFERENCES departments(department_id)
);

-- PATIENTS table
CREATE TABLE patients (
    patient_id INTEGER PRIMARY KEY,
    patient_number VARCHAR(20) UNIQUE NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES users(user_id)
);

-- HOSPITALIZATIONS table
CREATE TABLE hospitalizations (
    hospitalization_id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL,
    admission_date DATE NOT NULL,
    discharge_date DATE,
    diagnosis TEXT,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

-- DOCTOR_HOSPITALIZATIONS table
CREATE TABLE doctor_hospitalizations (
    hospitalization_id INTEGER,
    doctor_employee_id INTEGER,
    PRIMARY KEY (hospitalization_id, doctor_employee_id),
    FOREIGN KEY (hospitalization_id) REFERENCES hospitalizations(hospitalization_id),
    FOREIGN KEY (doctor_employee_id) REFERENCES employees(employee_id)
);

-- HOSPITALIZATION_WARDS table
CREATE TABLE hospitalization_wards (
    hospitalization_ward_id SERIAL PRIMARY KEY,
    hospitalization_id INTEGER NOT NULL,
    ward_number INTEGER NOT NULL,
    department_id INTEGER NOT NULL,
    bed_number INTEGER,
    start_time TIME,
    end_time TIME,
    FOREIGN KEY (hospitalization_id) REFERENCES hospitalizations(hospitalization_id),
    FOREIGN KEY (ward_number, department_id) REFERENCES wards(ward_number, department_id)
);
