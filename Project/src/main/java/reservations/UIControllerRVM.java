package reservations;

import application.CurrentUser;
import application.DBController;
import application.UIController;
import entities.Node;
import entities.Reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.*;

import java.util.*;

/**
 * The UIController that handles the creation and sending of reservations
 * @author Jonathan Chang, imoralessirgo
 * @version iteration1
 */
public class UIControllerRVM extends UIController {

    private Map<String, String> nodeIDs; /** < Holds the reference of the short names to nodeIDs*/

    @FXML
    private JFXButton confirmButton; /** < The confirm button*/

    @FXML
    private ChoiceBox<String> nodeSelect; /** < The choicebox where the user selects the node*/

    @FXML
    private DatePicker datePicker; /** < The picker for the date*/

    @FXML
    private JFXTimePicker startTimePicker; /** < The picker for the start time*/

    @FXML
    private JFXTimePicker endTimePicker; /** < The picker for the end time */

    /**
     * Run when the scene is first loaded
     */
    @FXML
    public void initialize() {

    }

    /**
     * Loads in the rooms each time the scene is show
     */
    @Override
    public void onShow() {
        nodeIDs = new HashMap<String, String>();
        List<String> nodes = new LinkedList<String>();
        //DB Get nodes
        try {
            Connection conn = DBController.dbConnect();
            ResultSet rs = conn.createStatement().executeQuery("Select * From NODES where FLOOR = '2' AND BUILDING = 'Tower'");
            while(rs.next()){
                nodeIDs.put(rs.getString("SHORTNAME"),rs.getString("NODEID"));
                nodes.add(rs.getString("SHORTNAME"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        nodeSelect.setItems(FXCollections.observableList(nodes));

        // Set initial Startup values
        datePicker.setValue(LocalDate.now());
        startTimePicker.setValue(LocalTime.now());
        endTimePicker.setValue(LocalTime.now());
        nodeSelect.getSelectionModel().selectFirst();
    }

    /**
     * Updates the colorView based on the given date and times
     */
    @FXML
    private void updateColorView() {
        if(!checkValidReservation()) {
            return;
        }
        for(int i = 0; i < nodeSelect.getItems().size(); i++) {
            String nodeID = nodeIDs.get(nodeSelect.getItems().get(i));
            List<Reservation> reservations = new LinkedList<Reservation>();
            //DB Get Reservations of Node
            // Or use Rakesh's function
            Reservation reservation = new Reservation("TEMP", "TEMP", getDateString(), getTimeString(startTimePicker), getTimeString(endTimePicker));
            boolean isValid = reservation.isValid(reservations);
            // Draw NodeShape based on isValid
        }
    }

    /**
     * Creates a reservation and sends it to the database
     */
    @FXML
    private void setConfirmButton() {
        if(!checkValidReservation()) {
            return;
        }

        String dateString = getDateString();

        // If endTime before startTime return

        LocalTime endTime = endTimePicker.getValue();

        String startString = getTimeString(startTimePicker);
        String endString = getTimeString(endTimePicker);

        Reservation r = new Reservation(nodeIDs.get((String) nodeSelect.getValue()),
                CurrentUser.userID, dateString, startString, endString);
        // DB Send
        Connection conn = DBController.dbConnect();
        DBController.addReservation(r,conn);
    }

    /**
     * Checks if the reservation input is valid
     * Must be at least one day in advance
     * Must have start time before end time
     * @return Whether the reservation input is valid
     */
    private boolean checkValidReservation() {
        LocalDate ld = datePicker.getValue();
        Instant instant = Instant.from(ld.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);

        // If date selected is before today
        Date today = new Date();
        if(date.compareTo(today) <= 0) {
            return false;
        }

        LocalTime startTime = startTimePicker.getValue();
        LocalTime endTime = endTimePicker.getValue();
        if(endTime.compareTo(startTime) <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Gets the date string from the datePicker
     * @return The string from the datePicker
     */
    private String getDateString() {
        LocalDate ld = datePicker.getValue();
        Instant instant = Instant.from(ld.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(date);
    }

    /**
     * Gets the time string from the given JFXTimePicker
     * @param timePicker The JFXTimePicker to get the timeString from
     * @return The timeString
     */
    private String getTimeString(JFXTimePicker timePicker) {
        LocalTime localTime = timePicker.getValue();
        String timeString = localTime.toString();
        if(timeString.length() > 8) {
            timeString = timeString.substring(0, 8);
        } else if(timeString.length() == 5) {
            timeString += ":00";
        }
        return timeString;
    }
}
