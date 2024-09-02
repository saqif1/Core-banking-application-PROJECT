package sg.com.uob;

import java.sql.*;

public class DBUtils {

    public static Connection getConnection(){
        Connection con = null;
        try{
            con = DriverManager.getConnection(ReadProps.dbURL, ReadProps.user, ReadProps.pwd);
        } catch (SQLException excp) {
            System.out.println(" Potential Problem with Connection ");
            excp.printStackTrace();
        } finally {
            // System.out.println("Wrap-up, clean-up over here ");
        }
         return con;
    }
}
