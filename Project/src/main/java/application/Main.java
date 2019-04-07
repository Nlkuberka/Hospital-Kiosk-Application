package application;

import entities.Reservation;
import entities.User;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        UIController controller = new UIController(primaryStage);

        controller.goToScene(UIController.LOGIN_MAIN);

        System.out.println("Collaborator is " + "X");

        Connection connection = DBController.dbConnect();
//        Reservation reservation = new Reservation("HRETL00202", "AMDIN0001", "2019-04-03", "18:30:00", "21:30:49");
        Reservation reservation1 = new Reservation("HRETL00202", "AMDIN0001", "2019-04-06", "08:12:00", "10:41:50");
        Reservation reservation2 = new Reservation("DHALL00902", "USER0001", "2019-04-09", "14:35:00", "16:20:00");
        Reservation reservation3 = new Reservation("GDEPT01901", "AMDIN0001", "2019-04-14", "18:35:00", "21:52:09");

        reservation1.setRsvID(DBController.addReservation(reservation1, connection));
        System.out.println(reservation1.getRsvID());
        reservation2.setRsvID(DBController.addReservation(reservation2, connection));
        System.out.println(reservation2.getRsvID());
        reservation3.setRsvID(DBController.addReservation(reservation3, connection));
        System.out.println(reservation3.getRsvID());


//        Reservation reservation1Edit = new Reservation("HRETL00202", "AMDIN0001", "2019-04-05", "18:35:00", "21:41:50");
//
//        DBController.updateReservation(reservation1Edit, connection);




//        DBController.initializeAppDB();

        //DBController.loadNodeData(new File("nodesv3.csv"), conn);
        //DBController.loadEdgeData(new File("edgesv3.csv"), conn);


        // IF YOU DO NOT HAVE THE TABLES SET UP RUN THIS CODE TO GENERATE
        /*DBController.createTable("CREATE TABLE NODES(" +
                "NODEID VARCHAR(10),"+
                "XCOORD INTEGER," +
                "YCOORD INTEGER," +
                "FLOOR VARCHAR(3)," +
                "BUILDING VARCHAR(15)," +
                "NODETYPE VARCHAR(4)," +
                "LONGNAME VARCHAR(50)," +
                "SHORTNAME VARCHAR(50)," +
                "CONSTRAINT NODE_PK PRIMARY KEY(NODEID)" +
                ")",conn);
        DBController.createTable("CREATE TABLE USERS(" +
                "  USERID VARCHAR(10)," +
                "  PERMISSION SMALLINT," +
                "  USENAME VARCHAR(15)," +
                "  PASSWORD VARCHAR(15)," +
                "  CONSTRAINT USER_PK PRIMARY KEY(USERID)" +
                ")",conn);
        DBController.createTable("CREATE TABLE EDGES (" +
                "  EDGEID VARCHAR(21)," +
                "  STARTNODE VARCHAR(10) REFERENCES NODES(NODEID)," +
                "  ENDNODE varchar(10) REFERENCES NODES(NODEID)," +
                "  CONSTRAINT EDGE_PK PRIMARY KEY(EDGEID)" +
                ")",conn);
        DBController.createTable("CREATE TABLE SERVICEREQUEST(" +
                "  NODEID VARCHAR(10) REFERENCES NODES(NODEID)," +
                "  SERVICETYPE VARCHAR(20)," +
                "  MESSAGE VARCHAR(100)," +
                "  USERID VARCHAR(10) REFERENCES USERS(USERID)," +
                "  RESOLVED BOOLEAN" +
                ")",conn);
        DBController.createTable("CREATE TABLE RESERVATIONS(" +
                "  NODEID REFERENCES NODES(NODEID)," +
                "  USERID REFERENCES USERS(USERID)," +
                "  DAY DATE," +
                "  STARTTIME TIME," +
                "  ENDTIME TIME" +
                ")",conn); */

    }

    public static void main(String[] args) {
        launch(args);
    }
}