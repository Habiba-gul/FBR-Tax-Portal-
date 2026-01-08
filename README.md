<h1>FBR-TAX-PORTAL</h1>

<h2>Project Overview</h2>
<p>
<strong>FBR-TAX-PORTAL</strong> is a desktop-based <strong>Taxpayer Management System</strong>
developed as an <strong>Object-Oriented Programming (OOP) Java project</strong>.
</p>

<p>
The system simulates a user-friendly tax portal where taxpayers can register,
calculate taxes, make payments, and manage tax records, while administrators
can manage tax rates, users, and notifications.
</p>

<h3>OOP Principles Used</h3>
<ul>
  <li>Encapsulation</li>
  <li>Inheritance</li>
  <li>Polymorphism</li>
  <li>Frontend & Backend Separation</li>
</ul>

<p>
The project uses <strong>JavaFX</strong> for a modern graphical user interface and
<strong>MySQL</strong> for secure data storage and management.
</p>

<hr>

<h2>How to Run the Project</h2>
<ol>
  <li>Install <strong>JDK 25</strong></li>
  <li>Install <strong>MySQL Server</strong> and <strong>MySQL Workbench</strong></li>
  <li>Add <strong>MySQL Connector/J (JAR file)</strong> to the project classpath</li>
  <li>Update database credentials in <code>DBconnection.java</code></li>
  <li>Open the project in <strong>VS Code</strong></li>
  <li>Run the <strong>Main</strong> class</li>
</ol>

<hr>

<h2>How the System Works</h2>
<p>
When the program is executed, a splash screen appears for 5 seconds, after which
the main workflow starts.
</p>

<h3>Login & Registration</h3>
<p>When the application starts, the Login Interface appears.</p>
<img src="img/login.png" width="700">

<h3>Registration</h3>
<p>If the user is new, they click <strong>Register Now</strong>.</p>
<img src="img/register now.png" width="700">

<p>There are two types of registration:</p>
<ul>
  <li>User Registration</li>
  <li>Admin Registration</li>
</ul>
<p>
All credentials (CNIC, password, and role) are securely stored in the MySQL database.
</p>

<hr>

<h2>Admin Module</h2>
<p>After admin login, the Admin Dashboard is displayed.</p>
<img src="img/Admin Dashboard.png" width="700">

<h3>Admin Features</h3>
<ul>
  <li>View and manage user data</li>
  <li>Send tax reminders if deadlines are missed</li>
  <li>Update penalties</li>
  <li>Manage tax rates</li>
  <li>Update tax payment status (Paid / Unpaid)</li>
  <li>Logout securely</li>
</ul>

<h3>Tax Rates Management (Admin)</h3>
<p>When the Tax Rates button is clicked, the tax management menu appears.</p>
<img src="img/Admin TaxRates.png" width="700">

<p>Admin can:</p>
<ul>
  <li>Add tax rates</li>
  <li>Update tax ranges and categories</li>
  <li>Delete tax records</li>
  <li>Modify tax rules</li>
</ul>

<img src="img/Manage Salary.png" width="650">
<img src="img/Manage Property.png" width="650">
<img src="img/Manage Vehicles.png" width="650">
<img src="img/Manage GST.png" width="650">

<h3>Tax Status Toggle</h3>
<img src="img/toggle status.png" width="600">

<hr>

<h2>User Dashboard</h2>
<p>After user login, the User Dashboard appears.</p>
<img src="img/User Dahboard.png" width="700">

<h3>User Dashboard Features</h3>

<h4>1- User Info</h4>
<p>View and update personal information.</p>
<img src="img/User info.png" width="650">

<h4>2- Tax Rates (Read-Only)</h4>
<p>View tax ranges and categories managed by admin.</p>
<img src="img/User taxrates.png" width="650">
<img src="img/User taxrates2.png" width="650">

<h4>3- Tax Calculation</h4>
<p>
Select tax type, enter income/value, and the system calculates tax automatically
and sends it to the payment module.
</p>
<img src="img/tacCalculation.png" width="650">

<h4>4- Payment & Transactions</h4>
<p>View payable tax amount and click Pay Now to complete payment.</p>
<img src="img/payemnt and transaction.png" width="650">

<h4>5- History & Records</h4>
<p>View complete history of paid taxes.</p>
<img src="img/History and records.png" width="650">

<h4>6- Alerts & Notifications</h4>
<p>Receive tax reminders and admin notifications.</p>
<img src="img/Notifications.png" width="650">

<h4>7- Report Ready Data</h4>
<p>
Select paid tax, generate documented tax slip, save as PDF, and print report.
</p>
<img src="img/report ready data 1.png" width="650">
<img src="img/report ready data 2.png" width="650">

<h4>8- Logout</h4>
<p>User can securely log out of the system.</p>

<hr>

<h2>UML Diagram</h2>
<p>
The UML Class Diagram represents:
</p>
<ul>
  <li>User and Admin classes</li>
  <li>DAO classes</li>
  <li>Database connection</li>
  <li>Tax, Payment, and Notification modules</li>
</ul>
<img src="img/uml.jpeg" width="750">

<hr>

<h2>Future Improvements</h2>
<ul>
  <li>Online payment gateway integration</li>
  <li>Enhanced security and encryption</li>
  <li>Cloud-based database support</li>
  <li>Advanced reporting and analytics</li>
  <li>Mobile application version</li>
</ul>
