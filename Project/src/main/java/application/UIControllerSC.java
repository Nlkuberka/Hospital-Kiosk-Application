package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class UIControllerSC extends UIController implements Initializable {
    @FXML
    private Label loading;
    public static Label label;

    @FXML
    private void handleButtonAction(ActionEvent event){

    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        label = loading;
    }



}
