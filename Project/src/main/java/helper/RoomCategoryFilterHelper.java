package helper;

import database.DBController;
import database.DBControllerNE;
import entities.Node;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RoomCategoryFilterHelper {
    @FXML private ChoiceBox<String> nodeCategoryChoiceBox;
    @FXML private ChoiceBox<String> nodeSelectChoiceBox;

    private static List<String> roomCategories = new LinkedList<String>(){{
        add("Lower Level 2 - Rooms"); add("Lower Level 2 - Other");
        add("Lower Level 1 - Rooms"); add("Lower Level 1 - Other");
        add("Ground Floor - Rooms"); add("Ground Floor - Other");
        add("First Floor - Rooms"); add("First Floor - Other");
        add("Second Floor - Rooms"); add("Second Floor - Other");
        add("Third Floor - Rooms"); add("Third Floor - Other");
    }};

    private Map<String, String> nodeIDs;

    public RoomCategoryFilterHelper(ChoiceBox<String> nccb, ChoiceBox<String> nscb) {
        this.nodeCategoryChoiceBox = nccb;
        this.nodeSelectChoiceBox = nscb;
        nodeIDs = new HashMap<String, String>();;
    }

    private void setupChoiceBoxes() {
        nodeSelectChoiceBox.setDisable(true);
        nodeCategoryChoiceBox.setItems(FXCollections.observableList(roomCategories));
        nodeCategoryChoiceBox.setOnAction(param -> {
            String category = nodeCategoryChoiceBox.getValue();
            int index = roomCategories.indexOf(category);
            List<Node> nodes;
            Connection conn = DBController.dbConnect();
            switch (index) {
                case 0 :
                    nodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_L2);
                    break;
                case 2 :
                    nodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_FLOOR_L1);
                    break;
                case 4 :
                    nodes = DBControllerNE.generateListOfNodes(conn, DBControllerNE.ALL_ROOMS_);
            }
        });

    }
}
