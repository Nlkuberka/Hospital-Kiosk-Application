import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBController {
    //public Connection connection;


    public static Connection dbConnect() {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            Connection connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            return connection;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }


}
