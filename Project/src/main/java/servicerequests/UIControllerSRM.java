package servicerequests;

import application.UIController;
import com.jfoenix.controls.JFXTextField;
import entities.ServiceRequest;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;

/**
 * The UIController for the making and sending of service requests
 * @author Jonathan Chang, imoralessirgo
 * @verion iteration1
 */
public class UIControllerSRM extends UIController {

    @FXML
    private JFXButton ITButton;

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

    /**
     * Redirects to IT servicerequests
     */
    @FXML
    public void setITButton() {
        UIControllerSRIT controller = (UIControllerSRIT) this.goToScene(UIController.SERVICE_REQUEST_IT);
        controller.setServiceType("IT");
    }


    @FXML
    public void setFlowerButton(ActionEvent actionEvent)
    {
        UIControllerSRFD controller = (UIControllerSRFD) this.goToScene(UIController.SERVICE_REQUEST_FLOWER_DELIVERY);
        controller.setServiceType("Flower Delivery");
    }

    @FXML
    public void setSanitationButton() {
        UIControllerSRSA controller = (UIControllerSRSA) this.goToScene(UIController.SERVICE_REQUEST_SANITATION);
        controller.setServiceType("Sanitation");
    }
    @FXML
    public void setReligiousServices(){
        UIControllerSRRS controller = (UIControllerSRRS) this.goToScene(UIController.SERVICE_REQUEST_RELIGIOUS_SERVICES);
        controller.setServiceType("Religious Services");
    }

    @FXML
    public void setInterpreterButton() {
        UIControllerSRIN controller = (UIControllerSRIN) this.goToScene(UIController.SERVICE_REQUEST_INTERPRETER);
        controller.setServiceType("Interpreter");
    }

    @FXML
    public void setTransportButton() {
        UIControllerSRET controller = (UIControllerSRET) this.goToScene(UIController.SERVICE_REQUEST_TRANSPORT);
        controller.setServiceType("Transport");
    }


    /**
     * Redirects to BabysittingServiceRequest
     */

    @FXML
    public void setbabysittingButton() {
        UIControllerSRB controller = (UIControllerSRB) this.goToScene(UIController.SERVICE_REQUEST_BABYSITTING);
        controller.setServiceType("BABYSITTING");
    }

    @FXML
    public void setPrescriptionServicesButton() {
        UIControllerSRPS controller = (UIControllerSRPS) this.goToScene(UIController.SERVICE_REQUEST_PRESCRIPTION_SERVICES_MAIN);
        controller.setServiceType("Prescription Services");
    }



    @FXML
    public void setSecurityButton() {
        UIControllerSRSecurity controller = (UIControllerSRSecurity) this.goToScene(UIController.SERVICE_REQUEST_SECURITY);
        controller.setServiceType("SECURITY");
    }

    @FXML
    private void avButton(ActionEvent actionEvent) {
        UIControllerSRAVE controller = (UIControllerSRAVE) this.goToScene(UIController.SERVICE_REQUEST_AV_EQUIPMENT);
        controller.setServiceType("Audio Visual");
    }
}
