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
        return new ArrayList<Transaction>(ledger);
    }

    public void addTransaction(String desc, String vendor, double amount) {
        Transaction t = new Transaction(desc, vendor, amount);
        ledger.add(t);
        repository.save(ledger);
    }

    public ArrayList<Transaction> getDeposits() {
        ArrayList<Transaction> deposits = new ArrayList<>();
        for (Transaction t : ledger) {
            if (t.getAmount() > 0) {
                deposits.add(t);
            }
        }
        return deposits;
    }

    public ArrayList<Transaction> getPayments() {
        ArrayList<Transaction> payments = new ArrayList<>();
        for (Transaction t : ledger) {
            if (t.getAmount() < 0) {
                payments.add(t);
            }
        }
        return payments;
    }

    public ArrayList<Transaction> getByVendor(String vendor) {
        ArrayList<Transaction> byVendor = new ArrayList<>();
        vendor = vendor.trim();
        for (Transaction t : ledger) {
            if (t.getVendor().equalsIgnoreCase(vendor)) {
                byVendor.add(t);
            }
        }
        return byVendor;
    }

    public ArrayList<Transaction> getMonthToDate() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.withDayOfMonth(1);
        return getSubsetDate(startDate, endDate);
    }

    public ArrayList<Transaction> getPreviousMonth() {
        LocalDate endDate = LocalDate.now().withDayOfMonth(1);
        LocalDate startDate = endDate.minusMonths(1);
        return getSubsetDate(startDate, endDate);
    }

    public ArrayList<Transaction> getYearToDate() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.withDayOfYear(1);
        return getSubsetDate(startDate, endDate);
    }

    public ArrayList<Transaction> getPreviousYear() {
        LocalDate endDate = LocalDate.now().withDayOfYear(1);
        LocalDate startDate = endDate.minusYears(1);
        return getSubsetDate(startDate, endDate);
    }

    public ArrayList<Transaction> getSubsetDate(LocalDate startDate, LocalDate endDate) {
        ArrayList<Transaction> subset = new ArrayList<>();
        for (Transaction t : ledger) {
            LocalDate transactionDate = t.getDate();
            boolean isPreviousMonth = !transactionDate.isBefore(startDate) &&
                    transactionDate.isBefore(endDate);
            if (isPreviousMonth) {
                subset.add(t);
            }
        }
        return subset;
    }



}