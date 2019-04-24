package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import pathfinding.UIControllerPFM;

import java.time.Clock;

public class SessionTimeoutThread extends Thread{
    public static long timeout = 30 * 1000;
    public String currentSceneString;
    public UIController currentUIController;

    public void run() {
        String homeSceneMemo = saveMemo();
        while(true) {
            try {
                Thread.sleep(timeout);
                restoreMemo(homeSceneMemo);
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
}
