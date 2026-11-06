package com.pluralsight;

import javax.swing.JFrame;

public class WindowMenuController {

    public WindowMenuController() {

        JFrame frame = new JFrame();

        frame.setTitle("Accounting Ledger");

        frame.setSize(800, 600);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
}