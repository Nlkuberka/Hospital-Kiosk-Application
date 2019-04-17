package admintools;

import application.UIController;
import com.jfoenix.controls.JFXButton;
import entities.Node;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class UIControllerPUMVNO extends UIController {
    public JFXButton button_cancel;
    public JFXButton button_editNode;
    public JFXButton button_setKioskLocation;
    public JFXButton button_addEdge;
    public JFXButton button_deleteNode;
    public JFXButton button_deleteEdge;
    public StackPane parentPane;
    private UIControllerATMV uiControllerATMV;
    private Node node;

    @FXML
    void closeWindow(MouseEvent mouseEvent) {
        parentPane.getScene().getWindow().hide();
    }

    void closeWindow() {
        parentPane.getScene().getWindow().hide();
    }

    @FXML
    private void editNode(MouseEvent mouseEvent) throws IOException {
        closeWindow();
        uiControllerATMV.editNode(node);
    }

    @FXML
    private void setKiosk(MouseEvent mouseEvent) throws IOException {
        closeWindow();
        uiControllerATMV.setKiosk(node);
    }

    @FXML
    private void addEdge(MouseEvent mouseEvent) throws IOException {
        closeWindow();
        uiControllerATMV.isAddingEdge = true;
        uiControllerATMV.showAddedNode(node);
        uiControllerATMV.previousNodeID = node.getNodeID();
    }

    @FXML
    private void deleteNode(MouseEvent mouseEvent) throws IOException {
        closeWindow();
        uiControllerATMV.deleteNode(node);
    }

    @FXML
    private void deleteEdge(MouseEvent mouseEvent) throws IOException {
        closeWindow();
        uiControllerATMV.isAddingEdge = false;
        uiControllerATMV.showAddedNode(node);
        uiControllerATMV.previousNodeID = node.getNodeID();
    }

    void setUiControllerATMV(UIControllerATMV uiControllerATMV, Node node) {
        this.uiControllerATMV = uiControllerATMV;
        this.node = node;
    }
}
