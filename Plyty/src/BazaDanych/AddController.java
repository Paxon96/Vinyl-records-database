package BazaDanych;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddController implements Initializable {

    @FXML
    private Label wytworniaLabel;
    @FXML
    private Label krajLabel;
    @FXML
    private Label gatunekLabel;
    @FXML
    private Button backButton;
    @FXML
    private TextField tytulTextField;
    @FXML
    private TextField wykonawcaTextField;
    @FXML
    private TextField rokTextField;
    @FXML
    private TextField rodzajTextField;
    @FXML
    private TextField wytworniaTextField;
    @FXML
    private TextField krajTextField;
    @FXML
    private TextField gatunekTextField;
    @FXML
    private Label tytulLabel;
    @FXML
    private Label wykonawcaLabel;
    @FXML
    private Label rokLabel;
    @FXML
    private Label rodzajLabel;
    @FXML
    private Button addButton;
    @FXML
    private Label isCorrectLabel;
    @FXML
    private Button cancelButton;

    private boolean isRokCorrect = true, isRodzajCorrect = true, isTytulCorrect = false, isWykonawcaCorrect = false, isConnectedToDatabase = true,  isKrajCorrect = true, isWytworniaCorrect = true,  isGatunekCorrect = true;
    private MsSqlConnection dc;
    private Stage myParent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dc = new MsSqlConnection();
        wykonawcaLabel.textProperty().set("Wykonawca nie może być pusty!");
        tytulLabel.textProperty().set("Tytuł nie może być pusty!");
    }



    public void addButtonAction(ActionEvent actionEvent) {
        System.out.println("Rok " + isRokCorrect);

        if(isTytulCorrect && isRodzajCorrect && isWykonawcaCorrect && isRokCorrect && isKrajCorrect && isWytworniaCorrect && isGatunekCorrect){

            boolean insertWykonawca = false, insertWytwornia = false, insertKraj = false, insertGatunek = false, isAddGood = true;

            Connection connection = null;

            try {
                connection = dc.dbConnect();
            } catch (Exception e) {
                e.printStackTrace();
                isConnectedToDatabase = false;
            }

            if(isConnectedToDatabase){
                String query = "Insert into Płyty values(?,?,?,replaceWykonawca,replaceWytwornia,replaceKraj,replaceGatunek)";

                if(wykonawcaTextField.getText().length() > 0){
                    query = query.replace("replaceWykonawca"," (select IDWykonawców from Wykonawcy where [Nazwa wykonawcy] like ?) ");
                    insertWykonawca = true;
                }

                if(wytworniaTextField.getText().length() > 0){
                    query = query.replace("replaceWytwornia"," (select IDWytwórni from Wytwórnie where Wytwórnie like ? ) ");
                    insertWytwornia = true;
                }else{
                    query = query.replace("replaceWytwornia"," null ");
                }

                if(krajTextField.getText().length() > 0){
                    query = query.replace("replaceKraj"," (select IDKraju from Kraje where Kraje like ?) ");
                    insertKraj = true;
                }else{
                    query = query.replace("replaceKraj"," null ");
                }

                if(gatunekTextField.getText().length() > 0){
                    query = query.replace("replaceGatunek"," (select IDGatunku from Gatunki where Gatunki like ?) ");
                    insertGatunek = true;
                }else{
                    query = query.replace("replaceGatunek"," null ");
                }
                PreparedStatement statement = null;

                System.out.println(query);
                try {
                    statement = connection.prepareStatement(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    statement.setString(1,tytulTextField.getText());
                    statement.setString(2,rodzajTextField.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if(rokTextField.getText().length() >0){
                    try {
                        statement.setString(3,rokTextField.getText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        statement.setString(3,null);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                int index = 4;
                if(insertWykonawca){
                    WykonawcaInsert(connection);
                    try {
                        statement.setString(index,wykonawcaTextField.getText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        statement.setString(index,null);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (insertWytwornia) {
                    index ++;
                    WytwórniaInsert(connection);
                    try {
                        statement.setString(index,wytworniaTextField.getText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                /*else{
                    try {
                        statement.setString(index,"null");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }*/



                if(insertKraj){
                    index++;
                    KrajInsert(connection);
                    try {
                        statement.setString(index,krajTextField.getText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                /*else{
                    try {
                        statement.setString(index,"null");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }*/

                

                if(insertGatunek){
                    index ++;
                    GatunekInsert(connection);
                    try {
                        statement.setString(index,gatunekTextField.getText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                /*else{
                    try {
                        statement.setString(index,"null");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }*/

                try {
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    isAddGood = false;
                }

                if(isAddGood){
                    FXMLLoader errorController = new FXMLLoader();
                    errorController.setLocation(getClass().getResource("scenes/PlytaDodana.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(errorController.load(), 300, 200);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Logger logger = Logger.getLogger(getClass().getName());
                        logger.log(Level.SEVERE, "Failed to create new Window.", e);
                    }
                    Stage stage = new Stage();
                    stage.setTitle("Sukces!");
                    stage.setAlwaysOnTop(true);
                    stage.initOwner(this.myParent);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.getIcons().add(new Image("BazaDanych/icons/tick.png"));
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();

                    stage.setOnHidden(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            tytulTextField.clear();
                            wykonawcaTextField.clear();
                            rokTextField.clear();
                            rodzajTextField.clear();
                            wytworniaTextField.clear();
                            krajTextField.clear();
                            gatunekTextField.clear();
                            wykonawcaLabel.textProperty().set("Wykonawca nie może być pusty!");
                            tytulLabel.textProperty().set("Tytuł nie może być pusty!");
                        }
                    });
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
                }

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

            }

        }else
            isCorrectLabel.setText("Popraw dane!");

    }

    public void cancelButtonAction(ActionEvent actionEvent) {
        backButton.fire();
    }


    public void tytulKeyTypedAction(KeyEvent keyEvent) {
        if(tytulTextField.getText().length() == 0){
            isTytulCorrect = false;
            tytulLabel.textProperty().set("Tytuł nie może być pusty!");
        }else if(tytulTextField.getText().length()>0) {
            System.out.println("Jestem");
            StringChecker s = new StringChecker(tytulTextField.getText());
            if (!s.stringCheck()) {
                tytulLabel.setText("Błędne znaki");
                isTytulCorrect = false;
            } else{
                tytulLabel.setText("");
                isTytulCorrect = true;
                isCorrectLabel.setText("");
            }

        }
            else{
            isTytulCorrect = true;
            tytulLabel.textProperty().set("");
            isCorrectLabel.setText("");
        }
    }

    public void wykonawcaKeyTypedAction(KeyEvent keyEvent) {
        if(wykonawcaTextField.getText().length() == 0){
            isWykonawcaCorrect = false;
            wykonawcaLabel.textProperty().set("Wykonawca nie może być pusty!");
        }else if(wykonawcaTextField.getText().length()>0) {
            System.out.println("Jestem");
            StringChecker s = new StringChecker(wykonawcaTextField.getText());
            if (!s.stringCheck()) {
                wykonawcaLabel.setText("Błędne znaki");
                isWykonawcaCorrect = false;
            } else{
                wykonawcaLabel.setText("");
                isWykonawcaCorrect = true;
                isCorrectLabel.setText("");
            }

        }else{
            isWykonawcaCorrect = true;
            wykonawcaLabel.textProperty().set("");
            isCorrectLabel.setText("");
        }
    }

    public void rokKeyTypedAction(KeyEvent keyEvent) {
        if (rokTextField.getText().equalsIgnoreCase(""))
            rokLabel.textProperty().set("");

        if (rokTextField.getText().length() > 0) {
            NumberChecker checkNumber = new NumberChecker(rokTextField.getText());
            System.out.println(checkNumber.stringCheck());
            if (!checkNumber.stringCheck() || rokTextField.getText().length() != 4 || Integer.parseInt(rokTextField.getText()) > Year.now().getValue() || Integer.parseInt(rokTextField.getText()) < 1800){
                isRokCorrect = false;
                rokLabel.textProperty().set("Błędny rok");
            }
            else{
                isRokCorrect = true;
                rokLabel.textProperty().set("");
                isCorrectLabel.setText("");
            }
        }else{
            isRokCorrect = true;
            rokLabel.textProperty().set("");
            isCorrectLabel.setText("");
        }

    }


    public void rodzajKeyTypedAction(KeyEvent keyEvent) {
        if (rodzajTextField.getText().equalsIgnoreCase(""))
            rodzajLabel.textProperty().set("");

        if(rodzajTextField.getText().length() > 0){
            if((!rodzajTextField.getText().equalsIgnoreCase("cd") && !rodzajTextField.getText().equalsIgnoreCase("winyl"))){
                rodzajLabel.textProperty().set("Tylko CD lub WINYL");
                isRodzajCorrect = false;
            }
            else{
                isRodzajCorrect = true;
                rodzajLabel.textProperty().set("");
                isCorrectLabel.setText("");
            }
        }else{
            isRodzajCorrect = true;
            rodzajLabel.textProperty().set("");
            isCorrectLabel.setText("");
        }
    }

    public void wytworniaKeyTypedAction(KeyEvent keyEvent) {
        if(wytworniaTextField.getText().length() == 0){
            isWytworniaCorrect = true;
            isCorrectLabel.setText("");
            wytworniaLabel.setText("");
        }
        else if(wytworniaTextField.getText().length()>0) {
            StringChecker s = new StringChecker(wytworniaTextField.getText());
            if (!s.stringCheck()) {
                wytworniaLabel.setText("Błędne znaki");
                 isWytworniaCorrect= false;
            } else{
                wytworniaLabel.setText("");
                isWytworniaCorrect = true;
                isCorrectLabel.setText("");
            }
        }
    }

    public void krajKeyTypedAction(KeyEvent keyEvent) {
        if(krajTextField.getText().length() == 0){
            isKrajCorrect = true;
            isCorrectLabel.setText("");
            krajLabel.setText("");
        }
        else if(krajTextField.getText().length()>0) {
            StringChecker s = new StringChecker(krajTextField.getText());
            if (!s.stringCheck()) {
                krajLabel.setText("Błędne znaki");
                isKrajCorrect= false;
            } else{
                krajLabel.setText("");
                isKrajCorrect = true;
                isCorrectLabel.setText("");
            }
        }
    }

    public void gatunekKeyTypedAction(KeyEvent keyEvent) {
        if(gatunekTextField.getText().length() == 0){
            isGatunekCorrect = true;
            isCorrectLabel.setText("");
            gatunekLabel.setText("");
        }
        else if(gatunekTextField.getText().length()>0) {
            StringChecker s = new StringChecker(gatunekTextField.getText());
            if (!s.stringCheck()) {
                gatunekLabel.setText("Błędne znaki");
                isGatunekCorrect= false;
            } else{
                gatunekLabel.setText("");
                isGatunekCorrect = true;
                isCorrectLabel.setText("");
            }
        }
    }

    public void backButtonAction(ActionEvent actionEvent) {
        Parent window3 = null;
        try {
            window3 = (AnchorPane)FXMLLoader.load(getClass().getResource("scenes/MainScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene newScene;
        newScene = new Scene(window3,650,400);

        Stage mainWindow;
        mainWindow = (Stage)  ((Node)actionEvent.getSource()).getScene().getWindow();
        mainWindow.setMinWidth(500);
        mainWindow.setMinHeight(300);
        mainWindow.setTitle("Main");
        mainWindow.setScene(newScene);
    }

    private void UpdateIfNotInBase(Connection connection, String s, String s2, String text, String s3) {
        boolean isExist = false;
        System.out.println(s);
        String temp = s2;
        PreparedStatement czyJestTakiGatunek = null;

        try {
            czyJestTakiGatunek = connection.prepareStatement(temp);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            czyJestTakiGatunek.setString(1, text);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet rs = czyJestTakiGatunek.executeQuery();
            while (rs.next())
                isExist = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!isExist) {
            String query = s3;

            PreparedStatement insertGatunek = null;

            try {
                insertGatunek = connection.prepareStatement(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                insertGatunek.setString(1, text);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                insertGatunek.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void GatunekInsert(Connection connection){
        UpdateIfNotInBase(connection, "Wchodzę do gatunek!!", " Select IDGatunku from Gatunki where Gatunki like ? ", gatunekTextField.getText(), "Insert into Gatunki values(?)");
    }

    private void KrajInsert(Connection connection) {
        UpdateIfNotInBase(connection, "Wchodzę do kraju!!", " Select IDKraju from Kraje where Kraje like ? ", krajTextField.getText(), "Insert into Kraje values(?)");
    }

    private void WytwórniaInsert(Connection connection) {
        UpdateIfNotInBase(connection, "Wchodzę do wytwórni!!", "Select IDWytwórni from Wytwórnie where Wytwórnie like ? ", wytworniaTextField.getText(), "Insert into Wytwórnie values(?)");
    }

    private void WykonawcaInsert(Connection connection) {
        UpdateIfNotInBase(connection, "Wchodzę do wykonawcy!!", "Select IDWykonawców from Wykonawcy where [Nazwa wykonawcy] like ? ", wykonawcaTextField.getText(), "Insert into Wykonawcy values(?)");
    }
}
