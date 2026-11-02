package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TransactionRepository {
    public ArrayList<Transaction> readCSV(String filename) {
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
}
