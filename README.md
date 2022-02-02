# TradeStore
# Swagger URL:
http://localhost:8080/swagger-ui.html#/

# Securing API's:
Using Spring Basic Authentication.

UserName: test 

Password:test

# Database:

H2 Database

# API's:

# Get All Trades:
GET - http://localhost:8080/trades

# Get Trade by TradeId
GET - http://localhost:8080/trades/IT1

# Save Trade
POST - http://localhost:8080/trades

<B>Request</B>:

{
"tradeStore":
{
"tradeId": "T1",
"version": 3,
"counterPartyId": "CP-4",
"bookId": "B3",
"maturityDate": "2022-05-20",
"createdDate": "2020-05-18",
"expired": "N"
}
}

# Unit Test Cases
- TradeStoreManagementControllerMockTest.java
- TradeStoreManagementServiceMockTest.java

# Integration Test Cases
- TradeStoreManagementControllerIntegrationTest.java
