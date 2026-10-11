# MedPlus-Hospital-Management-system

          MEDPULSE HOSPITAL MANAGEMENT SYSTEM (HMS) - PROJECT MANUAL

1. PROJECT OVERVIEW
--------------------------------------------------------------------------------
The MedPulse Hospital Management System is a full-stack web application designed 
to streamline patient admissions, KYC document handling, dynamic multi-tier 
treatment billing, and discharge management. 

It implements a segregated lifecycle architecture where patients are categorized 
into two primary states:
  * Active In-Patients: Undergoing diagnosis and continuous treatment.
  * Discharged Records: Patients whose accounts have been settled and who have 
    been released from the facility.

--------------------------------------------------------------------------------
2. KEY FEATURES & WORKFLOW
--------------------------------------------------------------------------------
* Patient Registration & KYC:
  - Captures demographic details: Name, Age, Location, Identity Type (Aadhaar/PAN).
  - Handles multipart file upload for government identity verification.
  - Automatically generates a unique hospital identifier (UHID).
  - Levies a mandatory, locked initial registration advance of Rs. 1,500.

* Doctor & Severity Multiplier Assignment:
  - Assigns an available attending doctor upon admission.
  - Triage-based cost calculation: Adjusts consultation fees based on injury 
    severity (Minor = 1.0x, Moderate = 1.5x, Major = 2.0x).
  - Automatically matches hospital facility overhead charges to doctor fees.

* Real-Time Itemized Billing Desk:
  - Dynamically add or remove line items during admission:
    - MRI Scans
    - CT Scans
    - X-Rays
    - Pharmacy & Medication
    - Miscellaneous facility charges
  - Recalculates patient ledger balance immediately upon item addition or removal.

* Automated Discharge & Archive:
  - Finalizes total billing: Line Items + Initial Advance.
  - Generates a unique invoice number (INV-timestamp).
  - Instantly removes the patient from the Active list and archives them under 
    Discharged Records with an immutable audit timestamp.

--------------------------------------------------------------------------------
3. TECH STACK
--------------------------------------------------------------------------------
* Backend:
  - Java 17 / 21
  - Spring Boot 3 (REST API, Spring Data JPA, Spring Web)
  - Hibernate ORM
  - Multipart File Handling

* Database:
  - MySQL 8.x

* Frontend:
  - React (Vite / Create React App)
  - Axios for asynchronous HTTP communication
  - Pure CSS Design System (Responsive, Modern Clinical Dashboard)

                            SETUP & RUN GUIDE

PREREQUISITES:
  * Java JDK 17 or higher installed (`java -version`)
  * Node.js v18 or higher & npm installed (`node -v`, `npm -v`)
  * MySQL Server running on port 3306
  * IntelliJ IDEA (or preferred IDE)

--------------------------------------------------------------------------------
STEP 1: DATABASE CONFIGURATION (MySQL)
--------------------------------------------------------------------------------
1. Connect to your MySQL instance (via MySQL Workbench, terminal, or IDE):
   
   mysql -u root -p

2. Run the initialization script:

   CREATE DATABASE IF NOT EXISTS hospital_management;
   USE hospital_management;

   CREATE TABLE IF NOT EXISTS doctors (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(100) NOT NULL,
       specialization VARCHAR(100) NOT NULL,
       base_consultation_fee DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
       is_available BOOLEAN DEFAULT TRUE
   );

   -- Seed default doctor record required for the default registration form
   INSERT INTO doctors (id, name, specialization, base_consultation_fee, is_available)
   VALUES (1, 'Dr. Sharma', 'General Medicine', 20000.00, TRUE)
   ON DUPLICATE KEY UPDATE name=name;

--------------------------------------------------------------------------------
STEP 2: BACKEND CONFIGURATION & STARTUP (Spring Boot)
--------------------------------------------------------------------------------
1. Open the project in IntelliJ IDEA.
2. Verify `src/main/resources/application.properties`:

   server.port=8080
   spring.datasource.url=jdbc:mysql://localhost:3306/hospital_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.servlet.multipart.max-file-size=10MB
   spring.servlet.multipart.max-request-size=10MB

3. Ensure the project package structure matches:
   com.hospital
   ├── HospitalApplication.java   <-- @SpringBootApplication entry point
   ├── controller/
   ├── entity/
   ├── repository/
   └── service/

4. Run the application:
   - Click the green Run (▶) button on `HospitalApplication.java`, OR
   - Run in terminal:
     ./mvnw spring-boot:run

   Tomcat will initialize on: http://localhost:8080

--------------------------------------------------------------------------------
STEP 3: FRONTEND SETUP & STARTUP (React)
--------------------------------------------------------------------------------
1. Open a terminal and navigate to the frontend directory:
   cd frontend

2. Install dependencies:
   npm install axios

3. Place files inside the frontend directory structure:
   frontend/src/
   ├── components/
   │   ├── RegistrationForm.jsx
   │   └── HospitalDashboard.jsx
   ├── App.jsx
   ├── index.css (or App.css)
   └── main.jsx

4. Start the development server:
   - If using Vite:
     npm run dev
     (Runs on http://localhost:5173)

   - If using Create React App:
     npm start
     (Runs on http://localhost:3000)

--------------------------------------------------------------------------------
STEP 4: VERIFICATION & TESTING WORKFLOW
--------------------------------------------------------------------------------
1. Open http://localhost:3000 (or http://localhost:5173) in your browser.
2. Fill out the "In-Patient Admission" form:
   - Enter Full Name, Age, Place.
   - Choose Aadhaar or PAN.
   - Select an ID document file.
   - Select Severity (Minor / Moderate / Major).
   - Click "Register Patient • Advance ₹1,500".
3. Check the "Active In-Patients" section:
   - The new patient will appear with their generated UHID.
4. Click "Open Billing & Discharge →":
   - Inspect the advance fee, base doctor charge, and hospital charge.
   - Add extra items (e.g., Scan MRI for ₹7,500).
   - Verify that deleting a line item updates the list.
5. Click "Generate Final Invoice & Settle Discharge":
   - The total bill settles.
   - The patient is removed from "Active In-Patients" and moved to "Discharged Records".

