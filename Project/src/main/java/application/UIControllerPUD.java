package application;

import entities.emailDirection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class UIControllerPUD extends UIController{

    @FXML
    private TextArea directions; //the actual directions

    @FXML
    private Button printDirections; //the option to print a receipt

    @FXML
    private Button textDirections; //the option to text the directions to a cell phone

    @FXML
    private Button emailDirections; //the option to email the directions

    @FXML
    private ScrollPane directionsBox; //gives the ability to sroll with directionsS

    @FXML
    private TextField phoneNumber; //the prompt box for a phone number

    @FXML
    private TextField email; //the prompt box for an email

    @FXML
    public void setDirections(String message){
        directions.setText(message); //  sets the text received from the pathfinding
    }

    @FXML
    public void sendEmail(ActionEvent event){
        System.out.println("an email is sent");
        emailDirection sendDirections = new emailDirection();

        String receivingEmail = email.getText();

        sendDirections.sendEmail(directions.getText(), receivingEmail);
    }
}
