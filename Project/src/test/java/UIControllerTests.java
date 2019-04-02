import javafx.embed.swing.JFXPanel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UIControllerTests {
    UIController controller;
    Node node;
    ServiceRequest serviceRequest;
    Label label;
    TextField textField;

    @Before
    public void beforeActions() {
        new JFXPanel();
        controller = new UIController();
        node = new Node("testNodeID", 123, 456, "FLR1", "BLD2", "NodeTypeTest", "LongName1", "ShortName2");
        serviceRequest = new ServiceRequest("testNodeID", "Maintence", "message", "userID", false, "You");
        label = new Label("TEST");
        textField = new TextField("TEXTTEXTFIELD");
    }

    @Test
    public void stringGetterTests() {
        controller.runStringGetter(node, "getNodeID", label);
        Assert.assertEquals("testNodeID", label.getText());
    }

    @Test
    public void stringGetterEditableTests() {
        controller.runStringGetterEditable(node, "getNodeID", label, textField);
        Assert.assertEquals("testNodeID", label.getText());
        Assert.assertEquals("testNodeID", textField.getText());
    }

    @Test
    public void runSetterTests() {
        controller.runSetter(node, "setNodeID", String.class, "TESTNID");
        controller.runSetter(node, "setXcoord", int.class, 987);
        controller.runSetter(serviceRequest, "setResolved", boolean.class, true);
        Assert.assertEquals("TESTNID", node.getNodeID());
        Assert.assertEquals(987, node.getXcoord());
        Assert.assertEquals(true, serviceRequest.isResolved());
    }
}
