import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;

public class UIControllerATVE extends UIController {
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
            column.setCellFactory(param -> new TableCell<Edge, Edge>() {
                private TextField textField = new TextField("TEST");
                private int index = indexOut;

                @Override
                protected void updateItem(Edge edge, boolean empty) {
                    super.updateItem(edge, empty);

                    try {
                        Method method = edge.getClass().getMethod(edgeGetters[index]);
                        textField.setText((String) method.invoke(edge));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    setGraphic(textField);
                    textField.setOnAction(et -> {
                        try {
                            Method method2 = edge.getClass().getMethod(edgeSetters[index], String.class);
                            method2.invoke(edge, textField.getText());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println(edge);
                        if(index == 0) {
                            //DB Remove
                        }
                        //DB Add or Update
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
        //DB get Edges
        try {
            Connection conn = DBController.dbConnect();
            ResultSet rs = conn.createStatement().executeQuery("select * from EDGES;");
            while(rs.next()){
                edgeTable.getItems().add(new Edge(rs.getString("EDGEID"),rs.getString("STARTNODE"),rs.getString("ENDNODE")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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