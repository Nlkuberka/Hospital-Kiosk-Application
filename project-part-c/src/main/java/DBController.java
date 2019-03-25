
import java.sql.*;


public class DBController {

    public DBController(){

    }


    public void DBConnect(){
    Connection connection = null;
        try {
            // substitute your database name for myDB
            connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            connection.setSchema("APP");
        } catch (SQLException e) {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
            return;
        }
    }
}
