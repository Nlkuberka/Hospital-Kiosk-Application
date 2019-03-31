import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

    public static void loadNodeData(File file, Connection connection){
        BufferedReader br = null;
        String line = "";
        String[] arr;
        try{
            br = new BufferedReader(new FileReader(file));
            br.readLine(); // skip header
            while((line = br.readLine()) != null){
                arr = line.split(",");
                connection.createStatement().execute("insert into NODES" +
                        "values ('"+ arr[0] +"',"+ arr[1]+","+ arr[2]+","+ arr[3]+",'"+ arr[4]+"','"+ arr[5]+"','"+ arr[6]+"','"+ arr[7]+"');");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void loadEdgeData(File file, Connection connection){
        BufferedReader br = null;
        String line = "";
        String[] arr;
        try{
            br = new BufferedReader(new FileReader(file));
            br.readLine(); // skip header
            while((line = br.readLine()) != null){
                arr = line.split(",");
                connection.createStatement().execute("insert into EDGES" +
                        "values ('"+ arr[0] + "','"+ arr[1]+"','"+ arr[2]+"',);");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void createTable(String createStatement, Connection conn){
        try {
            conn.createStatement().execute(createStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void loadEdgeData(File file){

    }

}
