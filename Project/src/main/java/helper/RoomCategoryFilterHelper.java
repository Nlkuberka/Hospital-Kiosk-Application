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
import java.util.stream.Collectors;

/**
 * Handles the filtering of the room catgory choiceboxes
 * @author Jonathan Chang
 * @version iteration3
 */
public class RoomCategoryFilterHelper {
    @FXML private ChoiceBox<String> nodeCategoryChoiceBox;
    @FXML private ChoiceBox<String> nodeSelectChoiceBox;
    private EventHandler eventHandler;
    private String nodeID;

    /** > The map that holds both the room catgories but also maps them to the correct DBController String */
    private static Map<String, String> roomCategories = new HashMap<String, String>() {{
        put("Lower Level 2 - Rooms", DBControllerNE.ALL_ROOMS_FLOOR_L2); 
        put("Lower Level 2 - Other", DBControllerNE.ALL_BUT_ROOMS_L2);
        put("Lower Level 1 - Rooms", DBControllerNE.ALL_ROOMS_FLOOR_L1);
        put("Lower Level 1 - Other", DBControllerNE.ALL_BUT_ROOMS_L1);
        put("Ground Floor - Rooms", DBControllerNE.ALL_ROOMS_FLOOR_G);
        put("Ground Floor - Other", DBControllerNE.ALL_BUT_ROOMS_G);
        put("First Floor - Rooms", DBControllerNE.ALL_ROOMS_FLOOR_1);
        put("First Floor - Other", DBControllerNE.ALL_BUT_ROOMS_1);
        put("Second Floor - Rooms", DBControllerNE.ALL_ROOMS_FLOOR_2);
        put("Second Floor - Other", DBControllerNE.ALL_BUT_ROOMS_2);
        put("Third Floor - Rooms", DBControllerNE.ALL_ROOMS_FLOOR_3);
        put("Third Floor - Other", DBControllerNE.ALL_BUT_ROOMS_3);
    }};

    private Map<String, String> nodeIDs;

    /**
     * Contructor
     * @param nccb The node category choiceBox to setup
     * @param nscb The node select choiceBox to setup
     * @param eh The Event handler that should be run when the node is selected
     * @param roomsOnly Whether to display only rooms
     */
    public RoomCategoryFilterHelper(ChoiceBox<String> nccb, ChoiceBox<String> nscb, EventHandler eh, boolean roomsOnly) {
        this.nodeCategoryChoiceBox = nccb;
        this.nodeSelectChoiceBox = nscb;
        this.eventHandler = eh;
        nodeIDs = new HashMap<String, String>();;
        setupChoiceBoxes(roomsOnly);
    }

    /**
     * Sets up the choiceboxes and their events
     */
    private void setupChoiceBoxes(boolean roomOnly) {
        nodeSelectChoiceBox.setDisable(true);
        // Set the room categories in the choicebox
        if(roomOnly) {
            List<String> allCategories = new LinkedList<String>(roomCategories.keySet());
            List<String> categories = allCategories.stream().filter(string -> string.contains("Rooms")).collect(Collectors.toList());
            nodeCategoryChoiceBox.setItems(FXCollections.observableList(categories));
        } else {
            nodeCategoryChoiceBox.setItems(FXCollections.observableList(new LinkedList<String>(roomCategories.keySet())));
        }

        nodeCategoryChoiceBox.setOnAction(param -> {
            String category = nodeCategoryChoiceBox.getValue();

            // Gets the appropriate list of nodes
            Connection conn = DBController.dbConnect();
            List<Node> nodes = DBControllerNE.generateListOfNodes(conn, roomCategories.get(category));
            DBController.closeConnection(conn);

            // Add nodes to the node select choicebox
            nodeIDs = new HashMap<String, String>();
            for(Node node : nodes) {
                nodeIDs.put(node.getLongName(), node.getNodeID());
            }
            nodeSelectChoiceBox.setItems(FXCollections.observableList(new LinkedList<String>(nodeIDs.keySet())));
            nodeSelectChoiceBox.setDisable(false);
        });

        // Set the nodeID and run the given event handler
        nodeSelectChoiceBox.setOnAction(param -> {
            nodeID = nodeIDs.get(nodeSelectChoiceBox.getValue());
            if(eventHandler != null) {
                eventHandler.handle(param);
            }
        });
    }

    public String getNodeID() {
        return this.nodeID;
    }
}
