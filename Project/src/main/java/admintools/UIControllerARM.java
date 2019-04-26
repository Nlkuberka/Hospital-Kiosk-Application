package admintools;

import application.CurrentUser;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

public class UIControllerARM extends UIController {

    @FXML
    private Menu homeButton; /**< The Home Button*/

    @FXML
    private MenuItem backMenuItem; /**< The Home Button*/

    @FXML
    private JFXButton makeReservation; /**< The Make Reservation Button*/

    @FXML
    private JFXButton viewReservations; /**< The View/Edit MY Reservations Button*/

    @FXML
    private JFXButton editReservation;

    @FXML
    private JFXButton calendarView;





    @FXML
    private ImageView backgroundImage;



    public UIControllerARM() {

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
        this.goToScene(UIController.ADMIN_TOOLS_MAIN);
    }

    @FXML
    private void setCalendarView() {
        this.goToScene(UIController.RESERVATIONS_CALENDAR_VIEW);
    }

    @FXML
    private void setEditReservation(){
        this.goToScene(UIController.ADMIN_TOOLS_EDIT_RESERVATIONS);
    }
}

