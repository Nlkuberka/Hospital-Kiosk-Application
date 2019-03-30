import javafx.fxml.FXML;

import javafx.scene.control.Button;

public class UIControllerLM extends UIController {

    @FXML
    private Button loginAsGuestButton; /**< The Login As Guest Button*/

    @FXML
    private Button loginAsUserButton; /**< The Login As User Button*/

    @FXML
    private Button loginAsAdminButton; /**< The Login As Admin Button*/

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
        User.permissions = User.GUEST_PERMISSIONS;
        this.goToScene(UIController.GUEST_MAIN_MENU_MAIN);
    }

    /**
     * Goes to the User Main Menu
     */
    @FXML
    private void setLoginAsUserButton() {
        User.permissions = User.BASIC_PERMISSIONS;
        this.goToScene(UIController.BASIC_MAIN_MENU_MAIN);
    }

    /**
     * Goes to the Admin Main Menu
     */
    @FXML
    private void setLoginAsAdminButton() {
        User.permissions = User.ADMIN_PERMISSIONS;
        this.goToScene(UIController.ADMIN_MAIN_MENU_MAIN);
    }
}
