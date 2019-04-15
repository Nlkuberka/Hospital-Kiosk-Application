package admintools;

import application.UIController;
import com.jfoenix.controls.JFXButton;
import entities.Node;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;

import static java.lang.Math.random;

public class UIControllerPUMVNO extends UIController {
    public JFXButton button_cancel;
    public JFXButton button_editNode;
    public JFXButton button_setKioskLocation;
    public JFXButton button_addEdge;
    public JFXButton button_deleteNode;
    public JFXButton button_deleteEdge;
    public StackPane parentPane;
    private UIControllerATMV uiControllerATMV;
    private Node node;
    
    @FXML
    void closeWindow(MouseEvent mouseEvent) {
        parentPane.getScene().getWindow().hide();
    }

    void closeWindow() {
        parentPane.getScene().getWindow().hide();
    }

    @FXML
    private void editNode(MouseEvent mouseEvent) throws IOException {
        closeWindow();
        uiControllerATMV.editNode(node);
    }

    @FXML
    private void setKiosk(MouseEvent mouseEvent) throws IOException {
        closeWindow();
        uiControllerATMV.setKiosk(node);
    }

    @FXML
    private void addEdge(MouseEvent mouseEvent) throws IOException {
        closeWindow();
        //status = "ADD-EDGE";
    }

    @FXML
    private void deleteNode(MouseEvent mouseEvent) throws IOException {
        closeWindow();
        uiControllerATMV.deleteNode(node);
    }

    @FXML
    private void deleteEdge(MouseEvent mouseEvent) throws IOException {
        closeWindow();
        //status = "DELETE-EDGE";
    }

    void setUiControllerATMV(UIControllerATMV uiControllerATMV, Node node) {
        this.uiControllerATMV = uiControllerATMV;
        this.node = node;
    }
}
