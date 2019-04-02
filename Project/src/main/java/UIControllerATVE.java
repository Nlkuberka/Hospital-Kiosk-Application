import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.lang.reflect.Method;

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
        for(int i = 0; i < tableColumns.size() - 1; i++) {
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
                        if(textField.getText().length() > lengthRequirements[index]) {
                            setGraphic(label);
                            textField.setText(label.getText());
                            return;
                        }
                        runSetter(edge, edgeSetters[index], String.class, textField.getText());
                        System.out.println(edge);
                        if(index == 0) {
                            //DB Remove
                        }
                        //DB Add or Update
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

                setGraphic(removeButton);
                removeButton.setOnAction( e -> {
                        //DB Remove Edge
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
        //DB get Edges
        for(int i = 0; i < 100; i++) {
            Edge edge  = new Edge(i + "", i * 2 + "", i * 3 + "");
            edgeTable.getItems().add(edge);
        }
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
