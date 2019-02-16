package gui;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
/**
 * Class controls PopUp.fxml which shows text on popup window
 * @author Kelvin
 *
 */
public class PopUp implements Initializable {
	String popUp;
    @FXML private Label text;
    @FXML private Button closeButton;
    @FXML
    void close(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();    		
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		text.setText(null);
	}
	/**
	 * 
	 * @param input is set for popUp variable
	 */
	public void setPopUp(String input) {
		popUp = input;
	}
	/**
	 * Method displays text on the popup window
	 */
	public void setText() {
		text.setText(popUp);
	}

}

