import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public class UIControllerRVM extends UIController {

    @FXML
    private Map<String, String> nodeIDs;

    @FXML
    private JFXButton confirmButton;

    @FXML
    private ChoiceBox nodeSelect;

    @FXML
    private DatePicker datePicker;

    @FXML
    private JFXTimePicker startTimePicker;

    @FXML
    private JFXTimePicker endTimePicker;

    @FXML
    public void initalize() {
        nodeIDs = new HashMap<String, String>();
        //DB Get nodes
        nodeSelect.getItems();
    }

    @FXML
    private void setConfirmButton() {
        LocalDate ld = datePicker.getValue();
        Instant instant = Instant.from(ld.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Reservation r = new Reservation((String) nodeSelect.getValue(), CurrentUser.userID, format.format(date), startTimePicker.getValue() + ":00", endTimePicker.getValue() + ":00");
        // DB Send
    }
}
