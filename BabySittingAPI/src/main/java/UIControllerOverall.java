import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class UIControllerOverall extends UIController{
    @FXML private TabPane tabPane;
    @FXML private AnchorPane createServiceRequestTabPane;
    @FXML private AnchorPane addUserTabPane;
    @FXML private AnchorPane viewServiceRequestTabPane;
    @FXML private AnchorPane viewUsersTabPane;

    @FXML
    public void initialize() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/service_request_babysitting.fxml"));
            StackPane subTabPane = (StackPane) fxmlLoader.load();
            createServiceRequestTabPane.getChildren().setAll(subTabPane);

            fxmlLoader = new FXMLLoader(getClass().getResource("/add_user.fxml"));
            subTabPane = (StackPane) fxmlLoader.load();
            addUserTabPane.getChildren().setAll(subTabPane);

            fxmlLoader = new FXMLLoader(getClass().getResource("/admin_tools_view_service_request.fxml"));
            subTabPane = (StackPane) fxmlLoader.load();
            UIControllerATVSR controller1 = (UIControllerATVSR) fxmlLoader.getController();
            controller1.onShow();
            viewServiceRequestTabPane.getChildren().setAll(subTabPane);

            fxmlLoader = new FXMLLoader(getClass().getResource("/view_users.fxml"));
            subTabPane = (StackPane) fxmlLoader.load();
            UIControllerVU controller2 = (UIControllerVU) fxmlLoader.getController();
            controller2.onShow();
            viewUsersTabPane.getChildren().setAll(subTabPane);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShow() {

    }

}
