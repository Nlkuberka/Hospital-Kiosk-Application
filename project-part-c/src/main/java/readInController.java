import javafx.fxml.FXML;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class readInController extends Controller{
    private int rowNum = 1;
    private int colNum = 8;
    private Map<String, Integer> nodeRowNum;

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

    /**
     * Initializes the view list on scene creation
     */
    @FXML
    public void initialize(){
        nodeRowNum = new TreeMap<String, Integer>();
        for(int i = 0; i < 100; i++) {
            Node node1 = new Node("" + i, i * 7, i * 8, i + 2, "Building " + i, "Room", "Room 1", "RM!");
            addRow(node1);
        }
        removeNode("3");
    }

    /**
     * Clears all of the current node rows in the grid
     */
    public void clearGrid() {
        rowNum = 1;
        databaseGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != GridPane.getRowIndex(nodeIDLabel));
        databaseGrid.setPrefHeight(rowNum * 30.0);
        databaseAnchor.setPrefHeight(rowNum * 30.0);
    }

    /**
     * Adds all of the nodes in the list to the grid
     * @param nodes The list of nodes to add
     */
    public void addNodes(List<Node> nodes) {
        for(int i = 0; i < nodes.size(); i++) {
            addRow(nodes.get(i));
        }
    }

    /**
     * Adds a single node row to the grid along with an edit button
     * @param node The node to add
     */
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

        // Add labels to grid
        for(int i = 0; i < colNum; i++) {
            labels[i] = new Label(values[i]);
            labels[i].setAlignment(Pos.CENTER);
            labels[i].setPrefHeight(30.0);
            GridPane.setHalignment(labels[i], HPos.CENTER);
            databaseGrid.add(labels[i], i, rowNum);
        }

        // Add button to grid
        editButton.setAlignment(Pos.CENTER);
        editButton.setId("" + values[0]);
        editButton.setOnAction(e -> goToEditViewer(editButton.getId()));
        GridPane.setHalignment(editButton, HPos.CENTER);
        databaseGrid.add(editButton, 8, rowNum);

        nodeRowNum.put(node.getNodeID(), rowNum);

        rowNum++;
        databaseGrid.setPrefHeight(rowNum * 30.0);
        databaseAnchor.setPrefHeight(rowNum * 30.0);
    }

    /**
     * Removes the row with the given nodeID
     * @param nodeID The nodeID to remove
     */
    private void removeNode(String nodeID) {
        rowNum--;
        databaseGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == nodeRowNum.get(nodeID));
        databaseGrid.setPrefHeight(rowNum * 30.0);
        databaseAnchor.setPrefHeight(rowNum * 30.0);
    }

    /**
     * To the the edit scene
     * @param nodeID The ID of the node to edit
     */
    @FXML
    private void goToEditViewer(String nodeID){
        goToEdit((Stage) databaseGrid.getScene().getWindow(), nodeID);
    }

    /**
     * Go to the download scene
     */
    @FXML
    private void goToDownloadViewer() {
        goToDownload((Stage) databaseGrid.getScene().getWindow());
    }

    /**
     * Goes to the edit scene but passes in a null nodeID for a new node
     */
    @FXML
    private void setAddNodeButton() {
        goToEdit((Stage) databaseGrid.getScene().getWindow(), null);
    }
}
