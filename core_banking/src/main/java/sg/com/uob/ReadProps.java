package sg.com.uob;

import java.io.InputStream;
import java.util.Properties;

public class ReadProps {
    static String dbURL;
    static String user;
    static String pwd;
    static {
        Properties props = new Properties();
        try (InputStream resStream = ClassLoader.getSystemResourceAsStream("db_connection.properties")){
            props.load(resStream);
            dbURL = props.getProperty("dbURL");
            user = props.getProperty("user");
            pwd = props.getProperty("pwd");
            // System.out.println( dbURL + "::" + user + "::"+ "pwd");
        }catch(Exception e){
            System.out.println( " Problem loading resource file ");
        }
    }
}