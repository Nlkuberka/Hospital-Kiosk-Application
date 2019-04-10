package reservations;

import application.CurrentUser;
import application.DBController;
import application.UIController;
import entities.Node;
import entities.Reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;

import entities.Workplace;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.shape.Shape;
import sun.util.resources.cldr.shi.LocaleNames_shi_Tfng;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.*;

import java.util.*;
import java.util.List;

/**
 * The UIController that handles the creation and sending of reservations
 * @author Jonathan Chang, imoralessirgo, Ryan O'Brien
 * @version iteration1
 */
public class UIControllerRVM extends UIController {

    private Map<String, String> workplaceIDs; /** < Holds the reference of the short names to nodeIDs*/

    @FXML private JFXButton confirmButton; /** < The confirm button*/

    @FXML private ChoiceBox<String> workplaceSelect; /** < The choicebox where the user selects the node*/

    @FXML private DatePicker datePicker; /** < The picker for the date*/

    @FXML private JFXTimePicker startTimePicker; /** < The picker for the start time*/

    @FXML private JFXTimePicker endTimePicker; /** < The picker for the end time */

    @FXML private Shape classroom1; @FXML private Shape classroom2; @FXML private Shape classroom3;
    @FXML private Shape classroom4; @FXML private Shape classroom5; @FXML private Shape classroom6;
    @FXML private Shape classroom7; @FXML private Shape classroom8; @FXML private Shape classroom9;
    @FXML private Shape pantry; @FXML private Shape MHA; @FXML private Shape MHCR;

    private Shape[] shapes = {classroom1, classroom2, classroom3, classroom4, classroom5, classroom6, classroom7,
                                classroom8, classroom9, pantry, MHA, MHCR};
    /**
     * Run when the scene is first loaded
     */
    @FXML
    public void initialize() {

        classroom1.setFill(javafx.scene.paint.Color.GREEN);
        classroom2.setFill(javafx.scene.paint.Color.GREEN);
        classroom3.setFill(javafx.scene.paint.Color.GREEN);
        classroom4.setFill(javafx.scene.paint.Color.GREEN);
        classroom5.setFill(javafx.scene.paint.Color.GREEN);
        classroom6.setFill(javafx.scene.paint.Color.GREEN);
        classroom7.setFill(javafx.scene.paint.Color.GREEN);
        classroom8.setFill(javafx.scene.paint.Color.GREEN);
        classroom9.setFill(javafx.scene.paint.Color.GREEN);
        pantry.setFill(javafx.scene.paint.Color.GREEN);
        MHA.setFill(javafx.scene.paint.Color.GREEN);
        MHCR.setFill(javafx.scene.paint.Color.GREEN);

    }

    /**
     * Loads in the rooms each time the scene is show
     */
    @Override
    public void onShow() {
        workplaceIDs = new HashMap<String, String>();
        List<String> workplaces = new LinkedList<String>();
        //DB Get nodes
        try {
            Connection conn = DBController.dbConnect();
            ResultSet rs = conn.createStatement().executeQuery("Select * From WORKPLACES");
            while(rs.next()){
                workplaceIDs.put(rs.getString("ROOMNAME"),rs.getString("WKPLACEID"));
                workplaces.add(rs.getString("ROOMNAME"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        workplaceSelect.setItems(FXCollections.observableList(workplaces));

        // Set initial Startup values
        datePicker.setValue(LocalDate.now());
        startTimePicker.setValue(LocalTime.now());
        endTimePicker.setValue(LocalTime.now());
        workplaceSelect.getSelectionModel().selectFirst();
    }

    /**
     * Updates the colorView based on the given date and times
     */

//    Time startTime = Time.valueOf(reservation.getStartTime());
//    Time endTime = Time.valueOf(reservation.getEndTime());
//    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(reservation.getDate());

    @FXML
    private void updateColorView() {
        Connection connection = DBController.dbConnect();
        if(!checkValidReservation()) {
            return;
        }
        for(int i = 0; i < workplaceSelect.getItems().size(); i++) {
            if(!DBController.isRoomAvailableString(workplaceSelect.getItems().get(i), getDateString(),
                    getTimeString(startTimePicker), getTimeString(endTimePicker), connection)) {

                shapes[i].setFill(javafx.scene.paint.Color.RED);
                classroom6.setFill(javafx.scene.paint.Color.RED);


            }


//            String nodeID = workplaceIDs.get(workplaceSelect.getItems().get(i));
//            List<Reservation> reservations = new LinkedList<Reservation>();
//            Reservation reservation = new Reservation("TEMP", "TEMP", getDateString(), getTimeString(startTimePicker), getTimeString(endTimePicker));
//            boolean isValid = reservation.isValid(reservations);
//            // Draw NodeShape based on isValid
        }
    }

    /**
     * Creates a reservation and sends it to the database
     */
    @FXML
    private void setConfirmButton() {
        if(!checkValidReservation()) {
            popupMessage("Invalid Reservation", true);
            return;
        }

        String dateString = getDateString();

        // If endTime before startTime return

        LocalTime endTime = endTimePicker.getValue();

        String startString = getTimeString(startTimePicker);
        String endString = getTimeString(endTimePicker);

        Connection conn = DBController.dbConnect();
        List<Reservation> reservations = DBController.getResForRoom(workplaceIDs.get((String) workplaceSelect.getValue()), dateString, conn);
        System.out.println(reservations);
        Reservation r = new Reservation(workplaceIDs.get((String) workplaceSelect.getValue()),
                CurrentUser.user.getUserID(), dateString, startString, endString);

        if(!r.isValid(reservations)) {
            popupMessage("This reservation conflicts with another.", true);
            return;
        }

        DBController.addReservation(r,conn);
        DBController.closeConnection(conn);
        popupMessage("Reservation Confirmed.", false);
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
