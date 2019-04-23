package admintools;

import application.CurrentUser;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import database.DBController;
import application.UIController;
import com.jfoenix.controls.JFXButton;
import database.DBControllerRW;
import entities.Reservation;
import entities.User;
import helper.ReservationTableHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import reservations.UIControllerRVM;
import reservations.UIControllerRVMM;

import java.awt.dnd.DnDConstants;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * The UIController for the viewing and editing, a user's reservations
 * Allows the user to see and manage reservations
 * @author Ryan O'Brien
 * @version iteration2
 */
public class UIControllerATER extends UIController {
    private static final int[] lengthRequirements = {-1, 10, 10, 10, 8, 8};
    private static final String[] reservationSetters  = {"", "setWkplaceID", "setUserID", "setDate", "setStartTime", "setEndTime"};
    private static final String[] reservationGetters  = {"getRsvID", "getWkplaceID", "getUserID", "getDate", "getStartTime", "getEndTime"};
    @FXML
    private ImageView backgroundImage;
    /**< The Various Reservation Columns used for cell factories */
    @FXML
    private MenuItem backButton; /**< The Back Button */

    @FXML
    private Menu homeButton; /**< The Home Button */

    @FXML
    private TableView<Reservation> reservationTable; /**< The table that holds all of the reservations */

    /**
     * Called when the scene is first created
     * Sets up all the cell factories
     */
    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        // Initialize the cell factories of the reservation field columns

        ObservableList<TableColumn<Reservation, ?>> tableColumns = reservationTable.getColumns();
        new ReservationTableHelper(reservationTable,
            (TableColumn<Reservation, Reservation>) tableColumns.get(0),
            (TableColumn<Reservation, Reservation>) tableColumns.get(1),
            (TableColumn<Reservation, Reservation>) tableColumns.get(2),
            (TableColumn<Reservation, Reservation>) tableColumns.get(3),
            (TableColumn<Reservation, Reservation>) tableColumns.get(4),
            (TableColumn<Reservation, Reservation>) tableColumns.get(5),
            (TableColumn<Reservation, Reservation>) tableColumns.get(6));

    }

    /**
     * Run when the scene is shown
     * Gets the nodes from the database and puts them into the table
     */
    @Override
    public void onShow() {
        //DB get Nodes
        Connection conn = DBController.dbConnect();
        ObservableList<Reservation> rsvData = FXCollections.observableArrayList();

        try {
            ResultSet rs = conn.createStatement().executeQuery("Select * from RESERVATIONS WHERE 1=1");
            while (rs.next()) {
                rsvData.add(new Reservation(rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        reservationTable.setItems(rsvData);
    }

    /**
     * Goes back to the admin tools application menu
     */
    @FXML
    private void setBackButton() {
        this.goToScene(UIController.ADMIN_TOOLS_MAIN);
    }


    @FXML
    private void setHomeButton() { this.goToScene(UIController.ADMIN_MAIN_MENU_MAIN); }

}
