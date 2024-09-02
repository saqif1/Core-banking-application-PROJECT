package sg.com.uob;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Account {
    /// ATTRIBUTES
    private int acctId;
    private int custId;
    private String acctNumber;
    private String acctType;
    private double acctBalance;

    /// CONSTRUCTORS
    public Account(int custId, String acctNumber,String acctType, double acctBalance) {
        this.custId = custId;
        this.acctNumber = acctNumber;
        this.acctType = acctType;
        this.acctBalance = acctBalance;
    }

    /// GETTER
    public int getAcctId() {
        return acctId;
    }

    public int getCustId() {
        return this.custId;
    }

    public String getAcctNumber() {
        return this.acctNumber;
    }

    public String getAcctType() {
        return this.acctType;
    }

    public double getAcctBalance() {
        return this.acctBalance;
    }

    /// SETTER
    public void setAcctId(int acctId) {
        this.acctId = acctId;
    }
    
    public void setCustId(int custId) {
        this.custId = custId;
    }

    public void setAcctNumber(String acctNumber) {
        this.acctNumber = acctNumber;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public void setAcctBalance(double acctBalance) {
        this.acctBalance = acctBalance;
    }

    /// STATIC METHODS
    public static Account openAccount(int custId, String acctType, double acctBalance) {
        Account account = null;
        Random random = new Random();
        int acctNumber = 100000 + random.nextInt(900000); // Generates a random 6-digit number

        try (Connection connection = DBUtils.getConnection()) {
            String insertStatement = "INSERT INTO accounts(custID, acctNumber, acctType, acctBalance) VALUES(?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, custId);
                preparedStatement.setInt(2, acctNumber);
                preparedStatement.setString(3, acctType);
                preparedStatement.setDouble(4, acctBalance);
                int count = preparedStatement.executeUpdate();
                if (count > 0) {
                    System.out.println("Account Opening successful! Account Number: " + acctNumber);
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int acctId = generatedKeys.getInt(1);
                        account = new Account(custId, String.valueOf(acctNumber), acctType, acctBalance);
                        account.setAcctId(acctId);
                        System.out.println("Registering account!");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }

    public static void transferAccount(String acctNumber, int custId) {
        try (Connection connection = DBUtils.getConnection()) {
            String updateStatement = "UPDATE accounts SET custID = ? WHERE acctNumber = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateStatement)) {
                preparedStatement.setInt(1, custId);
                preparedStatement.setString(2, acctNumber);
                int count = preparedStatement.executeUpdate();
                if (count > 0) {
                    System.out.println("Account ownership transfer successful!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Invalid operation!");
            System.out.println(e.getMessage());
        }
    }

    public static void closeAccount(String acctNumber) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure you want to close the account with account number " + acctNumber + "? [yes/no]");
        String confirmation = scanner.nextLine().toLowerCase().trim();

        if (confirmation.equals("yes")) {
            try (Connection connection = DBUtils.getConnection()) {
                String deleteStatement = "DELETE FROM accounts WHERE acctNumber=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement)) {
                    preparedStatement.setString(1, acctNumber);
                    int count = preparedStatement.executeUpdate();
                    if (count > 0) {
                        System.out.println("Account closure successful!");
                    } else {
                        System.out.println("No account found with the given account number.");
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Account closure canceled.");
        }
    }

    public static void getAcctStatement(String acctNumber, int year) {
        String query = "SELECT acctNumber, transactionType, amount, transactionDate FROM transactions WHERE acctNumber = ? AND YEAR(transactionDate) = ?";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
    
            preparedStatement.setString(1, acctNumber);
            preparedStatement.setInt(2, year);
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (!resultSet.next()) {
                System.out.println("Invalid Account Number! No transactions found.");
                return;
            }
    
            resultSet.beforeFirst(); // Reset the cursor back to the beginning for processing
    
            while (resultSet.next()) {
                String transactionType = resultSet.getString("transactionType");
                double amount = resultSet.getDouble("amount");
                String transactionDate = resultSet.getString("transactionDate");
    
                System.out.println("Account Number: " + acctNumber + " | " + "Transaction Type: " + transactionType + " | " + "Amount: " + amount + " | " + "Date: " + transactionDate);
                System.out.println("--------------------------------------------------------------------------------------------------------");
            }
            System.out.println("End of account statement");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}


