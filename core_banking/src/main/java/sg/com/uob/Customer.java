package sg.com.uob;

import java.sql.*;

public class Customer {
        /// ATTRIBUTES
        public int custId;
        private String custName;
        private String custDOB;
        private String custNRIC;
        private String custAddress;
        private String custPhoneNo;
        private String custEmail;
        private String custOccupation;
        
        /// CONSTRUCTOR 
        public Customer(String custName, String custDOB, String custNRIC, String custAddress, String custPhoneNo,
                String custEmail, String custOccupation) {
            this.custName = custName;
            this.custDOB = custDOB;
            this.custNRIC = custNRIC;
            this.custAddress = custAddress;
            this.custPhoneNo = custPhoneNo;
            this.custEmail = custEmail;
            this.custOccupation = custOccupation;
        }
                
        /// GETTERS
        public String getCustName() {
            return this.custName;
        }
        public String getCustDOB() {
            return this.custDOB;
        }
        public String getCustNRIC() {
            return this.custNRIC;
        }
        public String getCustAddress() {
            return this.custAddress;
        }
        public String getCustPhoneNo() {
            return this.custPhoneNo;
        }
        public String getCustEmail() {
            return this.custEmail;
        }
        public String getCustOccupation() {
            return this.custOccupation;
        }

        /// SETTERS
        public void setCustId(int custId) {
            this.custId = custId;
        }
        public void setCustName(String custName) {
            this.custName = custName.toUpperCase();
        }
        public void setCustDOB(String custDOB) {
            this.custDOB = custDOB;
        }
        public void setCustNRIC(String custNRIC) {
            this.custNRIC = custNRIC.toUpperCase();
        }
        public void setCustAddress(String custAddress) {
            this.custAddress = custAddress.toUpperCase();
        }
        public void setCustPhoneNo(String custPhoneNo) {
            this.custPhoneNo = custPhoneNo;
        }
        public void setCustEmail(String custEmail) {
            this.custEmail = custEmail.toUpperCase();
        }
        public void setCustOccupation(String custOccupation) {
            this.custOccupation = custOccupation.toUpperCase();
        }

        /// STATIC METHODS
        public static Customer registerCustomer(String custName, String custDOB, String custNRIC, String custAddress, String custPhoneNo,
            String custEmail, String custOccupation) {
        Customer cust = null;
        // Convert string inputs to uppercase
        custName = custName.toUpperCase();
        custDOB = custDOB.toUpperCase();
        custNRIC = custNRIC.toUpperCase();
        custAddress = custAddress.toUpperCase();
        custPhoneNo = custPhoneNo.toUpperCase();
        custEmail = custEmail.toUpperCase();
        custOccupation = custOccupation.toUpperCase();
        
        try(Connection connection = DBUtils.getConnection()) {
            Statement statement = connection.createStatement();
            // Instantiate object
            cust = new Customer(custName, custDOB, custNRIC, custAddress, custPhoneNo, custEmail, custOccupation);
            // Insertion process
            String insertStatement = "INSERT INTO customers(custName,custDOB,custNRIC,custAddress,custPhoneNo,custEmail,custOccupation) VALUES('" + cust.getCustName() + "','" + cust.getCustDOB() + "','" + cust.getCustNRIC() + "','" + cust.getCustAddress() + "','" + cust.getCustPhoneNo() + "','" + cust.getCustEmail() + "','" + cust.getCustOccupation() + "');";
            int count = statement.executeUpdate(insertStatement);
            if (count > 0) {
                System.out.println("Insertion successful!");
                String selectStatement = "SELECT * FROM customers WHERE custNRIC='" + cust.getCustNRIC() + "';";
                ResultSet rs = statement.executeQuery(selectStatement);
                // Assigning SQL's custId to our cust object
                int custId = rs.next() ? rs.getInt(1) : 0;
                if (custId > 0) {
                    System.out.println("Registering customer!");
                    cust.setCustId(custId);
                }
                else    
                    System.out.println("Invalid operation!");
            }
        } catch (SQLException e) {
            System.out.println("Invalid operation!");
            System.out.println(e);
        }
        return cust;
        }

        public static String getCustomerDetails(String custNRIC) {
            StringBuilder custDetails = new StringBuilder();
            try (Connection connection = DBUtils.getConnection()) {
                Statement statement = connection.createStatement();
                String selectStatement = "SELECT * FROM customers WHERE custNRIC='" + custNRIC + "';";
                ResultSet resultSet = statement.executeQuery(selectStatement);
        
                if (resultSet.next()) {
                    int custID = resultSet.getInt(1);
                    String custName = resultSet.getString(2).toUpperCase();
                    String custDOB = resultSet.getString(3).toUpperCase();
                    String custNric = resultSet.getString(4).toUpperCase();
                    String custAddress = resultSet.getString(5).toUpperCase();
                    String custPhoneNo = resultSet.getString(6).toUpperCase();
                    String custEmail = resultSet.getString(7).toUpperCase();
                    String custOccupation = resultSet.getString(8).toUpperCase();
        
                    custDetails.append("Customer ID: ").append(custID).append("\n")
                               .append("Name: ").append(custName).append("\n")
                               .append("DOB: ").append(custDOB).append("\n")
                               .append("NRIC: ").append(custNric).append("\n")
                               .append("Address: ").append(custAddress).append("\n")
                               .append("Phone Number: ").append(custPhoneNo).append("\n")
                               .append("Email: ").append(custEmail).append("\n")
                               .append("Occupation: ").append(custOccupation);
                } else {
                    custDetails.append("Customer not found!");
                }
            } catch (SQLException e) {
                custDetails.append("Error: ").append(e.getMessage());
            }
            return custDetails.toString();
        }

        public static void updateCustomer(String custNRIC, String custAddress, String custPhoneNo, String custEmail, String custOccupation) {
            try (Connection connection = DBUtils.getConnection()) {
                Statement statement = connection.createStatement();
                String updateStatement = "UPDATE customers SET custAddress = '" + custAddress.toUpperCase() + "',custPhoneNo = '" + custPhoneNo.toUpperCase() + "', custEmail = '" + custEmail.toUpperCase() + "',custOccupation = '" + custOccupation.toUpperCase() + "' " + " WHERE custNRIC='" + custNRIC.toUpperCase() + "';";
                int count = statement.executeUpdate(updateStatement);
                if (count > 0) {
                    System.out.println("Update successful!");
                }
                //else
                    //System.out.println("No customer found with the given NRIC!");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        public static void deleteCustomer(String custNRIC) {
            try (Connection connection = DBUtils.getConnection()) {
                Statement statement = connection.createStatement();
                String deleteStatement = "DELETE FROM customers WHERE custNRIC='" + custNRIC + "';";
                int count = statement.executeUpdate(deleteStatement);
                if (count > 0) {
                    System.out.println("Delete successful!");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
}
