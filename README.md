# FBR Tax Portal - JavaFX OOP Project

A desktop-based Taxpayer Portal using **JavaFX** (frontend) and **MySQL** (backend).

## Current Features (Completed)

- **User Registration**: New users can register with CNIC, name, email, phone, and password. Data is saved directly in the database.
- **User Login**: Registered users can log in using their CNIC and password.
- **User Info & Status**: Fully functional button and interface to view and update user profile (Name, CNIC, DOB, Gender, Address, Email, Phone).
- **Database Connectivity**: 
  - Data entered through registration is stored in the database.
  - Users manually added in the SQL database can also log in and view their data.
  - Two-way integration: portal ↔ database.

## Technologies Used

- Java (JDK 25)
- Scene Bulider
- JavaFX
- MySQL
- MySQL Connector/J

## How to Run

1. Add MySQL Connector JAR to classpath.
2. Update `DBconnection.java` with your MySQL password (if any).
3. Run `Frontend.Main`.
4. Backend

**Project Status: Core features (Registration, Login, User Info & Status with full database integration) completed and working.**

---
OOP Course Project