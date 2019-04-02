import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoginTests {
    @Before
    public void beforeActions() {
        System.out.println("I'm Working");
    }

    @Test
    public void basicTest() {
        Assert.assertEquals(5, 5);
        //Assert.assertEquals(1, 5);
    }
}
