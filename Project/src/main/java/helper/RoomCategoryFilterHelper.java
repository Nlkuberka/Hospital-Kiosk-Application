package helper;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import database.DBController;
import database.DBControllerNE;
import entities.Node;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.event.EventHandler;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Connection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Handles the filtering of the room catgory choiceboxes
 * @author Jonathan Chang
 * @version iteration3
 */
public class RoomCategoryFilterHelper {
    @FXML private ChoiceBox<String> nodeCategoryChoiceBox;
    @FXML private ChoiceBox<String> nodeSelectChoiceBox;
    @FXML private JFXComboBox<String> nodeSelectComboBox;
    @FXML private JFXTextField nodeSelectTextField;
    private EventHandler eventHandler;
    private String nodeID;
    private String longName;
    private boolean roomsOnly;
    private List<Node> nodes;

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
        put("All Rooms", DBControllerNE.ALL_ROOMS);
    }};

    private static Map<String, String> roomLevels = new HashMap<String, String>() {{
        put("Lower Level 2 - Rooms", "L2");
        put("Lower Level 2 - Other", "L2");
        put("Lower Level 1 - Rooms", "L1");
        put("Lower Level 1 - Other", "L1");
        put("Ground Floor - Rooms", "G");
        put("Ground Floor - Other", "G");
        put("First Floor - Rooms", "1");
        put("First Floor - Other", "1");
        put("Second Floor - Rooms", "2");
        put("Second Floor - Other", "2");
        put("Third Floor - Rooms", "3");
        put("Third Floor - Other", "3");
        put("All Rooms", "L2L1G123");
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
        this.roomsOnly = roomsOnly;
        nodeIDs = new HashMap<String, String>();;
        setupChoiceBoxes();
    }

    /**
     * Constructor
     * @param comboBox The combobox to setup
     * @param eh The event handler that should be run when the node is selected
     * @param roomsOnly Whether to display only rooms
     */
    public RoomCategoryFilterHelper(JFXComboBox<String> comboBox, EventHandler eh, boolean roomsOnly) {
        this.nodeSelectComboBox = comboBox;
        this.eventHandler = eh;
        this.roomsOnly = roomsOnly;
        nodeIDs = new HashMap<String, String>();;
        setupComboBox();
    }

    /**
     * Constructor
     * @param textField The textfield to setup
     * @param eh The event handler that should be run when the node is selected
     * @param roomsOnly Whether to display only rooms
     */
     public RoomCategoryFilterHelper(JFXTextField textField, EventHandler eh, boolean roomsOnly) {
        this.nodeSelectTextField = textField;
        this.eventHandler = eh;
         this.roomsOnly = roomsOnly;
         nodeIDs = new HashMap<String, String>();;
         setupTextField();
     }

    /**
     * Sets up the choiceboxes and their events
     */
    private void setupChoiceBoxes() {
        nodeSelectChoiceBox.setDisable(true);
        // Set the room categories in the choicebox
        if(roomsOnly) {
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
            longName = nodeSelectChoiceBox.getValue();
            nodeID = nodeIDs.get(longName);
            if(eventHandler != null) {
                eventHandler.handle(param);
            }
        });
    }

    /**
     * Sets up the combobox
     */
    private void setupComboBox() {
        nodeSelectComboBox.setEditable(true);

        getNodes();

        // Set to all nodes
        nodeSelectComboBox.setItems(FXCollections.observableList(new LinkedList<String>(nodeIDs.keySet())));
        nodeSelectComboBox.setOnAction(param -> {
            longName = nodeSelectComboBox.getValue();
            nodeID = nodeIDs.get(longName);
            if(eventHandler != null) {
                eventHandler.handle(param);
            }
        });
        // Filter nodes based on user input
        nodeSelectComboBox.setOnKeyReleased(param -> {
            nodeSelectComboBox.setItems(FXCollections.observableList(new LinkedList<String>(nodeIDs.keySet()).stream()
                    .filter(longName -> showNode(longName, nodeSelectComboBox.getValue())).collect(Collectors.toList())));
        });
    }

    /**
     * Sets up the textfield
     */
    private void setupTextField() {
        getNodes();
        List<String> nodeLongNames = new LinkedList<String>(nodeIDs.keySet());
        TextFields.bindAutoCompletion(nodeSelectTextField, nodeLongNames.toArray());

        nodeSelectTextField.setOnAction(param -> {
            if(!new LinkedList<String>(nodeIDs.keySet()).contains(nodeSelectTextField.getText())) {
                nodeSelectTextField.setText("");
                return;
            }
            longName = nodeSelectTextField.getText();
            nodeID = nodeIDs.get(longName);
            if(eventHandler != null) {
                eventHandler.handle(param);
            }
        });
    }

    /**
     * Returns whether or not to display the node
     * @param nodeLongName The node's long name
     * @param input The user input
     * @return Whether or not to display the node
     */
    public boolean showNode(String nodeLongName, String input) {
        Connection conn = DBController.dbConnect();
        nodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS);
        DBController.closeConnection(conn);

        String[] parts = input.split(" ");
        boolean[] show = new boolean[parts.length];
        for(int i = 0; i < parts.length; i++) {
            show[i] = showNodeHelper(nodeLongName, parts[i]);
        }
        boolean result = true;
        for(boolean b : show) {
            result = result && b;
        }

        nodes = null;
        return result;
    }

    /**
     * Returns whether or not to display the node
     * @param nodeLongName The node's long name
     * @param input The user input
     * @return Whether or not to display the node
     */
    public boolean showNodeHelper(String nodeLongName, String input) {
        if(input == null || input.equals("")) {
            return true;
        }

        input = input.toLowerCase();
        nodeLongName = nodeLongName.toLowerCase();
        if(nodeLongName.toLowerCase().contains(input)) {
            return true;
        }
        // Check for category
        for(String category : roomCategories.keySet()) {
            String nodeLongNameSub = nodeLongName;
            if(category.toLowerCase().contains(input)) {
                List<Node> subNodes = nodes.stream().filter(node -> roomLevels.get(category).contains(node.getFloor())).collect(Collectors.toList());
                subNodes = subNodes.stream().filter(node -> node.getLongName().toLowerCase().equals(nodeLongNameSub)).collect(Collectors.toList());
                if(subNodes.size() >= 1) {
                    return true;
                }
            }
        }
        // Bathroom and restroom interchangeable
        if(("bathroom".contains(input) || "restroom".contains(input)) && (nodeLongName.contains("bathroom") || nodeLongName.contains("restroom"))) {
            return true;
        }
        return false;
    }

    private void getNodes() {
        Connection conn = DBController.dbConnect();
        List<Node> nodes = roomsOnly ? DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS) :  DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_NODES);

        for(Node node : nodes) {
            nodeIDs.put(node.getLongName(), node.getNodeID());
        }
    }

    public String getNodeID() {
        return this.nodeID;
    }

    public String getLongName() {
        return this.longName;
    }
}
