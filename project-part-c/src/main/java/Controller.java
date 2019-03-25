import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
    private final int WIDTH = 1200;
    private final int HEIGHT = 800;
    @FXML
    protected void goToView(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("readIn.fxml"));
            stage.setTitle("Database Viewer");
            stage.setScene(new Scene(root, WIDTH, HEIGHT));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    protected void goToEdit(Stage stage, String nodeID) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editNode.fxml"));
            Parent root = fxmlLoader.load();
            editNodeController controller = fxmlLoader.getController();

            controller.setNodeID(nodeID);

            stage.setTitle("Database Editor");
            stage.setScene(new Scene(root, WIDTH, HEIGHT));
            stage.show();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @FXML
    protected void goToDownload(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("download.fxml"));
            Parent root = fxmlLoader.load();
            stage.setTitle("Database Download");
            stage.setScene(new Scene(root, WIDTH, HEIGHT));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
