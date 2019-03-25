import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class readInController {
    private int rowNum = 1;
    private int colNum = 8;

    @FXML
    private GridPane databaseGrid;

    @FXML
    private AnchorPane databaseAnchor;

    @FXML
    private Button toDownloadButton;

    @FXML
    public void initialize(){
        for(int i = 0; i < 100; i++) {
            Node node1 = new Node("" + i, i * 7, i * 8, i + 2, "Building " + i, "Room", "Room 1", "RM!");
            addRow(node1);
        }
    }

    private void addRow(Node node){
        Label[] labels = new Label[colNum];
        String[] values = new String[colNum];
        Button editButton = new Button("Edit");

        values[0] = node.getNodeID();
        values[1] = "" + node.getXcoord();
        values[2] = "" + node.getYcoord();
        values[3] = "" + node.getFloor();
        values[4] = node.getBuilding();
        values[5] = node.getNodeType();
        values[6] = node.getLongName();
        values[7] = node.getShortName();

        for(int i = 0; i < colNum; i++) {
            labels[i] = new Label(values[i]);
            labels[i].setAlignment(Pos.CENTER);
            labels[i].setPrefHeight(30.0);
            databaseGrid.add(labels[i], i, rowNum);
        }

        editButton.setAlignment(Pos.CENTER);
        editButton.setId("" + values[0]);
        editButton.setOnAction(e -> goToEdit(e, editButton.getId()));
        databaseGrid.add(editButton, 8, rowNum);

        rowNum++;
        databaseGrid.setPrefHeight(rowNum * 30.0);
        databaseAnchor.setPrefHeight(rowNum * 30.0);
    }

    @FXML
    private void goToEdit(ActionEvent e, String nodeID){
        try {
            Stage stage = (Stage) databaseGrid.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editNode.fxml"));
            Parent root = fxmlLoader.load();
            editNodeController controller = fxmlLoader.getController();

            controller.setNodeID(nodeID);

            stage.setTitle("Database Editor");
            stage.setScene(new Scene(root, 1200, 800));
            stage.show();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @FXML
    private void goToDownload() {
        try {
            Stage stage = (Stage) databaseGrid.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("download.fxml"));
            Parent root = fxmlLoader.load();
            //downloadController controller = fxmlLoader.getController();

            stage.setTitle("Database Download");
            stage.setScene(new Scene(root, 1200, 800));
            stage.show();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
