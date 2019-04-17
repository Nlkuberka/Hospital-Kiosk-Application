package servicerequests;

import application.CurrentUser;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import database.DBControllerSR;
import entities.ServiceRequest;
import helper.RoomCategoryFilterHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;

public class UIControllerSRRS extends UIController {
    /**
     * < The confirm button
     */

    @FXML
    public CheckBox denomCheckBox1;
    public CheckBox denomCheckBox2;
    public CheckBox denomCheckBox3;
    public CheckBox denomCheckBox4;
    public CheckBox denomCheckBox5;
    public CheckBox denomCheckBox6;
    public CheckBox denomCheckBox7;
    public CheckBox denomCheckBox8;
    public JFXTextField OtherDenomField;

    public JFXCheckBox serviceCheckBox1;
    public JFXCheckBox serviceCheckBox2;
    public JFXCheckBox serviceCheckBox3;
    public JFXCheckBox serviceCheckBox4;
    public JFXCheckBox serviceCheckBox5;
    public JFXCheckBox serviceCheckBox6;
    public JFXCheckBox serviceCheckBox7;

    public JFXButton clearButton;
    public JFXTextField OtherServiceField;
    public TextArea additionalCommentField;
    public StackPane parentPane;
    @FXML
    private ImageView backgroundImage;
    private String serviceType;
    private String finalMessage;
    private RoomCategoryFilterHelper filterHelper;

