import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;

public class UIControllerATM extends UIController {

    @FXML
    private Menu homeButton; /**< The Home Button*/

    @FXML
    private JFXButton ViewNodesButton; /**< The View Node Button*/

    @FXML
    private JFXButton ViewEdgesEdges; /**< The View Edges Button*/

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
