package application;

import entities.User;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

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
        CurrentUser.user = null; // DB get guest user
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
            this.popupWarning("Incorrect username or password.");
            return;
        }

        this.goToScene(UIController.USER_MAIN_MENU_MAIN);
    }

    /**
     * Goes to the Admin Main Menu
     */
    @FXML
    private void setLoginAsAdminButton() {
        String username = userUsernameTextField.getText();
        String password = userPasswordTextField.getText();

        User user = checkLogin(username, password, User.ADMIN_PERMISSIONS);
        if(user == null) {
            this.popupWarning("Incorrect username or password.");
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
        // DB check username and password
        if(user == null) {
            return null;
        }
        CurrentUser.user = user;
        return user;
    }
}
