package BazaDanych;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public Button addButton;
    public Button searchButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addButtonAction(ActionEvent actionEvent) {

        Parent window3 = null;
        try {
            window3 = (AnchorPane)FXMLLoader.load(getClass().getResource("scenes/AddScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene;
        newScene = new Scene(window3,600,600);
        Stage mainWindow;
        mainWindow = (Stage)  ((Node)actionEvent.getSource()).getScene().getWindow();
        mainWindow.setTitle("Dodawanie");
        mainWindow.setScene(newScene);
    }

    public void searchButtonAction(ActionEvent actionEvent) {
        Parent window3 = null; //we need to load the layout that we want to swap
        try {
            window3 = (AnchorPane)FXMLLoader.load(getClass().getResource("scenes/search.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene newScene; //then we create a new scene with our new layout
        newScene = new Scene(window3,1100,600);
        Stage mainWindow; //Here is the magic. We get the reference to main Stage.

        mainWindow = (Stage)  ((Node)actionEvent.getSource()).getScene().getWindow();
        mainWindow.setTitle("Wyszukiwanie");
        mainWindow.setScene(newScene);
    }
}
