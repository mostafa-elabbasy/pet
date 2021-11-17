# Pet task
A simple Spring boot application that sells a dogs for a pet shop

## Tools and Technologies used

* Java 11
* Spring boot 2.5.6
* MySQL
* JPA
* Hibernate
* Maven
* Spring Data 
* Spring MVC Validation  
* Maven Jacoco Plugin
* Unit Testing


# For Spring boot


## Steps to install


**1. Create MySQL database**

```sql
CREATE DATABASE pet
```
	
	
**2. Change MySQL Username and Password as per your MySQL Installation**
	
+ open `src/main/resources/application.properties` file.

+ change `spring.datasource.username` and `spring.datasource.password` as per your installation
	
**3. Run the app**

You can run the spring boot app by typing the following command -

```bash
mvn spring-boot:run
```

The server will start on port 8080.

**3. Run the app tests and create coverage report**

You can run the spring boot app by typing the following command -

```bash
mvn clean package spring-boot:repackage

```

the report will create in target/site/jacoco
you can open it from target/site/jacoco/index.html
