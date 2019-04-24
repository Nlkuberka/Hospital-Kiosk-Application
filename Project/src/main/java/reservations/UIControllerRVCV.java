package reservations;

import application.CurrentUser;
import application.UIController;
import com.calendarfx.model.*;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DayView;
import com.calendarfx.view.page.DayPage;
import database.DBController;
import database.DBControllerRW;
import entities.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;


public class UIControllerRVCV extends UIController {

    Calendar CL001 = new Calendar("Classroom 1");
    Calendar CL002 = new Calendar("Classroom 2");
    Calendar CL003 = new Calendar("Classroom 3");
    Calendar CL004 = new Calendar("Classroom 4");
    Calendar CL005 = new Calendar("Classroom 5");
    Calendar CL006 = new Calendar("Classroom 6");
    Calendar CL007 = new Calendar("Classroom 7");
    Calendar CL008 = new Calendar("Classroom 8");
    Calendar CL009 = new Calendar("Classroom 9");
    Calendar MHA = new Calendar("Mission Hill Auditorium");
    Calendar MHCR = new Calendar("Mission Hill Conference Room");
    Calendar PNTRY = new Calendar("Pantry");

    CalendarSource workplaceCal = new CalendarSource("Workplaces");


    @FXML
    CalendarView cv;


    /**
     * Run when the scene is first loaded
     */
    @FXML
    public void initialize() {
        CL001.setStyle(Calendar.Style.STYLE1);
        CL002.setStyle(Calendar.Style.STYLE2);
        CL003.setStyle(Calendar.Style.STYLE3);
        CL004.setStyle(Calendar.Style.STYLE4);
        CL005.setStyle(Calendar.Style.STYLE5);
        CL006.setStyle(Calendar.Style.STYLE6);
        CL007.setStyle(Calendar.Style.STYLE7);
        CL008.setStyle(Calendar.Style.STYLE1);
        CL009.setStyle(Calendar.Style.STYLE4);
        MHA.setStyle(Calendar.Style.STYLE5);
        MHCR.setStyle(Calendar.Style.STYLE6);
        PNTRY.setStyle(Calendar.Style.STYLE7);


        Connection conn = DBController.dbConnect();
        for(Entry e : DBControllerRW.getEntriesforRoom("CL001",conn)) { CL001.addEntry(e); }
        for(Entry e : DBControllerRW.getEntriesforRoom("CL002",conn)) { CL002.addEntry(e); }
        for(Entry e : DBControllerRW.getEntriesforRoom("CL003",conn)) { CL003.addEntry(e); }
        for(Entry e : DBControllerRW.getEntriesforRoom("CL004",conn)) { CL004.addEntry(e); }
        for(Entry e : DBControllerRW.getEntriesforRoom("CL005",conn)) { CL005.addEntry(e); }
        for(Entry e : DBControllerRW.getEntriesforRoom("CL006",conn)) { CL006.addEntry(e); }
        for(Entry e : DBControllerRW.getEntriesforRoom("CL007",conn)) { CL007.addEntry(e); }
        for(Entry e : DBControllerRW.getEntriesforRoom("CL008",conn)) { CL008.addEntry(e); }
        for(Entry e : DBControllerRW.getEntriesforRoom("CL009",conn)) { CL009.addEntry(e); }
        for(Entry e : DBControllerRW.getEntriesforRoom("MHA001",conn)) { MHA.addEntry(e); }
        for(Entry e : DBControllerRW.getEntriesforRoom("MHCR001",conn)) { MHCR.addEntry(e); }
        for(Entry e : DBControllerRW.getEntriesforRoom("PNTRY",conn)) { PNTRY.addEntry(e); }

        cv.setShowPrintButton(false);
        cv.setShowAddCalendarButton(false);
        CL001.setReadOnly(true);
        CL002.setReadOnly(true);
        CL003.setReadOnly(true);
        CL004.setReadOnly(true);
        CL005.setReadOnly(true);
        CL006.setReadOnly(true);
        CL007.setReadOnly(true);
        CL008.setReadOnly(true);
        CL009.setReadOnly(true);
        MHA.setReadOnly(true);
        MHCR.setReadOnly(true);
        PNTRY.setReadOnly(true);



        workplaceCal.getCalendars().addAll(CL001,CL002,CL003,CL004,CL005,CL006,CL007,CL008,CL009,MHA,MHCR,PNTRY);

        cv.getCalendarSources().set(0, workplaceCal);

    }

    /**
     * Loads in the rooms each time the scene is show
     */
    @Override
    public void onShow() {
        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        cv.setToday(LocalDate.now());
                        cv.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
    }


    @FXML
    private void setBackButton() {
        UIController controller = CurrentUser.user.getPermissions() == User.ADMIN_PERMISSIONS ? this.goToScene(UIController.ADMIN_RESERVATION_MAIN) : this.goToScene(UIController.RESERVATIONS_MAIN_MENU);
    }
}
