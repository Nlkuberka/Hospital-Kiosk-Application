package database;

import application.CurrentUser;
import entities.ServiceRequest;
import network.DBNetwork;

import java.sql.*;
import java.util.LinkedList;

public class DBControllerSR extends DBController {
    public static final String TYPE_OTHER = "Other";
    public static final String TYPE_BABYSITTER = "Babysitter";
    public static final String TYPE_RELIGIOUS_SERVICES = "Religious Services";
    public static final String TYPE_FLOWER_DELIVERY = "Flower Delivery";
    public static final String TYPE_ALL = "*";


    public static LinkedList<ServiceRequest> getServiceRequests(String type, Connection conn){
        LinkedList<ServiceRequest> list = new LinkedList<ServiceRequest>();
        try{
            PreparedStatement ps;
            if(type == "*"){
               ps = conn.prepareStatement("SELECT * from SERVICEREQUEST");
            }else {
                ps = conn.prepareStatement("SELECT * from SERVICEREQUEST where servicetype = ?");
                ps.setString(1, type);
            }
           ResultSet rs = ps.executeQuery();
           while(rs.next()){
               list.add(new ServiceRequest(rs.getString("NODEID"),rs.getString("SERVICETYPE"),
                       rs.getString("MESSAGE"),rs.getString("USERID"),rs.getBoolean("RESOLVED"),rs.getString("RESOLVERID"),rs.getString("SERVICEID")));
           }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
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
                s = connection.prepareStatement("INSERT into SERVICEREQUEST (SERVICEID, NODEID, SERVICETYPE, MESSAGE, USERID, RESOLVED, RESOLVERID)" +
                        " values ( '"+serviceRequest.getServiceID() +"'," + serviceRequest.getNodeID() +
                        ",'"+ serviceRequest.getServiceType() +"','"+ serviceRequest.getMessage() + "','"+
                        serviceRequest.getUserID()+"',"+serviceRequest.isResolved()+","+ serviceRequest.getResolverID()+")");
            }else{
                s = connection.prepareStatement("INSERT into SERVICEREQUEST (SERVICEID, NODEID, SERVICETYPE, MESSAGE, USERID, RESOLVED, RESOLVERID)" +
                        " values ('"+serviceRequest.getServiceID() +"','"+ serviceRequest.getNodeID() +
                        "','"+ serviceRequest.getServiceType() +"','"+ serviceRequest.getMessage() + "','"+
                        serviceRequest.getUserID()+"',"+serviceRequest.isResolved()+","+ serviceRequest.getResolverID()+")");

            }
            s.execute();
            ResultSet rs = s.getGeneratedKeys();
            CurrentUser.network.sendServiceRequestPacket(DBNetwork.ADD_SERVICE_REQUEST, serviceRequest);
            return 1;
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * updateServiceRequest
     *
     * saves changes made to a ServiceRequest object
     *
     * UP TO DATE
     * @param serviceRequest
     * @param connection
     */
    public static void updateServiceRequest(ServiceRequest serviceRequest, Connection connection){
        try{
            System.out.println(serviceRequest);
            PreparedStatement ps = connection.prepareStatement("UPDATE SERVICEREQUEST SET SERVICETYPE = ?, MESSAGE = ?, RESOLVERID = ?, RESOLVED = ? WHERE SERVICEID = ?");
            ps.setString(1,serviceRequest.getServiceType());
            ps.setString(2,serviceRequest.getMessage());
            ps.setString(3,serviceRequest.getResolverID());
            ps.setBoolean(4,serviceRequest.isResolved());
            ps.setString(5,serviceRequest.getServiceID());
//            Statement s = connection.createStatement();
//            s.execute("UPDATE SERVICEREQUEST SET SERVICETYPE ='"+ serviceRequest.getServiceType() +"',"+
//                    "MESSAGE = '"+ serviceRequest.getMessage() + "'," +
//                    "RESOLVED = " + serviceRequest.isResolved() + "," +
//                    "RESOLVERID = '"+serviceRequest.getResolverID()+"' " +
//                    "where  SERVICEID = '" + serviceRequest.getServiceID()+"'");
            ps.execute();
            CurrentUser.network.sendServiceRequestPacket(DBNetwork.UPDATE_SERVICE_REQUEST, serviceRequest);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * deleteServiceRequest
     *
     *
     * @param connection
     */
    public static void deleteServiceRequest(String ID, Connection connection){
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE * FROM SERVICEREQUEST WHERE SERVICEID = ?");
            ps.setString(1,ID);
            ps.execute();
            ServiceRequest serviceRequest = new ServiceRequest();
            serviceRequest.setServiceID(ID);
            CurrentUser.network.sendServiceRequestPacket(DBNetwork.DELETE_SERVICE_REQUEST, serviceRequest);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
