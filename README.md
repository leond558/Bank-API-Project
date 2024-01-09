# Starling Bank Technical Challenge Leon Dailani

This was really fun technical challenge and I learnt a lot from it! I want to thank Starling Bank for
considering my application and taking the time to go through my work.

I usually code in Python these days, so I found using Java to be a great revision opportunity!

## Description
This project is a Java-based application that interacts with the Starling Bank API to provide a 'round-up' feature for transactions.
It rounds up the transactions to the nearest pound and transfers the difference into a savings goal.

The approach that I have taken towards implementation is to establish a simple embedded web server using Spark.

Making a call to this server with an access token pertaining to a Starling Bank customer, a name for the savings goal,
a target amount and the desired customer account to attach the saving goal allows for the round-up savings goal to be created.

I wanted to use Java as a learning opportunity for me. Recently, I have been very data and stats focused
and thus programming following strict OOP paradigms would be good for me.

The program assumes uses of GBP exclusively as the currency.

The program works by the following steps:
1. The embedded server is set up and the input parameters for a call to the server are parsed and validated.
2. The main round up functionality is then called. First all customer accounts are retrieved and the desired account is then isolated. The desired account is specified by an index when the server call is made. Index 0 would be the first account registered to a customer and this is also the option that the program will default to if the index provided does not have an account associated with it.
3. The transaction feed from the past week is retrieved. The past week is defined from the day the server call is made to 7 days back.
4. A hash is generated which combines the account and the time interval. If this hash already exists in hashes.txt, it indicates that a round-up savings goal for this account has already been made for this time frame and an exception is thrown. SHA-256 is used.
5. A savings goal is created.
5. The round-up is calculated and checked against the funds in the account.
6. If funds are sufficient, the round-up amount is transferred to the savings goal. If the funds< round-up amount, as much as possible is transferred into the goal. If there are no funds in the account, an exception is thrown.
7. After successful fund transfer, the hash for round-up the savings goal is stored.
8. A confirmation message detailing the saving goal creation is output.

Validation occurs throughout the program. The success of the savings goal and the transfer are checked and if there is a failure at any point, appropriate exceptions are thrown.

I have used:
- Maven in order to build and manage dependencies efficiently.
- Jackson for parsing JSON from the API calls.
- JUnit for unit test cases
- Spark for the embedded web server


## Features
- Retrieve account details
- Fetch transactions for a given week from a desired account
- Calculate the round-up total for the week's transactions
- Create a savings goal
- Check the account balance is sufficient for transfer of funds
- Transfer the rounded-up amount to the savings goal
- Handles exceptions from the API calls effectively
- Remembers what saving goals have been created over which time frame.
- Robust unit testing

Hashing has been used as to avoid storing accountUiDs in plain text.


## Future Developments
- Handle multiple users (being able to apply this round-up method to multiple
users without having to manually swap the access token in and out).
- Handle multiple accounts at once (some users have multiple accounts and thus being able
to select which account to create the savings goal would be good).
- Account for the potential use of foreign currencies in the transaction.
- Create a GUI for interaction with the server.


## Installation and setup

### Prerequisites
- Java JDK 11 or later
- Maven for building and managing dependencies
- Access to Starling Bank API (Access token)

### Setup
Explain how to get a local copy up and running.

1. Clone the repository: git clone https://github.com/leond558/LeonDailaniStarlingRoundUp.git
2. Navigate to the project directory: cd LeonDailaniStarlingRoundUp
3. Build the project with Maven: mvn clean install


### Running the Program

Execute the main application using Maven:

mvn exec:java -Dexec.mainClass="com.leondailani.starlingroundup.Main"

### Testing

Run the unit tests using Maven:

mvn test

### Running the round-up

With the program now running and the embedded web server now up,
creating a savings goal from the round up is now possible through
accessing the server through an API call or a curl command.

For instance, the server can be access using the following command in a UNIX terminal:

curl -X POST "http://localhost:8080/roundup" \
-d "access_token=YOUR_ACCESS_TOKEN" \
-d "savings_goal_name=YOUR_SAVINGS_GOAL_NAME" \
-d "savings_goal_amount=YOUR_SAVINGS_GOAL_AMOUNT" \
-d "account_index=YOUR_ACCOUNT_INDEX"

Or equivalently for use in a Microsoft command prompt terminal:

curl -X POST "http://localhost:8080/roundup" ^
-d "access_token=YOUR_ACCESS_TOKEN" ^
-d "savings_goal_name=YOUR_SAVINGS_GOAL_NAME" ^
-d "savings_goal_amount=YOUR_SAVINGS_GOAL_AMOUNT" ^
-d "account_index=YOUR_ACCOUNT_INDEX"

Where:
- YOUR_ACCESS_TOKEN = the access_token for the customer you are interested in
- YOUR_SAVINGS_GOAL_NAME = the name you want to give to the savings goal
- YOUR_SAVINGS_GOAL_AMOUNT = the amount that you want the savings goal to be
- YOUR_ACCOUNT_INDEX = the index of the account for the customer you want to attach the savings goal to (0 is the first account)

A success message will then output detailing the creation of the savings goal and the amount from the round-up that has been deposited in it!





