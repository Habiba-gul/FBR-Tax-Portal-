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
Following files and ides are required to run program:
JDK 25
MySQL Server and MySQL Workbench.
MySQL Connector/J  and JAR file to the project classpath.
VS code

How the System Works:
when program is run a splash screen appears for 5sec and then
1) Login & Registration
When the program starts, the Login Interface appears.
/img/login.png
 
2) Registration
If the user is new, they click Register Now.
 /img/register now.png
 
There are two types of registration:
1- User Registration
2- Admin Registration
All credentials (CNIC, password, role) are securely stored in the MySQL database.

3) Admin Module
After admin login, the Admin Dashboard is displayed.

/img/Admin Dahboard.png
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
/img/Admin TaxRates.png
Admin can:

Add tax rates
Update tax ranges and categories
Delete tax records
Modify tax rules as required
 /img/Manage Salary.png
 /img/Manage Property.png
 /img/Manage Vehicles.png
 /img/Manage GST.png
Admin toogle status:
/img/toggle status.png
User Dashboard:
After user login, the User Dashboard appears.

/img/User Dahboard.png
User Dashboard Features

1️⃣ User Info
View and update personal information
Save updated profile data
 /img/User info.png

2️⃣ Tax Rates (Read-Only)
View tax ranges and categories
Data is managed by admin
 /img/User taxrates.png
 /img/User taxrates2.png

3️⃣ Tax Calculation
Select tax type
Enter income/value
System calculates tax automatically
Sends calculated amount to payment module
 /img/tacCalculation.png

4️⃣ Payment & Transactions
View payable tax amount
Click Pay Now to complete payment
 /img/payemnt and transaction.png

5️⃣ History & Records
View complete history of paid taxes
/img/payemnt and transaction.png

6️⃣ Alerts & Notifications
Receive tax reminders and admin notifications
/img/Notifications.png
7️⃣ Report Ready Data
Select paid tax
Generate documented tax slip
Save as PDF
Print report
/img/report ready data 1.png
/img/report ready data 1.png

8️⃣ Logout
User can securely log out of the system

UML Diagram
