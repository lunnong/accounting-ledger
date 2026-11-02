package com.pluralsight;

public class AccountingLedgerApplication {
    static void main(String[] args) {
        TransactionRepository repository = new TransactionRepository();
        TransactionService tController = new TransactionService(repository);
        MenuController mController = new MenuController(tController);
    }
}
