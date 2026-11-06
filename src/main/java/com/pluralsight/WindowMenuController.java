package com.pluralsight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class WindowMenuController {
    private final TransactionService service;

    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    // Used by the results screen
    private final JLabel resultsTitleLabel;
    private final DefaultTableModel tableModel;
    private String backCard = "LEDGER";

    public WindowMenuController(TransactionService service) {
        this.service = service;

        frame = new JFrame("Accounting Ledger");
        frame.setSize(750, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Results screen pieces (created here so the other methods can reuse them)
        resultsTitleLabel = new JLabel("", SwingConstants.CENTER);
        String[] columnNames = {"Date", "Time", "Description", "Vendor", "Amount"};
        tableModel = new DefaultTableModel(columnNames, 0);

        cardPanel.add(createMainMenuPanel(), "MAIN");
        cardPanel.add(createLedgerMenuPanel(), "LEDGER");
        cardPanel.add(createReportsMenuPanel(), "REPORTS");
        cardPanel.add(createResultsPanel(), "RESULTS");

        frame.add(cardPanel);
        cardLayout.show(cardPanel, "MAIN");
        frame.setVisible(true);
    }

    // ---------------------------------------------------------------
    // Building the screens
    // ---------------------------------------------------------------

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createTitleLabel("MAIN MENU"), BorderLayout.NORTH);

        JPanel buttons = createButtonArea();
        addMenuButton(buttons, "Add Deposit", e -> addTransaction(true));
        addMenuButton(buttons, "Make Payment", e -> addTransaction(false));
        addMenuButton(buttons, "Ledger", e -> cardLayout.show(cardPanel, "LEDGER"));
        addMenuButton(buttons, "Exit", e -> {
            JOptionPane.showMessageDialog(frame, "Thank you for using the Accounting Ledger.\nGood bye!");
            System.exit(0);
        });

        panel.add(buttons, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLedgerMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createTitleLabel("LEDGER MENU"), BorderLayout.NORTH);

        JPanel buttons = createButtonArea();
        addMenuButton(buttons, "All - Display all entries",
                e -> showResults("ALL TRANSACTIONS", service.getLedger(), "LEDGER"));
        addMenuButton(buttons, "Deposits - Display all deposits",
                e -> showResults("ALL DEPOSITS", service.getDeposits(), "LEDGER"));
        addMenuButton(buttons, "Payments - Display all payments",
                e -> showResults("ALL PAYMENTS", service.getPayments(), "LEDGER"));
        addMenuButton(buttons, "Reports - Custom Search", e -> cardLayout.show(cardPanel, "REPORTS"));
        addMenuButton(buttons, "Home", e -> cardLayout.show(cardPanel, "MAIN"));

        panel.add(buttons, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReportsMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createTitleLabel("REPORTS MENU"), BorderLayout.NORTH);

        JPanel buttons = createButtonArea();
        addMenuButton(buttons, "Month To Date",
                e -> showResults("MONTH-TO-DATE TRANSACTIONS", service.getMonthToDate(), "REPORTS"));
        addMenuButton(buttons, "Previous Month",
                e -> showResults("PREVIOUS MONTH TRANSACTIONS", service.getPreviousMonth(), "REPORTS"));
        addMenuButton(buttons, "Year To Date",
                e -> showResults("YEAR-TO-DATE TRANSACTIONS", service.getYearToDate(), "REPORTS"));
        addMenuButton(buttons, "Previous Year",
                e -> showResults("PREVIOUS YEAR TRANSACTIONS", service.getPreviousYear(), "REPORTS"));
        addMenuButton(buttons, "Search by Vendor", e -> searchByVendor());
        addMenuButton(buttons, "Custom Search", e -> customSearch());
        addMenuButton(buttons, "Back", e -> cardLayout.show(cardPanel, "LEDGER"));

        panel.add(buttons, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        resultsTitleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        resultsTitleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        panel.add(resultsTitleLabel, BorderLayout.NORTH);

        JTable table = new JTable(tableModel);
        table.setEnabled(false); // read-only: the user can't edit cells
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, backCard));
        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(backButton);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------------------------------------------------------
    // Small helpers for building the screens
    // ---------------------------------------------------------------

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        return label;
    }

    private JPanel createButtonArea() {
        JPanel buttons = new JPanel(new GridLayout(0, 1, 10, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 150, 20, 150));
        return buttons;
    }

    private void addMenuButton(JPanel panel, String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        panel.add(button);
    }

    // ---------------------------------------------------------------
    // Showing results
    // ---------------------------------------------------------------

    private void showResults(String title, ArrayList<Transaction> transactions, String backTo) {
        resultsTitleLabel.setText(title);
        backCard = backTo;

        tableModel.setRowCount(0); // clear out the old rows
        for (Transaction t : transactions) {
            Object[] row = {
                    t.getDate().toString(),
                    t.getTime().toString(),
                    t.getDesc(),
                    t.getVendor(),
                    String.format("%.2f", t.getAmount())
            };
            tableModel.addRow(row);
        }

        cardLayout.show(cardPanel, "RESULTS");
    }

    // ---------------------------------------------------------------
    // Adding deposits and payments
    // ---------------------------------------------------------------

    private void addTransaction(boolean isDeposit) {
        String title = isDeposit ? "Add Deposit" : "Make Payment";
        String descLabel = isDeposit ? "Deposit description:" : "Payment description:";
        String vendorLabel = isDeposit ? "Depositor's name:" : "Recipient vendor:";
        String amountLabel = isDeposit ? "Amount deposited ($):" : "Amount paid ($):";

        JTextField descField = new JTextField(20);
        JTextField vendorField = new JTextField(20);
        JTextField amountField = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel(descLabel));
        form.add(descField);
        form.add(new JLabel(vendorLabel));
        form.add(vendorField);
        form.add(new JLabel(amountLabel));
        form.add(amountField);

        while (true) {
            int choice = JOptionPane.showConfirmDialog(
                    frame, form, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (choice != JOptionPane.OK_OPTION) {
                return; // user pressed Cancel or closed the dialog
            }

            try {
                double amount = Double.parseDouble(amountField.getText().trim());

                if (amount <= 0) {
                    showError("Enter a number greater than 0");
                } else {
                    if (!isDeposit) {
                        amount = -amount; // payments are stored as negative numbers
                    }
                    service.addTransaction(descField.getText(), vendorField.getText(), amount);

                    String message = isDeposit ? "Deposit Added to Ledger!" : "Payment Added to Ledger!";
                    JOptionPane.showMessageDialog(frame, message);
                    return;
                }
            } catch (NumberFormatException e) {
                showError("That isn't a number");
            }
        }
    }

    // ---------------------------------------------------------------
    // Searching
    // ---------------------------------------------------------------

    private void searchByVendor() {
        String vendor = JOptionPane.showInputDialog(frame, "Enter the vendor name:");

        if (vendor == null) {
            return; // user pressed Cancel
        }

        showResults("VENDOR FILTERED TRANSACTIONS", service.getByVendor(vendor), "REPORTS");
    }

    private void customSearch() {
        JTextField startField = new JTextField(15);
        JTextField endField = new JTextField(15);
        JTextField descField = new JTextField(15);
        JTextField vendorField = new JTextField(15);
        JTextField lowField = new JTextField(15);
        JTextField highField = new JTextField(15);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Start date (YYYY-MM-DD):"));
        form.add(startField);
        form.add(new JLabel("End date (YYYY-MM-DD):"));
        form.add(endField);
        form.add(new JLabel("Description:"));
        form.add(descField);
        form.add(new JLabel("Vendor:"));
        form.add(vendorField);
        form.add(new JLabel("Minimum amount:"));
        form.add(lowField);
        form.add(new JLabel("Maximum amount:"));
        form.add(highField);

        JLabel instructions = new JLabel(
                "<html>Leave a field blank if you don't want to filter by it.<br>"
                        + "Input is not case sensitive.</html>");

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.add(instructions, BorderLayout.NORTH);
        content.add(form, BorderLayout.CENTER);

        while (true) {
            int choice = JOptionPane.showConfirmDialog(
                    frame, content, "Custom Search", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (choice != JOptionPane.OK_OPTION) {
                return;
            }

            try {
                LocalDate startDate = readDate(startField.getText());
                LocalDate endDate = readDate(endField.getText());
                String desc = descField.getText().trim();
                String vendor = vendorField.getText().trim();
                Double lowAmount = readAmount(lowField.getText());
                Double highAmount = readAmount(highField.getText());

                // If the min is bigger than the max, swap them
                if (lowAmount != null && highAmount != null && lowAmount > highAmount) {
                    double temp = lowAmount;
                    lowAmount = highAmount;
                    highAmount = temp;
                }

                showResults("CUSTOM FILTERED TRANSACTIONS",
                        service.getFilteredSubset(startDate, endDate, desc, vendor, lowAmount, highAmount),
                        "REPORTS");
                return;
            } catch (DateTimeParseException e) {
                showError("That's not a valid date. Use the format YYYY-MM-DD.");
            } catch (NumberFormatException e) {
                showError("That's not a valid amount.");
            }
        }
    }

    // Returns null if the text is blank, otherwise a LocalDate.
    // Throws DateTimeParseException if the text isn't a valid date.
    private LocalDate readDate(String text) {
        text = text.trim();
        if (text.isEmpty()) {
            return null;
        }
        return LocalDate.parse(text);
    }

    // Returns null if the text is blank, otherwise a Double.
    // Throws NumberFormatException if the text isn't a number.
    private Double readAmount(String text) {
        text = text.trim();
        if (text.isEmpty()) {
            return null;
        }
        return Double.parseDouble(text);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}