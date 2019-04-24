package pathfinding;

import application.UIController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import entities.Direction;
import entities.Graph;
import entities.emailDirection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class UIControllerPUD extends UIController {

    public static final String ACCOUNT_SID = "AC176f9cd821ffa8dcad559ceecad9ecf1";
    public static final String AUTH_TOKEN = "ab7f1a58335f47c98bac61b471920dfe";
    String returnText;
    List<List<List<Direction>>> path = new LinkedList<>();
    List<String> addedPath = new LinkedList<>();
    String sceneString = "POPUP_DIRECTIONS";
    @FXML
    private TextArea directions; //the actual directions
    @FXML
    private JFXButton printDirections; //the option to print a receipt
    @FXML
    private JFXButton textDirections; //the option to text the directions to a cell phone
    @FXML
    private JFXButton emailDirections; //the option to email the directions
    @FXML
    private ScrollPane directionsBox; //gives the ability to sroll with directionsS
    @FXML
    private TextField phoneNumber; //the prompt box for a phone number
    @FXML
    private TextField email; //the prompt box for an email
    @FXML
    private JFXButton okButton; //ok button to exit confirmation window
    @FXML
    private JFXComboBox<String> floorSelect; //drop down to select which floor's directions to display

    @FXML
    public void initialize(){
        floorSelect.getItems().addAll("All", "4", "3", "2", "1", "G", "L1", "L2");
    }
    /**
     * Populates the object's linked list
     * @param message the input path
     */
    void populateDirections(List<List<List<Direction>>> message){
        this.path = message;
        convertMessage(floorSelect.getValue());
        setDirections();
    }

    public List<String> getAddedPath() {
        return addedPath;
    }

    void setAddedPath(List<String> addedPath) {
        this.addedPath = Graph.getGraph().allTextDirections(addedPath);
    }

    /**
     * converts the input path to a single string
     * @param selectedFloor the selected floor to view directions
     */
    void convertMessage(String selectedFloor){
        this.returnText = "";
        System.out.println(selectedFloor);
        if(selectedFloor == null || selectedFloor.equals("All")) {
            //if the selected floor is All, print all directions
            for (int l = 0; l < this.addedPath.size() - 1; l++) {
                this.returnText += this.addedPath.get(l);
            }
        }

        for(int i = 0; i < this.path.size(); i++){
            for(int j = 0; j < this.path.get(i).size(); j++){
                for(int k = 0; k < this.path.get(i).get(j).size(); k++) {
                    if (this.path.get(i).get(j).get(k).getFloor().equals(selectedFloor)) {
                        //if the selected floor equals the floor of the given direction
                        this.returnText += this.path.get(i).get(j).get(k).getDirection();
                    }
                }
            }
        }

        if(this.returnText.equals("")){
            this.returnText = "There are no paths on this floor";
        }

    }

    @FXML
    public void setDirections(){
        directions.setText(this.returnText); //  sets the text received from the pathfinding
    }

    @FXML
    public void selectFloor(){ //changes the displayed directions based on floor
        String selectedFloor = floorSelect.getValue();
        convertMessage(selectedFloor);
        setDirections();
    }

    @FXML
    public void sendEmail(ActionEvent event) {
        if (!isValidEmail(email.getText())) {
            popupMessage("Please Enter A Valid Email", true);
        } else {
            String receivingEmail = email.getText();
            emailDirection.sendEmail(directions.getText(), receivingEmail);
            popupMessage("An email has been sent to you!", false);
        }
    }

    @FXML
    public void sendText(ActionEvent event) {
        String number = phoneNumber.getText();

        if (!number.matches("\\d{10}")) {
            popupMessage("Please Enter A Valid Phone Number", true);

        } else {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message.creator(
                    new com.twilio.type.PhoneNumber("+1" + number),
                    new com.twilio.type.PhoneNumber("+17472290044"),
                    directions.getText())
                    .create();

            popupMessage("A text message has been sent to you!", false);
        }

    }

    @FXML
    public void setOkButton() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    private static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
