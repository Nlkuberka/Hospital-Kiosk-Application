import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editNodeController extends Controller{
    private Node oldNode;

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

    public void setNodeID(String nodeID) {
        oldNode = null;
        if(nodeID == null) {
            nodeIDTextfield.setDisable(false);
            oldNode = new Node();
        }
        System.out.println(nodeID);

        //Get Node Data
        //setFields(oldNode);
    }

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

    @FXML
    private void validateEdits() {
        if(!oldNode.getNodeID().equals(nodeIDTextfield.getText()) ||
           oldNode.getXcoord() != Integer.parseInt(xcoordTextfield.getText()) ||
           oldNode.getYcoord() != Integer.parseInt(ycoordTextfield.getText()) ||
           oldNode.getFloor() != Integer.parseInt(floorTextfield.getText()) ||
           !oldNode.getBuilding().equals(buildingTextfield.getText()) ||
           !oldNode.getNodeType().equals(nodeTypeTextfield.getText()) ||
           !oldNode.getLongName().equals(longNameTextfield.getText()) ||
           !oldNode.getShortName().equals(shortNameTextfield.getText())) {
            saveButton.setDisable(false);
        } else {
            saveButton.setDisable(true);
        }
    }

    @FXML
    private void setCancelButton() {
        goToView((Stage) cancelButton.getScene().getWindow());
    }

    @FXML
    private void setSaveButton() {
        nodeIDTextfield.setDisable(true);
        goToView((Stage) saveButton.getScene().getWindow());
    }

    @FXML
    private void setRemoveButton() {

    }

}
