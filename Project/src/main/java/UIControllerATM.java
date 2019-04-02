import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class UIControllerATM extends UIController {

    @FXML
    private Menu homeButton; /**< The Home Button*/

    @FXML
    private MenuItem backMenuItem; /**< The Home Button*/

    @FXML
    private JFXButton viewNodesButton; /**< The View Node Button*/

    @FXML
    private JFXButton viewEdgesButton; /**< The View Edges Button*/

    @FXML
    private JFXButton viewServiceRequestsButton; /**< The View Service Request Button */

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

    /**
     * Goes to the
     */
    @FXML
    private void setViewServiceRequestsButton() {
        this.goToScene(UIController.ADMIN_TOOLS_VIEW_SERVICE_REQUESTS);
    }

    /**
     * Goes back t0 the admin main Menu
     */
    @FXML
    private void setBackMenuItem() {
        this.goToScene(UIController.ADMIN_MAIN_MENU_MAIN);
    }
}
