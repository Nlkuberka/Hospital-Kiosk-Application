package reservations;

import application.CurrentUser;
import com.calendarfx.view.DayView;
import com.jfoenix.controls.JFXToggleButton;
import database.DBController;
import application.UIController;
import database.DBControllerRW;
import entities.Reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;

import entities.User;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import utilities.Tooltip;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.*;

import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The UIController that handles the creation and sending of reservations
 * @author Jonathan Chang, imoralessirgo, Ryan O'Brien
 * @version iteration1
 */
public class UIControllerRVM extends UIController {

    private Map<String, String> workplaceIDs;
    private ArrayList<Shape> rooms = new ArrayList<>();
    private ArrayList<Shape> workZones= new ArrayList<>();
    private Boolean colorShift = true;
    private Boolean roomBooking = true; //When true, user is booking rooms. When false, user will be booking work zones
    private ArrayList<Shape> fixshapes = new ArrayList<>();
    private boolean isCBlind = false;

    /**
     * < Holds the reference of the short names to nodeIDs
     */

    @FXML
    private JFXButton confirmButton;
    /**
     * < The confirm button
     */

    @FXML
    private ChoiceBox<String> workplaceSelect;
    /**
     * < The choicebox where the user selects the node
     */

    @FXML
    private DatePicker datePicker;
    /**
     * < The picker for the date
     */

    @FXML
    private JFXTimePicker startTimePicker;
    /**
     * < The picker for the start time
     */

    @FXML
    private JFXTimePicker endTimePicker;
    /**
     * < The picker for the end time
     */

    /**
     * < Shape objects for reservable workplaces
     */
    @FXML private Shape classroom1; @FXML private Shape classroom2; @FXML private Shape classroom3;
    @FXML private Shape classroom4; @FXML private Shape classroom5; @FXML private Shape classroom6;
    @FXML private Shape classroom7; @FXML private Shape classroom8; @FXML private Shape classroom9;
    @FXML private Shape MHA; @FXML private Shape MHCR; @FXML private Shape pantry;

    /**
     *  < Shape objects for flexible work stations
     */
    @FXML private Shape workzone4_t1; @FXML private Shape workzone4_t2;

    @FXML private Shape workzone3_t1; @FXML private Shape workzone3_t2; @FXML private Shape workzone3_t3;
    @FXML private Shape workzone3_d4; @FXML private Shape workzone3_r1; @FXML private Shape workzone3_r2;
    @FXML private Shape workzone3_r4; @FXML private Shape workzone3_r3; @FXML private Shape workzone3_r5;
    @FXML private Shape workzone3_r6; @FXML private Shape workzone3_d3; @FXML private Shape workzone3_d2;
    @FXML private Shape workzone3_d1; @FXML private Shape workzone3_d5; @FXML private Shape workzone3_d6;
    @FXML private Shape workzone3_d7; @FXML private Shape workzone3_d8; @FXML private Shape workzone3_d9;
    @FXML private Shape workzone3_d10; @FXML private Shape workzone3_d11; @FXML private Shape workzone3_d12;
    @FXML private Shape workzone3_d13; @FXML private Shape workzone3_d14; @FXML private Shape workzone3_d15;
    @FXML private Shape workzone3_d16;

    @FXML private Shape workzone1_d17; @FXML private Shape workzone1_d18; @FXML private Shape workzone1_d19;
    @FXML private Shape workzone1_d11; @FXML private Shape workzone1_d12; @FXML private Shape workzone1_d13;
    @FXML private Shape workzone1_d20; @FXML private Shape workzone1_d21; @FXML private Shape workzone1_d22;
    @FXML private Shape workzone1_d14; @FXML private Shape workzone1_d15; @FXML private Shape workzone1_d16;
    @FXML private Shape workzone1_d1; @FXML private Shape workzone1_d3; @FXML private Shape workzone1_d5;
    @FXML private Shape workzone1_d7; @FXML private Shape workzone1_d9; @FXML private Shape workzone1_d10;
    @FXML private Shape workzone1_d8; @FXML private Shape workzone1_d6; @FXML private Shape workzone1_d4;
    @FXML private Shape workzone1_d2; @FXML private Shape workzone1_r3; @FXML private Shape workzone1_r2;
    @FXML private Shape workzone1_r1;@FXML private Shape workzone1_r4;

