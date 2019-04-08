package servicerequests;

import application.UIController;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;

/**
 * The UIController for the making and sending of service requests
 * @author Jonathan Chang, imoralessirgo
 * @verion iteration1
 */
public class UIControllerSRM extends UIController {

    @FXML
    private JFXButton baseButton;

    /**
     * Runs on the scene creation and adds the various service request types
     */
    @FXML
    public void initialize() {

    }

    /**
     * Runs whenever the scene is show and gets the node names for the choicebox
     */
    @Override
    public void onShow() {

    }

    @FXML
    public void setBaseButton() {
        UIControllerSRBase controller = (UIControllerSRBase) this.goToScene(UIController.SERVICE_REQUEST_BASE);
        controller.setServiceType("BASE");
    }

    @FXML
    public void setTransportButton() {
        UIControllerSRBase controller = (UIControllerSRBase) this.goToScene(UIController.SERVICE_REQUEST_TRANSPORT);
        controller.setServiceType("Transport");
    }
}
