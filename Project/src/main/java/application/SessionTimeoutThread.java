package application;

import java.time.Clock;

public class SessionTimeoutThread extends Thread{
    public long timeout = 10 * 1000;
    public String currentSceneString;
    public UIController currentUIController;

    public void run() {
        String homeSceneMemo = saveMemo();
        while(true) {
            try {
                Thread.sleep(timeout);
                System.out.println("Logged out");
                restoreMemo(homeSceneMemo);
            }
            catch(InterruptedException e) {
                System.out.println("Action happened");
            }
        }
    }

    public String saveMemo() {
        return currentSceneString;
    }

    public void restoreMemo(String memo) {
        currentUIController.goToScene(memo);
    }
}
