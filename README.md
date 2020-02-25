# Money Transfer Application

## Task Description
Design and implement a RESTful API (including data model and the backing implementation) for money transfers between accounts.

### The project uses the following technologies:
* Java 8
* Dropwizard
* Hibernate
* H2 (In Memory Database) 
* Gradle
* JUnit
* Mockito

### Running the application: ###
In the terminal, go to project's root directory and run the following command

    ```
    gradlew run
    ```
   
 This will create H2 file database (revolut_db.mv.db) and the application will be up and running on port 8888 i.e. http://localhost:8888/
 
### Exposed API Endpoints
 
 | Http method | Endpoint                                        | Sample Request Body                                                        | Description                                                       |
 |-------------|-------------------------------------------------|----------------------------------------------------------------------------|-------------------------------------------------------------------|
 | POST        | /account                                        | {"name" : "Chirag","balance" : 1010.11}                                    | This endpoint creates a new account with given balance.           |
 | GET         | /account/{:account_id}                          |                                                                            | This endpoint fetches the details of a given account.             |
 | POST        | /tansfer                                        | {"sourceAccountNumber": 1,"destinationAccountNumber": 2,"amount": 10.11}   | This endpoint performs money transfers between given two accounts.|
 
### Concurrency Management of Money Transfers:
1. ```@UnitOfWork``` will automatically open a session, begin a transaction, perform database operations, commit the transaction, and finally close the session. If an exception is thrown, the transaction is rolled back
2. ```Optimistic Lock``` with Hibernate is implemented by applying the @Version annotation to a version instance variable resulting in a VERSION column
