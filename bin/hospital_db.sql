CREATE DATABASE hospital_db;
USE hospital_db;

CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10),
    contact VARCHAR(20),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);
select * from patients;

CREATE TABLE doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    contact VARCHAR(20)
);
select * from doctors;

DROP TABLE IF EXISTS appointments;

CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name VARCHAR(100),
    patient_email VARCHAR(100),  -- Email used in Java code
    doctor_name VARCHAR(100),
    appointment_date DATE,
    appointment_time TIME,
    reason TEXT
);

ALTER TABLE appointments ADD COLUMN doctor_id INT;
ALTER TABLE patients ADD COLUMN treatment VARCHAR(255);

select * from appointments;

CREATE TABLE records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_email VARCHAR(100),
    date DATE,
    diagnosis TEXT,
    prescription TEXT,
    doctor VARCHAR(100)
);


CREATE TABLE admin (
    id INT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);
select * from admin;
select * from patients;
INSERT INTO admin (id, username, password) VALUES (1, 'admin1', 'admin123');
INSERT INTO admin (id, username, password) VALUES (2, 'admin2', 'admin456');

INSERT INTO patients (name, age, gender, contact, email, password) VALUES
('Priya Shankar', 30, 'Female', '9876543210', 'priya@gmail.com', 'priya123'),  
('Jennie', 25, 'Female', '9898989898', 'jennie@rediffmail.com', 'jenniepass'),  
('Vikram Aditya', 35, 'Male', '9812345678', 'vikram@hotmail.com', 'vikram123'),
('Christopher', 40, 'Male', '9856231478', 'chris@gmail.com', 'chrispass'),
('Dhruv', 28, 'Male', '9998887776', 'dhruv@gmail.com', 'dhruv123');

-- Insert sample doctors
INSERT INTO doctors (name, specialization, contact) VALUES
('Dr. Anjali Gupta', 'Cardiology', '9876543210'),
('Dr. Rajeev Kumar', 'Neurology', '9823456789'),
('Dr. Sarah Williams', 'Orthopedics', '9856341023'),
('Dr. Amit Sharma', 'Pediatrics', '9709876543'),
('Dr. Lisa Manoban', 'General Medicine', '9712345678');

-- Modify Records Table
INSERT INTO records (patient_email, date, diagnosis, prescription, doctor) VALUES
('priya@gmail.com', '2022-06-12', 'Hypertension', 'Atenolol 50mg daily', 'Dr. Anjali Gupta'),
('priya@gmail.com', '2023-02-20', 'Migraine', 'Sumatriptan 100mg as needed', 'Dr. Rajeev Kumar'),
('jennie@rediffmail.com', '2019-08-05', 'Fractured arm', 'Cast for 6 weeks', 'Dr. Sarah Williams'),
('jennie@rediffmail.com', '2020-12-22', 'Common Cold', 'Paracetamol 500mg 3x a day', 'Dr. Amit Sharma'),
('vikram@hotmail.com', '2021-10-10', 'Back pain', 'Physical Therapy, Ibuprofen 200mg daily', 'Dr. Lisa Manoban'),
('vikram@hotmail.com', '2022-11-25', 'Chest Pain', 'Nitroglycerin 0.3mg sublingual as needed', 'Dr. Anjali Gupta'),
('chris@gmail.com', '2020-01-17', 'Depression', 'Fluoxetine 20mg daily', 'Dr. Rajeev Kumar'),
('chris@gmail.com', '2023-09-19', 'Osteoarthritis', 'Glucosamine Sulfate 500mg daily', 'Dr. Sarah Williams'),
('dhruv@gmail.com', '2018-03-30', 'Gastritis', 'Omeprazole 20mg before breakfast', 'Dr. Amit Sharma'),
('dhruv@gmail.com', '2021-07-22', 'Asthma', 'Albuterol inhaler as needed', 'Dr. Lisa Manoban');
