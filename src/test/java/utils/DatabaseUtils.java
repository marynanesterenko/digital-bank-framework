package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseUtils {

    static String url = ConfigReader.getProperty("db.url") ;
    static String username=ConfigReader.getProperty("db.username");
    static String password=ConfigReader.getProperty("db.password");
    static Connection con;
    static Statement statement;
    static ResultSet resultSet;

    public static void initializeDBProperties() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
            statement = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet executeQuery(String query) {
        try {
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void deleteQuery(String query){
        try {
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeDataBaseConnection(){
        try{
            statement.close();
            con.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
