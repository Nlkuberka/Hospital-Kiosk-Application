package tests;

import entities.Reservation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Runs Tests on the Reservation object
 */
public class ReservationTests {
    Reservation reservation;
    List<Reservation> reservations;

    /**
     * Initialize the reservations list
     */
    @Before
    public void beforeTests() {
        reservation = new Reservation();
        reservations = new LinkedList<Reservation>();
        reservations.add(getReservation("09:00:00", "11:00:00"));
        reservations.add(getReservation("15:00:00", "18:00:00"));
    }

    /**
     * Tests to ensure that the isValid function is working
     */
    @Test
    public void testReservationIsValid() {
        // Reservation is entirely before any reservation in the list
        reservation = getReservation("07:00:00", "08:00:00");
        Assert.assertEquals(true, reservation.isValid(reservations));

        // Reservation is between two reservations in the list
        reservation = getReservation("12:00:00", "14:00:00");
        Assert.assertEquals(true, reservation.isValid(reservations));

        // Reservation is entirely after any reservation in the list
        reservation = getReservation("20:00:00", "22:00:00");
        Assert.assertEquals(true, reservation.isValid(reservations));

        // Reservation endTime conflicts with another Reservation
        reservation = getReservation("07:00:00", "10:00:00");
        Assert.assertEquals(false, reservation.isValid(reservations));

        // Reservation startTime conflicts with another Reservation
        reservation = getReservation("16:00:00", "23:00:00");
        Assert.assertEquals(false, reservation.isValid(reservations));

        // Reservation is entirely within another Reservation
        reservation = getReservation("10:00:00", "10:30:00");
        Assert.assertEquals(false, reservation.isValid(reservations));

        // Reservation encloses another Reservation
        reservation = getReservation("13:00:00", "22:00:00");
        Assert.assertEquals(false, reservation.isValid(reservations));
    }

    private Reservation getReservation(String startTime, String endTime) {
        return new Reservation("NODEID", "USERID", "2019-04-06", startTime, endTime);
    }

    private LocalTime getLocalTime(String time) {
        return LocalTime.parse(time);
    }
}
