import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class UIControllerPFM extends UIController {

    @FXML
    public ChoiceBox<String> initialLocationSelect;
    public VBox leftVBox;
    public Rectangle rectangleLeft;
    public ChoiceBox<String> destinationSelect;
    public ImageView leftImage;
    public AnchorPane parentAnchorPane;
    public Path path;
    public MenuItem backButton;

    @FXML
    public void initialize() {
//        leftVBox.prefWidthProperty().bind(parentAnchorPane.widthProperty().multiply(0.15));

        // bind choicebox widths to pane
        initialLocationSelect.prefWidthProperty().bind(leftVBox.prefWidthProperty().multiply(0.75));
        destinationSelect.prefWidthProperty().bind(leftVBox.prefWidthProperty().multiply(0.75));

        // bind background image size to window size
        // ensures auto resize works
        leftImage.fitHeightProperty().bind(parentAnchorPane.heightProperty());
        leftImage.fitWidthProperty().bind(parentAnchorPane.widthProperty());

        // bind opaque rectangle to leftVbox width
        rectangleLeft.widthProperty().bind(leftVBox.prefWidthProperty());

        // set default location
//        initialLocationSelect.getItems().add("This Kiosk");
        initialLocationSelect.getSelectionModel().selectFirst();

        Connection conn = DBController.dbConnect();
        LinkedList<Node> allNodes = DBController.generateListofNodes(conn);
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LinkedList<Node> usefulNodes = new LinkedList<>();
        for (Node node : allNodes) {
            if (node.getFloor().equals("2") && node.getBuilding().equals("Tower")) {
                // update choices for initial location
                initialLocationSelect.getItems().add(node.getLongName());
                // update choices for destination location
                destinationSelect.getItems().addAll(node.getLongName());
            }
        }

        // path demo code
//        path.getElements().add(new MoveTo(0.0f, 0.0f));
//        path.getElements().add(new LineTo(100.0f, 100.0f));
//        path.getElements().add(new LineTo(200.0f, 150.0f));
    }

    @FXML
    public void initLocChanged(ActionEvent actionEvent) {
        System.out.println("Initial location selected: " + initialLocationSelect.getValue());
    }

    @FXML
    public void destLocChanged(ActionEvent actionEvent) {
        String value = destinationSelect.getValue();

        System.out.println("Initial location: " + initialLocationSelect.getValue());
        System.out.println("Destination selected: " + value);

        // call getPath if not null
        if (!(value == null || value.length() == 0)) {
            getPath();
        }
    }

    @FXML
    private void clearPath(ActionEvent actionEvent) {
        initialLocationSelect.getSelectionModel().selectFirst();
        destinationSelect.getSelectionModel().clearSelection();
        clearPathOnMap();
    }

    private void getPath() {
        System.out.println("getPath called");
    }

    // NEED: list of all nodes that have: names, XY coords
    private void drawPath(ArrayList<Node> nodes) {
        clearPathOnMap();
        path.getElements().add(new MoveTo(nodes.get(0).getXcoord(), nodes.get(0).getYcoord())); // move path to initLocation

        // get all XY pairs and turn them into lines
        for (int i = 1; i < nodes.size() - 1; i++) {
            Node node = nodes.get(i);
            path.getElements().add(new LineTo(node.getXcoord(), node.getYcoord()));
        }

        // draw lines
    }

    private void clearPathOnMap() {
        path.getElements().removeAll();
    }

    public void goBack(ActionEvent actionEvent) {
        this.goToScene(UIController.LOGIN_MAIN);
    }
}
