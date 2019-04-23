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

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

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
 *
 * @author Jonathan Chang, imoralessirgo, Ryan O'Brien
 * @version iteration1
 */
public class UIControllerRVM extends UIController {

    private Map<String, String> workplaceIDs;
    private ArrayList<Shape> shapes = new ArrayList<>();
    private ArrayList<Shape> workZone1 = new ArrayList<>();
    private ArrayList<Shape> workZone2 = new ArrayList<>();
    private ArrayList<Shape> workZone3 = new ArrayList<>();
    private ArrayList<Shape> workZone4 = new ArrayList<>();
    private ArrayList<Shape> workZone5 = new ArrayList<>();
    private Boolean colorShift = true;

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
    @FXML
    private Shape classroom1;
    @FXML
    private Shape classroom2;
    @FXML
    private Shape classroom3;
    @FXML
    private Shape classroom4;
    @FXML
    private Shape classroom5;
    @FXML
    private Shape classroom6;
    @FXML
    private Shape classroom7;
    @FXML
    private Shape classroom8;
    @FXML
    private Shape classroom9;
    @FXML
    private Shape MHA;
    @FXML
    private Shape MHCR;
    @FXML
    private Shape pantry;

    /**
     * < Shape objects for flexible work stations
     */
    @FXML
    private Shape workzone4_t1;
    @FXML
    private Shape workzone4_t2;

    @FXML
    private Shape workzone3_t1;
    @FXML
    private Shape workzone3_t2;
    @FXML
    private Shape workzone3_t3;
    @FXML
    private Shape workzone3_d4;
    @FXML
    private Shape workzone3_r1;
    @FXML
    private Shape workzone3_r2;
    @FXML
    private Shape workzone3_r4;
    @FXML
    private Shape workzone3_r3;
    @FXML
    private Shape workzone3_r5;
    @FXML
    private Shape workzone3_r6;
    @FXML
    private Shape workzone3_d3;
    @FXML
    private Shape workzone3_d2;
    @FXML
    private Shape workzone3_d1;
    @FXML
    private Shape workzone3_d5;
    @FXML
    private Shape workzone3_d6;
    @FXML
    private Shape workzone3_d7;
    @FXML
    private Shape workzone3_d8;
    @FXML
    private Shape workzone3_d9;
    @FXML
    private Shape workzone3_d10;
    @FXML
    private Shape workzone3_d11;
    @FXML
    private Shape workzone3_d12;
    @FXML
    private Shape workzone3_d13;
    @FXML
    private Shape workzone3_d14;
    @FXML
    private Shape workzone3_d15;
    @FXML
    private Shape workzone3_d16;

    @FXML
    private Shape workzone1_d17;
    @FXML
    private Shape workzone1_d18;
    @FXML
    private Shape workzone1_d19;
    @FXML
    private Shape workzone1_d11;
    @FXML
    private Shape workzone1_d12;
    @FXML
    private Shape workzone1_d13;
    @FXML
    private Shape workzone1_d20;
    @FXML
    private Shape workzone1_d21;
    @FXML
    private Shape workzone1_d22;
    @FXML
    private Shape workzone1_d14;
    @FXML
    private Shape workzone1_d15;
    @FXML
    private Shape workzone1_d16;
    @FXML
    private Shape workzone1_d1;
    @FXML
    private Shape workzone1_d3;
    @FXML
    private Shape workzone1_d5;
    @FXML
    private Shape workzone1_d7;
    @FXML
    private Shape workzone1_d9;
    @FXML
    private Shape workzone1_d10;
    @FXML
    private Shape workzone1_d8;
    @FXML
    private Shape workzone1_d6;
    @FXML
    private Shape workzone1_d4;
    @FXML
    private Shape workzone1_d2;
    @FXML
    private Shape workzone1_r3;
    @FXML
    private Shape workzone1_r2;
    @FXML
    private Shape workzone1_r1;
    @FXML
    private Shape workzone1_r4;

    @FXML
    private Shape workzone2_d9;
    @FXML
    private Shape workzone2_d7;
    @FXML
    private Shape workzone2_d5;
    @FXML
    private Shape workzone2_d3;
    @FXML
    private Shape workzone2_d1;
    @FXML
    private Shape workzone2_d8;
    @FXML
    private Shape workzone2_d6;
    @FXML
    private Shape workzone2_d4;
    @FXML
    private Shape workzone2_d2;

    @FXML
    private Shape workzone5_d13;
    @FXML
    private Shape workzone5_d9;
    @FXML
    private Shape workzone5_d5;
    @FXML
    private Shape workzone5_d1;
    @FXML
    private Shape workzone5_r1;
    @FXML
    private Shape workzone5_t1;
    @FXML
    private Shape workzone5_r5;
    @FXML
    private Shape workzone5_r4;
    @FXML
    private Shape workzone5_r3;
    @FXML
    private Shape workzone5_r2;
    @FXML
    private Shape workzone5_d14;
    @FXML
    private Shape workzone5_d10;
    @FXML
    private Shape workzone5_d6;
    @FXML
    private Shape workzone5_d2;
    @FXML
    private Shape workzone5_d15;
    @FXML
    private Shape workzone5_d11;
    @FXML
    private Shape workzone5_d7;
    @FXML
    private Shape workzone5_d16;
    @FXML
    private Shape workzone5_d12;
    @FXML
    private Shape workzone5_d4;
    @FXML
    private Shape workzone5_d8;
    @FXML
    private Shape workzone5_d3;
    @FXML
    private Shape workzone5_t2;
    @FXML
    private Shape workzone5_t3;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private List<String> IDs = new LinkedList<String>();

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

