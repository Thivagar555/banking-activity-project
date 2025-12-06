🏦 Banking Activity Simulation Platform

A full-stack banking simulation system built using Spring Boot (Backend) and Angular (Frontend).
The platform provides secure user authentication, account management, transactions, alerts, and reporting modules.

🚀 Features
🔐 User Authentication & Security

User registration & login

OTP-based authentication

JWT token-based security

Fraud detection service for enhanced protection

🧾 Account Management

Create and manage bank accounts

View account details and balances

Secure API endpoints with proper authorization

💸 Transaction Module

Deposit

Withdrawal

Fund Transfer

Transaction validation and exception handling

Atomic and reliable operations

📊 Reporting Module

View transaction history

Download reports

Structured data for audit and tracking

📬 Alerts System

Low balance alerts

Email notifications

Manage alert thresholds

🖥️ Frontend (Angular)

Responsive UI for customer operations

Account dashboard

Transaction forms

Secure API integration

🏗️ Tech Stack
Backend:

Spring Boot

Java

Spring Security

Spring Data JPA

Java Mail Sender

JWT Authentication

MySQL

Frontend:

Angular

TypeScript

HTML, CSS

Bootstrap / Material UI (optional)

📁 Project Structure
Backend (Spring Boot)
BankingProject/
│── src/main/java/com/banking/BankingProject
│   ├── account/
│   ├── admin/
│   ├── auth/
│   ├── config/
│   ├── email/
│   ├── otp/
│   ├── security/
│   ├── transaction/
│   ├── user/
│── src/main/resources/
│── pom.xml
│── application.properties

Frontend (Angular)
BankingProject-frontend/
│── src/
│── angular.json
│── package.json
│── tsconfig.json

⚙️ How to Run the Backend
1. Clone the repository
git clone https://github.com/Thivagar555/banking-activity-project.git
cd banking-activity-project

2. Configure the database

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/banking
spring.datasource.username=root
spring.datasource.password=yourpassword
jwt.secret=yourSecretKey

3. Run the Spring Boot application
mvn spring-boot:run


OR

java -jar target/BankingProject.jar

💻 How to Run the Frontend
1. Install dependencies
cd BankingProject-frontend
npm install

2. Start the development server
ng serve

3. Access the UI
http://localhost:4200/

🧪 Testing
Backend
mvn test

Frontend
ng test
| Method | Endpoint                | Description          |
| ------ | ----------------------- | -------------------- |
| POST   | `/auth/register`        | Register new user    |
| POST   | `/auth/login`           | Login & obtain JWT   |
| POST   | `/account/create`       | Create account       |
| GET    | `/account/{id}`         | View account details |
| POST   | `/transaction/deposit`  | Deposit amount       |
| POST   | `/transaction/withdraw` | Withdraw amount      |
| POST   | `/transaction/transfer` | Transfer funds       |

👥 Team Members

Thivagar — Backend & Integration

Anushree Joshi — Frontend UI & Testing

Archana — Backend Development & Alerts Module

📌 Project Status

✔️ Core modules completed
✔️ Frontend integrated
✔️ Security implemented
✔️ Alerts & reporting functional

🤝 Contributions

Pull requests are welcome!
Feel free to fork this repository and contribute enhancements.

📜 License

This project is licensed under the MIT License.
