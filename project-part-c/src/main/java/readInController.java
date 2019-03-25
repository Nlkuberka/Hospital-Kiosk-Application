import javafx.fxml.FXML;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class readInController extends Controller{
    private int rowNum = 1;
    private int colNum = 8;

    @FXML
    private GridPane databaseGrid;

    @FXML
    private AnchorPane databaseAnchor;

    @FXML
    private Button toDownloadButton;

    @FXML
    private Label nodeIDLabel;

    @FXML
    private Button addNodeButton;

    @FXML
    public void initialize(){
        for(int i = 0; i < 100; i++) {
            Node node1 = new Node("" + i, i * 7, i * 8, i + 2, "Building " + i, "Room", "Room 1", "RM!");
            addRow(node1);
        }
    }

    public void clearGrid() {
        rowNum = 1;
        databaseGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != GridPane.getRowIndex(nodeIDLabel));
        databaseGrid.setPrefHeight(rowNum * 30.0);
        databaseAnchor.setPrefHeight(rowNum * 30.0);
    }

    public void addRow(Node node){
        Label[] labels = new Label[colNum];
        String[] values = new String[colNum];
        Button editButton = new Button("Edit");

        values[0] = node.getNodeID();
        values[1] = "" + node.getXcoord();
        values[2] = "" + node.getYcoord();
        values[3] = "" + node.getFloor();
        values[4] = node.getBuilding();
        values[5] = node.getNodeType();
        values[6] = node.getLongName();
        values[7] = node.getShortName();

        for(int i = 0; i < colNum; i++) {
            labels[i] = new Label(values[i]);
            labels[i].setAlignment(Pos.CENTER);
            labels[i].setPrefHeight(30.0);
            databaseGrid.add(labels[i], i, rowNum);
        }

        editButton.setAlignment(Pos.CENTER);
        editButton.setId("" + values[0]);
        editButton.setOnAction(e -> goToEditViewer(editButton.getId()));
        databaseGrid.add(editButton, 8, rowNum);

        rowNum++;
        databaseGrid.setPrefHeight(rowNum * 30.0);
        databaseAnchor.setPrefHeight(rowNum * 30.0);
    }

    @FXML
    private void goToEditViewer(String nodeID){
        goToEdit((Stage) databaseGrid.getScene().getWindow(), nodeID);
    }

    @FXML
    private void goToDownloadViewer() {
        goToDownload((Stage) databaseGrid.getScene().getWindow());
    }

    @FXML
    private void setAddNodeButton() {
        goToEdit((Stage) databaseGrid.getScene().getWindow(), null);
    }
}
