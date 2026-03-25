# WeCare Hospital - Patient Management System

A modern, attractive, and fully functional Hospital Patient Management System built with Java Swing and MySQL. This system provides a comprehensive solution for managing patient registrations, appointments, doctor details, and administrative tasks.

## 🚀 Key Features
- **Admin Control Panel**: 
  - Manage hospital staff (Add/View Doctors).
  - Oversee scheduled appointments.
- **Patient Portal**:
  - Secure self-registration and login.
  - View available specialists.
  - Schedule appointments with specific doctors, dates, and times.
  - View personal medical history and upcoming visits.
- **Reliable Backend**: Robust MySQL integration using modern `try-with-resources` for data integrity.
- **UI Overhaul**: Modern Design, Interactive Dashboards, Optimized Forms, Styled Tables

## 🛠️ Technology Stack
- **Languages**: Java (Swing for UI, JDBC for Database).
- **Database**: MySQL.
- **Dependencies**: `mysql-connector-j-9.0.0.jar` (Included in `lib/`).

## 📋 Prerequisites
- **Java Development Kit (JDK)**: Version 8 or higher.
- **MySQL Server**: Installed and running locally.

## 🚦 Getting Started

### 1. Database Setup
1. Create a database named `hospital_db`.
2. Apply the schema and sample data using the provided `src/hospital_db.sql` file.
3. Update the password in `src/DatabaseConnection.java` if your local MySQL root password is not `root123`.

### 2. Compilation
Compile the project from the root directory:
```powershell
javac -d bin src/*.java
```

### 3. Running the Application
Launch the app with the database driver included in the classpath:
```powershell
java -cp "bin;lib/mysql-connector-j-9.0.0.jar" Main
```

## 🔐 Authentication (Sample Data)
| User Type | Username/Email | Password |
| :--- | :--- | :--- |
| **Admin** | `admin1` | `admin123` |
| **Patient** | `priya@gmail.com` | `priya123` |

