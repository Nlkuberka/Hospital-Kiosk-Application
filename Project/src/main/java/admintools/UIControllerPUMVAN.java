package admintools;

import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entities.Node;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class UIControllerPUMVAN extends UIController {
    public StackPane parentPane;
    public JFXButton button_cancel;
    public JFXButton button_save;
    public JFXTextField TextField_NodeID;
    private Node node;

    @Override
    public void onShow()
    {

    }

    public void closeWindow(MouseEvent mouseEvent) {
        ((javafx.scene.Node)(mouseEvent.getSource())).getScene().getWindow().hide();
    }

    public void setNode(Node node)
    {
        this.node = node;
        TextField_NodeID.setText(node.getNodeID());
    }
}
