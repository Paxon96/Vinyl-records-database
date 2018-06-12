package BazaDanych;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
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

public class UpdateController implements Initializable {

    public Button deleteButton;
    @FXML
    private Label isCorrectLabel;
    @FXML
    private Button saveButton;
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
    private Label wytworniaLabel;
    @FXML
    private Label krajLabel;
    @FXML
    private Label gatunekLabel;

    private Stage myParent;
    private MsSqlConnection dc;
    private String tytul = null, wykonawca = null, rok = null, rodzaj = null, wytwornia = null, kraj = null, gatunek = null;
    private String T,W,R,Ro,Wy,K,G;
    private int IDPlyty;
    private boolean isRokCorrect = true, isRodzajCorrect = true, isTytulCorrect = true, isWykonawcaCorrect = true, isConnectedToDatabase = true, isKrajCorrect = true, isWytworniaCorrect = true,  isGatunekCorrect = true;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
        dc = new MsSqlConnection();
    }


    public void setIDPlyty(int IDPlyty) {
        this.IDPlyty = IDPlyty;
    }

    public void setTytul(String tytul){
        this.tytul = tytul;
    }

    public void setWykonawca(String wykonawca){
        this.wykonawca = wykonawca;
    }

    public void setRok(String rok){
        this.rok = rok;
    }

    public void setRodzaj(String rodzaj){
        this.rodzaj = rodzaj;
    }

    public void setWytwornia(String wytwornia){
        this.wytwornia = wytwornia;
    }

    public void setKraj(String kraj){
        this.kraj = kraj;
       //krajTextField.setText(this.kraj);
        System.out.println(this.kraj);
    }

    public void setGatunek(String gatunek){
        this.gatunek = gatunek;
    }

    public void loadFromDataBase() {

        String temp = " ";
        String select = "select P.[TYTUŁ PŁYTY] as Tytuł, (W.[Nazwa wykonawcy]) as Wykonawca, (P.[ROK]) as Rok, (P.[RODZAJ PŁYTY]) as Rodzaj, (Wy.Wytwórnie) as Wytwórnia,  (K.Kraje) as Kraj,  (G.Gatunki) as Gatunek",
                join = " left join Wykonawcy as W on P.IDWykonawcy = W.IDWykonawców  left join Wytwórnie as Wy on P.IDWytwórni = Wy.IDWytwórni left join Kraje as K on P.IDKraju = K.IDKraju  left join Gatunki as G on P.IDGatunku = G.IDGatunku",
                from = " from Płyty as P ", where = " where P.[TYTUŁ PŁYTY] is not null";
        boolean flag = false, isEverythingGood = true;

        if (tytul != null) {
            flag = true;
            //String wh = " and P.[TYTUŁ PŁYTY] like '" + tytul + "'";
            String wh = " and P.[TYTUŁ PŁYTY] like ?";
            where += wh;
        }

        if (wykonawca != null) {
            //String wh = " and W.[Nazwa Wykonawcy] is not null";

            //wh += " and W.[Nazwa wykonawcy] like '" + wykonawca + "'";
            String wh = " and W.[Nazwa wykonawcy] like ?";
            where += wh;
            flag = true;
        }

        if (rok != null) {
            //String wh = " and P.[ROK] is not null";

            //wh += " and P.[ROK] like '" + rok + "'";
            String wh = " and P.[ROK] like ?";
            where += wh;
            flag = true;
        }

        if (rodzaj != null) {

            //String wh =" and P.[RODZAJ PŁYTY] is not null ";


            //wh += " and P.[RODZAJ PŁYTY] like '" + rodzaj + "'";
            String wh = " and P.[RODZAJ PŁYTY] like ?";
            where += wh;
            flag = true;
        }

        if (wytwornia != null) {

            //String wh = " and Wy.Wytwórnie is not null";
            //String j = " left join Wytwórnie as Wy on P.IDWytwórni = Wy.IDWytwórni";
            //wh += " and Wy.Wytwórnie like '" + wytwornia + "'";
            String wh = " and Wy.Wytwórnie like ?";
            where += wh;
            // join +=j;
            flag = true;
        }

        if (kraj != null) {
            //String wh = " and K.Kraje is not null";

            //String j = "left join Kraje as K on P.IDKraju = K.IDKraju ";
            //wh += " and K.Kraje like '" + kraj + "'";
            String wh = " and K.Kraje like ?";
            where += wh;
            // join += j;
            flag = true;
        }

        if (gatunek != null) {

            //String wh = " and G.Gatunki is not null ";
            //wh += " and G.Gatunki like '" + gatunek + "'";
            String wh = " and G.Gatunki like ?";
            where += wh;
            //String j = " left join Gatunki as G on P.IDGatunku = G.IDGatunku";
            flag = true;
            //join += j;
        }


        String ready = select + temp + from + join + where + " and P.IDPłyty = ?";
        System.out.println(ready);

        Connection connection = null;
        try {
            connection = dc.dbConnect();
        } catch (Exception e) {
            e.printStackTrace();
            isConnectedToDatabase = false;
        }


        if (isConnectedToDatabase) {

            int index = 1;
            PreparedStatement statemet = null;
            try {
                statemet = connection.prepareStatement(ready);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (tytul != null) {
                try {
                    assert statemet != null;
                    statemet.setString(index, tytul);
                    index++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (wykonawca != null) {
                try {
                    assert statemet != null;
                    statemet.setString(index, wykonawca);
                    index++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (rok != null) {
                try {
                    assert statemet != null;
                    statemet.setString(index, rok);
                    index++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (rodzaj != null) {
                try {
                    assert statemet != null;
                    statemet.setString(index, rodzaj);
                    index++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (wytwornia != null) {
                try {
                    assert statemet != null;
                    statemet.setString(index, wytwornia);
                    index++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (kraj != null) {
                try {
                    assert statemet != null;
                    statemet.setString(index, kraj);
                    index++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (gatunek != null) {
                try {
                    assert statemet != null;
                    statemet.setString(index, gatunek);
                    index++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            try {
                assert statemet != null;
                statemet.setInt(index, IDPlyty);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            try {
                assert statemet != null;
                ResultSet rs = statemet.executeQuery(); //connection.createStatement().executeQuery(ready); // Generować query!

                if (rs.next()) {
                    T = rs.getString(1);
                    tytulTextField.setText(T);
                    W = rs.getString(2);
                    wykonawcaTextField.setText(W);
                    R = rs.getString(3);
                    rokTextField.setText(R);
                    if (R == null)
                        R = "brak";
                    Ro = rs.getString(4);
                    rodzajTextField.setText(Ro);
                    if (Ro == null)
                        Ro = "brak";
                    Wy = rs.getString(5);
                    wytworniaTextField.setText(Wy);
                    if (Wy == null)
                        Wy = "brak";
                    K = rs.getString(6);
                    krajTextField.setText(K);
                    if (K == null)
                        K = "brak";
                    G = rs.getString(7);
                    gatunekTextField.setText(G);
                    if (G == null)
                        G = "brak";
                } else
                    System.out.println("No nie ma xd");

            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println(IDPlyty);
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

    }

    @FXML
    private void saveButtonAction(ActionEvent actionEvent) {
        if(isRokCorrect && isRodzajCorrect && isTytulCorrect && isWykonawcaCorrect && isKrajCorrect && isGatunekCorrect && isWytworniaCorrect){

            Connection connection = null;
            try {
                connection = dc.dbConnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean isUpdating = false, tytul = false, wykonawca = false, rok = false, rodzaj = false, wytwornia = false, kraj = false, gatunek = false;
            PreparedStatement updatePlyta = null;
            String update = "Update Płyty set ", where = " where IDPłyty = ? ";

            if(!T.equals(tytulTextField.getText())){
                isUpdating = true;
                String up = " [TYTUŁ PŁYTY] = replacePlace ,";

                String setToNullIfEmpty = tytulTextField.getText();
                if( setToNullIfEmpty!= null)
                    if(setToNullIfEmpty.equals(""))
                        setToNullIfEmpty = null;

                if(setToNullIfEmpty == null)
                    up = up.replace("replacePlace", "null");
                else{
                    tytul = true;
                    up = up.replace("replacePlace","?");
                }
               update += up;
            }

            if(!W.equals(wykonawcaTextField.getText())){
                isUpdating = true;
                String up = " IDWykonawcy = replacePlace ,";
                System.out.println(wykonawcaTextField.getText().length());

                String setToNullIfEmpty = wykonawcaTextField.getText();
                if( setToNullIfEmpty!= null)
                    if(setToNullIfEmpty.equals(""))
                        setToNullIfEmpty = null;

                if(setToNullIfEmpty == null)
                    up = up.replace("replacePlace","null" );
                else{
                    wykonawca = true;
                    up = up.replace("replacePlace","(select IDWykonawców from Wykonawcy where [Nazwa wykonawcy] like ?)" );
                    WykonawcaUpdate(connection);
                }
                update += up;
            }

            if(!R.equals(rokTextField.getText())){
                isUpdating = true;
                String up = " ROK = replacePlace ,";

                String setToNullIfEmpty = rokTextField.getText();
                if( setToNullIfEmpty!= null)
                    if(setToNullIfEmpty.equals(""))
                        setToNullIfEmpty = null;

                if(setToNullIfEmpty == null)
                    up = up.replace("replacePlace","null");
                else{
                    rok = true;
                    up = up.replace("replacePlace","?");
                }
                update += up;
            }

            if(!Ro.equals(rodzajTextField.getText())){
                isUpdating = true;
                String up = " [RODZAJ PŁYTY] = replacePlace ,";

                String setToNullIfEmpty = rodzajTextField.getText();
                if( setToNullIfEmpty!= null)
                    if(setToNullIfEmpty.equals(""))
                        setToNullIfEmpty = null;

                if(setToNullIfEmpty == null)
                    up = up.replace("replacePlace","null");
                else{
                    rodzaj = true;
                    up = up.replace("replacePlace","?");
                }

                update += up;
            }

            if(!Wy.equals(wytworniaTextField.getText())){
                isUpdating = true;
                String up = " IDWytwórni = replacePlace ,";

                String setToNullIfEmpty = wytworniaTextField.getText();
                if( setToNullIfEmpty!= null)
                    if(setToNullIfEmpty.equals(""))
                        setToNullIfEmpty = null;

                if(setToNullIfEmpty==null)
                    up = up.replace("replacePlace", "null");
                else {
                    wytwornia = true;
                    up = up.replace("replacePlace", " (select IDWytwórni from Wytwórnie where Wytwórnie like ? ) ");
                    WytwórniaUpdate(connection);
                }
                update += up;
            }

            if(!K.equals(krajTextField.getText())){
                isUpdating = true;
                String up = " IDKraju = replacePlace ,";

                String setToNullIfEmpty = krajTextField.getText();
                if( setToNullIfEmpty!= null)
                    if(setToNullIfEmpty.equals(""))
                        setToNullIfEmpty = null;

                if(setToNullIfEmpty == null)
                    up = up.replace("replacePlace","null");
                else{
                    kraj = true;
                    up = up.replace("replacePlace", " (select IDKraju from Kraje where Kraje like ?) ");
                    KrajUpdate(connection);
                }
                update += up;
            }

            if(!G.equals(gatunekTextField.getText())){
                isUpdating = true;
                String up = " IDGatunku = replacePlace ,";

                String setToNullIfEmpty = gatunekTextField.getText();
                if( setToNullIfEmpty!= null)
                    if(setToNullIfEmpty.equals(""))
                        setToNullIfEmpty = null;

                if(setToNullIfEmpty == null)
                    up = up.replace("replacePlace", "null");
                else{
                    gatunek = true;
                    up = up.replace("replacePlace", " (select IDGatunku from Gatunki where Gatunki like ?) ");
                    GatunekUpdate(connection);
                }
                update += up;
            }

            if(isUpdating){
                int index = 1;
                update = update.substring(0,update.length() - 1);
                update +=where;
                System.out.println(update);

                try {
                    updatePlyta = connection.prepareStatement(update);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if(tytul) {
                    try {
                        System.out.println("T");
                        updatePlyta.setString(index,tytulTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(wykonawca){
                    try {
                        System.out.println("W");
                        updatePlyta.setString(index,wykonawcaTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(rok){
                    try {
                        System.out.println("R");
                        updatePlyta.setString(index,rokTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(rodzaj){
                    try {
                        System.out.println("Ro");
                        updatePlyta.setString(index,rodzajTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(wytwornia){
                    try {
                        System.out.println("Wy");
                        updatePlyta.setString(index,wytworniaTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(kraj){
                    try {
                        System.out.println("K");
                        updatePlyta.setString(index,krajTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(gatunek){
                    try {
                        System.out.println("G");
                        updatePlyta.setString(index,gatunekTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    System.out.println("ID");
                    updatePlyta.setInt(index,IDPlyty);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    updatePlyta.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            Node source = (Node)  actionEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
        }else{
            isCorrectLabel.setText("Popraw dane!");
        }
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

    private void GatunekUpdate(Connection connection){
        UpdateIfNotInBase(connection, "Wchodzę do gatunek!!", " Select IDGatunku from Gatunki where Gatunki like ? ", gatunekTextField.getText(), "Insert into Gatunki values(?)");
    }

    private void KrajUpdate(Connection connection) {
        UpdateIfNotInBase(connection, "Wchodzę do kraju!!", " Select IDKraju from Kraje where Kraje like ? ", krajTextField.getText(), "Insert into Kraje values(?)");
    }

    private void WytwórniaUpdate(Connection connection) {
        UpdateIfNotInBase(connection, "Wchodzę do wytwórni!!", "Select IDWytwórni from Wytwórnie where Wytwórnie like ? ", wytworniaTextField.getText(), "Insert into Wytwórnie values(?)");
    }

    private void WykonawcaUpdate(Connection connection) {
        UpdateIfNotInBase(connection, "Wchodzę do wykonawcy!!", "Select IDWykonawców from Wykonawcy where [Nazwa wykonawcy] like ? ", wykonawcaTextField.getText(), "Insert into Wykonawcy values(?)");
    }

    public void tytulKeyTypedAction(KeyEvent keyEvent) {
        if(tytulTextField.getText().length() == 0){
            isTytulCorrect = false;
            tytulLabel.textProperty().set("Tytuł nie może być pusty!");
        }else if(tytulTextField.getText().length()>0){
            System.out.println("Jestem");
            StringChecker s = new StringChecker(tytulTextField.getText());
            if(!s.stringCheck()){
                tytulLabel.setText("Błędne znaki");
                isTytulCorrect = false;
            }else
                tytulLabel.setText("");
        }
        else{
            isTytulCorrect = true;
            tytulLabel.textProperty().set("");
        }

    }

    public void wykonawcaKeyTypedAction(KeyEvent keyEvent) {
        if(wykonawcaTextField.getText().length() == 0){
            isWykonawcaCorrect = false;
            wykonawcaLabel.textProperty().set("Wykonawca nie może być pusty!");
        }else if(wykonawcaTextField.getText().length()>0){
            StringChecker s = new StringChecker(wykonawcaTextField.getText());
            if(!s.stringCheck()){
                wykonawcaLabel.setText("Błędne znaki");
                isWykonawcaCorrect = false;
            }else
                wykonawcaLabel.setText("");
        }
        else{
            isWykonawcaCorrect = true;
            wykonawcaLabel.textProperty().set("");
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
            }
        }else{
            isRokCorrect = true;
            rokLabel.textProperty().set("");
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
            }
        }else{
            isRodzajCorrect = true;
            rodzajLabel.textProperty().set("");
        }
    }

    public void wytworniaKeyTypedAction(KeyEvent keyEvent) {
     if(wytworniaTextField.getText().length()>0){
            StringChecker s = new StringChecker(wytworniaTextField.getText());
            if(!s.stringCheck()){
                wytworniaLabel.setText("Błędne znaki");
                isWytworniaCorrect = false;
            }else
                wytworniaLabel.setText("");
        }
        else{
         isWytworniaCorrect = true;
         wytworniaLabel.textProperty().set("");
        }
    }

    public void krajKeyTypedAction(KeyEvent keyEvent) {
        if(krajTextField.getText().length()>0){
            StringChecker s = new StringChecker(krajTextField.getText());
            if(!s.stringCheck()){
                krajLabel.setText("Błędne znaki");
                isKrajCorrect = false;
            }else
                krajLabel.setText("");
        }
        else{
            isKrajCorrect = true;
            krajLabel.textProperty().set("");
        }
    }

    public void gatunekKeyTypedAction(KeyEvent keyEvent) {
        if(gatunekTextField.getText().length()>0){
            StringChecker s = new StringChecker(gatunekTextField.getText());
            if(!s.stringCheck()){
                gatunekLabel.setText("Błędne znaki");
                isGatunekCorrect = false;
            }else
                gatunekLabel.setText("");
        }
        else{
            isGatunekCorrect = true;
            gatunekLabel.textProperty().set("");
        }
    }

    public void cancelButtonAction(ActionEvent actionEvent) {
        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void deleteButtonAction(ActionEvent actionEvent) {

        FXMLLoader deleteController = new FXMLLoader();
        deleteController.setLocation(getClass().getResource("scenes/DeleteMessage.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(deleteController.load(), 300, 200);
        } catch (IOException e) {
            e.printStackTrace();
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }

        Stage stage = new Stage();
        stage.setTitle("Usuwanie " + tytul);
        stage.setAlwaysOnTop(true);
        stage.initOwner(this.myParent);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image("BazaDanych/icons/error.png"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        DelereMessageController dmc = deleteController.getController();
        dmc.setTytul(tytul);
        dmc.setIDPlyty(IDPlyty);

        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Node source = (Node)  actionEvent.getSource();
                Stage stage2  = (Stage) source.getScene().getWindow();
                stage2.close();
            }
        });


    }

}
