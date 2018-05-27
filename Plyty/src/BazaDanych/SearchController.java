package BazaDanych;

import java.awt.*;
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

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.table.JTableHeader;

import static javafx.scene.paint.Color.*;

public class SearchController implements Initializable {

    @FXML
    private Button updateButton;
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
    // @FXML
    // private TextField kompozytorTextField;
    @FXML
    private Label wykonawcaLabel;
    @FXML
    private Label tytulLabel;
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
    //@FXML
    // private Label kompozytorLabel;
    @FXML
    private TextField tytulTextField;
    @FXML
    private Label reaserchAmount;
    @FXML
    private CheckBox rodzajCheckBox;
    @FXML
    private CheckBox wytworniaCheckBox;
    @FXML
    private CheckBox krajCheckBox;
    @FXML
    private CheckBox gatunekCheckBox;
    @FXML
    private CheckBox kompozytorCheckBox;
    @FXML
    private CheckBox tytulCheckBox;
    @FXML
    private CheckBox wykonawcaCheckBox;
    @FXML
    private CheckBox rokCheckBox;
    @FXML
    private TableView<ObservableList> table = new TableView<>();
    @FXML
    private Button dbLoad;

    private Stage myParent;
    private ObservableList<ObservableList> data;
    private MsSqlConnection dc;
    private boolean tytulCB = false, wykonawcaCB = false, rokCB = false, rodzajCB = false, wytworniaCB = false, krajCB = false, gatunekCB = false;// kompozytorCB = false;
    private int count = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
        dc = new MsSqlConnection();
        table.setPlaceholder(new Label("Wybierz dane ktore chcesz zobaczyc i nacisnij 'Load Database'"));
    }

    @FXML
    private void loadDataFromDatabase(ActionEvent event) {
        Connection connection = dc.dbConnect();
        String temp = " ";
        String select = "select ", from = " from Płyty as P ", where = " where P.[TYTUŁ PŁYTY] is not null ";
        boolean flag = false, isEverythingGood = true;

        table.getItems().clear();
        table.getColumns().clear();
        count = 0;

        if (tytulCB) {
            String tytul = "P.[TYTUŁ PŁYTY] as Tytuł";
            temp = temp + tytul;
            flag = true;
           /* try {
                String a = tytulTextField.getText();
                PreparedStatement wh = connection.prepareStatement(" and P.[TYTUŁ PŁYTY] like ?");
                wh.setString(1,a);
                System.out.println(wh);

            } catch (SQLException e) {
                e.printStackTrace();
            }*/
            if (tytulTextField.getText().length() > 0) {
/*                StringChecker toCheck = new StringChecker(tytulTextField.getText());
                if(toCheck.stringCheck()){
                String wh = " and P.[TYTUŁ PŁYTY] like '" + tytulTextField.getText() + "%'";
                    where += wh;
                }
                else
                    isEverythingGood = false;*/
                String wh = " and P.[TYTUŁ PŁYTY] like ?";
                where += wh;
            }

        }

        if (wykonawcaCB) {
            String wykonawca = " distinct (W.[Nazwa wykonawcy]) as Wykonawca ";
            String join = " left join Wykonawcy as W on P.IDWykonawcy = W.IDWykonawców ";
            String wh = " and W.[Nazwa Wykonawcy] is not null";

            if (flag) {
                wykonawca = wykonawca.replace("distinct", "");
                temp += ",";
            }

            if (wykonawcaTextField.getText().length() > 0) {
/*                StringChecker toCheck = new StringChecker(wykonawcaTextField.getText());
                if(toCheck.stringCheck())
                wh += " and W.[Nazwa wykonawcy] like '" + wykonawcaTextField.getText() + "%'";
                else
                    isEverythingGood = false;*/
                wh += " and W.[Nazwa wykonawcy] like ?";
            }

            temp += wykonawca;
            from += join;
            where += wh;
            flag = true;
        }

        if (rokCB) {
            String rok = " distinct (P.[ROK]) as Rok ";
            String wh = "";// = " and P.[ROK] is not null";

            if (flag) {
                rok = rok.replace("distinct", "");
                temp += ",";
            }

            if (rokTextField.getText().length() > 0) {
/*                NumberChecker toCheck = new NumberChecker(rokTextField.getText());
                if(toCheck.stringCheck() && rokTextField.getText().length() <= 4 &&  Integer.parseInt(rokTextField.getText()) <= Year.now().getValue())
                wh += " and P.[ROK] like '" + rokTextField.getText() + "%'";
                else
                    isEverythingGood = false;*/
                wh += " and P.[ROK] like ?";
            }


            temp += rok;
            where += wh;
            flag = true;
        }

        if (rodzajCB) {
            String rodzaj = " distinct(P.[RODZAJ PŁYTY]) as Rodzaj ";
            String wh = "";//" and P.[RODZAJ PŁYTY] is not null ";

            if (flag) {
                rodzaj = rodzaj.replace("distinct", "");
                temp += ",";
            }

            if (rodzajTextField.getText().length() > 0) {
/*                StringChecker toCheck = new StringChecker(rodzajTextField.getText());
                if(*//*toCheck.stringCheck() && *//*(rodzajTextField.getText().equalsIgnoreCase("cd")|| rodzajTextField.getText().equalsIgnoreCase("winyl")) )
                wh += " and P.[RODZAJ PŁYTY] like '" + rodzajTextField.getText() + "%'";
                else
                    isEverythingGood = false;*/
                wh += " and P.[RODZAJ PŁYTY] like ?";
            }

            temp += rodzaj;
            where += wh;
            flag = true;
        }

        if (wytworniaCB) {
            String wytwornia = " distinct(Wy.Wytwórnie) as Wytwórnia";
            String wh = "";//" and Wy.Wytwórnie is not null";
            String join = " left join Wytwórnie as Wy on P.IDWytwórni = Wy.IDWytwórni ";

            if (flag) {
                wytwornia = wytwornia.replace("distinct", "");
                temp += ",";
            }

            if (wytworniaTextField.getText().length() > 0) {
/*                StringChecker toCheck = new StringChecker(wytworniaTextField.getText());
                if(toCheck.stringCheck())
                wh += " and Wy.Wytwórnie like '" + wytworniaTextField.getText() + "%'";
                else
                    isEverythingGood = false;*/
                wh += " and Wy.Wytwórnie like ?";
            }

            temp += wytwornia;
            where += wh;
            from += join;
            flag = true;
        }

        if (krajCB) {
            String kraj = " distinct (K.Kraje) as Kraj";
            String wh = "";//" and K.Kraje is not null";
            String join = " full join Kraje as K on P.IDKraju = K.IDKraju";

            if (flag) {
                kraj = kraj.replace("distinct", "");
                temp += ",";
            }

            if (krajTextField.getText().length() > 0) {
/*                StringChecker toCheck = new StringChecker(krajTextField.getText());
                if(toCheck.stringCheck())
                wh += " and K.Kraje like '" + krajTextField.getText() + "%'";
                else
                    isEverythingGood = false;*/
                wh += " and K.Kraje like ?";
            }

            temp += kraj;
            where += wh;
            from += join;
            flag = true;
        }

        if (gatunekCB) {
            String gatunek = " distinct (G.Gatunki) as Gatunek";
            String wh = "";//" and G.Gatunki is not null ";
            String join = " left join Gatunki as G on P.IDGatunku = G.IDGatunku";

            if (flag) {
                gatunek = gatunek.replace("distinct", "");
                temp += ",";
            }

            if (gatunekTextField.getText().length() > 0) {
/*                StringChecker toCheck = new StringChecker(gatunekTextField.getText());
                if(toCheck.stringCheck())
            wh += " and G.Gatunki like '" + gatunekTextField.getText() + "%'";
                else
                    isEverythingGood = false;*/
                wh += " and G.Gatunki like ?";
            }

            temp += gatunek;
            where += wh;
            from += join;
            flag = true;
        }

        /*if(kompozytorCB){
            String kompozytor = " distinct (Ko.Kompozytorzy) as Kompozytor";
            String wh = " and Ko.Kompozytorzy is not null";
            String join = " join Kompozytorzy as Ko on P.IDKOmpozytora = Ko.IDKompozytorów";

            if(flag){
                kompozytor = kompozytor.replace("distinct","");
                temp += ",";
            }

            if(kompozytorTextField.getText().length() > 0){
                StringChecker toCheck = new StringChecker(kompozytorTextField.getText());
                if(toCheck.stringCheck())
                    wh += " and Ko.Kompozytorzy like '" + kompozytorTextField.getText() + "%'";
                else
                    isEverythingGood = false;
            }

            temp += kompozytor;
            where += wh;
            from += join;
            flag = true;
        }*/
        String ready = select + temp + from + where;
        System.out.println(ready);

        PreparedStatement statemet = null;
        try {
            statemet = connection.prepareStatement(ready);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int index = 1;

        if (tytulCB && tytulTextField.getText().length() > 0) {
            try {
                String temporary = tytulTextField.getText() + "%";
                statemet.setString(index, temporary);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (wykonawcaCB && wykonawcaTextField.getText().length() > 0) {
            try {
                String temporary = wykonawcaTextField.getText() + "%";
                statemet.setString(index, temporary);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (rokCB && rokTextField.getText().length() > 0) {
            try {
                String temporary = rokTextField.getText() + "%";
                statemet.setString(index, temporary);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (rodzajCB && rodzajTextField.getText().length() > 0) {
            try {
                String temporary = rodzajTextField.getText() + "%";
                statemet.setString(index, temporary);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (wytworniaCB && wytworniaTextField.getText().length() > 0) {
            try {
                String temporary = wytworniaTextField.getText() + "%";
                statemet.setString(index, temporary);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (krajCB && krajTextField.getText().length() > 0) {
            try {
                String temporary = krajTextField.getText() + "%";
                statemet.setString(index, temporary);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (gatunekCB && gatunekTextField.getText().length() > 0) {
            try {
                String temporary = gatunekTextField.getText() + "%";
                statemet.setString(index, temporary);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (flag && isEverythingGood) {
            try {
                //Connection connection = dc.dbConnect();
                data = FXCollections.observableArrayList();
                count = 0;

                //String queryString = ready; //"select P.[TYTUŁ PŁYTY] as TP, W.[Nazwa wykonawcy] as W from Płyty as P join Wykonawcy as W on P.IDWykonawcy = W.IDWykonawców where P.[TYTUŁ PŁYTY] is not null order by IDPłyty";
                ResultSet rs = statemet.executeQuery(); //connection.createStatement().executeQuery(queryString);

                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {

                            SimpleStringProperty as = new SimpleStringProperty((String) param.getValue().get(j));
                            if (as.getValueSafe().equals("")) {
                                SimpleStringProperty isNull = new SimpleStringProperty("BRAK");
                                return isNull;
                            }


                            return as;
                        }

                    });

                    col.setMinWidth(200);
                    //ol.setMaxWidth(400);
                    table.getColumns().addAll(col);
                }


                while (rs.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
                        row.add(rs.getString(i));
                    count++;
                    data.add(row);
                }

                table.setItems(data);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (!flag)
            table.setPlaceholder(new Label("Nie wybrano żadnego kryterium względem którego chcesz wyszukać dane!"));
        else if (!isEverythingGood)
            table.setPlaceholder(new Label("Popraw dane!"));


        //tableColumn1.setCellValueFactory(new PropertyValueFactory<>("tytulPlyty"));

        reaserchAmount.textProperty().set("Wyszukano " + count + " pozycji");
    }


    public void tytulCheckBoxAction(ActionEvent actionEvent) {
        if (!tytulCB)
            tytulCB = true;
        else
            tytulCB = false;
    }

    public void wykonawcaCheckBoxAction(ActionEvent actionEvent) {
        if (!wykonawcaCB)
            wykonawcaCB = true;
        else
            wykonawcaCB = false;
    }

    public void rokCheckBoxAction(ActionEvent actionEvent) {
        if (!rokCB)
            rokCB = true;
        else
            rokCB = false;

    }

    public void rodzajCheckBoxAction(ActionEvent actionEvent) {
        if (!rodzajCB)
            rodzajCB = true;
        else
            rodzajCB = false;
    }

    public void wytworniaCheckBoxAction(ActionEvent actionEvent) {
        if (!wytworniaCB)
            wytworniaCB = true;
        else
            wytworniaCB = false;
    }

    public void krajCheckBoxAction(ActionEvent actionEvent) {
        if (!krajCB)
            krajCB = true;
        else
            krajCB = false;
    }

    public void gatunekCheckBoxAction(ActionEvent actionEvent) {
        if (!gatunekCB)
            gatunekCB = true;
        else
            gatunekCB = false;
    }

   /* public void kompozytorCheckBoxAction(ActionEvent actionEvent) {
        if(!kompozytorCB)
            kompozytorCB = true;
        else
            kompozytorCB = false;
    }*/

    public void tytulKeyTapedAction(KeyEvent keyEvent) {
        if (tytulTextField.getText().equalsIgnoreCase(""))
            tytulLabel.textProperty().set("");

        if (tytulTextField.getText().length() > 0) {
            tytulLabel.textProperty().set("");
            StringChecker checkString = new StringChecker(tytulTextField.getText());
            System.out.println(checkString.stringCheck());
            if (!checkString.stringCheck())
                tytulLabel.textProperty().set("Błędny tytuł");
            else
                tytulLabel.textProperty().set("");
        }
    }

    public void wykonawcaKeyTapedAction(KeyEvent keyEvent) {
        if (wykonawcaTextField.getText().equalsIgnoreCase(""))
            wykonawcaLabel.textProperty().set("");

        if (wykonawcaTextField.getText().length() > 0) {
            StringChecker checkString = new StringChecker(wykonawcaTextField.getText());
            System.out.println(checkString.stringCheck());
            if (!checkString.stringCheck())
                wykonawcaLabel.textProperty().set("Błędny wykonawca");
            else
                wykonawcaLabel.textProperty().set("");
        }
    }

    public void rokKeyTapedAction(KeyEvent keyEvent) {
        if (rokTextField.getText().equalsIgnoreCase(""))
            rokLabel.textProperty().set("");

        if (rokTextField.getText().length() > 0) {
            NumberChecker checkNumber = new NumberChecker(rokTextField.getText());
            System.out.println(checkNumber.stringCheck());
            if (!checkNumber.stringCheck() || rokTextField.getText().length() != 4 || Integer.parseInt(rokTextField.getText()) > Year.now().getValue() || Integer.parseInt(rokTextField.getText()) < 1800)
                rokLabel.textProperty().set("Błędny rok");
            else
                rokLabel.textProperty().set("");
        }
    }

    public void rodzajKeyTapedAction(KeyEvent keyEvent) {
        if (rodzajTextField.getText().equalsIgnoreCase(""))
            rodzajLabel.textProperty().set("");

        if (rodzajTextField.getText().length() > 0) {
            StringChecker checkString = new StringChecker(rodzajTextField.getText());
            System.out.println(checkString.stringCheck());
            if (!checkString.stringCheck() || (!rodzajTextField.getText().equalsIgnoreCase("cd") && !rodzajTextField.getText().equalsIgnoreCase("winyl")))
                rodzajLabel.textProperty().set("Błędny rodzaj");
            else
                rodzajLabel.textProperty().set("");
        }
    }

    public void wytworniaKeyTapedAction(KeyEvent keyEvent) {
        if (wytworniaTextField.getText().equalsIgnoreCase(""))
            wytworniaLabel.textProperty().set("");

        if (wytworniaTextField.getText().length() > 0) {
            StringChecker checkString = new StringChecker(wytworniaTextField.getText());
            System.out.println(checkString.stringCheck());
            if (!checkString.stringCheck())
                wytworniaLabel.textProperty().set("Błędna wytwórnia");
            else
                wytworniaLabel.textProperty().set("");
        }
    }

    public void krajKeyTapedAction(KeyEvent keyEvent) {
        if (krajTextField.getText().equalsIgnoreCase(""))
            krajLabel.textProperty().set("");

        if (krajTextField.getText().length() > 0) {
            StringChecker checkString = new StringChecker(krajTextField.getText());
            System.out.println(checkString.stringCheck());
            if (!checkString.stringCheck())
                krajLabel.textProperty().set("Błędny kraj");
            else
                krajLabel.textProperty().set("");
        }
    }

    public void gatunekKeyTapedAction(KeyEvent keyEvent) {
        if (gatunekTextField.getText().equalsIgnoreCase(""))
            gatunekLabel.textProperty().set("");

        if (gatunekTextField.getText().length() > 0) {
            StringChecker checkString = new StringChecker(gatunekTextField.getText());
            System.out.println(checkString.stringCheck());
            if (!checkString.stringCheck())
                gatunekLabel.textProperty().set("Błędny gatunek");
            else
                gatunekLabel.textProperty().set("");
        }

    }

    public void rowClicked(MouseEvent mouseEvent) {

        System.out.println(table.lookup("TableHeaderRow"));

        String tytul = null, wykonawca = null, rok = null, rodzaj = null, wytwornia = null, kraj = null, gatunek = null;
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {

            System.out.println(table.getSelectionModel().isEmpty());

            if (!table.getSelectionModel().isEmpty()) {
                int i = 0;
                if (tytulCB && wykonawcaCB && (table.getSelectionModel().getSelectedItem().get(i) != null) && (table.getSelectionModel().getSelectedItem().get(i + 1) != null)) {
                    tytul = table.getSelectionModel().getSelectedItem().get(i).toString();
                    i++;
                    wykonawca = table.getSelectionModel().getSelectedItem().get(i).toString();
                    i++;
                    if (rokCB && table.getSelectionModel().getSelectedItem().get(i) != null) {
                        rok = table.getSelectionModel().getSelectedItem().get(i).toString();
                        i++;
                    }
                    if (rodzajCB && table.getSelectionModel().getSelectedItem().get(i) != null) {
                        rodzaj = table.getSelectionModel().getSelectedItem().get(i).toString();
                        i++;
                    }
                    if (wytworniaCB && table.getSelectionModel().getSelectedItem().get(i) != null) {
                        System.out.println(table.getSelectionModel().getSelectedItem().get(i));
                        wytwornia = table.getSelectionModel().getSelectedItem().get(i).toString();
                        i++;
                    }
                    if (krajCB && table.getSelectionModel().getSelectedItem().get(i) != null) {
                        kraj = table.getSelectionModel().getSelectedItem().get(i).toString();
                        i++;
                    }
                    if (gatunekCB && table.getSelectionModel().getSelectedItem().get(i) != null)
                        gatunek = table.getSelectionModel().getSelectedItem().get(i).toString();
                }


                if (tytulCB && wykonawcaCB) {
                    FXMLLoader updateController = new FXMLLoader();
                    updateController.setLocation(getClass().getResource("update.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(updateController.load(), 600, 600);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Logger logger = Logger.getLogger(getClass().getName());
                        logger.log(Level.SEVERE, "Failed to create new Window.", e);
                    }
                    Stage stage = new Stage();
                    stage.setTitle("Aktualizacja płyty " + tytul);
                    stage.setScene(scene);
                    stage.initOwner(this.myParent);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.getIcons().add(new Image("BazaDanych/icons/icon.png"));
                    stage.setMinHeight(600);
                    stage.setMinWidth(500);
                    stage.show();
                    UpdateController send = updateController.getController();
                    if (tytulCB)
                        send.setTytul(tytul);
                    if (wykonawcaCB)
                        send.setWykonawca(wykonawca);
                    if (rokCB)
                        send.setRok(rok);
                    if (rodzajCB)
                        send.setRodzaj(rodzaj);
                    if (wytworniaCB)
                        send.setWytwornia(wytwornia);
                    if (krajCB)
                        send.setKraj(kraj);
                    if (gatunekCB)
                        send.setGatunek(gatunek);

                    send.loadFromDataBase();

                } else {
                    FXMLLoader errorController = new FXMLLoader();
                    errorController.setLocation(getClass().getResource("error.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(errorController.load(), 300, 200);
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
        }else
            System.out.println("Działa!");
    }

    public void test(SortEvent<TableView<ObservableList>> tableViewSortEvent) {
        System.out.println("On sort");
    }

    /*public void openUpdateWindow(ActionEvent actionEvent) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("update.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle("New Window");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }*/


    /*public void kompozytorKeyTapedAction(KeyEvent keyEvent) {
        if(kompozytorTextField.getText().equalsIgnoreCase(""))
            kompozytorLabel.textProperty().set("");

            if (kompozytorTextField.getText().length() > 0) {
                StringChecker checkString = new StringChecker(kompozytorTextField.getText());
                System.out.println(checkString.stringCheck());
                if (!checkString.stringCheck())
                    kompozytorLabel.textProperty().set("Błędny kompozytor");
                else
                    kompozytorLabel.textProperty().set("");
            }
    }*/
}

