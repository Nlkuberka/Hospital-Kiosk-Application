package tests;

import entities.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class UserBitTests {
    User user;
    List<String> serviceRequests;

    boolean[] bits4095 = {true, true, true, true, true, true, true, true, true, true, true, true};
    boolean[] bits2820 = {true, false, true, true, false, false, false, false, false, true, false, false};
    boolean[] bits1024 = {false, true, false, false, false, false, false, false, false, false, false, false};

    @Before
    public void beforeEach() {
        user = new User();
        serviceRequests = new LinkedList<String>(Arrays.asList(User.serviceRequests));
    }

    @Test
    public void convertToBitsTests() {
        // Test Admin Permissions and all service Requests
        user.setPermissionsNumber(4095);
        boolean[] bits = user.getBitArray(user.getPermissionsNumber());
        Assert.assertArrayEquals(bits4095, bits);
        Assert.assertEquals(user.ADMIN_PERMISSIONS, user.getPermissions());

        // Test User Permissions and some service Requests
        user.setPermissionsNumber(2820);
        bits = user.getBitArray(user.getPermissionsNumber());
        Assert.assertArrayEquals(bits, bits2820);
        Assert.assertEquals(user.BASIC_PERMISSIONS, user.getPermissions());

        // Test Guest Permissions with no service Requests
        user.setPermissionsNumber(1024);
        bits = user.getBitArray(user.getPermissionsNumber());
        Assert.assertArrayEquals(bits1024, bits);
        Assert.assertEquals(user.GUEST_PERMISSIONS, user.getPermissions());

    }

    @Test
    public void serviceRequestTests() {
        // Test admin with all service requests
        user.setPermissionsNumber(4095);
        boolean[] bits = user.getBitArray(user.getPermissionsNumber());
        Assert.assertEquals(serviceRequests, user.getServiceRequestFullfillment());

        serviceRequests.remove("Prescription Services");
        serviceRequests.remove("Flower Delivery");
        serviceRequests.remove("IT Services");
        serviceRequests.remove("Sanitation");
        serviceRequests.remove("External Transportation");
        serviceRequests.remove("Babysitter");
        serviceRequests.remove("Security");

        // Test user with add and removes
        user.setPermissionsNumber(2820);
        Assert.assertEquals(serviceRequests, user.getServiceRequestFullfillment());

        user.removeServiceRequest("Audio Visual");
        user.addServiceRequest("Security");
        user.addServiceRequest("Babysitter");
        serviceRequests.remove("Audio Visual");
        serviceRequests.add("Babysitter");
        serviceRequests.add("Security");
        Assert.assertEquals(serviceRequests, user.getServiceRequestFullfillment());

        serviceRequests = new LinkedList<>();

        // Test Guest with none
        user.setPermissionsNumber(1024);
        Assert.assertEquals(serviceRequests, user.getServiceRequestFullfillment());
    }
}
