package com.pluralsight;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuController {
    private final Scanner scanner;
    private final TransactionService service;

    public MenuController(TransactionService service) {
        this.scanner = new Scanner(System.in);
        this.service = service;
    }

    public void run() {
        boolean running = true;
        while (running) {
            String input = showMenuScreen();

            switch (input) {

                case "D":
                    addDeposit();
                    pause("Deposit Added to Ledger!");
                    break;

                case "P":
                    addPayment();
                    pause("Payment Added to Ledger!");
                    break;

                case "L":
                    processLedgerMenu();
                    break;

                case "E":
                    running = false;
                    System.out.println("Thank you for using the Accounting Ledger");
                    System.out.println("Good bye!");
                    break;

                default:
                    pause("That isn't a valid choice!");
            }

        }
    }

    private String showMenuScreen() {
        System.out.println("""
        ==============================
            ACCOUNTING LEDGER MENU
        ==============================
        D) Add Deposit
        P) Make Payment
        L) Ledger
        E) Exit
        """);
        return scanner.nextLine().trim().toUpperCase();
    }

    private String showLedgerMenu() {
        System.out.println("""
        ==============================
                  LEDGER MENU
        ==============================
        A) All - Display all entries
        D) Deposit - Display all deposits
        P) Payments - Display all payments
        R) Reports - Custom Search
        H) Home
        """);
        return scanner.nextLine().trim().toUpperCase();
    }

    private String showReportMenu() {
        System.out.println("""
        ==============================
                 REPORTS MENU
        ==============================
        1) Month To Date
        2) Previous Month
        3) Year To Date
        4) Previous Year
        5) Search by Vendor
        6) Custom Search
        0) Back
        """);
        return scanner.nextLine().trim().toUpperCase();
    }

    private void addDeposit() {
        System.out.println("Adding Deposit to Ledger");
        System.out.print("Enter the deposit description: ");
        String desc = scanner.nextLine();
        System.out.print("Enter the depositor's name: ");
        String vendor = scanner.nextLine();
        double amount = 0;
        while (amount <= 0) {
            System.out.print("Enter the amount deposited: $");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.out.println("Enter a number greater than 0");
                }
            } catch (Exception e) {
                System.out.println("That isn't a number");
                amount = 0;
            }
        }
        service.addTransaction(desc, vendor, amount);
    }

    private void addPayment() {
        System.out.println("Adding Payment to Ledger");
        System.out.print("Enter the payment description: ");
        String desc = scanner.nextLine();
        System.out.print("Enter the name of the recipient vendor: ");
        String vendor = scanner.nextLine();
        double amount = 0;
        while (amount <= 0) {
            System.out.print("Enter the amount paid: $");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.out.println("Enter a number greater than 0");
                }
            } catch (Exception e) {
                System.out.println("That isn't a number");
                amount = 0;
            }
        }
        service.addTransaction(desc, vendor, -amount);
    }

    private void processLedgerMenu() {
        boolean ledgerMenuRunning = true;
        while (ledgerMenuRunning) {
            String input = showLedgerMenu();
            switch (input) {
                case "A":
                    System.out.println("""
                    ==============================
                           ALL TRANSACTIONS
                    ==============================
                    """);
                    System.out.println(displayLedger(service.getLedger()));
                    pause();
                    break;
                case "D":
                    System.out.println("""
                    ==============================
                             ALL DEPOSITS
                    ==============================
                    """);
                    System.out.println(displayLedger(service.getDeposits()));
                    pause();
                    break;
                case "P":
                    System.out.println("""
                    ==============================
                             ALL PAYMENTS
                    ==============================
                    """);
                    System.out.println(displayLedger(service.getPayments()));
                    pause();
                    break;
                case "R":
                    processReportsMenu();
                    break;
                case "H":
                    ledgerMenuRunning = false;
                    pause("Returning to Previous Menu");
                    break;
                default:
                    pause("That isn't a valid choice!");
            }
        }
    }

    private void processReportsMenu() {
        boolean reportsMenuRunning = true;
        while (reportsMenuRunning) {
            String input = showReportMenu();
            switch (input) {
                case "1":
                    System.out.println("""
                    ==============================
                      MONTH-TO-DATE TRANSACTIONS
                    ==============================
                    """);
                    System.out.println(displayLedger(service.getMonthToDate()));
                    pause();
                    break;
                case "2":
                    System.out.println("""
                    ==============================
                      PREVIOUS MONTH TRANSACTIONS
                    ==============================
                    """);
                    System.out.println(displayLedger(service.getPreviousMonth()));
                    pause();
                    break;
                case "3":
                    System.out.println("""
                    ==============================
                       YEAR-TO-DATE TRANSACTIONS
                    ==============================
                    """);
                    System.out.println(displayLedger(service.getYearToDate()));
                    pause();
                    break;
                case "4":
                    System.out.println("""
                    ==============================
                      PREVIOUS YEAR TRANSACTIONS
                    ==============================
                    """);
                    System.out.println(displayLedger(service.getPreviousYear()));
                    pause();
                    break;
                case "5":
                    System.out.print("Enter the vendor name: ");
                    String vendor = scanner.nextLine().trim();
                    System.out.println("""
                    ==============================
                     VENDOR FILTERED TRANSACTIONS
                    ==============================
                    """);
                    System.out.println(displayLedger(service.getByVendor(vendor)));
                    pause();
                    break;
                case "6":
                    customSearch();
                    pause();
                    break;
                case "0":
                    reportsMenuRunning = false;
                    break;
                default:
                    pause("That isn't a valid choice!");
            }
        }
    }

    private void customSearch() {
        System.out.println("""
                You will be prompted for:
                
                Start Date/End Date - Transactions between these dates (inclusive) will be shown.
                Description         - Transactions with this description will be shown.
                Vendor              - Transactions with this vendor will be shown.
                Amount Range        - Transactions between these amounts will be shown.
                
                If you do not want to filter based on a category, press enter without input.
                Input is not case sensitive.
                """);

        LocalDate startDate = promptDate("Enter the start date (YYYY-MM-DD): ");
        LocalDate endDate = promptDate("Enter the end date (YYYY-MM-DD): ");

        System.out.print("Enter the description: ");
        String desc = scanner.nextLine().trim();

        System.out.print("Enter the vendor: ");
        String vendor = scanner.nextLine().trim();

        Double lowAmount = promptAmount("Enter minimum amount: ");
        Double highAmount = promptAmount("Enter maximum amount: ");
        if (lowAmount != null &&
            highAmount != null &&
            lowAmount > highAmount) {

            double temp = lowAmount;
            lowAmount = highAmount;
            highAmount = temp;
        }

        System.out.println("""
        ==============================
         CUSTOM FILTERED TRANSACTIONS
        ==============================
        """);
        System.out.println(displayLedger(service.getFilteredSubset(
                startDate, endDate, desc, vendor, lowAmount, highAmount
        )));
    }

    private Double promptAmount(String prompt) {
        System.out.print(prompt);

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }

            try {
                return Double.parseDouble(input);
            }
            catch (Exception e) {
                System.out.print("That's not a valid amount. Try again: ");
            }
        }
    }

    private LocalDate promptDate(String prompt) {
        System.out.print(prompt);

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(input);
            }
            catch (Exception e) {
                System.out.print("That's not a valid date. Try again: ");
            }
        }
    }

    private String displayLedger(ArrayList<Transaction> transactionsToDisplay) {
        StringBuilder output = new StringBuilder("Date | Time | Description | Vendor | Amount");
        for (Transaction t : transactionsToDisplay) {
            output.append("\n");
            output.append(t);
        }
        return output.toString();
    }

    private void pause(String message) {
        System.out.println(message);
        pause();
    }

    private void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }


}
