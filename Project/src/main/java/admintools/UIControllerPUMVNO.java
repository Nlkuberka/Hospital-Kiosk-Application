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
    private String status = "";

    @FXML
    void closeWindow(MouseEvent mouseEvent) {
        parentPane.getScene().getWindow().hide();
    }

    private void closeWindow() {
        parentPane.getScene().getWindow().hide();
    }

    @FXML
    private void editNode(MouseEvent mouseEvent) throws IOException {
        status = "EDIT-NODE";
        closeWindow();
    }

    @FXML
    private void setKiosk(MouseEvent mouseEvent) throws IOException {
        status = "SET-KIOSK";
        closeWindow();
    }

    @FXML
    private void addEdge(MouseEvent mouseEvent) throws IOException {
        status = "ADD-EDGE";
        closeWindow();
    }

    @FXML
    private void deleteNode(MouseEvent mouseEvent) throws IOException {
        status = "DELETE-NODE";
        closeWindow();
    }

    @FXML
    private void deleteEdge(MouseEvent mouseEvent) throws IOException {
        status = "DELETE-EDGE";
        closeWindow();
    }

    String getStatus()
    {
        return status;
    }

}
