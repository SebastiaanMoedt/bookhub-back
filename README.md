# BookhubBack

This project was generated using Java 21 and SpringBoot 3.5.13.

## To run project

- Create an empty database in SQL Server named BOOKHUB
- Run application using the Run button in your IDE
- Run SQL scripts in /sql folder :
1. /sql_data : execute programs in the following order : 1-category.sql, 2-book.sql, 3-book_copy.sql, user.sql
2. /sp_trg : execute the 2 programs in order to have stocked procedures, triggers and views available on your database

Use the associated front application (bookhub-front) or Postmann to test the backend application.

The backend server will run locally at the following URL: http://localhost:8080/.