package servicerequests;

import application.CurrentUser;
import application.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import entities.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
    @FXML
    private ImageView backgroundImage;
    private String serviceType;
    private String finalMessage;
    private Map<String, String> nodeIDs;

    private HashMap<String, String> denomAbrev;
    private HashMap<String, String> servAbrev;
    /**
     * < Holds reference between node short name and nodeID
     */
    private LinkedList<CheckBox> denomCheckBoxes;
    private LinkedList<CheckBox> serviceCheckBoxes;
    @FXML
    private ChoiceBox roomSelect;
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
        List<String> nodeShortNames = new ArrayList<String>();
        nodeIDs = new HashMap<String, String>();

        // DB Get all Nodes
        try {
            Connection conn = DBController.dbConnect();
            ResultSet rs = conn.createStatement().executeQuery("Select * From NODES where FLOOR = '2' AND BUILDING = 'Tower'");
            while (rs.next()) {
                nodeIDs.put(rs.getString("SHORTNAME"), rs.getString("NODEID"));
                nodeShortNames.add(rs.getString("SHORTNAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        roomSelect.setItems(FXCollections.observableList(nodeShortNames));
        roomSelect.getSelectionModel().selectFirst();
        serviceMessage.setText("");
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @FXML
    private void setConfirmButton() {
        String roomShortName = (String) roomSelect.getValue();
        String nodeID = nodeIDs.get(roomShortName);
        String message = finalMessage + "\n" + additionalCommentField.getText();

        if(message.length() > 149)
        {
            message = message.substring(0, 149);
        }



        ServiceRequest sr = new ServiceRequest(nodeID, serviceType, message, CurrentUser.user.getUserID(), false, null);
        Connection conn = DBController.dbConnect();
        DBController.addServiceRequest(sr, conn);
        DBController.closeConnection(conn);
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
}
