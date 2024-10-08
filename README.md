# Smart Contact Management System

## Overview

The **Smart Contact Management System** is designed to efficiently manage the details of contacts, credentials, telephone numbers, profiles, and emails. It streamlines the process of handling contact information, reducing manual work and improving accuracy. Built using **Spring Boot** for the backend and modern front-end technologies, this system is targeted towards administrative use, ensuring only the administrator has access to manage sensitive information.

## Features

- **Contact Management**: Easily manage details such as names, mobile numbers, and emails for all contacts.
- **Credential Management**: Securely store and manage user credentials.
- **Telephone and Profile Information**: Track telephone numbers and profiles, ensuring all information is up-to-date.
- **Email Tracking**: Manage and store email addresses for quick reference.
- **Administrative Access**: Only authorized administrators can access and manage the system, ensuring data security.

## Objectives

The main goal of the Smart Contact Management System is to automate and streamline the process of managing contact information, including:
- Reducing manual data entry and management.
- Centralizing information about contacts, telephone numbers, and emails.
- Providing easy-to-use administrative tools for managing credentials and profiles.

## Technology Stack

- **Backend**: Spring Boot
- **Frontend**: HTML, CSS, Bootstrap, JavaScript
- **Database**: MySQL (managed using MySQL Workbench)
- **Other Tools**: MySQL Workbench for database design and management

## How to Run the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/Hithesh18/contact-management-system.git
2. Navigate to the project directory:
   ```bash
   cd contact-management-system
   ```

3. Set up the MySQL database:
   • Open MySQL Workbench and create a new database schema for the project.
   • Import the provided SQL scripts to set up the required tables.

4. Configure the Spring Boot application:
   • Update the `application.properties` file with your MySQL database credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/your-database-name
     spring.datasource.username=your-username
     spring.datasource.password=your-password
     ```

5. Set up the front-end:
   • Ensure the front-end HTML, CSS, Bootstrap, and JavaScript files are correctly placed in the appropriate templates directory.

6. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

7. Open your browser and navigate to:
   ```bash
   http://localhost:8080
   ```
Future Enhancements
• Add user roles and permissions for more flexible access management.
• Implement search and filter functionality for managing large contact lists.
• Add API integration for importing/exporting contact data.
License
This project is licensed under the MIT License. See the LICENSE file for more details.

Snapshots of Application
![image](https://github.com/user-attachments/assets/3fec9a64-476f-497e-a6d2-767931206725)
![image](https://github.com/user-attachments/assets/39cb6a7d-cf59-45ed-b063-970a9a631f23)
![image](https://github.com/user-attachments/assets/8f03b531-f9f9-4bb0-af00-4043090831a9)





