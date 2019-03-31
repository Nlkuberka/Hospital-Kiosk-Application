import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

public class UIControllerLM extends UIController {

    @FXML
    private JFXButton loginAsGuestButton; /**< The Login As Guest Button*/

    @FXML
    private JFXButton loginAsUserButton; /**< The Login As User Button*/

    @FXML
    private JFXButton loginAsAdminButton; /**< The Login As Admin Button*/

    public UIControllerLM() {

    }

    /**
     * Called when the scene is first created
     */
    @FXML
    public void initialize() {

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
        this.goToScene(UIController.BASIC_MAIN_MENU_MAIN);
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