    @FXML private Shape workzone2_d9; @FXML private Shape workzone2_d7; @FXML private Shape workzone2_d5;
    @FXML private Shape workzone2_d3; @FXML private Shape workzone2_d1; @FXML private Shape workzone2_d8;
    @FXML private Shape workzone2_d6; @FXML private Shape workzone2_d4; @FXML private Shape workzone2_d2;

    @FXML private Shape workzone5_d13; @FXML private Shape workzone5_d9; @FXML private Shape workzone5_d5;
    @FXML private Shape workzone5_d1; @FXML private Shape workzone5_r1; @FXML private Shape workzone5_t1;
    @FXML private Shape workzone5_r5; @FXML private Shape workzone5_r4; @FXML private Shape workzone5_r3;
    @FXML private Shape workzone5_r2; @FXML private Shape workzone5_d14; @FXML private Shape workzone5_d10;
    @FXML private Shape workzone5_d6; @FXML private Shape workzone5_d2; @FXML private Shape workzone5_d15;
    @FXML private Shape workzone5_d11; @FXML private Shape workzone5_d7; @FXML private Shape workzone5_d16;
    @FXML private Shape workzone5_d12; @FXML private Shape workzone5_d4; @FXML private Shape workzone5_d8;
    @FXML private Shape workzone5_d3; @FXML private Shape workzone5_t2; @FXML private Shape workzone5_t3;
  
    @FXML
    private List<String> wkIDs = new LinkedList<String>();
    private List<String> zIDs = new LinkedList<String>();


    @FXML
    private MenuItem backButton;
    /**
     * < The Back Button
     */

    @FXML
    private Menu homeButton;
    /**
     * < The Home Button
     */

    @FXML
    private JFXToggleButton colorBlindnessButton;


