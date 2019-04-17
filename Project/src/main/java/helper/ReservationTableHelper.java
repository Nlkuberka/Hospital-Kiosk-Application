package helper;

import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import database.DBController;
import database.DBControllerRW;
import entities.Reservation;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.*;
import reservations.UIControllerRVM;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationTableHelper extends UIController{
    private static final String[] reservationSetters  = {"", "setWkplaceID", "setUserID", "setDate", "setStartTime", "setEndTime"};
    private static final String[] reservationGetters  = {"getRsvID", "getWkplaceID", "getUserID", "getDate", "getStartTime", "getEndTime"};

    private TableView<Reservation> table;
    private TableColumn<Reservation, Reservation> reservationIDColumn;
    private TableColumn<Reservation, Reservation> workplaceIDColumn;
    private TableColumn<Reservation, Reservation> userIDColumn;
    private TableColumn<Reservation, Reservation> dateColumn;
    private TableColumn<Reservation, Reservation> startTimeColumn;
    private TableColumn<Reservation, Reservation> endTimeColumn;
    private TableColumn<Reservation, Reservation> removeColumn;

    public ReservationTableHelper(TableView<Reservation> table, TableColumn<Reservation, Reservation> reservationIDColumn, TableColumn<Reservation, Reservation> workplaceIDColumn, TableColumn<Reservation, Reservation> userIDColumn, TableColumn<Reservation, Reservation> dateColumn, TableColumn<Reservation, Reservation> startTimeColumn, TableColumn<Reservation, Reservation> endTimeColumn, TableColumn<Reservation, Reservation> removeColumn) {
        this.table = table;
        this.reservationIDColumn = reservationIDColumn;
        this.workplaceIDColumn = workplaceIDColumn;
        this.userIDColumn = userIDColumn;
        this.dateColumn = dateColumn;
        this.startTimeColumn = startTimeColumn;
        this.endTimeColumn = endTimeColumn;
        this.removeColumn = removeColumn;
        setupColumns();
    }

    private void setupColumns() {
        reservationIDColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        reservationIDColumn.setCellFactory(param -> new TableCell<Reservation, Reservation>() {
            private Label label = new Label();
            private int index = 0; //RSVID is last field in Reservation

            @Override
            protected  void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                runStringGetter(reservation, reservationGetters[index], label);
                setGraphic(label);
            }
        });

        workplaceIDColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        workplaceIDColumn.setCellFactory(param -> new TableCell<Reservation, Reservation>() {
            private Label label = new Label();
            private int index = 1; //RSVID is last field in Reservation

            @Override
            protected  void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                runStringGetter(reservation, reservationGetters[index], label);
                setGraphic(label);
            }
        });

        userIDColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        userIDColumn.setCellFactory(param -> new TableCell<Reservation, Reservation>() {
            private Label label = new Label();
            private int index = 2; //RSVID is last field in Reservation

            @Override
            protected  void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                runStringGetter(reservation, reservationGetters[index], label);
                setGraphic(label);
            }
        });

        dateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        dateColumn.setCellFactory(param -> new TableCell<Reservation, Reservation>() {
            private JFXDatePicker datePicker = new JFXDatePicker();
            private JFXDatePicker oldDatePicker = new JFXDatePicker();
            private Label label = new Label();

            private int index = 3;

            @Override
            protected  void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                if(reservation == null) {
                    setGraphic(null);
                    return;
                }
                runStringGetter(reservation, reservationGetters[index], label);
                oldDatePicker.setValue(LocalDate.parse(label.getText()));
                datePicker.setValue(oldDatePicker.getValue());
                datePicker.setOnAction(param -> {
                    if(!checkReservation(datePicker, null, null, reservation)) {
                        datePicker.setValue(oldDatePicker.getValue());
                        return;
                    }

                    runSetter(reservation, reservationSetters[index], String.class, UIControllerRVM.getDateString(datePicker));
                    oldDatePicker.setValue(datePicker.getValue());

                    Connection conn = DBController.dbConnect();
                    DBControllerRW.addReservation(reservation, conn);
                    dateColumn.getTableView().getItems().add(reservation);
                    DBController.closeConnection(conn);
                });
                setGraphic(datePicker);
            }
        });

        startTimeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        startTimeColumn.setCellFactory(param -> new TableCell<Reservation, Reservation>() {
            private JFXTimePicker timePicker = new JFXTimePicker();
            private JFXTimePicker oldPicker = new JFXTimePicker();
            private Label label = new Label();

            private int index = 4;

            @Override
            protected  void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                if(reservation == null) {
                    setGraphic(null);
                    return;
                }
                runStringGetter(reservation, reservationGetters[index], label);
                oldPicker.setValue(LocalTime.parse(label.getText()));
                timePicker.setValue(oldPicker.getValue());
                timePicker.setOnAction(param -> {
                    if(!checkReservation(null, timePicker, null, reservation)) {
                        timePicker.setValue(oldPicker.getValue());
                        return;
                    }
                    runSetter(reservation, reservationSetters[index], String.class, UIControllerRVM.getTimeString(timePicker));
                    oldPicker.setValue(timePicker.getValue());

                    Connection conn = DBController.dbConnect();
                    DBControllerRW.addReservation(reservation, conn);
                    startTimeColumn.getTableView().getItems().add(reservation);
                    DBController.closeConnection(conn);
                });
                setGraphic(timePicker);
            }
        });

        endTimeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        endTimeColumn.setCellFactory(param -> new TableCell<Reservation, Reservation>() {
            private JFXTimePicker timePicker = new JFXTimePicker();
            private JFXTimePicker oldPicker = new JFXTimePicker();
            private Label label = new Label();

            private int index = 5;

            @Override
            protected  void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                if(reservation == null) {
                    setGraphic(null);
                    return;
                }
                runStringGetter(reservation, reservationGetters[index], label);
                oldPicker.setValue(LocalTime.parse(label.getText()));
                timePicker.setValue(oldPicker.getValue());
                timePicker.setOnAction(param -> {
                    if(!checkReservation(null, null, timePicker, reservation)) {
                        timePicker.setValue(oldPicker.getValue());
                        return;
                    }

                    runSetter(reservation, reservationSetters[index], String.class, UIControllerRVM.getTimeString(timePicker));
                    oldPicker.setValue(timePicker.getValue());

                    Connection conn = DBController.dbConnect();
                    DBControllerRW.addReservation(reservation, conn);
                    endTimeColumn.getTableView().getItems().add(reservation);
                    DBController.closeConnection(conn);
                });
                setGraphic(timePicker);
            }
        });

        // Initialize cell factories of the remove rsv column
        removeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        removeColumn.setCellFactory(param -> new TableCell<Reservation, Reservation>() {
            private JFXButton removeButton = new JFXButton("Cancel");

            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                if(reservation == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(removeButton);
                removeButton.setOnAction( e -> {
                    Connection conn = DBController.dbConnect();
                    DBControllerRW.deleteReservation(reservation.getRsvID(), conn);
                    DBController.closeConnection(conn);
                    getTableView().getItems().remove(reservation);
                });
            }
        });
    }

    private boolean checkReservation(DatePicker datePicker, JFXTimePicker startTimePicker, JFXTimePicker endTimePicker, Reservation reservation) {
        if(datePicker == null) {
            datePicker = new DatePicker((LocalDate.parse(reservation.getDate())));
        }
        if(startTimePicker == null) {
            startTimePicker = new JFXTimePicker(LocalTime.parse(reservation.getStartTime()));
        }
        if(endTimePicker == null) {
            endTimePicker = new JFXTimePicker(LocalTime.parse(reservation.getEndTime()));
        }

        if(!UIControllerRVM.checkValidReservation(datePicker, startTimePicker, endTimePicker)) {
            popupMessage("Invalid Reservation", true);
            return false;
        }

        /*Connection conn = DBController.dbConnect();
        DBControllerRW.deleteReservation(reservation.getRsvID(), conn);
        removeColumn.getTableView().getItems().remove(reservation);
        Reservation r = new Reservation(reservation.getWkplaceID(), reservation.getUserID(), UIControllerRVM.getDateString(datePicker), UIControllerRVM.getTimeString(startTimePicker), UIControllerRVM.getTimeString(endTimePicker));

        List<Reservation> rs = DBControllerRW.generateListofUserReservations("ADMIN00001", conn);
        System.out.println(r);

        if(!DBControllerRW.isRoomAvailableString(r.getWkplaceID(), r.getDate(), r.getStartTime(), r.getEndTime(), conn)) {
            popupMessage("Reservation conflicts with another.", true);
            removeColumn.getTableView().getItems().add(reservation);
            DBControllerRW.addReservation(reservation, conn);
            DBController.closeConnection(conn);
            return false;
        }
        DBController.closeConnection(conn);*/
        return true;
    }
}
