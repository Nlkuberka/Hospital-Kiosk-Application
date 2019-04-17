package admintools;

import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DBController;
import database.DBControllerNE;
import entities.Node;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import utilities.Callback;
import utilities.Timer;

import java.sql.Connection;
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
    public Text Text_FieldsRequired;
    public ProgressBar progressBar;
    @FXML
    private JFXTextField TextField_ShortName;
    private Boolean isFading = false;
    private List<JFXTextField> textFields = new LinkedList<>();
    private Node node;
    private UIControllerATMV uiControllerATMV;
    private String action = "";

    @FXML
    public void initialize() {
        textFields.add(TextField_XCoor);
        textFields.add(TextField_YCoor);
        textFields.add(TextField_Building);
        textFields.add(TextField_Floor);
        textFields.add(TextField_LongName);
        textFields.add(TextField_ShortName);
        textFields.add(TextField_NodeID);
        textFields.add(TextField_NodeType);

        TextField_NodeType.textProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.length() > 4) {
                TextField_NodeType.setText(oldValue);
            }
        });

        TextField_NodeID.textProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.length() > 10) {
                TextField_NodeID.setText(oldValue);
            }
        });

        TextField_Building.textProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.length() > 15) {
                TextField_Building.setText(oldValue);
            }
        });

        TextField_LongName.textProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.length() > 49) {
                TextField_LongName.setText(oldValue);
            }
        });

        TextField_ShortName.textProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.length() > 49) {
                TextField_ShortName.setText(oldValue);
            }
        });

        TextField_NodeType.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        TextField_NodeID.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
    }

    @FXML
    public void closeWindow(MouseEvent mouseEvent) {
        parentPane.getScene().getWindow().hide();
    }

    public void closeWindow() {
        parentPane.getScene().getWindow().hide();
    }

    void setProperties(Node node, String action, UIControllerATMV uiControllerATMV) {
        this.node = node;
        this.action = action;
        this.uiControllerATMV = uiControllerATMV;
        TextField_XCoor.setText(Integer.toString(node.getXcoord()));
        TextField_YCoor.setText(Integer.toString(node.getYcoord()));
        TextField_Floor.setText(node.getFloor());
        if (action.equals("EDIT")) {
            TextField_NodeID.setText(node.getNodeID());
            TextField_Building.setText(node.getBuilding());
            TextField_ShortName.setText(node.getShortName());
            TextField_LongName.setText(node.getLongName());
            TextField_NodeType.setText(node.getNodeType());
            TextField_NodeID.setEditable(false);
        }
    }

    // TODO After multi-floor have
    @FXML
    public void saveNode(MouseEvent mouseEvent) {
        if (validateInput()) {
            node.setNodeID(TextField_NodeID.getText());
            node.setNodeType(TextField_NodeType.getText());
            node.setBuilding(TextField_Building.getText());
            node.setLongName(TextField_LongName.getText());
            node.setShortName(TextField_ShortName.getText());
            Connection conn = DBController.dbConnect();
            if (action.equals("ADD")) {
                if (!DBControllerNE.addNode(node, conn)) {
                    TextField_NodeID.clear();
                    return;
                }
            } else if (action.equals("EDIT")) {
                DBControllerNE.updateNode(node, conn);
            }
            DBController.closeConnection(conn);
            //noinspection unused
            Timer timer = new Timer(2, new Callback() {
                @Override
                public void onStart() {
                    progressBar.setProgress(0);
                    progressBar.setVisible(true);
                }

                @Override
                public void update(double delta) {
                    progressBar.setProgress(delta);
                }

                @Override
                public void onEnd() {
                    Platform.runLater(() -> {
                        closeWindow();
                        uiControllerATMV.set();
                        uiControllerATMV.showAddedNode(node);
                    });
                }
            });
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
        boolean isValid = true;
        for (JFXTextField jfxTextField : textFields) {
            if (jfxTextField.getText().isEmpty()) {
                isValid = false;
            }
        }
        return isValid;
    }
}
