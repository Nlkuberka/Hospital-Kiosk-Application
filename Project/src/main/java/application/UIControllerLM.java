package application;

import database.DBController;
import database.DBControllerU;
import entities.User;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.sql.Connection;

/**
 * The UIController for the Login screen
 * Allows a user to login, for admins to login, or for guests to enter
 * @author Jonathan Chang
 * @version iteration1
 */
public class UIControllerLM extends UIController {

    @FXML
    private ImageView backgroundImage;
    @FXML
    private JFXTabPane tabs;

    @FXML
    private Tab user_tab;

    @FXML
    private Tab admin_tab;

    @FXML
    private Tab guest_tab;

    /**
     * < The Login As Guest Button
     */

    @FXML
    private JFXButton loginAsUserButton;
    /**
     * < The Login As User Button
     */

    @FXML
    private JFXButton loginAsAdminButton;

    /**
     * < The Login As Admin Button
     */


    @FXML
    private JFXTextField userUsernameTextField;

    @FXML
    private JFXPasswordField userPasswordTextField;

    @FXML
    private JFXTextField adminUsernameTextField;

    @FXML
    private JFXPasswordField adminPasswordTextField;

    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton cancelButton1;

    @FXML
    private JFXButton beginButton;




    public UIControllerLM() {

    }

    /**
     * Called when the scene is first created
     */
    @FXML
    public void initialize() {
        tabs.getSelectionModel().selectedItemProperty().addListener(param -> {
            setDefaultButton();
        });
       // tabs.getTabs().get.setStyle("-fx-background-color: #fbe58e")
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());
        borderPane.setPrefHeight(primaryStage.getHeight());
        tabs.setPrefWidth(primaryStage.getWidth());
        tabs.setPrefHeight(primaryStage.getHeight());
    }

    /**
     * Called whenever the scene is shown
     */
    @Override
    public void onShow() {
        userUsernameTextField.setText("");
        userPasswordTextField.setText("");
        adminUsernameTextField.setText("");
        adminPasswordTextField.setText("");
    }


    /**
     * Goes to the User Main Menu
     */
    @FXML
    private void setLoginAsUserButton() {
        String username = userUsernameTextField.getText();
        String password = userPasswordTextField.getText();

        User user = checkLogin(username, password, User.BASIC_PERMISSIONS);
        if(user == null) {
            this.popupMessage("Incorrect username or password.", true);
            return;
        }

        this.goToScene(UIController.USER_MAIN_MENU_MAIN);
    }

    /**
     * Goes to the Admin Main Menu
     */
    @FXML
    private void setLoginAsAdminButton() {
        String username = adminUsernameTextField.getText();
        String password = adminPasswordTextField.getText();
        User user = checkLogin(username, password, User.ADMIN_PERMISSIONS);
        if(user == null) {
            this.popupMessage("Incorrect username or password.", true);
            return;
        }

        this.goToScene(UIController.ADMIN_MAIN_MENU_MAIN);
    }

    @FXML
    private void goToUserTab() {
        tabs.getSelectionModel().select(user_tab);
    }

    @FXML
    private void setCancelButton() {
        this.goToScene(UIController.PATHFINDING_MAIN);
    }

    @FXML
    private void setBeginButton() {
        this.goToScene(UIController.PATHFINDING_MAIN);
    }

    private User checkLogin(String username, String password, int permissions) {
        User user = null;
        Connection conn = DBController.dbConnect();
        user = DBControllerU.loginCheck(username,password,conn,permissions);
        DBController.closeConnection(conn);
        if(user == null) {
            return null;
        }
        CurrentUser.user = user;
        return user;
    }

    private void setDefaultButton() {
        String tabName = tabs.getSelectionModel().getSelectedItem().getText();
        loginAsUserButton.setDefaultButton(false);
        loginAsAdminButton.setDefaultButton(false);
        if(tabName.equals("User")) {
            loginAsUserButton.setDefaultButton(true);
        } else if(tabName.equals("Administrator")) {
            loginAsAdminButton.setDefaultButton(true);
        }
    }

}
