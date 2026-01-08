FBR-TAX-PORTAL
Project Overview

FBR-TAX-PORTAL is a desktop based Taxpayer Management System developed as an Object-Oriented Programming (OOP) Java project.
The system is designed to simulate a user-friendly tax portal where taxpayers can register, calculate taxes, make payments, and manage tax records, while administrators can manage tax rates, users, and notifications.

The project follows proper OOP principles such as:
Encapsulation
Inheritance
Polymorphism
Frontend & Backend

It uses JavaFX for a modern graphical user interface and MySQL for secure data storage and management.

How to Run the Project:
Install JDK 25
Install MySQL Server and MySQL Workbench
Add MySQL Connector/J (JAR file) to the project classpath
Update database credentials in DBconnection.java
Open the project in VS Code
Run the Main class

How the System Works:
when program is run a splash screen appears for 5sec and then
1) Login & Registration
When the program starts, the Login Interface appears.
<img src="img/login.png" width="700">
 
2) Registration
If the user is new, they click Register Now.
<img src="img/register now.png" width="700">
 
There are two types of registration:
1- User Registration
2- Admin Registration
All credentials (CNIC, password, role) are securely stored in the MySQL database.

3) Admin Module
After admin login, the Admin Dashboard is displayed.

<img src="img/Admin Dahboard.png" width="700">
Admin Features:

View and manage user data
Send tax reminders to users if deadlines are missed
Update penalties
Manage tax rates
Update tax payment status (Paid / Unpaid)
Logout securely
/img/Admin Dahboard.png
Tax Rates Management (Admin)
When the Tax Rates button is clicked, the tax management menu appears.
<img src="img/Admin TaxRates.png" width="700">
Admin can:

Add tax rates
Update tax ranges and categories
Delete tax records
Modify tax rules as required
<img src="img/Manage Salary.png" width="650">
 <img src="img/Manage Property.png" width="650"> 
 <img src="img/Manage Vehicles.png" width="650"> 
 <img src="img/Manage GST.png" width="650">
Admin toogle status:
<img src="img/toggle status.png" width="600">
User Dashboard:
After user login, the User Dashboard appears.

<img src="img/User Dahboard.png" width="700">
User Dashboard Features

1️⃣ User Info
View and update personal information
Save updated profile data
<img src="img/User info.png" width="650">

2️⃣ Tax Rates (Read-Only)
View tax ranges and categories
Data is managed by admin
<img src="img/User taxrates.png" width="650"> 
<img src="img/User taxrates2.png" width="650">

3️⃣ Tax Calculation
Select tax type
Enter income/value
System calculates tax automatically
Sends calculated amount to payment module
<img src="img/tacCalculation.png" width="650">

4️⃣ Payment & Transactions
View payable tax amount
Click Pay Now to complete payment
<img src="img/payemnt and transaction.png" width="650">

5️⃣ History & Records
View complete history of paid taxes
<img src="img/History and records.png" width="650">

6️⃣ Alerts & Notifications
Receive tax reminders and admin notifications
<img src="img/Notifications.png" width="650">

7️⃣ Report Ready Data
Select paid tax
Generate documented tax slip
Save as PDF
Print report
<img src="img/report ready data 1.png" width="650"> 
<img src="img/report ready data 2.png" width="650">

8️⃣ Logout
User can securely log out of the system

UML Diagram
The project includes a UML Class Diagram that represents:
User and Admin classes
DAO classes
Database connection
Tax, Payment, and Notification modules

<img src="img/uml.jpeg" width="750">

Future Improvements

Online payment gateway integration
Enhanced security and encryption
Cloud-based database support
Advanced reporting and analytics
Mobile application version