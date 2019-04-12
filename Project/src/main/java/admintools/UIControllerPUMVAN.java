package admintools;

import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entities.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class UIControllerPUMVAN extends UIController {
    public StackPane parentPane;

    public JFXButton button_cancel;
    public JFXButton button_save;

    public JFXTextField TextField_NodeID;
    public JFXTextField TextField_XCoor;
    public JFXTextField TextField_Building;
    public JFXTextField TextField_LongName;
    public JFXTextField TextField_NodeType;
    public JFXTextField TextField_YCoor;
    public JFXTextField TextField_Floor;
    public JFXTextField TextField_ShortName;
    private Node node;



    public void closeWindow(MouseEvent mouseEvent) {
        ((javafx.scene.Node)(mouseEvent.getSource())).getScene().getWindow().hide();
    }

    public void setNode(Node node)
    {
        this.node = node;
        TextField_NodeID.setText(node.getNodeID());
        TextField_XCoor.setText(Integer.toString(node.getXcoord()));
        TextField_YCoor.setText(Integer.toString(node.getYcoord()));
    }
}
