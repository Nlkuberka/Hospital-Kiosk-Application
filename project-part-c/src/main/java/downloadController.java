import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class downloadController extends Controller{

    @FXML
    private Button downloadButton;

    @FXML
    private Button backButton;

    @FXML
    private void setDownloadButton() {

    }

    @FXML
    private void setBackButton() {
        goToView((Stage) backButton.getScene().getWindow());
    }
}
