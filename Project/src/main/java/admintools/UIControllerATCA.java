package admintools;

import application.CurrentUser;
import application.UIController;
import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;

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
    private JFXTabPane tabs;

    @FXML
    public void initialize() {
        List<String> keys = new LinkedList<>();
        keys.addAll(algorithms.keySet());
    }

    @FXML
    private void changeAlgorithm() {
        String algorithm = tabs.getSelectionModel().getSelectedItem().getText();
        CurrentUser.currentAlgorithm = algorithms.get(algorithm);
        popupMessage("Algorithm changed to " + algorithm, false);
    }

    @FXML
    private void setBackMenuItem() {
        this.goToScene(UIController.ADMIN_TOOLS_MAIN);
    }
}
