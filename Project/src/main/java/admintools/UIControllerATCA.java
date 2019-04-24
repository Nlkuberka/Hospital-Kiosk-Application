package admintools;

import application.CurrentUser;
import application.UIController;
import com.jfoenix.controls.JFXTabPane;
import entities.Graph;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.util.HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * UIController for changes between algorithms
 * @author Jonathan Chang
 * @version iteration2
 */
public class UIControllerATCA extends UIController {
    private static final Map<String, Integer> algorithms = new HashMap<String, Integer>(){{
        put("A* Algorithm", CurrentUser.AALOGRITHM);
        put("Depth First Algorithm", CurrentUser.DFSEARCH);
        put("Breath First Algorithm", CurrentUser.BFSEARCH);
        put("Bellman-Ford Algorithm", CurrentUser.BELLMAN_FORD);
        put("Dijkstra's Algorithm", CurrentUser.DIJKSTRA);
    }};

    @FXML
    private ImageView backgroundImage;

    @FXML
    private JFXTabPane tabs;

    @FXML
    public void initialize() {
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());
    }

    @FXML
    private void changeAlgorithm() {
        String algorithm = tabs.getSelectionModel().getSelectedItem().getText();
        switch(algorithms.get(algorithm)) {
            case CurrentUser.AALOGRITHM:
                Graph.toAStar();
                break;
            case CurrentUser.BFSEARCH:
                Graph.toBFS();
                break;
            case CurrentUser.DFSEARCH:
                Graph.toDFS();
                break;
            case CurrentUser.BELLMAN_FORD:
                Graph.toBellmanFord();
                break;
            case CurrentUser.DIJKSTRA:
                Graph.toDijkstra();
                break;
        }
        popupMessage("Algorithm changed to " + algorithm, false);
    }

}
