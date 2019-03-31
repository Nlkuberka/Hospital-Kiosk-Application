import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class UIControllerATVN extends  UIController {
    private static final String[] nodeGetters  = {"getNodeID", "getXcoord", "getYcoord", "getFloor",
                                                "getBuilding", "getLongName", "getShortName"};
                                                /**< The Various Node Columns used for cell factories */

    @FXML
    private MenuItem backButton; /**< The Back Button */

    @FXML
    private Menu homeButton; /**< The Home Button */

    @FXML
    private JFXButton addButton; /**< The Add Button */

    @FXML
    private TableView<Node> nodeTable; /**< The table that holds all of the nodes */

    public UIControllerATVN() {

    }

    /**
     * Called when the scene is first created
     * Sets up all the cell factories
     */
    @FXML
    public void initialize() {
        List<TableColumn<Node, ?>> tableColumns = nodeTable.getColumns();

        // Initialize the cell factories of the node field columns
        System.out.println(tableColumns.size());
        for(int i = 0; i < tableColumns.size() - 2; i++) {
            int indexOut = i;
            TableColumn<Node, Node> column = (TableColumn<Node, Node>) tableColumns.get(i);
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            column.setCellFactory(param -> new TableCell<Node, Node>() {
                private TextField textField = new TextField("TEST");
                private int index = indexOut;

                @Override
                protected void updateItem(Node node, boolean empty) {
                    super.updateItem(node, empty);

                    try {
                        Method method = node.getClass().getMethod(nodeGetters[index]);
                        textField.setText((String) method.invoke(node));
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }

                    setGraphic(textField);
                    textField.setOnAction( e -> {
                            System.out.println(node.getNodeID());
                            if(index == 0) {
                                //DB Remove
                            }
                            //DB Add or Update
                        }
                    );
                }
            });
        }
        // Initialize cell factories of the remove node column
        TableColumn<Node, Node> removeColumn = (TableColumn<Node, Node>) tableColumns.get(tableColumns.size() - 1);
        removeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
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
        Connection conn = DBController.dbConnect();
        ObservableList<Node> nodeData = FXCollections.observableArrayList();
        try{
            ResultSet rs = conn.createStatement().executeQuery("Select * from NODES");
            while (rs.next()){
                nodeData.add(new Node(rs.getString(1),rs.getInt(2),rs.getInt(3),
                        rs.getInt(4),rs.getString(5),rs.getString(6),
                        rs.getString(7),rs.getString(8)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        nodeTable.setItems(nodeData);
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

    /**
     * Goes back to the admin tools main menu
     */
    @FXML
    private void setBackButton() {
        this.goToScene(UIController.ADMIN_TOOLS_MAIN);
    }
}
