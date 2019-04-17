import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;


public class UIControllerOverall extends UIController{
    @FXML private TabPane tabPane;
    @FXML private AnchorPane createTabPane;
    @FXML private AnchorPane viewTabPane;
    @FXML private AnchorPane userTabPane;

    @FXML
    public void initialize() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/service_request_babysitting.fxml"));
            Parent root = fxmlLoader.load();
            AnchorPane subTabPane = (AnchorPane) fxmlLoader.load();
            createTabPane.getChildren().setAll(subTabPane);
        } catch(Exception e) {

        }

    }

    @Override
    public void onShow() {

    }

}
