import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.LinkedList;


/**
 * @author imoralessirgo
 */
public class DBControllerAPI {


    public static final String ALL_ROOMS = "SELECT * FROM NODES WHERE NODETYPE != 'HALL' and NODETYPE != 'STAI' and NODETYPE != 'ELEV'";

    /**
     * dbConnect
     *
     * connects to the application's database for query execution
     * handles SQLExceptions
     *
     * @return
     */
    public static Connection dbConnect() {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            Connection connection = DriverManager.getConnection("jdbc:derby:apiDB;create=true");
            return connection;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    public static void initializeAppDB(Connection conn){
        String nodes = "CREATE TABLE NODES(\n" +
                "NODEID VARCHAR(10),\n" +
                "XCOORD INTEGER,\n" +
                "YCOORD INTEGER,\n" +
                "FLOOR VARCHAR(3),\n" +
                "BUILDING VARCHAR(15),\n" +
                "NODETYPE VARCHAR(4),\n" +
                "LONGNAME VARCHAR(50),\n" +
                "SHORTNAME VARCHAR(50),\n" +
                "CONSTRAINT NODE_PK PRIMARY KEY(NODEID)\n" +
                ")\n";
        String user = "CREATE TABLE USERS(\n" +
                "  USERID VARCHAR(10),\n" +
                "  PERMISSION SMALLINT,\n" +
                "  USERNAME VARCHAR(15),\n" +
                "  PASSWORD VARCHAR(15),\n" +
                "  CONSTRAINT USER_PK PRIMARY KEY(USERID),\n" +
                "  CONSTRAINT UN_UN UNIQUE (USERNAME)" +
                ")\n";
        String servicerequest = "CREATE TABLE SERVICEREQUEST(\n" +
                "  SERVICEID INTEGER GENERATED ALWAYS AS IDENTITY, \n" +
                "  NODEID VARCHAR(10) REFERENCES NODES(NODEID),\n" +
                "  SERVICETYPE VARCHAR(20),\n" +
                "  MESSAGE VARCHAR(200),\n" +
                "  USERID VARCHAR(10) REFERENCES USERS(USERID),\n" +
                "  RESOLVED BOOLEAN,\n" +
                "  RESOLVERID VARCHAR(10) REFERENCES USERS(USERID), \n" +
                "  CONSTRAINT SERVICE_PK PRIMARY KEY(SERVICEID)\n" +
                ")\n";



        createTable(nodes,conn);
        createTable(user,conn);
        createTable(servicerequest,conn);

        loadNodeData(new File("nodesv5.csv"),conn);

    }

    /**
     * loadNodeData
     *
     * reads and stores node data from given csv file
     *
     * @param file
     * @param connection
     */
    public static void loadNodeData(File file, Connection connection){
        BufferedReader br = null;
        String line = "";
        String[] arr;
        try{
            br = new BufferedReader(new FileReader(file));
            br.readLine(); // skip header
            while((line = br.readLine()) != null){
                arr = line.split(",");
                connection.createStatement().execute("insert into NODES " +
                        "values ('"+ arr[0] +"',"+ arr[1]+","+ arr[2]+",'"+ arr[3]+"','"+ arr[4]+"','"+ arr[5]+"','"+ arr[6]+"','"+ arr[7]+"')");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * CreateTable
     *
     * executes the given query
     *
     * @param createStatement
     * @param conn
     */
    public static void createTable(String createStatement, Connection conn){
        try {
            conn.createStatement().execute(createStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * closeConnection
     *
     * Terminates connection to database after use
     * ensures proper functionality during query execution
     *
     * @param connection
     */
    public static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * generateListofNodes
     *
     * creates and returns a list of node objects
     * @return LinkedList<Node>
     */
    public static LinkedList<Node> generateListOfNodes(Connection connection, String query){
        try{
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            LinkedList<Node> listOfNodes = new LinkedList<>();
            while(rs.next()){
                listOfNodes.add(buildNode(rs));
            }
            return listOfNodes;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private static Node buildNode(ResultSet rs){
        try {
            Node node = new Node(rs.getString("NODEID"), rs.getInt("XCOORD"), rs.getInt("YCOORD"),
                    rs.getString("FLOOR"), rs.getString("BUILDING"), rs.getString("NODETYPE"),
                    rs.getString("LONGNAME"), rs.getString("SHORTNAME"));
            return node;
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * addServiceRequest
     *
     * Enters ServiceRequest object to database
     *
     * @param serviceRequest
     * @param connection
     */
    public static int addServiceRequest(ServiceRequest serviceRequest, Connection connection){
        try{
            PreparedStatement s;
            if (serviceRequest.getNodeID() == null){
                s = connection.prepareStatement("INSERT into SERVICEREQUEST (NODEID, SERVICETYPE, MESSAGE, USERID, RESOLVED, RESOLVERID)" +
                        " values (" + serviceRequest.getNodeID() +
                        ",'"+ serviceRequest.getServiceType() +"','"+ serviceRequest.getMessage() + "','"+
                        serviceRequest.getUserID()+"',"+serviceRequest.isResolved()+","+ serviceRequest.getResolverID()+")");
            }else{
                s = connection.prepareStatement("INSERT into SERVICEREQUEST (NODEID, SERVICETYPE, MESSAGE, USERID, RESOLVED, RESOLVERID)" +
                        " values ('" + serviceRequest.getNodeID() +
                        "','"+ serviceRequest.getServiceType() +"','"+ serviceRequest.getMessage() + "','"+
                        serviceRequest.getUserID()+"',"+serviceRequest.isResolved()+","+ serviceRequest.getResolverID()+")");

            }
            s.execute();
            ResultSet rs = s.getGeneratedKeys();
            return 1;
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public static void addUser(User user, Connection connection){
        try{
            PreparedStatement s = connection.prepareStatement("INSERT into USERS (USERID, PERMISSION, USERNAME, PASSWORD)" +
                    " values ('" + user.getUserID() + "'," + user.getPermissions() + ",'" + user.getUsername() +
                    "','" + user.getPassword() + "')");
            s.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
