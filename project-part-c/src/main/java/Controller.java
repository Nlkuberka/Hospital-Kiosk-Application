import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private static Scene viewScene;
    private static readInController viewController;
    private static Scene editScene;
    private static editNodeController editController;
    private static Scene downloadScene;
    private static downloadController downloadController;

    public Controller() {
    }

    @FXML
    protected void goToView(Stage stage) {
        if(viewScene == null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("readIn.fxml"));
                Parent root = fxmlLoader.load();
                viewController = fxmlLoader.getController();
                viewScene = new Scene(root, WIDTH, HEIGHT);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        stage.setTitle("Database Viewer");
        stage.setScene(viewScene);
        stage.show();
    }

    @FXML
    protected void goToEdit(Stage stage, String nodeID) {
        if(editScene == null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editNode.fxml"));
                Parent root = fxmlLoader.load();
                editController = fxmlLoader.getController();
                editScene = new Scene(root, WIDTH, HEIGHT);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        editController.setNodeID(nodeID);

        stage.setTitle("Database Editor");
        stage.setScene(editScene);
        stage.show();
    }

    @FXML
    protected void goToDownload(Stage stage) {
        if(downloadScene == null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("download.fxml"));
                Parent root = fxmlLoader.load();
                downloadController = fxmlLoader.getController();
                downloadScene = new Scene(root, WIDTH, HEIGHT);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        stage.setTitle("Database Download");
        stage.setScene(downloadScene);
        stage.show();
    }
}
