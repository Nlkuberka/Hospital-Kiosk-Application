import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class UIControllerATVN extends  UIController {
    private static final String[] nodeValues = {"NodeID", "XCoord", "YCoord", "Floor", "Building", "LongName", "ShortName"};
    @FXML
    private Button homeButton;

    @FXML
    private TableView nodeTable;

    public UIControllerATVN() {

    }

    /**
     * Called when the scene is first created
     * Sets up all the cell factories
     */
    @FXML
    public void initialize() {
        List<TableColumn> tableColumns = nodeTable.getColumns();

        // Initialize the cell factories of the node field columns
        for(int i = 0; i < tableColumns.size() - 1; i++) {
            TableColumn column = tableColumns.get(i);
            column.setCellValueFactory(new PropertyValueFactory<>(nodeValues[i].toLowerCase()));
            column.setCellFactory(new PropertyValueFactory<>(nodeValues[i]));
            column.setOnEditCommit(e -> {
                //DB Update or DB add
            });
        }
        // Initialize cell factories of the remove node column
        TableColumn removeColumn = tableColumns.get(tableColumns.size() - 1);
        removeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>("Remove"));
        removeColumn.setCellFactory(param -> new TableCell<Node, Node>() {
            private Button removeButton = new Button("Remove");

            @Override
            protected void updateItem(Node node, boolean empty) {
                super.updateItem(node, empty);

                setGraphic(removeButton);
                removeButton.setOnAction( e -> {
                            //DB Remove Node
                            getTableView().getItems().remove(node);
                        }
                );
            }

        });
        //DB get Nodes
        for(int i = 0; i < 100; i++) {
            Node node  = new Node(i + "", 1 + 1, i + 2, i + 3, "B", "NT", "LN", "SN");
            nodeTable.getItems().add(node);
        }
    }

    /**
     * Adds an empty to the table
     */
    @FXML
    private void setAddButton() {
        Node node = new Node("", 0, 0, 0, "", "", "" ,"");
        nodeTable.getItems().add(node);
    }
}
