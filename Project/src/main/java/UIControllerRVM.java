import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;

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
 * @author Jonathan Chang
 * @version iteration1
 */
public class UIControllerRVM extends UIController {

    @FXML
    private Map<String, String> nodeIDs; /** < Holds the reference of the short names to nodeIDs*/

    @FXML
    private JFXButton confirmButton; /** < The confirm button*/

    @FXML
    private ChoiceBox nodeSelect; /** < The choicebox where the user selects the node*/

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
        List<Node> nodes = new LinkedList<Node>();
        //DB Get nodes
        try {
            Connection conn = DBController.dbConnect();
            ResultSet rs = conn.createStatement().executeQuery("Select * From NODES where FLOOR = '2' AND BUILDING = 'Tower'");
            while(rs.next()){
                nodeIDs.put(rs.getString("SHORTNAME"),rs.getString("NODEID"));
                nodes.add(new Node(rs.getString(1),rs.getInt(2),rs.getInt(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),
                        rs.getString(7),rs.getString(8)));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

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

        Reservation r = new Reservation(nodeIDs.get((String) nodeSelect.getValue()), CurrentUser.userID, format.format(date), startTime.toString().substring(0, 8), endTime.toString().substring(0, 8));
        // DB Send
        System.out.println(r);
    }
}
