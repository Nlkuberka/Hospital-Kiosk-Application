package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class UIControllerPUD extends UIController{

    String message = "";

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
    public void setDirections(String message){
        directions.setText(message);
    }

}
