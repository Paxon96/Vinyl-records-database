package BazaDanych;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ErrorController implements Initializable {

    @FXML
    private Button errorButton;
    @FXML
    private Label errorLabel1;
    @FXML
    private Label errorLabel2;

    private String errorMessage = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel1.setText("Żeby edytować płytę musisz wybrać ");
        errorLabel2.setText("pole TYTUŁ oraz WYKONAWCA");
        errorLabel1.setTextAlignment(TextAlignment.CENTER);
        errorLabel2.setTextAlignment(TextAlignment.CENTER);
    }

    public void errorButtonAction(ActionEvent actionEvent) {
        Node  source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