        shapes.add(classroom1);
        shapes.add(classroom2);
        shapes.add(classroom3);
        shapes.add(classroom4);
        shapes.add(classroom5);
        shapes.add(classroom6);
        shapes.add(classroom7);
        shapes.add(classroom8);
        shapes.add(classroom9);
        shapes.add(MHA);
        shapes.add(MHCR);
        shapes.add(pantry);


        // add in all fixed shapes

        fixshapes.add(workzone4_t1);
        //fixshapes.add(workzone4_t2);
        fixshapes.add(workzone3_t1);
        fixshapes.add(workzone3_t2);
        fixshapes.add(workzone3_t3);
        fixshapes.add(workzone3_d1);
        fixshapes.add(workzone3_d2);
        fixshapes.add(workzone3_d3);
        fixshapes.add(workzone3_d4);
        fixshapes.add(workzone3_d5);
        fixshapes.add(workzone3_d6);
        fixshapes.add(workzone3_d7);
        // fixshapes.add(workzone3_d8);
        fixshapes.add(workzone3_d9);
        fixshapes.add(workzone3_d10);
        fixshapes.add(workzone3_d11);
        fixshapes.add(workzone3_d12);
        fixshapes.add(workzone3_d13);
        fixshapes.add(workzone3_d14);
        fixshapes.add(workzone3_d15);
        fixshapes.add(workzone3_d16);
        fixshapes.add(workzone3_r1);
        fixshapes.add(workzone3_r2);
        // fixshapes.add(workzone3_r3);
        fixshapes.add(workzone3_r4);
        fixshapes.add(workzone3_r5);
        fixshapes.add(workzone3_r6);

        fixshapes.add(workzone2_d1);
        fixshapes.add(workzone2_d2);
        fixshapes.add(workzone2_d3);
        fixshapes.add(workzone2_d4);
        fixshapes.add(workzone2_d5);
        fixshapes.add(workzone2_d6);
        //  fixshapes.add(workzone2_d7);
        fixshapes.add(workzone2_d8);
        fixshapes.add(workzone2_d9);

        fixshapes.add(workzone5_d1);
        fixshapes.add(workzone5_d2);
        fixshapes.add(workzone5_d3);
        //  fixshapes.add(workzone5_d4);
        fixshapes.add(workzone5_d5);
        fixshapes.add(workzone5_d6);
        fixshapes.add(workzone5_d7);
        fixshapes.add(workzone5_d8);
        fixshapes.add(workzone5_d9);
        fixshapes.add(workzone5_d10);
        fixshapes.add(workzone5_d11);
        fixshapes.add(workzone5_d12);
        fixshapes.add(workzone5_d13);
        fixshapes.add(workzone5_d14);
        fixshapes.add(workzone5_d15);
        fixshapes.add(workzone5_d16);
        fixshapes.add(workzone5_r1);
        // fixshapes.add(workzone5_r2);
        fixshapes.add(workzone5_r3);
        fixshapes.add(workzone5_r4);
        fixshapes.add(workzone5_r5);
        fixshapes.add(workzone5_t1);
        // fixshapes.add(workzone5_t2);
        fixshapes.add(workzone5_t3);

        fixshapes.add(workzone1_d1);
        fixshapes.add(workzone1_d2);
        fixshapes.add(workzone1_d3);
        fixshapes.add(workzone1_d4);
        //fixshapes.add(workzone1_d5);
        fixshapes.add(workzone1_d6);
        fixshapes.add(workzone1_d7);
        fixshapes.add(workzone1_d8);
        fixshapes.add(workzone1_d9);
        fixshapes.add(workzone1_d10);
        fixshapes.add(workzone1_d11);
        fixshapes.add(workzone1_d12);
        fixshapes.add(workzone1_d13);
        fixshapes.add(workzone1_d14);
        fixshapes.add(workzone1_d15);
        //fixshapes.add(workzone1_d16);
        fixshapes.add(workzone1_d17);
        fixshapes.add(workzone1_d18);
        fixshapes.add(workzone1_d19);
        fixshapes.add(workzone1_d20);
        fixshapes.add(workzone1_d21);
        fixshapes.add(workzone1_d22);
        fixshapes.add(workzone1_r1);
        fixshapes.add(workzone1_r2);
        fixshapes.add(workzone1_r3);
        fixshapes.add(workzone1_r4);


        // backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        colorShapeRed(workzone1_d5);
        colorShapeRed(workzone1_d16);
        colorShapeRed(workzone3_d8);
        colorShapeRed(workzone3_r3);
        colorShapeRed(workzone2_d7);
        colorShapeRed(workzone4_t2);
        colorShapeRed(workzone5_r2);
        colorShapeRed(workzone5_d4);
        colorShapeRed(workzone5_t2);

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
                IDs.add(rs.getString("WKPLACEID"));
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

        if (!checkValidReservation(datePicker, startTimePicker, endTimePicker)) {
            return;
        }


        if (isColorBlind) {

            fixColorAllBlue();
            colorShapeYellow(workzone1_d5);
            colorShapeYellow(workzone1_d16);
            colorShapeYellow(workzone3_d8);
            colorShapeYellow(workzone3_r3);
            colorShapeYellow(workzone2_d7);
            colorShapeYellow(workzone4_t2);
            colorShapeYellow(workzone5_r2);
            colorShapeYellow(workzone5_d4);
            colorShapeYellow(workzone5_t2);



        } else {
            colorShapeRed(workzone1_d5);
            colorShapeRed(workzone1_d16);
            colorShapeRed(workzone3_d8);
            colorShapeRed(workzone3_r3);
            colorShapeRed(workzone2_d7);
            colorShapeRed(workzone4_t2);
            colorShapeRed(workzone5_r2);
            colorShapeRed(workzone5_d4);
            colorShapeRed(workzone5_t2);
           fixColorAllGreen();
        }


        if (checkValidReservation(datePicker, startTimePicker, endTimePicker)) {
            for (int i = 0; i < workplaceSelect.getItems().size(); i++) {
//                        if (workplaceIDs.get(workplaceSelect.getValue()).equals(roomToShape.get(shapes.get(i)))) {
                if (!DBControllerRW.isRoomAvailableString(IDs.get(i), getDateString(datePicker),
                        getTimeString(startTimePicker), getTimeString(endTimePicker), connection)) {
                    System.out.println(workplaceSelect.getItems().get(i) + "is reserved at this time");
//                                shapes.get(i).setFill(javafx.scene.paint.Color.RED);

                    if (isColorBlind) {
                        colorShapeYellow(shapes.get(i));
                    } else {
                        colorShapeRed(shapes.get(i));
                    }

                } else {
//                    classroom6.setFill(javafx.scene.paint.Color.RED);

                    if (isColorBlind) {
                        shapes.get(i).setFill(javafx.scene.paint.Color.BLUE);

                    } else {
                        shapes.get(i).setFill(javafx.scene.paint.Color.GREEN);
                    }


                }
//                        }
            }
        }
    }

    /**
     * Creates a reservation and sends it to the database
     */
    @FXML
    private void setConfirmButton() {
        if (!checkValidReservation(datePicker, startTimePicker, endTimePicker)) {
            popupMessage("Invalid Reservation", true);
            return;
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
        if (!DBControllerRW.isRoomAvailableString(r.getWkplaceID(), r.getDate(), r.getStartTime(), r.getEndTime(), conn)) {
            popupMessage("This reservation conflicts with another.", true);
            return;
        }

        DBControllerRW.addReservation(r, conn);
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
    public static boolean checkValidReservation(DatePicker picker, JFXTimePicker startPicker, JFXTimePicker endPicker) {
        LocalDate ld = picker.getValue();
        Instant instant = Instant.from(ld.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);

        // If date selected is before today
        Date today = new Date();
        if (date.compareTo(today) <= 0) {
            return false;
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
        for (int x = 0; x < shapes.size(); x++) {
            shapes.get(x).setFill(javafx.scene.paint.Color.GREEN);
        }
    }

    private void fixColorAllGreen() {
        for (int x = 0; x < fixshapes.size(); x++) {
            fixshapes.get(x).setFill(javafx.scene.paint.Color.GREEN);
        }
    }

    private void fixColorAllBlue() {
        for (int x = 0; x < fixshapes.size(); x++) {
            fixshapes.get(x).setFill(javafx.scene.paint.Color.BLUE);
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
    private void colorAllDefault() {
        if (colorBlindnessButton.isSelected() == true) {
            for (int x = 0; x < shapes.size(); x++) {
                shapes.get(x).setFill(javafx.scene.paint.Color.rgb(86, 180, 233));
            }
        } else {
            for (int x = 0; x < shapes.size(); x++) {
                shapes.get(x).setFill(javafx.scene.paint.Color.GREEN);
            }
        }
    }

    private void colorShapeNotAvailable(Shape shape) {
        if (colorBlindnessButton.isSelected() == true) {
            shape.setFill(javafx.scene.paint.Color.rgb(230, 159, 0));
        } else {
            shape.setFill(javafx.scene.paint.Color.RED);
        }

    }

    private void colorShapeAvailable(Shape shape) {
        if (colorBlindnessButton.isSelected() == true) {
            shape.setFill(javafx.scene.paint.Color.rgb(86, 180, 233));
        } else {
            shape.setFill(javafx.scene.paint.Color.GREEN);
        }
    }

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
        this.goToScene(UIController.RESERVATIONS_MAIN_MENU, new DayView());
    }
}