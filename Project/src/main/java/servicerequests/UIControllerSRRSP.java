package servicerequests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UIControllerSRRSP {
    public ScrollPane scrollPane_policy;
    public JFXTextArea textArea_policy;
    public JFXButton button_Decline;
    public JFXButton button_Accept;
    public AnchorPane parentPane;
    private boolean status;

    @FXML
    private void initialize()
    {
        button_Accept.setDisable(true);
        scrollPane_policy.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (Double.compare((Double) newValue, 1.0) == 0) {
                button_Accept.setDisable(false);
            }
        });
    }

    @FXML
    private void decline()
    {
        status = false;
        ((Stage) parentPane.getScene().getWindow()).close();
    }


    public void accept(MouseEvent mouseEvent) {
        status = true;
        ((Stage) parentPane.getScene().getWindow()).close();
    }

    boolean getStatus() {
        return status;
    }
}
