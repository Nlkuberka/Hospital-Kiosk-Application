import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        CurrentUser.userID = "Guest";
        CurrentUser.permissions = User.GUEST_PERMISSIONS;
        UIController controller = new UIController(primaryStage);
        controller.goToScene(UIController.ADMIN_TOOLS_VIEW_NODES);

        System.out.println("Collaborator is " + "X");

        /**Connection C = DBController.dbConnect();
         C.createStatement().execute("CREATE TABLE EDGES ("
         + "EDGEID VARCHAR(21),"
         + "STARTNODE VARCHAR(10),"
         + "ENDNODE varchar(10),"
         + "CONSTRAINT EDGE_PK PRIMARY KEY(EDGEID))"
         );

         C.createStatement().execute("CREATE TABLE NODES("
         + "NODEID VARCHAR(10),"
         + "XCOORD INTEGER,"
         + "YCOORD INTEGER,"
         + "FLOOR INTEGER,"
         + "BUILDING VARCHAR(15),"
         + "NODETYPE VARCHAR(4),"
         + "LONGNAME VARCHAR(50),"
         + "SHORTNAME VARCHAR(25),"
         + "CONSTRAINT NODE_PK PRIMARY KEY(NODEID))"
         );
         **/
    }

    public static void main(String[] args) {
        launch(args);
    }
}