    /**
     * Run when the scene is first loaded
     */
    @FXML
    public void initialize() {

        rooms.add(classroom1); rooms.add(classroom2); rooms.add(classroom3); rooms.add(classroom4); rooms.add(classroom5);
        rooms.add(classroom6); rooms.add(classroom7); rooms.add(classroom8); rooms.add(classroom9);
        rooms.add(MHA); rooms.add(MHCR); rooms.add(pantry);

        workZones.add(workzone1_r1);  workZones.add(workzone1_r2);  workZones.add(workzone1_r3);
        workZones.add(workzone1_r4);
        workZones.add(workzone1_d1);  workZones.add(workzone1_d2);  workZones.add(workzone1_d3);
        workZones.add(workzone1_d4);  workZones.add(workzone1_d5);  workZones.add(workzone1_d6);
        workZones.add(workzone1_d7);  workZones.add(workzone1_d8);  workZones.add(workzone1_d9);
        workZones.add(workzone1_d10); workZones.add(workzone1_d11); workZones.add(workzone1_d12);
        workZones.add(workzone1_d13); workZones.add(workzone1_d14); workZones.add(workzone1_d15);
        workZones.add(workzone1_d16); workZones.add(workzone1_d17); workZones.add(workzone1_d18);
        workZones.add(workzone1_d19); workZones.add(workzone1_d20); workZones.add(workzone1_d21);
        workZones.add(workzone1_d22);

        workZones.add(workzone2_d1);  workZones.add(workzone2_d2);  workZones.add(workzone2_d3);
        workZones.add(workzone2_d4);  workZones.add(workzone2_d5);  workZones.add(workzone2_d6);
        workZones.add(workzone2_d7);  workZones.add(workzone2_d8);  workZones.add(workzone2_d9);

        workZones.add(workzone3_t1);  workZones.add(workzone3_t2);  workZones.add(workzone3_t3);
        workZones.add(workzone3_r1);  workZones.add(workzone3_r2);  workZones.add(workzone3_r3);
        workZones.add(workzone3_r4);  workZones.add(workzone3_r5);  workZones.add(workzone3_r6);
        workZones.add(workzone3_d1);  workZones.add(workzone3_d2);  workZones.add(workzone3_d3);
        workZones.add(workzone3_d4);  workZones.add(workzone3_d5);  workZones.add(workzone3_d6);
        workZones.add(workzone3_d7);  workZones.add(workzone3_d8);  workZones.add(workzone3_d9);
        workZones.add(workzone3_d10); workZones.add(workzone3_d11); workZones.add(workzone3_d12);
        workZones.add(workzone3_d13); workZones.add(workzone3_d14); workZones.add(workzone3_d15);
        workZones.add(workzone3_d16);

        workZones.add(workzone4_t1);  workZones.add(workzone4_t2);

        workZones.add(workzone5_t1);  workZones.add(workzone5_t2);  workZones.add(workzone5_t3);
        workZones.add(workzone5_r1);  workZones.add(workzone5_r2);  workZones.add(workzone5_r3);
        workZones.add(workzone5_r4);  workZones.add(workzone5_r5);
        workZones.add(workzone5_d1);  workZones.add(workzone5_d2);  workZones.add(workzone5_d3);
        workZones.add(workzone5_d4);  workZones.add(workzone5_d5);  workZones.add(workzone5_d6);
        workZones.add(workzone5_d7);  workZones.add(workzone5_d8);  workZones.add(workzone5_d9);
        workZones.add(workzone5_d10); workZones.add(workzone5_d11); workZones.add(workzone5_d12);
        workZones.add(workzone5_d13); workZones.add(workzone5_d14); workZones.add(workzone5_d15);
        workZones.add(workzone5_d16);

//        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        //Classroom Tooltips
        int num;
        for (int i = 0; i < 9; i++) {
            num = i+1;
            new Tooltip(rooms.get(i), "Classroom " +num);
        }
        new Tooltip(rooms.get(9), "MHA");
        new Tooltip(rooms.get(10), "MHCR");
        new Tooltip(rooms.get(11), "Pantry");

        //Work Zone 1 Tooltips
        for (int i = 0; i < 4; i++) {
            num = i+1;
            new Tooltip(workZones.get(i), "Work Zone 1 Room " + num);
        }
        num = 0;
        for (int i = 4; i < 26; i++) {
            num++;
            new Tooltip(workZones.get(i), "Work Zone 1 Desk " + num);
        }

        //Work Zone 2 Tooltips
        num = 0;
        for (int i = 26; i < 35; i++) {
            num++;
            new Tooltip(workZones.get(i), "Work Zone 2 Desk " + num);
        }


        //Work Zone 3 Tooltips
        num = 0;
        for (int i = 35; i < 38; i++) {
            num ++;
            new Tooltip(workZones.get(i), "Work Zone 3 Table " + num);
        }
        num = 0;
        for (int i = 38; i < 44; i++) {
            num++;
            new Tooltip(workZones.get(i), "Work Zone 3 Room " + num);
        }
        num = 0;
        for (int i = 44; i < 60; i++) {
            num++;
            new Tooltip(workZones.get(i), "Work Zone 3 Desk " + num);
        }

        //Work Zone 4 Tooltips
        num = 0;
        for (int i = 60; i < 62; i++) {
            num ++;
            new Tooltip(workZones.get(i), "Work Zone 4 Table " + num);
        }

        //Work Zone 5 Tooltips
        num = 0;
        for (int i = 62; i < 65; i++) {
            num ++;
            new Tooltip(workZones.get(i), "Work Zone 5 Table " + num);
        }
        num = 0;
        for (int i = 65; i < 70; i++) {
            num++;
            new Tooltip(workZones.get(i), "Work Zone 5 Room " + num);
        }
        num = 0;
        for (int i = 70; i < 86; i++) {
            num++;
            new Tooltip(workZones.get(i), "Work Zone 5 Desk " + num);
        }
    }

