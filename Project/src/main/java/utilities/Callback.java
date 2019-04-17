package utilities;

public abstract class Callback {
    public abstract void onStart();
    public abstract void update(double delta);
    public abstract void onEnd();
}
