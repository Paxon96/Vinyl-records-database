package BazaDanych;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

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

    private MsSqlConnection dc;
    private String tytul = null, wykonawca = null, rok = null, rodzaj = null, wytwornia = null, kraj = null, gatunek = null;
    private String T,W,R,Ro,Wy,K,G;
    private int IDPlyty;
    private boolean isRokCorrect = true, isRodzajCorrect = true, isTytulCorrect = true, isWykonawcaCorrect = true;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
        dc = new MsSqlConnection();
    }

    public void setTytul(String tytul){
        this.tytul = tytul;
       // tytulTextField.setText(this.tytul);
        System.out.println(this.tytul);
    }

    public void setWykonawca(String wykonawca){
        this.wykonawca = wykonawca;
       // wykonawcaTextField.setText(this.wykonawca);
        System.out.println(this.wykonawca);
    }

    public void setRok(String rok){
        this.rok = rok;
       // rokTextField.setText(this.rok);
        System.out.println(this.rok);
    }

    public void setRodzaj(String rodzaj){
        this.rodzaj = rodzaj;
        //rodzajTextField.setText(this.rodzaj);
        System.out.println(this.rodzaj);
    }

    public void setWytwornia(String wytwornia){
        this.wytwornia = wytwornia;
        //wytworniaTextField.setText(this.wytwornia);
        System.out.println(this.wytwornia);
    }

    public void setKraj(String kraj){
        this.kraj = kraj;
       //krajTextField.setText(this.kraj);
        System.out.println(this.kraj);
    }

    public void setGatunek(String gatunek){
        this.gatunek = gatunek;
        //gatunekTextField.setText(this.gatunek);
        System.out.println(this.gatunek);
    }

    public void loadFromDataBase(){

        String temp = " ";
        String select = "select P.IDPłyty, P.[TYTUŁ PŁYTY] as Tytuł, (W.[Nazwa wykonawcy]) as Wykonawca, (P.[ROK]) as Rok, (P.[RODZAJ PŁYTY]) as Rodzaj, (Wy.Wytwórnie) as Wytwórnia,  (K.Kraje) as Kraj,  (G.Gatunki) as Gatunek",
                join = " left join Wykonawcy as W on P.IDWykonawcy = W.IDWykonawców  left join Wytwórnie as Wy on P.IDWytwórni = Wy.IDWytwórni left join Kraje as K on P.IDKraju = K.IDKraju  left join Gatunki as G on P.IDGatunku = G.IDGatunku",
                from = " from Płyty as P ", where =" where P.[TYTUŁ PŁYTY] is not null ";
        boolean flag = false, isEverythingGood = true;

        if(tytul != null){
            flag = true;
            //String wh = " and P.[TYTUŁ PŁYTY] like '" + tytul + "'";
            String wh = " and P.[TYTUŁ PŁYTY] like ?";
            where += wh;
        }

        if(wykonawca != null){
            //String wh = " and W.[Nazwa Wykonawcy] is not null";

           //wh += " and W.[Nazwa wykonawcy] like '" + wykonawca + "'";
            String wh = " and W.[Nazwa wykonawcy] like ?";
            where += wh;
            flag = true;
        }

        if(rok != null){
            //String wh = " and P.[ROK] is not null";

            //wh += " and P.[ROK] like '" + rok + "'";
            String wh = " and P.[ROK] like ?";
            where += wh;
            flag = true;
        }

        if(rodzaj != null){

            //String wh =" and P.[RODZAJ PŁYTY] is not null ";


            //wh += " and P.[RODZAJ PŁYTY] like '" + rodzaj + "'";
            String wh = " and P.[RODZAJ PŁYTY] like ?";
            where += wh;
            flag = true;
        }

        if(wytwornia != null){

            //String wh = " and Wy.Wytwórnie is not null";
            //String j = " left join Wytwórnie as Wy on P.IDWytwórni = Wy.IDWytwórni";
            //wh += " and Wy.Wytwórnie like '" + wytwornia + "'";
            String wh = " and Wy.Wytwórnie like ?";
            where += wh;
           // join +=j;
            flag = true;
        }

        if(kraj != null){
            //String wh = " and K.Kraje is not null";

            //String j = "left join Kraje as K on P.IDKraju = K.IDKraju ";
            //wh += " and K.Kraje like '" + kraj + "'";
            String wh = " and K.Kraje like ?";
            where += wh;
           // join += j;
            flag = true;
        }

        if(gatunek != null){

            //String wh = " and G.Gatunki is not null ";
            //wh += " and G.Gatunki like '" + gatunek + "'";
            String wh = " and G.Gatunki like ?";
            where += wh;
            //String j = " left join Gatunki as G on P.IDGatunku = G.IDGatunku";
            flag = true;
            //join += j;
        }


        String ready = select + temp + from + join+ where;
        System.out.println(ready);

        Connection connection = dc.dbConnect();

        int index = 1;
        PreparedStatement statemet = null;
        try {
            statemet = connection.prepareStatement(ready) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(tytul!=null){
            try {
                assert statemet != null;
                statemet.setString(index,tytul);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(wykonawca != null){
            try {
                assert statemet != null;
                statemet.setString(index,wykonawca);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(rok!=null){
            try {
                assert statemet != null;
                statemet.setString(index,rok);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(rodzaj != null){
            try {
                assert statemet != null;
                statemet.setString(index,rodzaj);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(wytwornia != null){
            try {
                assert statemet != null;
                statemet.setString(index,wytwornia);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(kraj != null){
            try {
                assert statemet != null;
                statemet.setString(index,kraj);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(gatunek != null){
            try {
                assert statemet != null;
                statemet.setString(index,gatunek);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            assert statemet != null;
            ResultSet rs = statemet.executeQuery(); //connection.createStatement().executeQuery(ready); // Generować query!

            if(rs.next()) {
                IDPlyty = rs.getInt(1);
                T = rs.getString(2);
                tytulTextField.setText(T);
                W = rs.getString(3);
                wykonawcaTextField.setText(W);
                R = rs.getString(4);
                rokTextField.setText(R);
                if(R == null)
                    R = "brak";
                Ro = rs.getString(5);
                rodzajTextField.setText(Ro);
                if(Ro == null)
                    Ro = "brak";
                Wy = rs.getString(6);
                wytworniaTextField.setText(Wy);
                if(Wy == null)
                    Wy = "brak";
                K = rs.getString(7);
                krajTextField.setText(K);
                if(K == null)
                    K = "brak";
                G = rs.getString(8);
                gatunekTextField.setText(G);
                if(G == null)
                    G = "brak";
            }else
                System.out.println("No nie ma xd");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void saveButtonAction(ActionEvent actionEvent) {
        if(isRokCorrect && isRodzajCorrect && isTytulCorrect && isWykonawcaCorrect){
            Connection connection = dc.dbConnect();
            boolean isUpdating = false, tytul = false, wykonawca = false, rok = false, rodzaj = false, wytwornia = false, kraj = false, gatunek = false;
            PreparedStatement updatePlyta = null;
            String update = "Update Płyty set ", where = " where IDPłyty = ? ";

            //if(T!= null)
            if(!T.equals(tytulTextField.getText())){
                isUpdating = true;
                String up = " [TYTUŁ PŁYTY] = replacePlace ,";

                String setToNullIfEmpty = tytulTextField.getText();
                System.out.println(tytulTextField.getText());
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

            //if(W!= null)
            if(!W.equals(wykonawcaTextField.getText())){
                isUpdating = true;
                String up = " IDWykonawcy = replacePlace ,";
                System.out.println(wykonawcaTextField.getText().length());

                String setToNullIfEmpty = wykonawcaTextField.getText();
                System.out.println(wykonawcaTextField.getText());
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

            //if(R != null)
            if(!R.equals(rokTextField.getText())){
                isUpdating = true;
                String up = " ROK = replacePlace ,";

                String setToNullIfEmpty = rokTextField.getText();
                System.out.println(rokTextField.getText());
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

            //if(Ro!=null)
            if(!Ro.equals(rodzajTextField.getText())){
                isUpdating = true;
                String up = " [RODZAJ PŁYTY] = replacePlace ,";

                String setToNullIfEmpty = rodzajTextField.getText();
                System.out.println(rodzajTextField.getText());
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

            //if(Wy!=null)
            if(!Wy.equals(wytworniaTextField.getText())){
                isUpdating = true;
                String up = " IDWytwórni = replacePlace ,";




                String setToNullIfEmpty = wytworniaTextField.getText();

                System.out.println(wytworniaTextField.getText());

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

            //if(K!=null)
            if(!K.equals(krajTextField.getText())){
                isUpdating = true;
                String up = " IDKraju = replacePlace ,";

                String setToNullIfEmpty = krajTextField.getText();
                System.out.println(krajTextField.getText());
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

            //if(G!=null)
            if(!G.equals(gatunekTextField.getText())){
                isUpdating = true;
                String up = " IDGatunku = replacePlace ,";

                String setToNullIfEmpty = gatunekTextField.getText();
                System.out.println(gatunekTextField.getText());
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

               // if(T!=null)
                if(tytul/*!T.equals(tytulTextField.getText()) && tytulTextField.getText() != null*/) {
                    try {
                        System.out.println("T");
                        updatePlyta.setString(index,tytulTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

               // if(W!=null)
                System.out.println(W.equals(wykonawcaTextField.getText()));
                System.out.println( wytworniaTextField.getText() != null);
                if(wykonawca){
                    try {
                        System.out.println("W");
                        updatePlyta.setString(index,wykonawcaTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

               // if(R != null)
                if(rok/*!R.equals(rokTextField.getText()) && rokTextField.getText() != null*/){
                    try {
                        System.out.println("R");
                        updatePlyta.setString(index,rokTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

              //  if(Ro !=null)
                if(rodzaj/*!Ro.equals(rodzajTextField.getText()) && rodzajTextField.getText() != null*/){
                    try {
                        System.out.println("Ro");
                        updatePlyta.setString(index,rodzajTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

               // if(Wy != null)
                if(wytwornia/*wytworniaTextField.getText().length() > 0*/){
                    try {
                        System.out.println("Wy");
                        updatePlyta.setString(index,wytworniaTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

              //  if(K != null)
                if(kraj/*!K.equals(krajTextField.getText()) && krajTextField.getText() != null*/){
                    try {
                        System.out.println("K");
                        updatePlyta.setString(index,krajTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

              //  if(G!=null)
                if(gatunek/*!G.equals(gatunekTextField.getText()) && gatunekTextField.getText() != null*/){
                    try {
                        System.out.println("G");
                        updatePlyta.setString(index,gatunekTextField.getText());
                        index++;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                try {
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

    private void UpdateIfNotItBase(Connection connection, String s, String s2, String text, String s3) {
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
        UpdateIfNotItBase(connection, "Wchodzę do gatunek!!", " Select IDGatunku from Gatunki where Gatunki like ? ", gatunekTextField.getText(), "Insert into Gatunki values(?)");
    }

    private void KrajUpdate(Connection connection) {
        UpdateIfNotItBase(connection, "Wchodzę do kraju!!", " Select IDKraju from Kraje where Kraje like ? ", krajTextField.getText(), "Insert into Kraje values(?)");
    }

    private void WytwórniaUpdate(Connection connection) {
        UpdateIfNotItBase(connection, "Wchodzę do wytwórni!!", "Select IDWytwórni from Wytwórnie where Wytwórnie like ? ", wytworniaTextField.getText(), "Insert into Wytwórnie values(?)");
    }

    private void WykonawcaUpdate(Connection connection) {
        UpdateIfNotItBase(connection, "Wchodzę do wykonawcy!!", "Select IDWykonawców from Wykonawcy where [Nazwa wykonawcy] like ? ", wykonawcaTextField.getText(), "Insert into Wykonawcy values(?)");
    }

    public void tytulKeyTypedAction(KeyEvent keyEvent) {
        if(tytulTextField.getText().length() == 0){
            isTytulCorrect = false;
            tytulLabel.textProperty().set("Tytuł nie może być pusty!");
        }else{
            isTytulCorrect = true;
            tytulLabel.textProperty().set("");
        }

    }

    public void wykonawcaKeyTypedAction(KeyEvent keyEvent) {
        if(wykonawcaTextField.getText().length() == 0){
            isWykonawcaCorrect = false;
            wykonawcaLabel.textProperty().set("Wykonawca nie może być pusty!");
        }else{
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
        }
    }

    public void wytworniaKeyTypedAction(KeyEvent keyEvent) {
    }

    public void krajKeyTypedAction(KeyEvent keyEvent) {
    }

    public void gatunekKeyTypedAction(KeyEvent keyEvent) {
    }

    public void cancelButtonAction(ActionEvent actionEvent) {
        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
