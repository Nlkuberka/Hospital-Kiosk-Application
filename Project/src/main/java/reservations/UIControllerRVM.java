package reservations;

import application.CurrentUser;
import application.DBController;
import application.UIController;
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

    @FXML
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
     * Creates a reservation and sends it to the database
     */
    @FXML
    private void setConfirmButton() {
        LocalDate ld = datePicker.getValue();
        Instant instant = Instant.from(ld.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);

        // If date selected is before today
        Date today = new Date();
        if(date.compareTo(today) <= 0) {
            return;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // If endTime before startTime return
        LocalTime startTime = startTimePicker.getValue();
        LocalTime endTime = endTimePicker.getValue();
        if(endTime.compareTo(startTime) <= 0) {
            return;
        }

        String startString = startTime.toString();
        String endString = endTime.toString();
        if(startString.length() > 8) {
            startString = startString.substring(0, 8);
        } else if(startString.length() == 5) {
            startString += ":00";
        }

        if(endString.length() > 8) {
            endString = endString.substring(0, 8);
        } else if(endString.length() == 5) {
            endString += ":00";
        }

        Reservation r = new Reservation(nodeIDs.get((String) nodeSelect.getValue()), CurrentUser.userID, format.format(date), startString, endString);
        // DB Send
        Connection conn = DBController.dbConnect();
        DBController.addReservation(r,conn);

        System.out.println(r);
    }
}
