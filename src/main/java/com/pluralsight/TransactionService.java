package com.pluralsight;

import java.time.LocalDate;
import java.util.ArrayList;

public class TransactionService {

    private final ArrayList<Transaction> ledger;
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
        this.ledger = repository.load("transactions.csv");
    }

    public ArrayList<Transaction> getLedger() {
        ArrayList<Transaction> reverseLedger = new ArrayList<Transaction>();
        for (int i = ledger.size() - 1; i >= 0; i--) {
            reverseLedger.add(ledger.get(i));
        }
        return reverseLedger;
    }

    public void addTransaction(String desc, String vendor, double amount) {
        Transaction t = new Transaction(desc, vendor, amount);
        ledger.add(t);
        repository.save(ledger);
    }

    public ArrayList<Transaction> getDeposits() {
        ArrayList<Transaction> deposits = new ArrayList<>();
        for (int i = ledger.size() - 1; i >= 0; i--) {
            Transaction t = ledger.get(i);
            if (t.getAmount() > 0) {
                deposits.add(t);
            }
        }
        return deposits;
    }

    public ArrayList<Transaction> getPayments() {
        ArrayList<Transaction> payments = new ArrayList<>();
        for (int i = ledger.size() - 1; i >= 0; i--) {
            Transaction t = ledger.get(i);

            if (t.getAmount() < 0) {
                payments.add(t);
            }
        }
        return payments;
    }

    public ArrayList<Transaction> getByVendor(String vendor) {
        ArrayList<Transaction> byVendor = new ArrayList<>();
        vendor = vendor.trim();
        for (int i = ledger.size() - 1; i >= 0; i--) {
            Transaction t = ledger.get(i);

            if (t.getVendor().equalsIgnoreCase(vendor)) {
                byVendor.add(t);
            }
        }
        return byVendor;
    }

    public ArrayList<Transaction> getMonthToDate() {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now();
        return getSubsetDate(startDate, endDate);
    }

    public ArrayList<Transaction> getPreviousMonth() {
        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1);
        return getSubsetDate(startDate, endDate);
    }

    public ArrayList<Transaction> getYearToDate() {
        LocalDate startDate = LocalDate.now().withDayOfYear(1);
        LocalDate endDate = LocalDate.now();
        return getSubsetDate(startDate, endDate);
    }

    public ArrayList<Transaction> getPreviousYear() {
        LocalDate startDate = LocalDate.now().minusYears(1).withDayOfYear(1);
        LocalDate endDate = LocalDate.now().withDayOfYear(1).minusDays(1);

        return getSubsetDate(startDate, endDate);
    }

    private ArrayList<Transaction> getSubsetDate(LocalDate startDate, LocalDate endDate) {
        ArrayList<Transaction> subset = new ArrayList<>();
        for (int i = ledger.size() - 1; i >= 0; i--) {
            Transaction t = ledger.get(i);
            LocalDate transactionDate = t.getDate();

            if (transactionDate.isBefore(startDate)) {
                break;
            }
            boolean beforeEnd = !transactionDate.isAfter(endDate);

            if (beforeEnd) {
                subset.add(t);
            }
        }
        return subset;
    }

    public ArrayList<Transaction> getFilteredSubset(LocalDate startDate, LocalDate endDate, String desc, String vendor, Double lowAmount, Double highAmount) {
        ArrayList<Transaction> subset = new ArrayList<>();

        for (int i = ledger.size() - 1; i >=0; i--) {
            Transaction t = ledger.get(i);

            LocalDate transactionDate = t.getDate();

            if (startDate != null && transactionDate.isBefore(startDate)) {
                break;
            }

            boolean beforeEnd =
                    endDate == null || !transactionDate.isAfter(endDate);

            boolean matchesDesc =
                    desc.isEmpty() || t.getDesc().equalsIgnoreCase(desc);

            boolean matchesVendor =
                    vendor.isEmpty() || t.getVendor().equalsIgnoreCase(vendor);

            boolean matchesAmount =
                    (lowAmount == null || t.getAmount() >= lowAmount) &&
                    (highAmount == null || t.getAmount() <= highAmount);

            if (beforeEnd && matchesDesc && matchesVendor && matchesAmount) {
                subset.add(t);
            }
        }

        return subset;
    }



}