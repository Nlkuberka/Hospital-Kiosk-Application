package admintools;

import application.CurrentUser;
import application.UIController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

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
    }};

    @FXML
    private ChoiceBox<String> algorithmChoiceBox;


    @FXML
    public void initialize() {
        List<String> keys = new LinkedList<>();
        keys.addAll(algorithms.keySet());
        algorithmChoiceBox.setItems(FXCollections.observableList(keys));
    }

    @FXML
    private void changeAlgorithm() {
        CurrentUser.currentAlgorithm = algorithms.get(algorithmChoiceBox.getValue());
    }
}
