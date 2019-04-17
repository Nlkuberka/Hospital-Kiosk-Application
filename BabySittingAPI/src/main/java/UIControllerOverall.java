import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class UIControllerOverall extends UIController{
    @FXML private TabPane tabPane;
    @FXML private Pane createServiceRequestTabPane;
    @FXML private Pane addUserTabPane;
    @FXML private Pane viewServiceRequestTabPane;
    @FXML private Pane viewUsersTabPane;

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

            tabPane.getSelectionModel().selectedItemProperty().addListener(param -> {
                controller1.onShow();
                controller2.onShow();
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShow() {

    }

}
