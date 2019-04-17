import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import java.sql.Connection;

import static jdk.nashorn.internal.objects.NativeString.trim;

public class UIControllerAU extends UIController {

    @FXML
    private JFXTextField userIDInput;
    @FXML
    private JFXTextField usernameInput;
    @FXML
    private JFXTextField passwordInput;
    @FXML
    private JFXTextField permissionsInput;

    @FXML
    private JFXButton addUserButton;

    @FXML
    public void initialize(){
        userIDInput.clear();
        usernameInput.clear();
        passwordInput.clear();
        permissionsInput.clear();
    }

    @FXML
    private void setAddButton(){
        //Check if each text field is filled
        if(!trim(userIDInput.getText()).equals("") && !trim(usernameInput.getText()).equals("")
                && !trim(passwordInput.getText()).equals("") && !trim(permissionsInput.getText()).equals("")){
            Connection connection = DBControllerAPI.dbConnect();
            User newUser = new User(userIDInput.getText(),Integer.parseInt(permissionsInput.getText()),
                    usernameInput.getText(),passwordInput.getText());
            DBControllerAPI.addUser(newUser,connection);
        }
        userIDInput.clear();
        usernameInput.clear();
        passwordInput.clear();
        permissionsInput.clear();
    }

    @FXML
    private void setCancelButton() {
        this.goToScene(UIController.SERVICE_REQUEST_MAIN,"");
    }

}
