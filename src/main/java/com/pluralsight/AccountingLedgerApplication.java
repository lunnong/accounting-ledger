package com.pluralsight;

public class AccountingLedgerApplication {
    public static void main(String[] args) {
        TransactionRepository repository = new TransactionRepository();
        TransactionService transactionService = new TransactionService(repository);

        // Uncomment the following two lines to use console control
        // Keep it commented out to use window control

//        MenuController menuController = new MenuController(transactionService);
//        menuController.run();

        // Comment out the next line to use console control
        // Keep it as is to use window control
        new WindowMenuController(transactionService);
    }
}
