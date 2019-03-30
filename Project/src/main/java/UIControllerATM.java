import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UIControllerATM extends UIController {

    @FXML
    private Button homeButton;

    @FXML
    private Button powerButton;

    public UIControllerATM() {

    }

    /**
     * Called when the scene is first created
     */
    @FXML
    public void initialize() {

    }

    @FXML
    private void setViewNodesButton() {
        this.goToScene(UIController.ADMIN_TOOLS_VIEW_NODES);
    }

    @FXML
    private void setViewEdgesButton() {
        this.goToScene(UIController.ADMIN_TOOLS_VIEW_EDGES);
    }
}
