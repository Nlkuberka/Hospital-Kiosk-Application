package tests;


import com.jfoenix.controls.JFXComboBox;
import helper.RoomCategoryFilterHelper;
import javafx.embed.swing.JFXPanel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FilterTests {
    private JFXComboBox comboBox;
    private RoomCategoryFilterHelper filter;
    private String input;

    @Before
    public void beforeEach() {
        new JFXPanel();
        comboBox = new JFXComboBox();
        filter = new RoomCategoryFilterHelper(comboBox, null, true);
    }

    @Test
    public void categoryTests() {
        Assert.assertTrue(filter.showNode("Restroom 1", input = ""));
        Assert.assertTrue(filter.showNode("Parking Garage L2", input = "Lower Level 2"));
        Assert.assertTrue(filter.showNode("Restroom L Elevator Floor L1", input = "Lower Level 1"));
        Assert.assertTrue(filter.showNode("Information Desk", input = "Ground Floor"));
        Assert.assertTrue(filter.showNode("Asthma Research Floor 1", input = "First Floor"));
        Assert.assertTrue(filter.showNode("Vending Machine Floor 2?", input = "Second Floor"));
        Assert.assertTrue(filter.showNode("Restroom Node 12 Floor 3", input = "Third Floor"));

        Assert.assertTrue(filter.showNode("Parking Garage L2", input = "Level"));
        Assert.assertTrue(filter.showNode("Restroom L Elevator Floor L1", input = "Level"));

        Assert.assertTrue(filter.showNode("Information Desk", input = "Floor"));
        Assert.assertTrue(filter.showNode("Asthma Research Floor 1", input = "Floor"));
        Assert.assertTrue(filter.showNode("Vending Machine Floor 2?", input = "Floor"));
        Assert.assertTrue(filter.showNode("Restroom Node 12 Floor 3", input = "Floor"));
    }

    @Test
    public void bathroomAndRestroomTests() {
        Assert.assertTrue(filter.showNode("Restroom Node 12 Floor 3", input = "Restroom"));
        Assert.assertTrue(filter.showNode("Restroom Node 12 Floor 3", input = "Rest"));
        Assert.assertTrue(filter.showNode("Restroom Node 12 Floor 3", input = "Bathroom"));
        Assert.assertTrue(filter.showNode("Restroom Node 12 Floor 3", input = "bath"));
    }

    @Test
    public void multipleTagTests() {
        Assert.assertTrue(filter.showNode("Restroom Node 12 Floor 3", input = "restroom third"));
        Assert.assertFalse(filter.showNode("Bathroom 1 Tower Floor 2", input = "restroom third"));
    }
}
