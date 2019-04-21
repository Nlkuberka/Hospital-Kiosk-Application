package SplashScreen;

import application.UIController;
import javafx.animation.PathTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.awt.geom.Point2D;

import static java.lang.Math.random;


public class ColorfulCircles extends UIController {
    private final int totalWidth = 1200;
    private final int totalHeight = 800;

    public void onShow() {
        Group root = new Group();
        Scene scene = new Scene(root, totalWidth, totalHeight, Color.BLACK);
        primaryStage.setScene(scene);
        scene.setOnMouseClicked(event -> goToScene(UIController.WELCOME_MAIN));
        primaryStage.show();
        Group circles = new Group();
        for (int i = 0; i < 30; i++) {
            Circle circle = new Circle(150, Color.web("white", 0.05));
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            circles.getChildren().add(circle);
        }
        Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(),
                new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#f8bd55")),
                        new Stop(0.14, Color.web("#c0fe56")),
                        new Stop(0.28, Color.web("#5dfbc1")),
                        new Stop(0.43, Color.web("#64c2f8")),
                        new Stop(0.57, Color.web("#be4af7")),
                        new Stop(0.71, Color.web("#ed5fc2")),
                        new Stop(0.85, Color.web("#ef504c")),
                        new Stop(1, Color.web("#f2660f"))));
        colors.widthProperty().bind(scene.widthProperty());
        colors.heightProperty().bind(scene.heightProperty());
        Group blendModeGroup =
                new Group(new Group(new Rectangle(scene.getWidth(), scene.getHeight(),
                        Color.BLACK), circles), colors);
        colors.setBlendMode(BlendMode.OVERLAY);
        root.getChildren().add(blendModeGroup);
        circles.setEffect(new BoxBlur(10, 10, 3));
        for (Node circle : circles.getChildren()) {
            Path path = new Path();
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.seconds(20));
            pathTransition.setNode(circle);
            pathTransition.setPath(path);
            pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pathTransition.setCycleCount(1);
            pathTransition.setAutoReverse(false);

            continuousTransition(path, pathTransition, getPointInBounds());
        }
    }

    private Point2D getPointInBounds() {
        return new Point2D.Double(random() * totalWidth, random() * totalHeight);
    }

    private void continuousTransition(Path path, PathTransition pathTransition, Point2D point2D) {
        path.getElements().clear();
        path.getElements().add(new MoveTo(point2D.getX(), point2D.getY()));
        Point2D finalPoint = new Point2D.Double(getPointInBounds().getX(), getPointInBounds().getY());
        path.getElements().add(new CubicCurveTo(getPointInBounds().getX(), getPointInBounds().getY(),
                getPointInBounds().getX(), getPointInBounds().getY(), finalPoint.getX(), finalPoint.getY()));
        pathTransition.play();

        pathTransition.setOnFinished(event -> continuousTransition(path, pathTransition, finalPoint));
    }

}