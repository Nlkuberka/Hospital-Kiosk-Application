package application;

import com.jfoenix.controls.JFXButton;
import entities.Graph;
import entities.emailDirection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.junit.FixMethodOrder;

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
        System.out.println("an email is sent");
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

    @FXML
    public void setOkButton(){
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
