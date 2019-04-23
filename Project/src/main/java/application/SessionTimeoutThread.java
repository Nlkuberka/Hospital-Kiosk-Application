package application;

import javafx.application.Platform;

import java.time.Clock;

public class SessionTimeoutThread extends Thread{
    public long timeout = 30 * 1000;    // the time, in milliseconds, the application will wait for the user to do some action before logging them out
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
                currentUIController.goToScene(memo);
            }
        });
    }
}
