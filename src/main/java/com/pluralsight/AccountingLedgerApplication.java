package com.pluralsight;

import java.util.ArrayList;

public class AccountingLedgerApplication {
    static void main(String[] args) {
        TransactionRepository repository = new TransactionRepository();
        ArrayList<Transaction> ledger = repository.readCSV("transactions.csv");
        System.out.println(ledger);
    }
}
