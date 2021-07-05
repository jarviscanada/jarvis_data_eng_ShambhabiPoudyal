# Introduction
This application is designed to read, write, update and delete (CURD) data against RDBMS using Java DataBase Connectivity
(JDBC). JDBC allows a connection between a Java application and RDBMS. Here, PostgreSql is used for RDBMS while maintaining
maven project management layout. Different Data Access Objects(DAOs) are used to represent objects that may be 
simple rows or complex queries. Data Transfer objects(DTOs) are created using these DAOs.

# Implementaiton
## ER Diagram
<p align="center">
	<img src="https://github.com/jarviscanada/jarvis_data_eng_ShambhabiPoudyal/blob/develop/core_java/assets/hplussport_ER_diagram.png">
</p>


## Design Patterns
Here, DAO design pattern is used to create Customer and Order DAOs and later simply call these DAOs to perform 
the required CRUD operation. This mechanism allows the user to isolate the visualization of the application layer
from the RDBMS using an abstract API. This makes it easier in this application to create an instance of the 
object in the JDBCExecutor class and use the CustomerDAO or OrderDAO interfaces to actually communicate with the database.
</br>
Repository design involves adding a layer between the domain and data mapping layers to isolate domain objects from 
details of the database access code and to minimize scattering and duplication of query code. 
The Repository pattern is 
especially useful in systems where number of domain classes is large or heavy querying is utilized.

# Test
This app is tested using JDBCExecutor class and executing the CustomerDAO or OrderDAO. IntelliJ is used 
to create the output to the SQL queries which is also compared to that of the DBeaver app.
