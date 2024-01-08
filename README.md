# Starling Bank Technical Challenge Leon Dailani

This was really fun technical challenge and I learnt a lot from it! I want to thank Starling Bank for
considering my application and taking the time to go through my work.

I usually code in Python these days, so I found using Java to be a great revision opportunity!

## Description
This project is a Java-based application that interacts with the Starling Bank API to provide a 'round-up' feature for transactions.
It rounds up the transactions to the nearest pound and transfers the difference into a savings goal.

The approach that I have taken towards implementation is to use Main method to run the round up
directly.

I wanted to use Java as a learning opportunity for me. Recently, I have been very data and stats focused
and thus programming following strict OOP paradigms would be good for me.

The method works on one customer and takes the first account linked to them and calculates the round up
from the past week to date's transactions. A savings goal is then created (the name and target of which
can be modified in the main method). The balance of the account is checked to ensure that there are sufficient
funds before transferring the round-up amount into the newly created savings goal. The success of the
savings goal and the transfer are checked.

I have used Maven in order to build and manage dependencies efficiently.
I have used Jackson for parsing JSON from the API calls.

I have generated unit test cases using Junit.



## Features
- Retrieve account details
- Fetch transactions for a given week from the first account
- Calculate the round-up total for the week's transactions
- Create a savings goal
- Check the account balance is sufficient for transfer of funds
- Transfer the rounded-up amount to the savings goal

- Handles exceptions from the API calls effectively
- Keeps the access token secure

## Future Developments
- Handle multiple users (being able to apply this round-up method to multiple
users without having to manually swap the access token in and out).
- Handle multiple accounts (some users have multiple accounts and thus being able
to select which account to create the savings goal would be good).
- Instead of having to call main method, a REST resource that is invoked to trigger
the round-up is more representative of how such a feature would be implemented in real
life.


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

### Configuration

In order for the program to work, an access token for a Starling Bank customer is required.

To set up your access token securely, follow these steps:

Create a config.properties file in the src/main/resources directory.
Add your Starling Bank API access token to the file as follows: 
    starling.access.token=INSERT_YOUR_ACCESS_TOKEN_HERE

### Running the Program

Execute the main application using Maven:

mvn exec:java -Dexec.mainClass="com.leondailani.starlingroundup.Main"

### Testing

Run the unit tests using Maven:

mvn test




