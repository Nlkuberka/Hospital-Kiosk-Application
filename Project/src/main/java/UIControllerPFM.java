import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class UIControllerPFM extends UIController {

    @FXML
    public ChoiceBox initialLocationSelect;

    @FXML
    public void initialize() {
        initialLocationSelect.getItems().addAll("choice1", "choice2");
    }

    @FXML
    public void initLocChanged(ActionEvent actionEvent) {
        System.out.println(initialLocationSelect.getValue());
    }
}
