package utilities;

public class Timer implements Runnable {
    private final Thread thread;
    private boolean isRunning = false;
    private double waitTime;
    private Callback callback;

    public Timer(float waitTimeInSeconds, Callback callback) {
        waitTime = (double) waitTimeInSeconds * 1000L;
        this.callback = callback;
        this.thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        synchronized ((Object) isRunning) {
            if (!isRunning) {
                isRunning = true;
                double startTime = System.currentTimeMillis();
                final double endTime = startTime + waitTime;
                synchronized (thread) {
                    this.callback.onStart();
                    while (true) {
                        double delta = ((System.currentTimeMillis() - startTime) / (endTime - startTime));
                        this.callback.update(delta);
                        if (delta >= 1) {
                            break;
                        }
                    }
                    this.callback.onEnd();
                    isRunning = false;
                }
            }
        }
    }
}
