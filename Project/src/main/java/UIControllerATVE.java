import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.List;
import java.util.Observable;

public class UIControllerATVE extends UIController {
    private static final String[] edgeValues  = {"edgeID", "node1ID", "node2ID"};
                                                /**< The Various Edge Columns used for cell factories */

    @FXML
    private MenuItem backButton; /**< The Back Button */

    @FXML
    private JFXButton homeButton; /**< The Home Button */

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

        // Initialize Node ID Column cell factories
        TableColumn<Edge, String> nodeIDColumn = (TableColumn<Edge, String>) tableColumns.get(0);
        nodeIDColumn.setCellValueFactory(new PropertyValueFactory<>(edgeValues[0]));
        nodeIDColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Initialize the cell factories of the node field columns
        for(int i = 1; i < tableColumns.size() - 1; i++) {
            TableColumn<Edge, Edge> column = (TableColumn<Edge, Edge>) tableColumns.get(i);
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            column.setCellFactory(param -> new TableCell<Edge, Edge>() {
                private TextField textField = new TextField("TEST");

                @Override
                protected void updateItem(Edge edge, boolean empty) {
                    super.updateItem(edge, empty);

                    textField.setText("TEST");
                    setGraphic(textField);
                    textField.setOnInputMethodTextChanged( e -> {
                            //DB Add or Update
                            getTableView().getItems().remove(edge);
                        }
                    );
                }
            });
        }
        // Initialize cell factories of the remove node column
        TableColumn<Edge, Edge> removeColumn = (TableColumn<Edge, Edge>) tableColumns.get(tableColumns.size() - 1);
        removeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        removeColumn.setCellFactory(param -> new TableCell<Edge, Edge>() {
            private Button removeButton = new Button("Remove");

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
        //DB get Edges
        for(int i = 0; i < 100; i++) {
            Edge edge  = new Edge(i + "", i * 2 + "", 2 * 3 + "");
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
