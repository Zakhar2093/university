# University

## I'm glad that you are looking at my project
   It is a copy of my educational project in GitLab    
   You can check this deployed project [here](https://university-zakhar.herokuapp.com/). Feel free to add or update data.    
   I continue to develop the project with the supervision of my mentor who prevents me from writing non-industry code.

## The purpose of the project
The main feature of the application is Class Timetable for students and teachers. Students or teachers can get their timetable for a day or for a month.    
##### Also you CAN:    
- add entities(Teacher, Room, Group, Student, Lesson);
- perform CRUD operations;
- assign students to a group;
- assign a group, a teacher and a room to a lesson.

##### To prevent making mistakes you CAN NOT: 
- save entities with empty fields. But you can still save e.g. lesson without group, if you need it;
- save lesson with entity which is busy in another lesson;
- save lesson with past date;
- save lesson if room's capacity less than group's size.

## What I already done:
:white_check_mark: Created the [UML diagram](https://github.com/Zakhar2093/university/blob/dev/UML/university.PNG) which represents all entities and relationships between them;    
:white_check_mark: Created DAO layer using **Spring JDBC Template**(The plain **JDBC** I'd studied in previous project);   
:white_check_mark: Used **PostgreSQL** as main DB and **H2** as test DB;    
:white_check_mark: Added service layer, exceptions and logging;    
:white_check_mark: Used **JUnit** and **Mockito** for tests;    
:white_check_mark: Added UI layer using **Spring MVC, HTML, Bootstrap, Thymeleaf**;    
:white_check_mark: Rewrote DAO layer using **Hibernate JPA**;    
:white_check_mark: Added **Spring Boot** support;     
:white_check_mark: Rewrote DAO layer again using **Spring Data JPA**;    
:white_check_mark: Added **Validation**;    
:white_check_mark: Added **REST api**;    
:white_check_mark: Added **Swagger** for automatic rest documentation.

## What I plan:
:white_large_square: add integration tests;    
:white_large_square: add **Spring security**.
