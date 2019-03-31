import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class UIControllerRVM extends UIController {

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

    }

    @FXML
    private void setConfirmButton() {
        LocalDate ld = datePicker.getValue();
        Instant instant = Instant.from(ld.atStartOfDay(ZoneId.systemDefault()));
        Date startDate = Date.from(instant);
        Date endDate = Date.from(instant);

        System.out.println(startTimePicker.getValue());
    }
}
