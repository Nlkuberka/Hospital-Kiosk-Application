import javafx.fxml.FXML;

public class UIControllerLM extends UIController {

    public UIControllerLM() {

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
