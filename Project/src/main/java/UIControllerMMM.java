import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UIControllerMMM extends  UIController {

    @FXML
    private Button pathfindingButton; /**< The Pathfinding Button*/

    @FXML
    private Button reservationsButton; /**< The Reservations Button*/

    @FXML
    private Button reserveRequestButton; /**< The Reserve Request Button*/

    @FXML
    private Button adminToolsButton; /**< The Admin Tools Button*/

    @FXML
    public void initialize() {

    }

    /**
     * Goes to the Pathfinding scene
     */
    @FXML
    private void setPathfindingButton() {
        this.goToScene(UIController.PATHFINDING_MAIN);
    }

    /**
     * Goes to the Reservations scene
     */
    @FXML
    private void setReservationsButton() {
        this.goToScene(UIController.RESERVATIONS_MAIN);
    }

    /**
     * Goes to the Service Request scene
     */
    @FXML
    private void setReserveRequestButton() {
        this.goToScene(UIController.SERVICE_REQUEST_MAIN);
    }

    /**
     * Goes to the Admin Tools scene
     */
    @FXML
    private void setAdminToolsButton() {
        this.goToScene(UIController.ADMIN_TOOLS_MAIN);
    }
}