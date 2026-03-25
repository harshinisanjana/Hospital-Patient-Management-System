-- Drop and recreate the database
DROP DATABASE IF EXISTS hospital_db;
CREATE DATABASE hospital_db;
USE hospital_db;

-- 1. Admin Table
CREATE TABLE admin (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL
);

INSERT INTO admin (username, password) VALUES 
('admin1', 'admin123'),
('admin2', 'admin456');

-- 2. Patients Table
CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10),
    contact VARCHAR(20),
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    treatment VARCHAR(255)
);

INSERT INTO patients (name, age, gender, contact, email, password, treatment) VALUES
('Priya Shankar', 30, 'Female', '9876543210', 'priya@gmail.com', 'priya123', 'Routine Checkup'),  
('Jennie Kim', 25, 'Female', '9898989898', 'jennie@rediffmail.com', 'jenniepass', 'Physiotherapy'),  
('Vikram Aditya', 35, 'Male', '9812345678', 'vikram@hotmail.com', 'vikram123', 'Cardiac Recovery'),
('Christopher Nolan', 40, 'Male', '9856231478', 'chris@gmail.com', 'chrispass', null),
('Dhruv Rathee', 28, 'Male', '9998887776', 'dhruv@gmail.com', 'dhruv123', 'Asthma Management');

-- 3. Doctors Table
CREATE TABLE doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    contact VARCHAR(20)
);

INSERT INTO doctors (name, specialization, contact) VALUES
('Dr. Anjali Gupta', 'Cardiology', '9876543210'),
('Dr. Rajeev Kumar', 'Neurology', '9823456789'),
('Dr. Sarah Williams', 'Orthopedics', '9856341023'),
('Dr. Amit Sharma', 'Pediatrics', '9709876543'),
('Dr. Lisa Manoban', 'General Medicine', '9712345678'),
('Dr. Robert Downey', 'Dermatology', '9123456701'),
('Dr. Scarlett Johansson', 'Gynecology', '9123456702'),
('Dr. Benedict Cumberbatch', 'Psychiatry', '9123456703'),
('Dr. Tom Holland', 'Ophthalmology', '9123456704'),
('Dr. Zendaya', 'Dentistry', '9123456705');

-- 4. Appointments Table
CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name VARCHAR(100),
    patient_email VARCHAR(100),
    doctor_name VARCHAR(100),
    appointment_date DATE,
    appointment_time TIME,
    reason TEXT,
    doctor_id INT
);

INSERT INTO appointments (patient_name, patient_email, doctor_name, appointment_date, appointment_time, reason) VALUES
('Priya Shankar', 'priya@gmail.com', 'Dr. Anjali Gupta', '2026-04-01', '10:00:00', 'Regular heart checkup'),
('Jennie Kim', 'jennie@rediffmail.com', 'Dr. Sarah Williams', '2026-04-02', '11:30:00', 'Leg pain follow up'),
('Vikram Aditya', 'vikram@hotmail.com', 'Dr. Rajeev Kumar', '2026-04-05', '09:00:00', 'Persistent headaches');

-- 5. Records Table (Historical)
CREATE TABLE records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_email VARCHAR(100),
    date DATE,
    diagnosis TEXT,
    prescription TEXT,
    doctor VARCHAR(100)
);

INSERT INTO records (patient_email, date, diagnosis, prescription, doctor) VALUES
('priya@gmail.com', '2023-06-12', 'Hypertension', 'Atenolol 50mg daily', 'Dr. Anjali Gupta'),
('priya@gmail.com', '2024-02-20', 'Migraine', 'Sumatriptan 100mg as needed', 'Dr. Rajeev Kumar'),
('jennie@rediffmail.com', '2022-08-05', 'Fractured arm', 'Cast for 6 weeks', 'Dr. Sarah Williams'),
('dhruv@gmail.com', '2025-01-15', 'Seasonal Allergies', 'Cetirizine 10mg daily', 'Dr. Lisa Manoban');
