CREATE TABLE "User" (
    user_id SERIAL PRIMARY KEY,
    surname VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role IN ('patient', 'doctor', 'nurse')),
    address TEXT NOT NULL,
    telephone_number VARCHAR(20)
);

CREATE TABLE Patient (
    user_id INT PRIMARY KEY,
    patient_number VARCHAR(20) UNIQUE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "User"(user_id)
        ON DELETE CASCADE,
    CHECK ((SELECT role FROM "User" WHERE user_id = Patient.user_id) = 'patient')
);


CREATE TABLE Employee (
    user_id INT PRIMARY KEY,
    employee_number VARCHAR(20) UNIQUE NOT NULL,
    salary DECIMAL(10,2) NOT NULL,
    department_id INT,  -- nullable for doctors
    speciality VARCHAR(100),  -- only for doctors
    FOREIGN KEY (user_id) REFERENCES "User"(user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES Department(department_id),
    CHECK ((SELECT role FROM "User" WHERE user_id = Employee.user_id) IN ('doctor', 'nurse')),
    CHECK (
        (SELECT role FROM "User" WHERE user_id = Employee.user_id) = 'doctor' AND speciality IS NOT NULL OR
        (SELECT role FROM "User" WHERE user_id = Employee.user_id) = 'nurse' AND speciality IS NULL
    )
);


CREATE TABLE Department (
    department_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    building VARCHAR(100),
    director_employee_id INT NOT NULL,
    FOREIGN KEY (director_employee_id) REFERENCES Employee(user_id)
        ON DELETE SET NULL
);


CREATE TABLE Ward (
    ward_number INT,
    department_id INT,
    number_of_beds INT NOT NULL,
    supervisor_nurse_id INT NOT NULL,
    PRIMARY KEY (ward_number, department_id),
    FOREIGN KEY (department_id) REFERENCES Department(department_id),
    FOREIGN KEY (supervisor_nurse_id) REFERENCES Employee(user_id),
    CHECK (
        (SELECT role FROM "User" WHERE user_id = supervisor_nurse_id) = 'nurse'
    )
);

-- ROTATION (only for nurses)
CREATE TABLE Rotation (
    rotation_id SERIAL PRIMARY KEY,
    nurse_id INT NOT NULL,
    rotation_date DATE NOT NULL,
    from_dep_id INT NOT NULL,
    to_dep_id INT NOT NULL,
    FOREIGN KEY (nurse_id) REFERENCES Employee(user_id),
    FOREIGN KEY (from_dep_id) REFERENCES Department(department_id),
    FOREIGN KEY (to_dep_id) REFERENCES Department(department_id),
    CHECK (
        (SELECT role FROM "User" WHERE user_id = nurse_id) = 'nurse'
    )
);

CREATE TABLE Hospitalization (
    hospitalization_id SERIAL PRIMARY KEY,
    patient_id INT NOT NULL,
    admission_date DATE NOT NULL,
    discharge_date DATE,
    diagnosis TEXT,
    FOREIGN KEY (patient_id) REFERENCES Patient(user_id)
);

CREATE TABLE DoctorHospitalization (
    hospitalization_id INT,
    employee_doctor_id INT,
    PRIMARY KEY (hospitalization_id, employee_doctor_id),
    FOREIGN KEY (hospitalization_id) REFERENCES Hospitalization(hospitalization_id),
    FOREIGN KEY (employee_doctor_id) REFERENCES Employee(user_id),
    CHECK (
        (SELECT role FROM "User" WHERE user_id = employee_doctor_id) = 'doctor'
    )
);

CREATE TABLE HospitalizationWard (
    hospitalization_ward_id SERIAL PRIMARY KEY,
    hospitalization_id INT NOT NULL,
    ward_number INT NOT NULL,
    department_id INT NOT NULL,
    bed_number INT,
    start_time TIME,
    end_time TIME,
    FOREIGN KEY (hospitalization_id) REFERENCES Hospitalization(hospitalization_id),
    FOREIGN KEY (ward_number, department_id) REFERENCES Ward(ward_number, department_id)
);
