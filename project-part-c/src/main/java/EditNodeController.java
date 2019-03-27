import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditNodeController extends Controller{
    private Node oldNode;
    private Node newNode;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextField nodeIDTextfield;

    @FXML
    private TextField xcoordTextfield;

    @FXML
    private TextField ycoordTextfield;

    @FXML
    private TextField floorTextfield;

    @FXML
    private TextField buildingTextfield;

    @FXML
    private TextField nodeTypeTextfield;

    @FXML
    private TextField longNameTextfield;

    @FXML
    private TextField shortNameTextfield;

    /**
     * Sets the node that is to be edited
     * and adds the node fields to the UI
     * @param nodeID The ID of the node to add, null for new node
     */
    public void setNodeID(String nodeID) {
        oldNode = null;
        System.out.println(nodeID);
        if(nodeID == null) {
            nodeIDTextfield.setDisable(false);
            oldNode = new Node("z", -1, -1, -1, "z", "z" , "z" ,"z");
        }
        System.out.println(oldNode);

        //Get Node Data
        //setFields(oldNode);
    }

    /**
     * Sets the UI textfields to the initial node values
     * @param node The node to put in
     */
    @FXML
    public void setFields(Node node)  {
        nodeIDTextfield.setText(node.getNodeID());
        xcoordTextfield.setText("" + node.getXcoord());
        ycoordTextfield.setText("" + node.getYcoord());
        floorTextfield.setText("" + node.getFloor());
        buildingTextfield.setText(node.getBuilding());
        nodeTypeTextfield.setText(node.getNodeType());
        longNameTextfield.setText(node.getLongName());
        shortNameTextfield.setText(node.getShortName());
    }

    /**
     * Validates that the user has actually made edits to the node fields
     * Enables the save button if so, disable otherwise
     */
    @FXML
    private void validateEdits() {
        try{
            Integer.parseInt(xcoordTextfield.getText());
            Integer.parseInt(ycoordTextfield.getText());
            Integer.parseInt(floorTextfield.getText());
        } catch(Exception e) {
            return;
        }
        // Validate that edits have been made
        if((!oldNode.getNodeID().equals(nodeIDTextfield.getText()) ||
            oldNode.getXcoord() != Integer.parseInt(xcoordTextfield.getText()) ||
            oldNode.getYcoord() != Integer.parseInt(ycoordTextfield.getText()) ||
            oldNode.getFloor() != Integer.parseInt(floorTextfield.getText()) ||
            !oldNode.getBuilding().equals(buildingTextfield.getText()) ||
            !oldNode.getNodeType().equals(nodeTypeTextfield.getText()) ||
            !oldNode.getLongName().equals(longNameTextfield.getText()) ||
            !oldNode.getShortName().equals(shortNameTextfield.getText())) &&

            // Validate lengths of the database columns
            nodeIDTextfield.getText().length() <= 10 &&
            Integer.parseInt(xcoordTextfield.getText()) >= 0 &&
            Integer.parseInt(ycoordTextfield.getText()) >= 0 &&
            Integer.parseInt(floorTextfield.getText()) >= 1 &&
            buildingTextfield.getText().length() <= 15 &&
            nodeTypeTextfield.getText().length() <= 4 &&
            longNameTextfield.getText().length() <= 50 &&
            shortNameTextfield.getText().length() <= 25
        ) {
            saveButton.setDisable(false);
        } else {
            saveButton.setDisable(true);
        }
    }

    /**
     * Cancels the current changes and sets the scene back to the view scene
     */
    @FXML
    private void setCancelButton() {
        this.goToScene(this.VIEW_STRING);
    }

    /**
     * Saves the current node and its changes
     * Sets the scene back to the view nodes scene
     */
    @FXML
    private void setSaveButton() {
        newNode = new Node();
        newNode.setNodeID(nodeIDTextfield.getText());
        newNode.setXcoord(Integer.parseInt(xcoordTextfield.getText()));
        newNode.setYcoord(Integer.parseInt(ycoordTextfield.getText()));
        newNode.setFloor(Integer.parseInt(floorTextfield.getText()));
        newNode.setBuilding(buildingTextfield.getText());
        newNode.setNodeType(nodeTypeTextfield.getText());
        newNode.setLongName(longNameTextfield.getText());
        newNode.setShortName(shortNameTextfield.getText());

        nodeIDTextfield.setDisable(true);

        this.goToScene(this.VIEW_STRING);
    }

    /**
     * Removes the current node and sets the scene back to the view nodes scene
     */
    @FXML
    private void setRemoveButton() {
        this.goToScene(this.VIEW_STRING);
    }

}
