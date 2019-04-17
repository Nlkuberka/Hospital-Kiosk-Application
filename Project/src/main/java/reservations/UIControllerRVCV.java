package reservations;

import application.UIController;
import com.calendarfx.model.*;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DayView;
import com.calendarfx.view.page.DayPage;
import database.DBController;
import database.DBControllerRW;
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

        //Entry e = new Entry<>("Hello",new Interval(LocalDate.of(2019,04,19),LocalTime.of(11,00,00),LocalDate.of(2019,04,19),LocalTime.of(18,00,00)));
        //CL002.addEntry(e);
        
        workplaceCal.getCalendars().addAll(CL001,CL002,CL003,CL004,CL005,CL006,CL007,CL008,CL009,MHA,MHCR,PNTRY);

        for(Calendar c : workplaceCal.getCalendars()){
            System.out.println("y");
            System.out.println(c.findEntries("Hello"));
        }
        //cv = new CalendarView();

        //cv.getCalendarSources().addAll(workplaceCal);
        cv.getCalendarSources().set(0, workplaceCal);
        for(CalendarSource cs : cv.getCalendarSources()){
            System.out.println("f");
        }

        if(cv.getDayPage().getDayPageLayout() == null){
            System.out.println("null");
        }
    }

    /**
     * Loads in the rooms each time the scene is show
     */
    @Override
    public void onShow() {

    }


    public void setBackMenuItem(ActionEvent actionEvent) {
    }
}
