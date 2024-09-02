package sg.com.uob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableCommands{

    public static void createTableinSQL(String tablename) {
        try {
            Connection connection = DriverManager.getConnection(ReadProps.dbURL, ReadProps.user, ReadProps.pwd);
            String createTableStatement = "CREATE TABLE " + tablename + " (Entry_ID INT AUTO_INCREMENT PRIMARY KEY)";
            PreparedStatement createTableStmt = connection.prepareStatement(createTableStatement);
            int count = createTableStmt.executeUpdate();
            if (count > 0) {
                System.out.println("Table creation successful!");
            }
            connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    
    public void addRow(String tablename, String columnName, String columnValue) {
        try {
            Connection connection = DriverManager.getConnection(ReadProps.dbURL, ReadProps.user, ReadProps.pwd);
            String addRowStatement = "INSERT INTO " + tablename + " (" + columnName + ") VALUES (?)";
            PreparedStatement addRowStmt = connection.prepareStatement(addRowStatement);
            addRowStmt.setString(1, columnValue);
            int count = addRowStmt.executeUpdate();
            if (count > 0) {
                System.out.println("Row added into "+tablename+" successfully!");
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteRow(String tablename, String conditionColumn, String conditionValue) {
        try {
            Connection connection = DriverManager.getConnection(ReadProps.dbURL, ReadProps.user, ReadProps.pwd);
            String deleteRowStatement = "DELETE FROM " + tablename + " WHERE " + conditionColumn + " = ?";
            PreparedStatement deleteRowStmt = connection.prepareStatement(deleteRowStatement);
            deleteRowStmt.setString(1, conditionValue);
            int count = deleteRowStmt.executeUpdate();
            if (count > 0) {
                System.out.println("Row deleted from "+tablename+" successfully!");
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifyRow(String tablename, String columnName, String newValue, String conditionColumn, String conditionValue) {
        try {
            Connection connection = DriverManager.getConnection(ReadProps.dbURL, ReadProps.user, ReadProps.pwd);
            String modifyRowStatement = "UPDATE " + tablename + " SET " + columnName + " = ? WHERE " + conditionColumn + " = ?";
            PreparedStatement modifyRowStmt = connection.prepareStatement(modifyRowStatement);
            modifyRowStmt.setString(1, newValue);
            modifyRowStmt.setString(2, conditionValue);
            int count = modifyRowStmt.executeUpdate();
            if (count > 0) {
                System.out.println("Row in "+tablename+" modified successfully!");
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}