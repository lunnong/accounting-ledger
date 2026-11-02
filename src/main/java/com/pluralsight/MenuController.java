package com.pluralsight;


import java.util.Scanner;

public class MenuController {
    private Scanner scanner;
    private TransactionService service;
    public MenuController(TransactionService service) {
        this.scanner = new Scanner(System.in);
        this.service = service;
    }

    public void run() {

    }
}
