package pathfinding;

import javafx.animation.PathTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class PathHandler {
    private MapHandler mapHandler;

    PathHandler(MapHandler mapHandler) {
        this.mapHandler = mapHandler;
    }

    private PathTransition pathTransition;

    void playPathAnimation() {
        pathTransition = new PathTransition();

        //Setting the duration of the path transition
        pathTransition.setDuration(Duration.seconds(4));

        //Setting the node for the transition
        Rectangle ant = new Rectangle(8, 3);
        ant.setFill(Color.LIGHTGREEN);
        mapHandler.getTopPane().getChildren().add(ant);
        pathTransition.setNode(ant);

        //Setting the path
        pathTransition.setPath(mapHandler.getCurrentPath());

        //Setting the orientation of the path
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        //Setting auto reverse value to false
        pathTransition.setAutoReverse(false);

        pathTransition.setCycleCount(1);

        pathTransition.setOnFinished(e -> mapHandler.getTopPane().getChildren().remove(ant));

        if (mapHandler.getCurrentPath() != null)
            pathTransition.play();
    }

    void cancel()
    {
        pathTransition.stop();
    }
}
