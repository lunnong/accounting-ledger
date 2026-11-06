# Accounting Ledger Application

## Overview

The Accounting Ledger Application is a Java-based console application that allows users to record, store, and analyze financial transactions. Transactions are persisted in a CSV file and can be viewed through a menu-driven interface.

Users can add deposits and payments, review transaction history, generate standard financial reports, and perform custom searches using multiple filter criteria.

## Features

### Transaction Management
- Add deposits
- Add payments
- Automatically record transaction date and time
- Save transactions to a CSV file

### Ledger Views
- Display all transactions
- Display deposits only
- Display payments only
- Display transactions in reverse chronological order (newest first)

### Reporting
- Month-to-Date transactions
- Previous Month transactions
- Year-to-Date transactions
- Previous Year transactions
- Search transactions by vendor
- Custom transaction search using:
    - Date range
    - Description
    - Vendor
    - Amount range

### Data Persistence
- Loads transaction data from `transactions.csv` at startup
- Automatically saves changes to the CSV file after new transactions are added

## Technologies Used

- Java
- Object-Oriented Programming (OOP)
- File I/O (`BufferedReader`, `BufferedWriter`)
- Collections Framework (`ArrayList`)
- Java Time API (`LocalDate`, `LocalTime`)

## Project Structure

```text
src/
├── AccountingLedgerApplication.java
├── MenuController.java
├── Transaction.java
├── TransactionService.java
└── TransactionRepository.java
```

### Class Responsibilities

| Class | Responsibility |
|---------|---------------|
| AccountingLedgerApplication | Application entry point |
| MenuController | User interface and menu navigation |
| Transaction | Transaction data model |
| TransactionService | Business logic and transaction filtering |
| TransactionRepository | CSV file loading and saving |

## How to Run

### Prerequisites

- Java JDK 17 or later
- An IDE such as IntelliJ IDEA

### Running the Application

1. Clone or download the project.
2. Ensure a `transactions.csv` file exists in the project directory.
3. Open the project in IntelliJ IDEA.
4. Choose either console control or window control by commenting out 
5. Run the `AccountingLedgerApplication` class.
6. Use the console to interact with the ledger.

### Example CSV Format

```csv
date|time|description|vendor|amount
2026-11-01|08:00:00|Paycheck|Contoso Corp|2800.00
2026-11-03|12:15:00|Coffee|Starbucks|-6.25
```
## Future Improvements
- [x] Graphical User Interface (GUI) using Java Swing 
- [ ] Support for multiple ledger files
    - Allow the user to select a CSV file when opening the application.
    - Allow the user to save ledger data to a user-specified file


## Author: Lunn Ong

Created as a Java accounting ledger application project to demonstrate:
- Java fundamentals
- Object-oriented design
- Layered application architecture
- File persistence
- Collection processing
- User input validation
- Transaction reporting and filtering
- Swing fundamentals