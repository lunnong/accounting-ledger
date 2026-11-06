package com.pluralsight;

public class AccountingLedgerApplication {
    public static void main(String[] args) {
        TransactionRepository repository = new TransactionRepository();
        TransactionService transactionService = new TransactionService(repository);
        MenuController menuController = new MenuController(transactionService);

        menuController.run();
    }
}
