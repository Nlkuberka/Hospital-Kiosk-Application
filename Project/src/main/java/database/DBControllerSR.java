package database;

import application.CurrentUser;
import entities.ServiceRequest;
import network.DBNetwork;

import java.sql.*;

public class DBControllerSR extends DBController {

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
     * saves changes msde to a ServiceRequest object
     *
     * UP TO DATE
     * @param serviceRequest
     * @param connection
     */
    public static void updateServiceRequest(ServiceRequest serviceRequest, Connection connection){
        try{
            Statement s = connection.createStatement();
            s.execute("UPDATE SERVICEREQUEST SET  SERVICETYPE ='"+ serviceRequest.getServiceType() +"',"+
                    "MESSAGE = '"+ serviceRequest.getMessage() + "'," +
                    "RESOLVED = '" + serviceRequest.isResolved() + "'," +
                    "RESOLVERID = '"+serviceRequest.getResolverID()+"' " +
                    "where  SERVICEID = " + serviceRequest.getServiceID());
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
    public static void deleteServiceRequest(int ID, Connection connection){
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE * FROM SERVICEREQUEST WHERE SERVICEID = ?");
            ps.setInt(1,ID);
            ps.execute();
            ServiceRequest serviceRequest = new ServiceRequest();
            serviceRequest.setServiceID(ID);
            CurrentUser.network.sendServiceRequestPacket(DBNetwork.DELETE_SERVICE_REQUEST, serviceRequest);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
