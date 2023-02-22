# Technical Test

This Project is based on Spring Boot and Java 11, with following Features:
* add a deposit to an account based on a company available balance
* Retrieve account with balance of gift and meal ( calculation done indirectly by job - 
verification of consumable balance logical limit)
    
## Architecture

The Database Model include 4 Tables :
* User : user definition table
* Account : User accounts can have many (personal , professional ...)
* Company : company definition table
* deposit : a deposit in relation with account and company responsible for the transfer
    
## Technical Features

* Jpa
    * H2 Base [console](http://localhost:8080/h2-console)
* scheduler
* mapper
* Documentation
    * Swagger ([link](http://localhost:8080/swagger-ui.html) for html page)
    * Postman collection [link](./Glady.postman_collection.json)
* Testing
    * Junit & Mockito
* Spring Validation

## Business Aspect

### deposits

deposits have a required start date by the api:
* case 1 : deposit start time too old -> deposit will not be taken
* case 2 : deposit have the right start date -> deposit will be taken and the balance of related account will be
 changed directly according to type
* case 3 : deposit start date is in the future -> deposit will be added to database but account balance will not be changed .
later on the account balance will be changed by a job at the exact start date

### Balance of account

Active balance(by type) :
    refer to the balance in the user account -> means we can have a case where the user did use part of his balance before in a transaction ( no table so theory )

Consumable balance(by type):
    refer to the sum of the amount of deposits by type with startDate<=current date<expirationDate of deposit

<b>RULE</b> : Consumable balance should always be bigger than active balance

if ACTIVE_BALANCE > CONSUMABLE_BALANCE -> old deposits still in active balance -> floor to active balance

### jobs for balance synch

#### gift job

run daily at 00:00 to update gift balance of users based on gift deposits:
* add the deposits that were saved but not treated ( because start date was in future )
* compare active balance with consumable balance -> floor in case of higher active balance
    -> lower or equal we leave it as it is because the user didn't use his balance or used some of it
    
#### meal job
run in the first of march of every year at 00:00 
same as gift job but for meal type deposit and balance

## NOTES

in a real life scenario db initiation should be based on liquibase...

tests are basic example not related to project coverage

maybe a more dynamic way to take in balance by type in account

batch to replace job for chunks treatment of large data

