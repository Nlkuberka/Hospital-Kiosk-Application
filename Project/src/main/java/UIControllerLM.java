import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class UIControllerLM extends UIController {

    @FXML
    private JFXButton loginAsGuestButton; /**< The Login As Guest Button*/

    @FXML
    private JFXButton loginAsUserButton; /**< The Login As User Button*/

    @FXML
    private JFXButton loginAsAdminButton; /**< The Login As Admin Button*/

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
        CurrentUser.permissions = User.GUEST_PERMISSIONS;
        this.goToScene(UIController.GUEST_MAIN_MENU_MAIN);
    }

    /**
     * Goes to the User Main Menu
     */
    @FXML
    private void setLoginAsUserButton() {
        CurrentUser.permissions = User.BASIC_PERMISSIONS;
        this.goToScene(UIController.USER_MAIN_MENU_MAIN);
    }

    /**
     * Goes to the Admin Main Menu
     */
    @FXML
    private void setLoginAsAdminButton() {
        CurrentUser.permissions = User.ADMIN_PERMISSIONS;
        this.goToScene(UIController.ADMIN_MAIN_MENU_MAIN);
    }
}
