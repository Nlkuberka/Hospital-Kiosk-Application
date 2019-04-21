package application;

import com.jfoenix.controls.JFXButton;

import database.DBController;
import database.DBControllerU;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import java.sql.Connection;

/**
 * The UIController for the application menus
 * Allows a user to select which section of the application they want to move to
 * @author Jonathan Chang
 * @version iteration1
 */
public class UIControllerMMM extends  UIController {

    @FXML
    private ImageView backgroundImage;

    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());
    }

    /**
     * Goes to the Pathfinding scene
     */
    @FXML
    private void setPathfindingButton() {
        Connection conn = DBController.dbConnect();
        CurrentUser.user = DBControllerU.getGuestUser(conn);
        DBController.closeConnection(conn);
        this.goToScene(UIController.PATHFINDING_MAIN);
    }

    /**
     * Goes to the Reservations scene
     */
    @FXML
    private void setReservationsButton() {
        this.goToScene(UIController.RESERVATIONS_MAIN_MENU);
    }

    /**
     * Goes to the Admin Tools scene
     */
    @FXML
    private void setAdminToolsButton() {
        this.goToScene(UIController.ADMIN_TOOLS_MAIN);
    }

    /**
     * Goest back to the login scene
     */
    @FXML
    private void setBackMenuItem() {
        this.goToScene(UIController.LOGIN_MAIN);
    }
}
