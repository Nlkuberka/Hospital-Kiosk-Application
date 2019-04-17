import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class UIControllerOverall extends UIController{
    @FXML private TabPane tabPane;
    @FXML private AnchorPane createTabPane;
    @FXML private AnchorPane viewTabPane;
    @FXML private AnchorPane userTabPane;

    @FXML
    public void initialize() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/service_request_babysitting.fxml"));
            StackPane subTabPane = (StackPane) fxmlLoader.load();
            createTabPane.getChildren().setAll(subTabPane);

            fxmlLoader = new FXMLLoader(getClass().getResource("/admin_tools_view_service_request.fxml"));
            subTabPane = (StackPane) fxmlLoader.load();
            viewTabPane.getChildren().setAll(subTabPane);

            fxmlLoader = new FXMLLoader(getClass().getResource("/admin_tools_view_service_request.fxml"));
            subTabPane = (StackPane) fxmlLoader.load();
            userTabPane.getChildren().setAll(subTabPane);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShow() {

    }

}
