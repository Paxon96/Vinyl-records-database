package BazaDanych;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1110, 800));
        primaryStage.getIcons().add(new Image("BazaDanych/icons/icon.png"));
        primaryStage.setMinWidth(1110);
        primaryStage.setMinHeight(800);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
