package application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.application.Preloader;

public class TeamDPreloader extends Preloader {
    private Stage preloadStage;
    private Scene scene;
    public TeamDPreloader(){

    }

    @Override
    public void init()throws Exception{
        Parent root1 = FXMLLoader.load(getClass().getResource("splash_screen.fxml"));
        scene = new Scene(root1);
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.preloadStage = primaryStage;

        preloadStage.setScene(scene);
        preloadStage.initStyle(StageStyle.UNDECORATED);
        preloadStage.show();

    }

    @Override
    public void handleApplicationNotification(Preloader.PreloaderNotification info){
        if (info instanceof ProgressNotification) {
            UIControllerSC.label.setText("Loading " + ((ProgressNotification) info).getProgress() + "%");
        }
    }

    @Override
    public void handleStateChangeNotification(Preloader.StateChangeNotification info){
        StateChangeNotification.Type type = info.getType();
        switch(type){
            case BEFORE_START:
                System.out.println("BEFORE_START");
                preloadStage.hide();
                break;
        }

    }
}