    private HashMap<String, String> denomAbrev;
    private HashMap<String, String> servAbrev;
    /**
     * < Holds reference between node short name and nodeID
     */
    private LinkedList<CheckBox> denomCheckBoxes;
    private LinkedList<CheckBox> serviceCheckBoxes;
    @FXML
    private JFXComboBox<String> roomSelect;
    @FXML
    private TextArea serviceMessage;
    @FXML
    private JFXButton confirmButton;

    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        serviceMessage.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 150 ? e : null
        ));

        finalMessage = "";

        denomCheckBoxes = new LinkedList<>();
        serviceCheckBoxes = new LinkedList<>();
        denomAbrev = new HashMap<>();
        servAbrev = new HashMap<>();

        denomCheckBoxes.add(denomCheckBox1);
        denomCheckBoxes.add(denomCheckBox2);
        denomCheckBoxes.add(denomCheckBox3);
        denomCheckBoxes.add(denomCheckBox4);
        denomCheckBoxes.add(denomCheckBox5);
        denomCheckBoxes.add(denomCheckBox6);
        denomCheckBoxes.add(denomCheckBox7);
        denomCheckBoxes.add(denomCheckBox8);
        setDenomination(denomCheckBoxes);

        serviceCheckBoxes.add(serviceCheckBox1);
        serviceCheckBoxes.add(serviceCheckBox2);
        serviceCheckBoxes.add(serviceCheckBox3);
        serviceCheckBoxes.add(serviceCheckBox4);
        serviceCheckBoxes.add(serviceCheckBox5);
        serviceCheckBoxes.add(serviceCheckBox6);
        serviceCheckBoxes.add(serviceCheckBox7);
        setService(serviceCheckBoxes);

        denomAbrev.put("Roman Catholic", "RC");
        denomAbrev.put("Judaism", "J");
        denomAbrev.put("Islam", "I");
        denomAbrev.put("Protestant", "PT");
        denomAbrev.put("Hindu", "H");
        denomAbrev.put("Presbyterian", "PS");
        denomAbrev.put("Unitarian", "U");
        denomAbrev.put("Quaker", "Q");

        servAbrev.put("General Support", "GS");
        servAbrev.put("Ethical Guidance", "EG");
        servAbrev.put("Spiritual Healing", "SH");
        servAbrev.put("Meditation", "M");
        servAbrev.put("Counseling", "C");
        servAbrev.put("End of Life", "EOL");
        servAbrev.put("Prayer", "P");
    }

    @FXML
    public void onShow() {
        for(int i = 0; i < serviceCheckBoxes.size(); i++) {
            serviceCheckBoxes.get(i).setSelected(false);
        }
        for(int i = 0; i < denomCheckBoxes.size(); i++) {
            denomCheckBoxes.get(i).setSelected(false);
        }
        roomSelect.getSelectionModel().clearSelection();
        filterHelper = new RoomCategoryFilterHelper(roomSelect, null, true);
        OtherDenomField.setText("");
        OtherServiceField.setText("");
        additionalCommentField.setText("");
        serviceMessage.setText("");
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @FXML
    private void setConfirmButton() throws IOException {
        if(!enablePolicy())
        {
            return;
        }

        String roomShortName = (String) roomSelect.getValue();
        String nodeID = filterHelper.getNodeID();
        String message = finalMessage + "\n" + additionalCommentField.getText();

        if (message.length() > 149) {
            message = message.substring(0, 149);
        }


        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, message, CurrentUser.user.getUserID(), false, null);
        Connection conn = DBControllerSR.dbConnect();
        DBControllerSR.addServiceRequest(sr, conn);
        DBControllerSR.closeConnection(conn);
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

    @FXML
    private void setCancelButton() {
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

    private void setDenomination(LinkedList<CheckBox> denomCheckBoxes) {
        for (CheckBox denomCheckBox : denomCheckBoxes) {
            denomCheckBox.selectedProperty().addListener((observable, wasSelected, isNowSelected) -> {
                if (!wasSelected && isNowSelected) {
                    serviceMessage.appendText("Denomination: " + denomCheckBox.getText() + "\n");
                    finalMessage += ("D: " + denomAbrev.get(denomCheckBox.getText()) + "\n");
                } else if (wasSelected && !isNowSelected) {
                    serviceMessage.setText(serviceMessage.getText().replace(("Denomination: " + denomCheckBox.getText() + "\n"), ""));
                    finalMessage = finalMessage.replace(("D: " + denomAbrev.get(denomCheckBox.getText()) + "\n"), "");
                }
            });
        }
    }

    private void setService(LinkedList<CheckBox> serviceCheckBoxes) {
        for (CheckBox serviceCheckBox : serviceCheckBoxes) {
            serviceCheckBox.selectedProperty().addListener((observable, wasSelected, isNowSelected) -> {
                if (!wasSelected && isNowSelected) {
                    serviceMessage.appendText("Service: " + serviceCheckBox.getText() + "\n");
                    finalMessage += ("S: " + servAbrev.get(serviceCheckBox.getText()) + "\n");
                } else if (wasSelected && !isNowSelected) {
                    serviceMessage.setText(serviceMessage.getText().replace(("Service: " + serviceCheckBox.getText() + "\n"), ""));
                    finalMessage = finalMessage.replace(("S: " + servAbrev.get(serviceCheckBox.getText()) + "\n"), "");
                }
            });
        }
    }

    @FXML
    private void setServiceOther(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER && !(OtherServiceField.getText().isEmpty())) {
            serviceMessage.appendText("Service: " + OtherServiceField.getText() + "\n");
            finalMessage += ("S: " + OtherServiceField.getText() + "\n");
            OtherServiceField.clear();
        }
    }

    @FXML
    private void setDenominationOther(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER && !(OtherDenomField.getText().isEmpty())) {
            serviceMessage.appendText("Denomination: " + OtherDenomField.getText() + "\n");
            finalMessage += ("D: " + OtherDenomField.getText() + "\n");
            OtherDenomField.clear();
        }
    }

    @FXML
    public void setClearButton(ActionEvent actionEvent) {
        serviceMessage.clear();
        additionalCommentField.clear();
        finalMessage = "";

        for (CheckBox denomCheckBox : denomCheckBoxes) {
            denomCheckBox.setSelected(false);
        }

        for (CheckBox serviceCheckBox : serviceCheckBoxes) {
            serviceCheckBox.setSelected(false);
        }
    }

    @FXML
    public void setCancelButton(ActionEvent actionEvent) {
        goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

    private boolean enablePolicy() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/servicerequests/service_request_rs_policy.fxml"));
        Parent root = loader.load();
        UIControllerSRRSP uiControllerSRRSP = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setHeight(400);
        stage.setWidth(600);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.toFront();
        stage.showAndWait();
        return uiControllerSRRSP.getStatus();
    }
}
