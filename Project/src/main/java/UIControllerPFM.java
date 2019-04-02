import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;

import java.util.ArrayList;

public class UIControllerPFM extends UIController {

    @FXML
    public ChoiceBox<String> initialLocationSelect;
    public VBox leftVBox;
    public Rectangle rectangleLeft;
    public ChoiceBox<String> destinationSelect;
    public ImageView leftImage;
    public AnchorPane parentAnchorPane;
    public Path path;

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
        initialLocationSelect.getItems().add("This Kiosk");
        initialLocationSelect.getSelectionModel().selectFirst();

        // update choices for initial location
        initialLocationSelect.getItems().addAll("choice1", "choice2", "hi");
        // update choices for destination location
        destinationSelect.getItems().addAll("dest1", "dest2");

        path.getElements().add(new MoveTo(0.0f, 0.0f));
        path.getElements().add(new LineTo(100.0f, 100.0f));
    }

    @FXML
    public void initLocChanged(ActionEvent actionEvent) {
        System.out.println("Initial location selected: " + initialLocationSelect.getValue());
    }

    @FXML
    public void destLocChanged(ActionEvent actionEvent) {
        System.out.println("Destination selected: " + destinationSelect.getValue());
        System.out.println("Initial location: " + initialLocationSelect.getValue());
        getPath();
    }

    @FXML
    private void clearPath(ActionEvent actionEvent) {
        initialLocationSelect.getSelectionModel().selectFirst();
        destinationSelect.getSelectionModel().clearSelection();
    }

    private void getPath() {
        System.out.println("getPath called");
    }

    // NEED: list of all nodes that have: names, XY coords
    private void drawPath(ArrayList<Node> nodes) {
        path.getElements().removeAll();
        path.getElements().add(new MoveTo(nodes.get(0).getXcoord(), nodes.get(0).getYcoord())); // move path to origin

        // get all XY pairs and turn them into lines
        for (int i = 1; i < nodes.size() - 1; i++) {
            Node node = nodes.get(i);
            path.getElements().add(new LineTo(node.getXcoord(), node.getYcoord()));
        }

        // draw lines
        // NEED: stack pane
    }
}
