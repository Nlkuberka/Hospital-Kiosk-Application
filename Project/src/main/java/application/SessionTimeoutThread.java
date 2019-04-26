package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import pathfinding.UIControllerPFM;

import java.time.Clock;
import java.util.LinkedList;
import java.util.List;

public class SessionTimeoutThread extends Thread{
    public static long timeout = 30 * 1000;
    public String currentSceneString;
    public UIController currentUIController;
    private List<Stage> popupList = new LinkedList<>();

    public void run() {
        String homeSceneMemo = saveMemo();
        while(true) {
            try {
                Thread.sleep(timeout);
                restoreMemo(homeSceneMemo);
                for(Stage stage : popupList) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.close();
                        }
                    });
                }
                popupList.clear();
            }
            catch(InterruptedException e) {
            }
        }
    }

    public String saveMemo() {
        return currentSceneString;
    }

    public void restoreMemo(String memo) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ((UIControllerPFM) currentUIController.getUIController(UIController.PATHFINDING_MAIN)).cancel();
                currentUIController.goToScene(memo);
            }
        });
    }

    public void addPopup(Stage stage) {
        popupList.add(stage);
    }
}
