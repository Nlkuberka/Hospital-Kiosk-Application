import javafx.fxml.FXML;

public class UIControllerLoginMain extends UIController {

    public UIControllerLoginMain() {

    }

    /**
     * Called when the scene is first created
     */
    @FXML
    public void initialize() {

    }

    @FXML
    private void setLoginAsGuestButton() {
        this.goToScene(UIController.MAIN_MENU_MAIN);
    }
}
