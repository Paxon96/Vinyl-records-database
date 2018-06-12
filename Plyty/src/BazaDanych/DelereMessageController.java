package BazaDanych;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DelereMessageController implements Initializable{

    @FXML
    private Label errorLabel1;
    @FXML
    private Label errorLabel2;
    @FXML
    private Button noButton;
    @FXML
    private Button yesButton;

    private MsSqlConnection dc;
    private String  tytul = null;
    private int IDPlyty;
    private Stage myParent;

    public void setTytul(String tytul) {
        this.tytul = tytul;
        errorLabel2.setText(this.tytul + " ?");
        errorLabel2.setTextAlignment(TextAlignment.CENTER);
    }

    public void setIDPlyty(int IDPlyty) {
        this.IDPlyty = IDPlyty;
    }

    public DelereMessageController getController(){
        return DelereMessageController.this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel1.setText("Czy chcesz usunąć");
        errorLabel1.setTextAlignment(TextAlignment.CENTER);
        dc = new MsSqlConnection();
    }


    public void yesButtonAction(ActionEvent actionEvent) {
        boolean isConnected = true;
        Connection connection = null;
        try {
            connection = dc.dbConnect();
        } catch (Exception e) {
            e.printStackTrace();
            isConnected = false;
        }

        if(isConnected){

            String query = "Delete Płyty where IDPłyty = ?";

            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                statement.setInt(1,IDPlyty);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Node source = (Node)  actionEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();

        }else{
            FXMLLoader noConnectionError = new FXMLLoader();
            noConnectionError.setLocation(getClass().getResource("scenes/NoConnectionError.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(noConnectionError.load(), 300, 200);
            } catch (IOException e) {
                e.printStackTrace();
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
            Stage stage = new Stage();
            stage.setTitle("ERROR");
            stage.setAlwaysOnTop(true);
            stage.initOwner(this.myParent);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("BazaDanych/icons/error.png"));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            Node source = (Node) actionEvent.getSource();
            Stage stage2  = (Stage) source.getScene().getWindow();
            stage2.close();
        }
    }

    public void noButtonAction(ActionEvent actionEvent) {
        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }


}
