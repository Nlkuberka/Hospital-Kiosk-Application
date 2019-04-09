package application;

import entities.User;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.sql.Connection;

/**
 * The UIController for the Login screen
 * Allows a user to login, for admins to login, or for guests to enter
 * @author Jonathan Chang
 * @version iteration1
 */
public class UIControllerLM extends UIController {

    @FXML
    private JFXTabPane login_tabpane;

    @FXML
    private Tab guest_tab;

    @FXML
    private Tab user_tab;

    @FXML
    private Tab admin_tab;

    @FXML
    private JFXButton loginAsGuestButton;
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

    public UIControllerLM() {

    }

    /**
     * Called when the scene is first created
     */
    @FXML
    public void initialize() {

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
     * Goes to the Guest Main Menu
     */
    @FXML
    private void setLoginAsGuestButton() {
        Connection conn = DBController.dbConnect();
        CurrentUser.user = DBController.getGuestUser(conn);
        DBController.closeConnection(conn);
        this.goToScene(UIController.GUEST_MAIN_MENU_MAIN);
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
    private void goToUserTab()
    {
        login_tabpane.getSelectionModel().select(user_tab);
    }

    private User checkLogin(String username, String password, int permissions) {
        User user = null;
        Connection conn = DBController.dbConnect();
        user = DBController.loginCheck(username,password,conn,permissions);
        if(user == null) {
            return null;
        }
        DBController.closeConnection(conn);
        CurrentUser.user = user;
        return user;
    }
}
