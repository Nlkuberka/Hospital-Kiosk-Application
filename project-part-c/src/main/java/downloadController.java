import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class downloadController {

    @FXML
    private Button downloadButton;

    @FXML
    private Button backButton;

    @FXML
    private void setDownloadButton() {

    }

    @FXML
    private void setBackButton() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("readIn.fxml"));
            Parent root = fxmlLoader.load();
            //downloadController controller = fxmlLoader.getController();

            stage.setTitle("Database Viewer");
            stage.setScene(new Scene(root, 1200, 800));
            stage.show();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
