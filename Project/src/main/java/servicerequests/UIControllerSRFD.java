package servicerequests;

import application.CurrentUser;
import com.jfoenix.controls.JFXTextField;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import database.DBControllerSR;
import entities.ServiceRequest;
import helper.RoomCategoryFilterHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;
import utilities.Tooltip;

import java.sql.Connection;

public class UIControllerSRFD extends UIController {
    private String flowerDelivery;
    private RoomCategoryFilterHelper filterHelper;

    @FXML private JFXComboBox<String> roomSelect;

    @FXML
    private JFXTextArea serviceMessage1;

    @FXML
    private JFXTextField phoneNum;

    @FXML
    private JFXButton confirmButton; /**< The confirm button*/

    @FXML
    private Label costLabel;

    @FXML public ImageView Pic1;
    @FXML public ImageView Pic2;
    @FXML public ImageView Pic3;
    @FXML public ImageView Pic4;
    @FXML public ImageView Pic5;
    @FXML public ImageView Pic6;
    @FXML public ImageView Pic7;
    @FXML public ImageView Pic8;
    @FXML public ImageView Pic9;


    @FXML
    public void initialize() {
        //backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());
        this.flowerDelivery = "Flower Delivery";
        serviceMessage1.setTextFormatter(new TextFormatter<String>(e ->
                e.getControlNewText().length() <= 100 ? e : null
        ));
        new Tooltip(Pic1, "Daisies");
        new Tooltip(Pic2, "Small Assorted");
        new Tooltip(Pic3, "Large Roses");
        new Tooltip(Pic4, "Daffodils");
        new Tooltip(Pic5, "Snapdragons");
        new Tooltip(Pic6, "Small Roses");
        new Tooltip(Pic7, "Medium Assorted");
        new Tooltip(Pic8, "Large Assorted");
        new Tooltip(Pic9, "Sunflower");
    }

    @FXML
    public void onShow() {
        roomSelect.getSelectionModel().clearSelection();
        phoneNum.setText("");
        filterHelper = new RoomCategoryFilterHelper(roomSelect, null, true);
        serviceMessage1.setText("");
    }

    @FXML
    private void setConfirmButton() {
        String nodeID = filterHelper.getNodeID();
        String message = serviceMessage1.getText();
        String phoneNumber = phoneNum.getText();

        ServiceRequest sr = new ServiceRequest(nodeID, flowerDelivery, phoneNumber + message + costLabel.getText(), CurrentUser.user.getUserID(), false, null);
        sr.setServiceID(sr.getTimeStamp());

        Connection conn = DBControllerSR.dbConnect();
        DBControllerSR.addServiceRequest(sr,conn);
        DBControllerSR.closeConnection(conn);
        setCancelButton();
    }

    @FXML
    private void setCancelButton() {
        Stage stage = (Stage) phoneNum.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void costPic1() {
        costLabel.setText("$20.00");
        applyShadow(Pic1);
    }
    @FXML
    public void costPic2() {
        costLabel.setText("$15.00");
        applyShadow(Pic2);
    }
    @FXML
    public void costPic3() {
        costLabel.setText("$30.00");
        applyShadow(Pic3);
    }
    @FXML
    public void costPic4() {
        costLabel.setText("$13.00");
        applyShadow(Pic4);
    }
    @FXML
    public void costPic5() {
        costLabel.setText("$25.00");
        applyShadow(Pic5);
    }
    @FXML
    public void costPic6() {
        costLabel.setText("$29.00");
        applyShadow(Pic6);
    }
    @FXML
    public void costPic7() {
        costLabel.setText("$23.00");
        applyShadow(Pic7);
    }
    @FXML
    public void costPic8() {
        costLabel.setText("$10.00");
        applyShadow(Pic8);
    }
    @FXML
    public void costPic9() {
        costLabel.setText("$5.00");
        applyShadow(Pic9);
    }

    public void applyShadow(ImageView image){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setHeight(image.getFitHeight() * 1.1);
        dropShadow.setWidth(image.getFitWidth() * 1.1);
        dropShadow.setColor(Color.web("ffc41e"));
        clearShadow();
        image.setEffect(dropShadow);
    }

    public void clearShadow(){
        Pic1.setEffect(null);
        Pic2.setEffect(null);
        Pic3.setEffect(null);
        Pic4.setEffect(null);
        Pic5.setEffect(null);
        Pic6.setEffect(null);
        Pic7.setEffect(null);
        Pic8.setEffect(null);
        Pic9.setEffect(null);
    }
}
