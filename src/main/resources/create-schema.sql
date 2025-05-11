-- USERS table (Base)
CREATE TABLE
    IF NOT EXISTS users (
        user_id SERIAL PRIMARY KEY,
        surname VARCHAR(50) NOT NULL,
        first_name VARCHAR(50) NOT NULL,
        role VARCHAR(20) NOT NULL CHECK (role IN ('Doctor', 'Nurse', 'Patient')),
        address VARCHAR(100),
        telephone_number VARCHAR(20)
    );

-- DEPARTMENTS table
CREATE TABLE
    IF NOT EXISTS departments (
        department_id SERIAL PRIMARY KEY,
        name VARCHAR(50) NOT NULL,
        building VARCHAR(50),
        director_employee_id INTEGER,
        FOREIGN KEY (director_employee_id) REFERENCES users (user_id)
    );

-- EMPLOYEES table
CREATE TABLE
    IF NOT EXISTS employees (
        employee_id INTEGER PRIMARY KEY,
        employee_number VARCHAR(20) UNIQUE NOT NULL,
        department_id INTEGER,
        salary NUMERIC(10, 2),
        FOREIGN KEY (employee_id) REFERENCES users (user_id),
        FOREIGN KEY (department_id) REFERENCES departments (department_id)
    );

-- WARDS table
CREATE TABLE
    IF NOT EXISTS wards (
        ward_number INTEGER,
        department_id INTEGER,
        number_of_beds INTEGER,
        supervisor_nurse_id INTEGER,
        PRIMARY KEY (ward_number, department_id),
        FOREIGN KEY (department_id) REFERENCES departments (department_id),
        FOREIGN KEY (supervisor_nurse_id) REFERENCES employees (employee_id)
    );

-- ROTATIONS table
CREATE TABLE
    IF NOT EXISTS rotations (
        rotation_id SERIAL PRIMARY KEY,
        nurse_id INTEGER NOT NULL,
        rotation_date DATE NOT NULL,
        from_dep_id INTEGER,
        to_dep_id INTEGER,
        FOREIGN KEY (nurse_id) REFERENCES employees (employee_id),
        FOREIGN KEY (from_dep_id) REFERENCES departments (department_id),
        FOREIGN KEY (to_dep_id) REFERENCES departments (department_id)
    );

-- PATIENTS table
CREATE TABLE
    IF NOT EXISTS patients (
        patient_id INTEGER PRIMARY KEY,
        patient_number VARCHAR(20) UNIQUE NOT NULL,
        FOREIGN KEY (patient_id) REFERENCES users (user_id)
    );

-- HOSPITALIZATIONS table
CREATE TABLE
    IF NOT EXISTS hospitalizations (
        hospitalization_id SERIAL PRIMARY KEY,
        patient_id INTEGER NOT NULL,
        admission_date DATE NOT NULL,
        discharge_date DATE,
        diagnosis TEXT,
        FOREIGN KEY (patient_id) REFERENCES patients (patient_id)
    );

-- DOCTOR_HOSPITALIZATIONS table
CREATE TABLE
    IF NOT EXISTS doctor_hospitalizations (
        hospitalization_id INTEGER,
        doctor_employee_id INTEGER,
        PRIMARY KEY (hospitalization_id, doctor_employee_id),
        FOREIGN KEY (hospitalization_id) REFERENCES hospitalizations (hospitalization_id),
        FOREIGN KEY (doctor_employee_id) REFERENCES employees (employee_id)
    );

-- HOSPITALIZATION_WARDS table
CREATE TABLE
    IF NOT EXISTS hospitalization_wards (
        hospitalization_ward_id SERIAL PRIMARY KEY,
        hospitalization_id INTEGER NOT NULL,
        ward_number INTEGER NOT NULL,
        department_id INTEGER NOT NULL,
        bed_number INTEGER,
        start_time TIME,
        end_time TIME,
        FOREIGN KEY (hospitalization_id) REFERENCES hospitalizations (hospitalization_id),
        FOREIGN KEY (ward_number, department_id) REFERENCES wards (ward_number, department_id)
    );

-- Insert 10 patients into USERS and PATIENTS tables
INSERT INTO
    users (
        surname,
        first_name,
        role,
        address,
        telephone_number
    )
