import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;

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

    @FXML
    private void goToUserTab()
    {
        login_tabpane.getSelectionModel().select(user_tab);
    }
}
