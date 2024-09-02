package sg.com.uob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transaction {
    public static void deposit(String acctNumber, double depositAmount) {
        try (Connection connection = DBUtils.getConnection()) {
            String updateStatement = "UPDATE accounts SET acctBalance = acctBalance + ? WHERE acctNumber ='" + acctNumber + "';";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateStatement)) {
                preparedStatement.setDouble(1, depositAmount);
                int count = preparedStatement.executeUpdate();
                if (count > 0) {
                    // Add entry to transactions table
                    String insertTransaction = "INSERT INTO transactions(acctNumber, transactionType, amount) VALUES (?, 'DEPOSIT', ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertTransaction)) {
                        insertStatement.setString(1, acctNumber);
                        insertStatement.setDouble(2, depositAmount);
                        insertStatement.executeUpdate();
                    }
                    System.out.println("Deposit Successful!");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void withdraw(String acctNumber, double withdrawAmount, double overdraftAmount) {
        try (Connection connection = DBUtils.getConnection()) {
            String selectStatement = "SELECT acctBalance, acctType FROM accounts WHERE acctNumber = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectStatement)) {
                preparedStatement.setString(1, acctNumber);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    double acctBalance = rs.getDouble("acctBalance");
                    String acctType = rs.getString("acctType");

                    if ((acctType.equals("CUR") && acctBalance - withdrawAmount >= -overdraftAmount) ||
                            (acctType.equals("SAV") && acctBalance - withdrawAmount >= 0)) {
                        String updateStatement = "UPDATE accounts SET acctBalance = acctBalance - ? WHERE acctNumber = ?";
                        try (PreparedStatement updatePreparedStatement = connection.prepareStatement(updateStatement)) {
                            updatePreparedStatement.setDouble(1, withdrawAmount);
                            updatePreparedStatement.setString(2, acctNumber);
                            int count = updatePreparedStatement.executeUpdate();
                            if (count > 0) {
                                // Add entry to transactions table
                                String insertTransaction = "INSERT INTO transactions(acctNumber, transactionType, amount) VALUES (?, 'WITHDRAWAL', ?)";
                                try (PreparedStatement insertStatement = connection.prepareStatement(insertTransaction)) {
                                    insertStatement.setString(1, acctNumber);
                                    insertStatement.setDouble(2, withdrawAmount);
                                    insertStatement.executeUpdate();
                                }
                                System.out.println("Withdraw Successful!");
                            }
                        }
                    } else {
                        System.out.println("Invalid Transaction!");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void creditInterest(String acctNumber, double annualInterestRate) {
        try (Connection connection = DBUtils.getConnection()) {
            // Check the account type before crediting interest
            String selectAccountType = "SELECT acctType FROM accounts WHERE acctNumber = ?";
            try (PreparedStatement typeStatement = connection.prepareStatement(selectAccountType)) {
                typeStatement.setString(1, acctNumber);
                ResultSet rs = typeStatement.executeQuery();
                if (rs.next()) {
                    String acctType = rs.getString("acctType");
                    if (acctType.equals("SAV")) {
                        // Account type is SAV, credit interest
                        String updateStatement = "UPDATE accounts SET acctBalance = acctBalance * (1 + ? / 100) WHERE acctNumber = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(updateStatement)) {
                            preparedStatement.setDouble(1, annualInterestRate);
                            preparedStatement.setString(2, acctNumber);
                            int count = preparedStatement.executeUpdate();
                            if (count > 0) {
                                // Add entry to transactions table
                                String insertTransaction = "INSERT INTO transactions(acctNumber, transactionType, amount) VALUES (?, 'INTEREST', ?)";
                                try (PreparedStatement insertStatement = connection.prepareStatement(insertTransaction)) {
                                    insertStatement.setString(1, acctNumber);
                                    insertStatement.setDouble(2, 0); // For interest, amount is 0
                                    insertStatement.executeUpdate();
                                }
                                System.out.println("Interest credited successfully!");
                            }
                        }
                    } else {
                        System.out.println("Interest can only be credited to SAV accounts.");
                    }
                } else {
                    System.out.println("Invalid account number.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}


