import com.jfoenix.controls.JFXButton;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The UIController for viewing, adding, editing, and removing the edges
 * Allows an admin to edit and modify the edges to conform to the new changes
 * @author Jonathan Chang, imoralessirgo
 * @version iteration1
 */
public class UIControllerATVE extends UIController {
    private static final int[] lengthRequirements = {21, 10, 10};
    private static final String[] edgeSetters  = {"setEdgeID", "setNode1ID", "setNode2ID"};
    private static final String[] edgeGetters  = {"getEdgeID", "getNode1ID", "getNode2ID"};
                                                /**< The Various Edge Columns used for cell factories */

    @FXML
    private MenuItem backButton; /**< The Back Button */

    @FXML
    private Menu homeButton; /**< The Home Button */

    @FXML
    private JFXButton addButton; /**< The Add Button */

    @FXML
    private TableView<Edge> edgeTable; /**< The table that holds all of the edges */

    public UIControllerATVE() {

    }

    /**
     * Called when the scene is first created
     * Sets up all the cell factories
     */
    @FXML
    public void initialize() {
        ObservableList<TableColumn<Edge, ?>> tableColumns = edgeTable.getColumns();

        // Initialize the cell factories of the node field columns
        TableColumn<Edge, Edge> edgeIDColumn = (TableColumn<Edge, Edge>) tableColumns.get(0);
        edgeIDColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        edgeIDColumn.setCellFactory(param -> new TableCell<Edge, Edge>() {
            private Label label = new Label();
            private int index = 0;


            @Override
            protected void updateItem(Edge edge, boolean empty) {
                super.updateItem(edge, empty);

                runStringGetter(edge, edgeGetters[index], label);
                setGraphic(label);
            }
        });
        for(int i = 1; i < tableColumns.size() - 1; i++) {
            int indexOut = i;
            TableColumn<Edge, Edge> column = (TableColumn<Edge, Edge>) tableColumns.get(i);
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            column.setCellFactory(param -> new EditableTextCell<Edge, Edge>(column, indexOut) {

                @Override
                protected void updateItem(Edge edge, boolean empty) {
                    super.updateItem(edge, empty);

                    // Get initial value
                    runStringGetterEditable(edge, edgeGetters[index], label, textField);

                    // When the user commits changes with enter
                    textField.setOnAction(et -> {
                        // Check length requirements
                        if(textField.getText().length() > lengthRequirements[index]) {
                            setGraphic(label);
                            textField.setText(label.getText());
                            return;
                        }
                        runSetter(edge, edgeSetters[index], String.class, textField.getText());
                        System.out.println(edge);
                            Connection conn = DBController.dbConnect();
                            if (index == 0) {
                                DBController.deleteEdge(label.getText(), conn);
                            }
                            DBController.addEdge(edge,conn);
                            DBController.closeConnection(conn);

                        setGraphic(label);
                        label.setText(textField.getText());
                    });
                }
            });
        }



        // Initialize cell factories of the remove node column
        TableColumn<Edge, Edge> removeColumn = (TableColumn<Edge, Edge>) tableColumns.get(tableColumns.size() - 1);
        removeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        removeColumn.setCellFactory(param -> new TableCell<Edge, Edge>() {
            private JFXButton removeButton = new JFXButton("Remove");

            @Override
            protected void updateItem(Edge edge, boolean empty) {
                super.updateItem(edge, empty);
                if(edge == null) {
                    return;
                }
                setGraphic(removeButton);
                removeButton.setOnAction( e -> {
                        Connection conn = DBController.dbConnect();
                        DBController.deleteEdge(edge.getEdgeID(),conn);
                        DBController.closeConnection(conn);
                        getTableView().getItems().remove(edge);
                    }
                );
            }
        });
    }

    /**
     * Runs when the scene is shown
     * Gets the edges from the database and puts them into the table
     */
    @Override
    public void onShow() {
        Connection conn = DBController.dbConnect();
        ObservableList<Edge> edgeData = FXCollections.observableArrayList();
        try{
            ResultSet rs = conn.createStatement().executeQuery("Select * from EDGES");
            while (rs.next()){
                edgeData.add(new Edge(rs.getString(1),rs.getString(2),rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        edgeTable.setItems(edgeData);
    }

    /**
     * Adds an empty to the table
     */
    @FXML
    private void setAddButton() {
        Edge edge  = new Edge("", "", "");
        edgeTable.getItems().add(edge);
    }

    /**
     * Goes back to the admin tools main menu
     */
    @FXML
    private void setBackButton() {
        this.goToScene(UIController.ADMIN_TOOLS_MAIN);
    }

}
