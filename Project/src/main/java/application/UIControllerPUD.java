package application;

import com.jfoenix.controls.JFXButton;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import entities.emailDirection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UIControllerPUD extends UIController {

    @FXML
    private TextArea directions; //the actual directions

    @FXML
    private JFXButton printDirections; //the option to print a receipt

    @FXML
    private JFXButton textDirections; //the option to text the directions to a cell phone

    @FXML
    private JFXButton emailDirections; //the option to email the directions

    @FXML
    private ScrollPane directionsBox; //gives the ability to sroll with directionsS

    @FXML
    private TextField phoneNumber; //the prompt box for a phone number

    @FXML
    private TextField email; //the prompt box for an email

    @FXML
    private JFXButton okButton; //ok button to exit confirmation window

    @FXML
    public void setDirections(String message) {
        directions.setText(message); //  sets the text received from the pathfinding
    }

    @FXML
    public void sendEmail(ActionEvent event) {
        if (email.getText().equals("")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/no_email_entered_popup.fxml"));

                Scene popupScene = new Scene(fxmlLoader.load(), 300, 150);
                Stage popupStage = new Stage();

                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initOwner(this.primaryStage);

                UIControllerPUD controller = (UIControllerPUD) fxmlLoader.getController();

                popupStage.setTitle("Email Error");
                popupStage.setScene(popupScene);
                popupStage.show();

            } catch (IOException e) {
                Logger logger = Logger.getLogger((getClass().getName()));
                logger.log(Level.SEVERE, "Failed to create new window.", e);

            }

        } else {
            emailDirection sendDirections = new emailDirection();

            String receivingEmail = email.getText();

            sendDirections.sendEmail(directions.getText(), receivingEmail);

            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/popup_confirm_email.fxml"));

                Scene popupScene = new Scene(fxmlLoader.load(), 300, 150);
                Stage popupStage = new Stage();

                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initOwner(this.primaryStage);

                UIControllerPUD controller = (UIControllerPUD) fxmlLoader.getController();

                popupStage.setTitle("Email Confirmation");
                popupStage.setScene(popupScene);
                popupStage.show();
            } catch (IOException e) {
                Logger logger = Logger.getLogger((getClass().getName()));
                logger.log(Level.SEVERE, "Failed to create new window.", e);

            }
        }
    }

    public static final String ACCOUNT_SID = "AC176f9cd821ffa8dcad559ceecad9ecf1";
    public static final String AUTH_TOKEN = "ab7f1a58335f47c98bac61b471920dfe";

    @FXML
    public void sendText(ActionEvent event){
        String number = phoneNumber.getText();

        if (number.length() != 10) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/no_number_entered_popup.fxml"));

                Scene popupScene = new Scene(fxmlLoader.load(), 300, 150);
                Stage popupStage = new Stage();

                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initOwner(this.primaryStage);

                UIControllerPUD controller = (UIControllerPUD) fxmlLoader.getController();

                popupStage.setTitle("Phone Number Error");
                popupStage.setScene(popupScene);
                popupStage.show();

            } catch (IOException e) {
                Logger logger = Logger.getLogger((getClass().getName()));
                logger.log(Level.SEVERE, "Failed to create new window.", e);

            }

        } else {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber("+1" + number),
                    new com.twilio.type.PhoneNumber("+17472290044"),
                    directions.getText())
                    .create();

            System.out.println("it sent");

            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/popup_confirm_text.fxml"));

                Scene popupScene = new Scene(fxmlLoader.load(), 300, 150);
                Stage popupStage = new Stage();

                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initOwner(this.primaryStage);

                UIControllerPUD controller = (UIControllerPUD) fxmlLoader.getController();

                popupStage.setTitle("Text Confirmation");
                popupStage.setScene(popupScene);
                popupStage.show();

            } catch (IOException e) {
                Logger logger = Logger.getLogger((getClass().getName()));
                logger.log(Level.SEVERE, "Failed to create new window.", e);

            }
        }

    }

    @FXML
    public void setOkButton() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
