package helper;

import database.DBController;
import database.DBControllerNE;
import entities.Node;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.event.EventHandler;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Handles the filtering of the room cetgory choiceboxes
 * @author Jonathan Chang
 * @version iteration3
 */
public class RoomCategoryFilterHelper {
    @FXML private ChoiceBox<String> nodeCategoryChoiceBox;
    @FXML private ChoiceBox<String> nodeSelectChoiceBox;
    private EventHandler eventHandler;
    private String nodeID;

    private static Map<String, String> roomCategories = new HashMap<String, String>() {{
        put("Lower Level 2 - Rooms", DBControllerNE.ALL_ROOMS_FLOOR_L2); 
        put("Lower Level 2 - Other", DBControllerNE.ALL_ROOMS_FLOOR_L1);
        put("Lower Level 1 - Rooms", DBControllerNE.ALL_ROOMS_FLOOR_L1);
        put("Lower Level 1 - Other", DBControllerNE.ALL_ROOMS_FLOOR_L1);
        put("Ground Floor - Rooms", DBControllerNE.ALL_ROOMS_FLOOR_G);
        put("Ground Floor - Other", DBControllerNE.ALL_ROOMS_FLOOR_G);
        put("First Floor - Rooms", DBControllerNE.ALL_ROOMS_FLOOR_1);
        put("First Floor - Other", DBControllerNE.ALL_ROOMS_FLOOR_1);
        put("Second Floor - Rooms", DBControllerNE.ALL_ROOMS_FLOOR_2);
        put("Second Floor - Other", DBControllerNE.ALL_ROOMS_FLOOR_2);
        put("Third Floor - Rooms", DBControllerNE.ALL_ROOMS_FLOOR_3);
        put("Third Floor - Other", DBControllerNE.ALL_ROOMS_FLOOR_3);
    }};

    private Map<String, String> nodeIDs;

    public RoomCategoryFilterHelper(ChoiceBox<String> nccb, ChoiceBox<String> nscb, EventHandler eh) {
        this.nodeCategoryChoiceBox = nccb;
        this.nodeSelectChoiceBox = nscb;
        this.eventHandler = eh;
        nodeIDs = new HashMap<String, String>();;
    }

    private void setupChoiceBoxes() {
        nodeSelectChoiceBox.setDisable(true);
        nodeCategoryChoiceBox.setItems(FXCollections.observableList(new LinkedList<String>(roomCategories.keySet())));
        nodeCategoryChoiceBox.setOnAction(param -> {
            String category = nodeCategoryChoiceBox.getValue();

            Connection conn = DBController.dbConnect();
            List<Node> nodes = DBControllerNE.generateListOfNodes(conn, roomCategories.get(category));
            DBController.closeConnection(conn);

            nodeIDs = new HashMap<String, String>();;
            for(Node node : nodes) {
                nodeIDs.put(node.getLongName(), node.getNodeID());
            }
            nodeSelectChoiceBox.setItems(FXCollections.observableList(new LinkedList<String>(nodeIDs.keySet())));
            nodeSelectChoiceBox.setDisable(false);
        });

        nodeSelectChoiceBox.setOnAction(param -> {
            nodeID = nodeIDs.get(nodeSelectChoiceBox.getValue());
            eventHandler.handle(param);
        });
    }

    public String getNodeID() {
        return this.nodeID
    }
}
