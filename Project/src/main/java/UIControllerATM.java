import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UIControllerATM extends UIController {

    @FXML
    private Button homeButton; /**< The Home Button*/

    public UIControllerATM() {

    }

    /**
     * Called when the scene is first created
     */
    @FXML
    public void initialize() {

    }

    /**
     * Goes to the View Node Scene
     */
    @FXML
    private void setViewNodesButton() {
        this.goToScene(UIController.ADMIN_TOOLS_VIEW_NODES);
    }

    /**
     * Goes to the View Edges Scene
     */
    @FXML
    private void setViewEdgesButton() {
        this.goToScene(UIController.ADMIN_TOOLS_VIEW_EDGES);
    }
}