VALUES
    (
        'Olson',
        'Alexander',
        'Patient',
        '42075 King Course Suite 450, Darrellberg, ID 45688',
        '404-041-8611x0666'
    );

INSERT INTO
    patients (patient_id, patient_number)
VALUES
    (currval ('users_user_id_seq'), 'PN0001');

INSERT INTO
    users (
        surname,
        first_name,
        role,
        address,
        telephone_number
    )
VALUES
    (
        'Herrera',
        'Michelle',
        'Patient',
        '1945 Johnson Estates, East Rebeccatown, ME 98881',
        '(355)082-1492x213'
    );

INSERT INTO
    patients (patient_id, patient_number)
VALUES
    (currval ('users_user_id_seq'), 'PN0002');

INSERT INTO
    users (
        surname,
        first_name,
        role,
        address,
        telephone_number
    )
VALUES
    (
        'Pratt',
        'Sheila',
        'Patient',
        '500 Emily Run, Stanleyport, FL 89519',
        '077.172.3767x45367'
    );

INSERT INTO
    patients (patient_id, patient_number)
VALUES
    (currval ('users_user_id_seq'), 'PN0003');

INSERT INTO
    users (
        surname,
        first_name,
        role,
        address,
        telephone_number
    )
VALUES
    (
        'Zamora',
        'Alicia',
        'Patient',
        '74120 Thomas Cape Apt. 284, East Lisa, FL 79547',
        '274.382.9967x6320'
    );

INSERT INTO
    patients (patient_id, patient_number)
VALUES
    (currval ('users_user_id_seq'), 'PN0004');

INSERT INTO
    users (
        surname,
        first_name,
        role,
        address,
        telephone_number
    )
VALUES
    (
        'Moss',
        'Angela',
        'Patient',
        '13329 Wyatt Haven Apt. 848, North Michellemouth, NV 60579',
        '(570)818-0884x905'
    );

INSERT INTO
    patients (patient_id, patient_number)
VALUES
    (currval ('users_user_id_seq'), 'PN0005');

INSERT INTO
    users (
        surname,
        first_name,
        role,
        address,
        telephone_number
    )
VALUES
    (
        'Stephens',
        'Kevin',
        'Patient',
        '872 Julie Ridge, South Matthewfort, OR 67542',
        '694-813-4025x807'
    );

INSERT INTO
    patients (patient_id, patient_number)
VALUES
    (currval ('users_user_id_seq'), 'PN0006');

INSERT INTO
    users (
        surname,
        first_name,
        role,
        address,
        telephone_number
    )
VALUES
    (
        'Walsh',
        'Douglas',
        'Patient',
        '8878 Stephanie Fork Apt. 533, Johnsonview, TN 14362',
        '0848262480'
    );

INSERT INTO
    patients (patient_id, patient_number)
VALUES
    (currval ('users_user_id_seq'), 'PN0007');

INSERT INTO
    users (
        surname,
        first_name,
        role,
        address,
        telephone_number
    )
VALUES
    (
        'Harper',
        'Debra',
        'Patient',
        '4509 Deanna Greens, North Laurahaven, NJ 24358',
        '(829) 295-4532'
    );

INSERT INTO
    patients (patient_id, patient_number)
VALUES
    (currval ('users_user_id_seq'), 'PN0008');

INSERT INTO
    users (
        surname,
        first_name,
        role,
        address,
        telephone_number
    )
VALUES
    (
        'Patton',
        'Catherine',
        'Patient',
        '4453 Murphy Passage Suite 240, South Thomas, TX 85962',
        '167-927-9645'
    );

INSERT INTO
    patients (patient_id, patient_number)
VALUES
    (currval ('users_user_id_seq'), 'PN0009');

INSERT INTO
    users (
        surname,
        first_name,
        role,
        address,
        telephone_number
    )
VALUES
    (
        'Shaw',
        'Brandon',
        'Patient',
        '57692 Dawn Trail, North Stevenport, KS 54329',
        '289.761.0124'
    );

INSERT INTO
    patients (patient_id, patient_number)
VALUES
    (currval ('users_user_id_seq'), 'PN0010');