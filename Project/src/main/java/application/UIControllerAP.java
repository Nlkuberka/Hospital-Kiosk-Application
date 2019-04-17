package application;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class UIControllerAP extends UIController {
    @FXML
    private Menu back;
    @FXML
    private MenuItem backMenuItem;

    public UIControllerAP() {

    }
    /**
     * Goes back to the path finding page
     */
    @FXML
    private void setBack() {
        this.goToScene(UIController.PATHFINDING_MAIN);
    }

}
