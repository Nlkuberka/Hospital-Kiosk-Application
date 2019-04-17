package reservations;

import application.UIController;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.DayView;
import database.DBController;
import database.DBControllerRW;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;


import com.calendarfx.model.Calendar;

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
    DayView dayView;

    /**
     * Run when the scene is first loaded
     */
    @FXML
    public void initialize() {
//        Connection conn = DBController.dbConnect();
//        for(Entry e : DBControllerRW.getEntriesforRoom("CL001",conn)) { CL001.addEntries(e); }
//        for(Entry e : DBControllerRW.getEntriesforRoom("CL002",conn)) { CL002.addEntries(e); }
//        for(Entry e : DBControllerRW.getEntriesforRoom("CL003",conn)) { CL003.addEntries(e); }
//        for(Entry e : DBControllerRW.getEntriesforRoom("CL004",conn)) { CL004.addEntries(e); }
//        for(Entry e : DBControllerRW.getEntriesforRoom("CL005",conn)) { CL005.addEntries(e); }
//        for(Entry e : DBControllerRW.getEntriesforRoom("CL006",conn)) { CL006.addEntries(e); }
//        for(Entry e : DBControllerRW.getEntriesforRoom("CL007",conn)) { CL007.addEntries(e); }
//        for(Entry e : DBControllerRW.getEntriesforRoom("CL008",conn)) { CL008.addEntries(e); }
//        for(Entry e : DBControllerRW.getEntriesforRoom("CL009",conn)) { CL009.addEntries(e); }
//        for(Entry e : DBControllerRW.getEntriesforRoom("MHA001",conn)) { MHA.addEntries(e); }
//        for(Entry e : DBControllerRW.getEntriesforRoom("MHCR001",conn)) { MHCR.addEntries(e); }
//        for(Entry e : DBControllerRW.getEntriesforRoom("PNTRY",conn)) { PNTRY.addEntries(e); }
//
//        workplaceCal.getCalendars().addAll(CL001,CL002,CL003,CL004,CL005,CL006,CL007,CL008,CL009,MHA,MHCR,PNTRY);
//        Platform.runLater(()->{
//            dayView.setToday(LocalDate.now());
//            dayView.setTime(LocalTime.now());
//        });

        dayView.setCalendarVisibility(CL001,true);
    }

    /**
     * Loads in the rooms each time the scene is show
     */
    @Override
    public void onShow() {

    }



}
