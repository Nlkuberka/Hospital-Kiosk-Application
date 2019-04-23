package utilities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.lang.reflect.Field;


public class Tooltip {
    javafx.scene.control.Tooltip tooltip;

    public Tooltip(Node node, String text) {
        tooltip = new javafx.scene.control.Tooltip(text);
        tooltip.setTextAlignment(TextAlignment.CENTER);
        tooltip.setStyle("-fx-background-color: #015080;" +
                "    -fx-background-radius: 0;" +
                "    -fx-border-radius: 0;");
        hackTooltipStartTiming(tooltip);
        javafx.scene.control.Tooltip.install(node, tooltip);
    }

    public Tooltip(Node node, String text, TextAlignment textAlignment) {
        tooltip = new javafx.scene.control.Tooltip(text);
        tooltip.setTextAlignment(textAlignment);
        hackTooltipStartTiming(tooltip);
        javafx.scene.control.Tooltip.install(node, tooltip);
    }

    private static void hackTooltipStartTiming(javafx.scene.control.Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public javafx.scene.control.Tooltip getTooltip() {
        return tooltip;
    }
}
