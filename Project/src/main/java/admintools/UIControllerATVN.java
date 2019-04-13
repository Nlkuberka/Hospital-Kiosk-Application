package admintools;

import application.DBController;
import application.UIController;
import entities.Node;

import com.jfoenix.controls.JFXButton;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

/**
 * The UIController for viewing, editing, removing, and adding nodes to the graph
 * Allows the admin to make and necessary changes to nodes
 * @author Jonathan Chang, imoralessirgo
 * @version iteration1
 */
public class UIControllerATVN extends  UIController {
    private static final int[] lengthRequirements = {10, -1, -1, 3, 15, 4, 50, 50};
    private static final String[] nodeSetters  = {"setNodeID", "setXcoord", "setYcoord", "setFloor",
                                                    "setBuilding", "setNodeType", "setLongName", "setShortName"};
    private static final String[] nodeGetters  = {"getNodeID", "getXcoord", "getYcoord", "getFloor",
                                                "getBuilding", "getNodeType", "getLongName", "getShortName"};
    @FXML
    private ImageView backgroundImage;
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
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());

        List<TableColumn<Node, ?>> tableColumns = nodeTable.getColumns();

        // Initialize the cell factories of the node field columns
        for(int i = 0; i < tableColumns.size() - 1; i++) {
            int indexOut = i;
            TableColumn<Node, Node> column = (TableColumn<Node, Node>) tableColumns.get(i);
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            column.setCellFactory(param -> new EditableTextCell<Node, Node>(column, indexOut) {

                // When the Node is updated on the textfield
                @Override
                protected void updateItem(Node node, boolean empty) {
                    super.updateItem(node, empty);

                    // Get the starting text of the label and textField
                    runStringGetterEditable(node, nodeGetters[index], label, textField);


                    // When an edit is committed with enter
                    textField.setOnAction(et -> {
                        // Catch if int is not able to be parsed
                        if(index == 1 || index == 2) {
                            try {
                                if(Integer.parseInt(textField.getText()) < 0) {
                                    popupMessage("Coordinate fields must be positive", true);
                                }
                            } catch(Exception e) {
                                setGraphic(label);
                                textField.setText(label.getText());
                                return;
                            }
                        } else {
                            // Resets if the input is too long
                            if(textField.getText().length() >lengthRequirements[index]) {
                                setGraphic(label);
                                textField.setText(label.getText());
                                popupMessage("Field must have equal to or less than " +  lengthRequirements[index] + " characters.", true);
                                return;
                            }
                        }
                        // Update the object with the new value
                        if(index == 1 || index == 2) {
                            runSetter(node, nodeSetters[index], int.class, Integer.parseInt(textField.getText()));
                        } else {
                            runSetter(node, nodeSetters[index], String.class, textField.getText());
                        }
                        System.out.println(node);

                        try{
                            Connection conn = DBController.dbConnect();
                            if(index == 0) {
                                DBController.deleteNode(label.getText(),conn);
                                DBController.addNode(node,conn);
                            } else {
                                DBController.updateNode(node, conn);
                            }
                            conn.close();
                        }catch(SQLException e){
                            e.printStackTrace();
                        }
                        setGraphic(label);
                        label.setText(textField.getText());
                    });
                }
            });
        }
        // Initialize cell factories of the remove node column
        TableColumn<Node, Node> removeColumn = (TableColumn<Node, Node>) tableColumns.get(tableColumns.size() - 1);
        removeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        removeColumn.setCellFactory(param -> new TableCell<Node, Node>() {
            private JFXButton removeButton = new JFXButton("Remove");

            @Override
            protected void updateItem(Node node, boolean empty) {
                super.updateItem(node, empty);
                if(node == null) {
                    return;
                }
                setGraphic(removeButton);
                removeButton.setOnAction( e -> {
                        try {
                            Connection conn = DBController.dbConnect();
                            DBController.deleteNode(node.getNodeID(), conn);
                            conn.close();
                        }catch(SQLException e1){
                            e1.printStackTrace();
                        }
                            getTableView().getItems().remove(node);
                        }
                );
            }

        });

    }

    /**
     * Run when the scene is shown
     * Gets the nodes from the database and puts them into the table
     */
    @Override
    public void onShow() {
        //DB get Nodes
        Connection conn = DBController.dbConnect();
        ObservableList<Node> nodeData = FXCollections.observableArrayList();
        try{
            ResultSet rs = conn.createStatement().executeQuery("Select * from NODES");
            while (rs.next()){
                nodeData.add(new Node(rs.getString(1),rs.getInt(2),rs.getInt(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),
                        rs.getString(7),rs.getString(8)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        nodeTable.setItems(nodeData);
    }

    /**
     * Adds an empty to the table
     */
    @FXML
    private void setAddButton() {
        Node node = new Node("", 0, 0, "", "", "", "" ,"");
        nodeTable.getItems().add(node);
    }

    /**
     * Goes back to the admin tools application menu
     */
    @FXML
    private void setBackButton() {
        this.goToScene(UIController.ADMIN_TOOLS_MAIN);
    }
}
