package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TransactionRepository {
    String filename;

    public ArrayList<Transaction> load(String filename) {
        this.filename = filename;
        ArrayList<Transaction> ledger = new ArrayList<Transaction>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String inputLine = bufferedReader.readLine();
            LocalDate date;
            LocalTime time;
            double amount;
            while((inputLine = bufferedReader.readLine()) != null) {
                String[] input = inputLine.split("\\|");
                for (int i = 0; i < input.length; i++) {
                    input[i] = input[i].trim();
                }
                date = LocalDate.parse(input[0]);
                time = LocalTime.parse(input[1]);
                amount = Double.parseDouble(input[4]);
                Transaction transaction = new Transaction(date, time, input[2], input[3], amount);
                ledger.add(transaction);
            }
            bufferedReader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ledger;
    }

    public void save(ArrayList<Transaction> ledger) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            String text;
            bw.write("date|time|description|vendor|amount");
            for (Transaction t : ledger) {
                text = String.format("\n%s|%s|%s|%s|%f",
                                     t.getDate(), t.getTime(), t.getDesc(), t.getVendor(), t.getAmount());
                bw.write(text);
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