    /**
     * Loads in the rooms each time the scene is show
     */
    @Override
    public void onShow() {

        colorAllGreen();

        workplaceIDs = new HashMap<String, String>();
        List<String> workplaces = new LinkedList<String>();
        //DB Get nodes
        try {
            Connection conn = DBController.dbConnect();
            ResultSet rs = conn.createStatement().executeQuery("Select * From WORKPLACES WHERE OUTLINE = '1'");
            while (rs.next()) {
                workplaceIDs.put(rs.getString("ROOMNAME"), rs.getString("WKPLACEID"));
                workplaces.add(rs.getString("ROOMNAME"));
                wkIDs.add(rs.getString("WKPLACEID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DBController.dbConnect();
            ResultSet ms = conn.createStatement().executeQuery("Select * From WORKPLACES WHERE OUTLINE = '0'");
            while (ms.next()) {
                zIDs.add(ms.getString("WKPLACEID"));
            }
        } catch (SQLException e) {
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

        boolean isColorBlind = this.isCBlind;
        Connection connection = DBController.dbConnect();

        /*if(colorShift) {
            colorShapeGreen(workzone3_r3); colorShapeGreen(workzone5_d4); colorShapeGreen(workzone1_d5);
            colorShapeRed(workzone2_d2); colorShapeRed(workzone3_d2); colorShapeRed(workzone5_d7);
        } else {
            colorShapeRed(workzone3_r3); colorShapeRed(workzone5_d4); colorShapeRed(workzone1_d5);
            colorShapeGreen(workzone2_d2); colorShapeGreen(workzone3_d2); colorShapeGreen(workzone5_d7);
        }
        colorShift = !colorShift;*/

        if (!checkValidReservation(datePicker, startTimePicker, endTimePicker, roomBooking)) {
            return;
        }


        if (isColorBlind) {
            fixColorAllBlue();
        } else {
           fixColorAllGreen();
        }


        if (checkValidReservation(datePicker, startTimePicker, endTimePicker, roomBooking)) {
            for (int i = 0; i < rooms.size(); i++) {
//                        if (workplaceIDs.get(workplaceSelect.getValue()).equals(roomToShape.get(shapes.get(i)))) {
                if (!DBControllerRW.isRoomAvailableString(wkIDs.get(i), getDateString(datePicker),
                        getTimeString(startTimePicker), getTimeString(endTimePicker), connection)) {
                    System.out.println(workplaceSelect.getItems().get(i) + "is reserved at this time");
                    if (isColorBlind) {
                        colorShapeYellow(rooms.get(i));
                    } else {
                        colorShapeRed(rooms.get(i));
                    }

                } else {
                    if (isColorBlind) {
                        colorShapeBlue(rooms.get(i));
                    } else {
                        colorShapeGreen(rooms.get(i));                    }

                }
            }
        }
        for (int i = 0; i < workZones.size(); i++) {
            if (!DBControllerRW.isRoomAvailableString(zIDs.get(i), getDateString(datePicker),
                    getTimeString(startTimePicker), getTimeString(endTimePicker), connection)) {
                System.out.println(workplaceSelect.getItems().get(i) + " is reserved at this time");
               if(isColorBlind) {
                   colorShapeYellow(workZones.get(i));
               } else {
                   colorShapeRed(workZones.get(i));
               }

            } else {
                if (isColorBlind) {
                    colorShapeBlue(workZones.get(i));
                } else {
                    colorShapeGreen(workZones.get(i));
                }
            }
        }
        DBController.closeConnection(connection);
    }

    /**
     * Creates a reservation and sends it to the database
     */
    @FXML
    private void setConfirmButton() {

        if(roomBooking == false) {
            if(startTimePicker.getValue().compareTo(LocalTime.now().plusMinutes(15)) >= 0) {
                System.out.println(startTimePicker.getValue().compareTo(LocalTime.now()));
                popupMessage("Work Zone reservations can only be made up to 15 minutes in advance", true);
                return;
            }
        }else {
            if (!checkValidReservation(datePicker, startTimePicker, endTimePicker, roomBooking)) {
                popupMessage("Invalid Reservation", true);
                return;
            }
        }

        String dateString = getDateString(datePicker);

        // If endTime before startTime return

        LocalTime endTime = endTimePicker.getValue();

        String startString = getTimeString(startTimePicker);
        String endString = getTimeString(endTimePicker);

        Connection conn = DBController.dbConnect();
        List<Reservation> reservations = DBControllerRW.getResForRoom(workplaceIDs.get((String) workplaceSelect.getValue()), dateString, conn);
        System.out.println(reservations);
        Reservation r = new Reservation(workplaceIDs.get(workplaceSelect.getValue()),
                CurrentUser.user.getUserID(), dateString, startString, endString);
        r.setRsvID(r.getTimeStamp());
//        if (!r.isValid(reservations)) {
        if(!DBControllerRW.isRoomAvailableString(r.getWkplaceID(), r.getDate(), r.getStartTime(), r.getEndTime(), conn)) {
            popupMessage("This reservation conflicts with another.", true);
            return;
        }

        DBControllerRW.addReservation(r,conn);
        DBController.closeConnection(conn);
        popupMessage("Reservation Confirmed.", false);
    }

    /**
     * Checks if the reservation input is valid
     * Must be at least one day in advance
     * Must have start time before end time
     *
     * @return Whether the reservation input is valid
     */
    public static boolean checkValidReservation(DatePicker picker, JFXTimePicker startPicker, JFXTimePicker endPicker, Boolean roomBooking) {
        LocalDate ld = picker.getValue();
        Instant instant = Instant.from(ld.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);

        // If date selected is before today
        if(roomBooking) {
            Date today = new Date();
            if (date.compareTo(today) <= 0) {
                return false;
            }
        }

        LocalTime startTime = startPicker.getValue();
        LocalTime endTime = endPicker.getValue();
        if (endTime.compareTo(startTime) <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Gets the date string from the datePicker
     *
     * @return The string from the datePicker
     */
    public static String getDateString(DatePicker picker) {
        LocalDate ld = picker.getValue();
        Instant instant = Instant.from(ld.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(date);
    }

    /**
     * Gets the time string from the given JFXTimePicker
     *
     * @param timePicker The JFXTimePicker to get the timeString from
     * @return The timeString
     */
    public static String getTimeString(JFXTimePicker timePicker) {
        LocalTime localTime = timePicker.getValue();
        String timeString = localTime.toString();
        if (timeString.length() > 8) {
            timeString = timeString.substring(0, 8);
        } else if (timeString.length() == 5) {
            timeString += ":00";
        }
        return timeString;
    }

    private void colorAllGreen() {
        for (int x = 0; x < rooms.size(); x++) {
            rooms.get(x).setFill(javafx.scene.paint.Color.GREEN);
        }
        for (int x = 0; x < workZones.size(); x++) {
            workZones.get(x).setFill(javafx.scene.paint.Color.GREEN);
        }
    }

    private void fixColorAllGreen() {
        for (int x = 0; x < workZones.size(); x++) {
            workZones.get(x).setFill(javafx.scene.paint.Color.GREEN);
        }
    }

    private void fixColorAllBlue() {
        for (int x = 0; x < workZones.size(); x++) {
            workZones.get(x).setFill(javafx.scene.paint.Color.BLUE);
        }
    }

    private void colorShapeRed(Shape shape) {
        shape.setFill(javafx.scene.paint.Color.RED);
    }

    private void colorShapeGreen(Shape shape) {
        shape.setFill(javafx.scene.paint.Color.GREEN);
    }

    private void colorShapeYellow(Shape shape) {
        shape.setFill(javafx.scene.paint.Color.YELLOW);
    }

    private void colorShapeBlue(Shape shape) {
        shape.setFill(javafx.scene.paint.Color.BLUE);
    }

    /**
     * Color blindness color setting
     */

    /**
     * Color blindness color setting
     */

    @FXML
    private void setColorBlindnessButton() {
        if (colorBlindnessButton.isSelected()) {
            isCBlind = true;
            updateColorView();
        } else
            isCBlind = false;
            updateColorView();
    }

    /**
     * Goes back to the admin tools application menu
     */
    @FXML
    private void setBackButton() {
        UIController controller = CurrentUser.user.getPermissions() == User.ADMIN_PERMISSIONS ? this.goToScene(UIController.ADMIN_RESERVATION_MAIN) : this.goToScene(UIController.RESERVATIONS_MAIN_MENU);
    }

    @FXML
    private void toggleMode() {

        roomBooking = !roomBooking;

        if(roomBooking == true) {
            workplaceIDs = new HashMap<String, String>();
            List<String> workplaces = new LinkedList<String>();
            //DB Get nodes
            try {
                Connection conn = DBController.dbConnect();
                ResultSet rs = conn.createStatement().executeQuery("Select * From WORKPLACES WHERE OUTLINE = '1'");
                while (rs.next()) {
                    workplaceIDs.put(rs.getString("ROOMNAME"), rs.getString("WKPLACEID"));
                    workplaces.add(rs.getString("ROOMNAME"));
//                    IDs.add(rs.getString("WKPLACEID"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            workplaceSelect.setItems(FXCollections.observableList(workplaces));

            // Set initial Startup values
            datePicker.setValue(LocalDate.now());
            startTimePicker.setValue(LocalTime.now());
            endTimePicker.setValue(LocalTime.now());
            workplaceSelect.getSelectionModel().selectFirst();
        }
        else {
            workplaceIDs = new HashMap<String, String>();
            List<String> workplaces = new LinkedList<String>();
            //DB Get nodes
            try {
                Connection conn = DBController.dbConnect();
                ResultSet rs = conn.createStatement().executeQuery("Select * From WORKPLACES WHERE OUTLINE = '0'");
                while (rs.next()) {
                    workplaceIDs.put(rs.getString("ROOMNAME"), rs.getString("WKPLACEID"));
                    workplaces.add(rs.getString("ROOMNAME"));
//                    IDs.add(rs.getString("WKPLACEID"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            workplaceSelect.setItems(FXCollections.observableList(workplaces));

            // Set initial Startup values
            datePicker.setValue(LocalDate.now());
            startTimePicker.setValue(LocalTime.now());
            endTimePicker.setValue(LocalTime.now());
            workplaceSelect.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void shapeSelect() {

    }
}