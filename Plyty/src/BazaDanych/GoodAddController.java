package BazaDanych;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GoodAddController implements Initializable {
    @FXML
    private Button errorButton;
    @FXML
    private Label errorLabel1;
    @FXML
    private Label errorLabel2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel1.setText("Udało się dodać płytę!");
    }


    public void errorButtonAction(ActionEvent actionEvent) {
        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
