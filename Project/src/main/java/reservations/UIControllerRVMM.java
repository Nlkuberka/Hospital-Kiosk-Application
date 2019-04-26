package reservations;

import application.CurrentUser;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

/**
 * The UIController for the Reservations Main Menu
 * Allows the user to select the various admin tools as well as go back to the Admin
 * Main Menu
 * @author Ryan O'Brien
 * @version iteration2
 */
public class UIControllerRVMM extends UIController {

    @FXML
    private Menu homeButton; /**< The Home Button*/

    @FXML
    private MenuItem backMenuItem; /**< The Home Button*/

    @FXML
    private JFXButton makeReservation; /**< The Make Reservation Button*/

    @FXML
    private JFXButton viewReservations; /**< The View/Edit Reservations Button*/

    @FXML
    private ImageView backgroundImage;



    public UIControllerRVMM() {

    }

    /**
     * Called when the scene is first created
     */
    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

    }


    /**
     * Goes to the View Node Scene
     */
    @FXML
    private void setMakeReservation() {
        this.goToScene(UIController.RESERVATIONS_MAIN);
    }

    /**
     * Goes to the View Edges Scene
     */
    @FXML
    private void setViewReservations() {
        this.goToScene(UIController.RESERVATIONS_EDIT);
    }


    /**
     * Goes back t0 the admin application Menu
     */
    @FXML
    private void setBackMenuItem() {
        int permission = CurrentUser.user.getPermissions();
        switch (permission){
            case 1:
                this.goToScene(UIController.LOGIN_MAIN);
                break;
            case 2:
                this.goToScene(UIController.PATHFINDING_MAIN);
                break;
            case 3:
                this.goToScene(UIController.ADMIN_MAIN_MENU_MAIN);
                break;
            default:
                this.goToScene(UIController.LOGIN_MAIN);
                break;
        }
    }

    @FXML
    private void setCalendarView() { this.goToScene(UIController.RESERVATIONS_CALENDAR_VIEW); }
}
