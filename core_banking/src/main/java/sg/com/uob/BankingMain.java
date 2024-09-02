package sg.com.uob;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class BankingMain {
    static Scanner sc = new Scanner(System.in);
    static int choice; 
    
    public static void menuPrompt() { // Helper 1
        System.out.println("+=========================================================+");
        System.out.println("|            Welcome to WFH Core Banking System!          |");
        System.out.println("+=========================================================+");
        System.out.println("\t1 --> Register Customer"); 
        System.out.println("\t2 --> Open Account"); 
        System.out.println("\t3 --> Transfer Account Ownership"); 
        System.out.println("\t4 --> Display Customer Details"); 
        System.out.println("\t5 --> Update Customer Details"); 
        System.out.println("\t6 --> Delete Customer");
        System.out.println("\t7 --> Funds Deposit");
        System.out.println("\t8 --> Funds Withdrawal");
        System.out.println("\t9 --> Annual Interest Crediting");
        System.out.println("\t10 --> Account Statement Generator");
        System.out.println("\t11 --> Account Closure");
        System.out.println("\t0 --> Exit");
        System.out.println("===========================================================");
        System.out.print("Choose options [1-11], 0 to exit :: ");
        choice = sc.nextInt();
    }

    public static void processCases() { // Helper 2
        int custId;
        String custName;
        String custNRIC;
        String custAddress;
        String custPhoneNo;
        String custEmail;
        String custOccupation;
        String acctNumber;
        double acctBalance;
        
        switch(choice) {
            case 1: {
                System.out.println();
                System.out.println("======= Customer Registration Interface =======");
                System.out.print("Enter custName :: ");
                sc.nextLine();
                custName = sc.nextLine();
                
                String custDOBPattern = "\\d{2}/\\d{2}/\\d{4}";
                boolean validDOB = false;
                String custDOB = "";
                while (!validDOB) {
                    System.out.print("Enter custDOB [MM/DD/YYYY] :: ");
                    custDOB = sc.nextLine();
                    if (custDOB.matches(custDOBPattern)) {
                        // Check if slashes are at the 3rd and 6th positions
                        String[] parts = custDOB.split("/");
                        int month = Integer.parseInt(parts[0]);
                        int day = Integer.parseInt(parts[1]);
                        int year = Integer.parseInt(parts[2]);
                        if (month >= 1 && month <= 12 && day >= 1 && day <= 31 && year >= 1800) {
                            // Calculate age based on DOB
                            LocalDate birthDate = LocalDate.of(year, month, day);
                            LocalDate currentDate = LocalDate.now();
                            long age = ChronoUnit.YEARS.between(birthDate, currentDate);
                            if (age >= 18) {
                                validDOB = true;
                            } else {
                                System.out.println("You must be at least 18 years old to open an account.");
                            }
                        } else {
                            System.out.println("Invalid date. Please enter a valid date in MM/DD/YYYY format.");
                        }
                    } else {
                        System.out.println("Invalid format. Please enter a valid date in MM/DD/YYYY format.");
                    }
                }
                
                if (validDOB) {
                    System.out.print("Enter custNRIC :: ");
                    custNRIC = sc.nextLine();
                    System.out.print("Enter custAddress :: ");
                    custAddress = sc.nextLine();
                    System.out.print("Enter custPhoneNo :: ");
                    custPhoneNo = sc.nextLine();
                    
                    boolean validEmail = false;
                    String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
                    
                    do {
                        System.out.print("Enter custEmail :: ");
                        custEmail = sc.nextLine();
                        
                        if (custEmail.matches(emailPattern)) {
                            validEmail = true;
                        } else {
                            System.out.println("Invalid email address. Please enter a valid email address.");
                        }
                    } while (!validEmail);
                    
                    System.out.print("Enter custOccupation :: ");
                    custOccupation = sc.nextLine();
                    
                    Customer.registerCustomer(custName, custDOB, custNRIC, custAddress, custPhoneNo, custEmail, custOccupation);
                }
                break;
            }
            case 2: {
                System.out.println();
                System.out.println("======= Account Opening Interface =======");
                System.out.print("Enter custID :: ");
                custId = sc.nextInt();
                sc.nextLine(); // Consume newline character left by nextInt()
            
                String validAcctTypes = "SAV|CUR"; // Regular expression for valid account types
                String acctType;
            
                do {
                    System.out.print("Enter acctType [SAV/CUR] :: ");
                    acctType = sc.nextLine().toUpperCase(); // Convert input to uppercase for case-insensitive comparison
            
                    if (!acctType.matches(validAcctTypes)) {
                        System.out.println("Invalid account type. Please enter 'SAV' for Savings or 'CUR' for Current.");
                    }
            
                } while (!acctType.matches(validAcctTypes)); // Continue asking until a valid account type is entered
            
                System.out.print("Enter initial deposit :: ");
                acctBalance = sc.nextDouble();
                Account.openAccount(custId, acctType, acctBalance);
                break;
            }                
            case 3: {
                System.out.println();
                System.out.println("======= Account Ownership Transfer Interface =======");
                System.out.print("Enter existing acctNumber :: ");
                sc.nextLine();
                acctNumber = sc.nextLine();
                //sc.nextLine();
                System.out.print("Enter new owner's custID :: ");
                custId = sc.nextInt();
                // sc.nextLine();
                Account.transferAccount(acctNumber, custId);
                break;
            }
            case 4: {
                System.out.println();
                System.out.println("======= Display Customer Details Interface =======");
                System.out.print("Enter custNRIC :: ");
                sc.nextLine();
                custNRIC = sc.nextLine();
                System.out.println();
                System.out.println(Customer.getCustomerDetails(custNRIC));
                break;
            }
            case 5: {
                System.out.println();
                System.out.println("======= Update Customer Details Interface =======");
                System.out.print("Enter customer's NRIC :: ");
                sc.nextLine();
                custNRIC = sc.nextLine();
                System.out.println("\n Important: **Kindly re-enter original details if there are no changes.** \n");
                System.out.print("Enter new custAddress :: ");
                custAddress = sc.nextLine();
                //sc.nextLine();
                System.out.print("Enter new custPhoneNo :: ");
                custPhoneNo = sc.nextLine();
                //sc.nextLine();
                System.out.print("Enter new custEmail :: ");
                custEmail = sc.nextLine();
                //sc.nextLine();
                System.out.print("Enter new custOccupation :: ");
                custOccupation = sc.nextLine();
                Customer.updateCustomer(custNRIC, custAddress, custPhoneNo, custEmail, custOccupation);
                //System.out.println("Update successful!");
                System.out.println(Customer.getCustomerDetails(custNRIC));
                break;
            }
            case 6: {
                System.out.println();
                System.out.println("======= Customer Deletion Interface =======");
                System.out.print("Enter custNRIC :: ");
                sc.nextLine(); // Consume newline character left by previous input
                custNRIC = sc.nextLine();
                
                // Ask for confirmation
                System.out.print("Are you sure you want to delete the customer with NRIC " + custNRIC + "? [yes/no] :: ");
                String confirmation = sc.nextLine().toLowerCase(); // Convert input to lowercase for case-insensitive comparison
                
                if (confirmation.equals("yes")) {
                    Customer.deleteCustomer(custNRIC);
                    System.out.println("Customer deletion successful!");
                } else {
                    System.out.println("Customer deletion cancelled.");
                }
                break;
            }
            case 7: {
                System.out.println();
                System.out.println("======= Funds Deposit Interface =======");
                sc.nextLine();
                System.out.print("Enter acctNumber :: ");
                acctNumber = sc.nextLine();
                double depositAmount = 0;
                boolean validAmount = false;
                while (!validAmount) {
                    System.out.print("Enter depositAmount :: ");
                    depositAmount = sc.nextDouble();
                    sc.nextLine();  // Consume newline character
                    
                    if (depositAmount > 0) {
                        validAmount = true;
                        Transaction.deposit(acctNumber, depositAmount);
                    } else {
                        System.out.println("Invalid amount. Please enter a positive amount greater than 0.");
                    }
                }
                break;
            }
            case 8: {
                System.out.println();
                System.out.println("======= Funds Withdrawal Interface =======");
                sc.nextLine();
                System.out.print("Enter acctNumber :: ");
                acctNumber = sc.nextLine();
            
                boolean isValid = false;
                double withdrawAmount = 0;
                double overdraftLimit = 0;
            
                // Validate positive withdrawal amount
                while (!isValid) {
                    System.out.print("Enter withdrawAmount :: ");
                    withdrawAmount = sc.nextDouble();
                    sc.nextLine();
                    if (withdrawAmount > 0) {
                        isValid = true;
                    } else {
                        System.out.println("Invalid amount. Please enter a positive number for withdrawal.");
                    }
                }
            
                isValid = false; // Reset isValid for overdraftLimit validation
            
                // Validate positive overdraft limit
                while (!isValid) {
                    System.out.print("Enter overdraftLimit [*DEFAULT 0 for SAV acc no matter the input] :: ");
                    overdraftLimit = sc.nextDouble();
                    sc.nextLine();
                    if (overdraftLimit >= 0) {
                        isValid = true;
                    } else {
                        System.out.println("Invalid overdraft limit. Please enter a non-negative number.");
                    }
                }
            
                Transaction.withdraw(acctNumber, withdrawAmount, overdraftLimit);
                break;
            }
            case 9: {
                System.out.println();
                System.out.println("======= Interest Crediting Interface =======");
                sc.nextLine();
                System.out.print("Enter acctNumber :: ");
                acctNumber = sc.nextLine();
            
                double AIR;
                boolean isValid = false;
            
                // Validate positive interest rate
                do {
                    System.out.print("Enter Annual Interest Rate :: ");
                    AIR = sc.nextDouble();
                    sc.nextLine();
                    if (AIR > 0) {
                        isValid = true;
                    } else {
                        System.out.println("Invalid interest rate.");
                    }
                } while (!isValid);
            
                Transaction.creditInterest(acctNumber, AIR);
                break;
            }
            case 10: {
                int statementYear;
            
                System.out.println();
                System.out.println("======= Account Statement Generator =======");
                System.out.print("Enter existing acctNumber :: ");
                sc.nextLine(); // Consume the newline character left by previous input
                acctNumber = sc.nextLine();
            
                // Prompt the user until a valid positive year is entered
                while (true) {
                    System.out.print("Enter year of record :: ");
                    statementYear = sc.nextInt();
                    if (statementYear > 1800) {
                        break; // 
                    } else {
                        System.out.println("Invalid input! Year must be greater than 0.");
                    }
                }
            
                Account.getAcctStatement(acctNumber, statementYear);
                break;
            }
            case 11: {
                System.out.println();
                System.out.println("======= Account Closure Interface =======");
                sc.nextLine();
                System.out.print("Enter acctNumber :: ");
                acctNumber = sc.nextLine();
                Account.closeAccount(acctNumber);
                break;
            }
        }
    }

    public static void main(String[] args) { // Main Execution
        while (true) {
            menuPrompt();
            if (choice == 0) {
                System.out.println("You have exited successfully.");
                System.out.println();    
                break;
            }
            else {
                processCases();
                System.out.println();
                System.out.println();
                continue;
            }     
        }
    }
}
