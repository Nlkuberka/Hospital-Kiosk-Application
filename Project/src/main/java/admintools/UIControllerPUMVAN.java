package admintools;

import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entities.Node;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import utilities.Callback;
import utilities.Timer;

import java.util.LinkedList;
import java.util.List;

public class UIControllerPUMVAN extends UIController {
    public StackPane parentPane;

    public JFXButton button_cancel;
    @FXML
    public JFXButton button_save;

    @FXML
    public JFXTextField TextField_NodeID;
    @FXML
    public JFXTextField TextField_XCoor;
    @FXML
    public JFXTextField TextField_Building;
    @FXML
    public JFXTextField TextField_LongName;
    @FXML
    public JFXTextField TextField_NodeType;
    @FXML
    public JFXTextField TextField_YCoor;
    @FXML
    public JFXTextField TextField_Floor;
    @FXML
    public JFXTextField TextField_ShortName;
    public Text Text_FieldsRequired;
    private Boolean isFading = false;
    private List<JFXTextField> textFields = new LinkedList<>();
    private Node node;

    @FXML
    public void initialize() {
        textFields.add(TextField_XCoor);
        textFields.add(TextField_YCoor);
        textFields.add(TextField_Building);
        textFields.add(TextField_Floor);
        textFields.add(TextField_LongName);
        textFields.add(TextField_ShortName);
        textFields.add(TextField_NodeID);
    }

    public void closeWindow(MouseEvent mouseEvent) {
        ((javafx.scene.Node) (mouseEvent.getSource())).getScene().getWindow().hide();
    }

    public void setNode(Node node) {
        this.node = node;
        TextField_NodeID.setText(node.getNodeID());
        TextField_XCoor.setText(Integer.toString(node.getXcoord()));
        TextField_YCoor.setText(Integer.toString(node.getYcoord()));
    }

    // TODO After multi-floor have
    @FXML
    public void saveNode(MouseEvent mouseEvent) {
        if (validateInput()) {
            node.setNodeID(TextField_NodeID.getText());
            node.setNodeType(TextField_NodeType.getText());
            node.setBuilding(TextField_Building.getText());
            node.setFloor(TextField_Floor.getText());
            node.setLongName(TextField_LongName.getText());
            node.setShortName(TextField_ShortName.getText());
        } else {
            //noinspection unused
            if (!isFading) {
                //noinspection unused
                Timer timer = new Timer(2, new Callback() {
                    @Override
                    public void onStart() {
                        Text_FieldsRequired.getOpacity();
                        Text_FieldsRequired.setOpacity(1);
                        Text_FieldsRequired.setVisible(true);
                        isFading = true;
                    }

                    @Override
                    public void update(double delta) {
                        //if (delta > 0.3) {
                        //Text_FieldsRequired.setOpacity(1 - delta);
                        //}
                    }

                    @Override
                    public void onEnd() {
                        Text_FieldsRequired.setVisible(false);
                        Text_FieldsRequired.setOpacity(0);
                        isFading = false;
                    }
                });
            }
        }
    }

    private boolean validateInput() {
        for (JFXTextField jfxTextField : textFields) {
            if (jfxTextField.getText().isEmpty()) return false;
        }
        return true;
    }
}
