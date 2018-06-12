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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import javax.swing.table.JTableHeader;

import static javafx.scene.paint.Color.*;

public class SearchController implements Initializable {

    @FXML
    private Button backButton;
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
    private boolean isConnectedToDatabase = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
        dc = new MsSqlConnection();
        table.setPlaceholder(new Label("Wybierz dane ktore chcesz zobaczyc i nacisnij 'Load Database'"));
    }

    @FXML
    private void loadDataFromDatabase(ActionEvent event) {
        int count = 0;

        Connection connection = null;
        try {
            connection = dc.dbConnect();
        } catch (Exception e) {
            e.printStackTrace();
            isConnectedToDatabase = false;
        }

        if(isConnectedToDatabase) {
            String temp = " ";
            String select = "select ", from = " from Płyty as P ", where = " where P.[TYTUŁ PŁYTY] is not null ";
            boolean flag = false, isEverythingGood = true;

            table.getItems().clear();
            table.getColumns().clear();

            if (tytulCheckBox.isSelected()) {
                String tytul = "P.[TYTUŁ PŁYTY] as Tytuł";
                temp = temp + tytul;
                flag = true;
                if (tytulTextField.getText().length() > 0) {
                    String wh = " and P.[TYTUŁ PŁYTY] like ?";
                    where += wh;
                    StringChecker s = new StringChecker(tytulTextField.getText());
                    if (!s.stringCheck())
                        isEverythingGood = false;
                }

            }

            if (wykonawcaCheckBox.isSelected()) {
                String wykonawca = " distinct (W.[Nazwa wykonawcy]) as Wykonawca ";
                String join = " left join Wykonawcy as W on P.IDWykonawcy = W.IDWykonawców ";
                String wh = " and W.[Nazwa Wykonawcy] is not null";

                if (flag) {
                    wykonawca = wykonawca.replace("distinct", "");
                    temp += ",";
                }

                if (wykonawcaTextField.getText().length() > 0) {
                    wh += " and W.[Nazwa wykonawcy] like ?";
                    StringChecker s = new StringChecker(wykonawcaTextField.getText());
                    if (!s.stringCheck())
                        isEverythingGood = false;
                }
                temp += wykonawca;
                from += join;
                where += wh;
                flag = true;
            }

            if (rokCheckBox.isSelected()) {
                String rok = " distinct (P.[ROK]) as Rok ";
                String wh = "";// = " and P.[ROK] is not null";

                if (flag) {
                    rok = rok.replace("distinct", "");
                    temp += ",";
                }

                if (rokTextField.getText().length() > 0) {
                    wh += " and P.[ROK] like ?";
                    NumberChecker checkNumber = new NumberChecker(rokTextField.getText());
                    if (!checkNumber.stringCheck() || rokTextField.getText().length() != 4 || Integer.parseInt(rokTextField.getText()) > Year.now().getValue() || Integer.parseInt(rokTextField.getText()) < 1800)
                        isEverythingGood = false;
                }


                temp += rok;
                where += wh;
                flag = true;
            }

            if (rodzajCheckBox.isSelected()) {
                String rodzaj = " distinct(P.[RODZAJ PŁYTY]) as Rodzaj ";
                String wh = "";//" and P.[RODZAJ PŁYTY] is not null ";

                if (flag) {
                    rodzaj = rodzaj.replace("distinct", "");
                    temp += ",";
                }

                if (rodzajTextField.getText().length() > 0) {
                    wh += " and P.[RODZAJ PŁYTY] like ?";
                    if((!rodzajTextField.getText().equalsIgnoreCase("cd") && !rodzajTextField.getText().equalsIgnoreCase("winyl")))
                        isEverythingGood = false;
                }

                temp += rodzaj;
                where += wh;
                flag = true;
            }

            if (wytworniaCheckBox.isSelected()) {
                String wytwornia = " distinct(Wy.Wytwórnie) as Wytwórnia";
                String wh = "";//" and Wy.Wytwórnie is not null";
                String join = " left join Wytwórnie as Wy on P.IDWytwórni = Wy.IDWytwórni ";

                if (flag) {
                    wytwornia = wytwornia.replace("distinct", "");
                    temp += ",";
                }

                if (wytworniaTextField.getText().length() > 0) {
                    wh += " and Wy.Wytwórnie like ?";
                    StringChecker s = new StringChecker(wytworniaTextField.getText());
                    if (!s.stringCheck())
                        isEverythingGood = false;
                }
                temp += wytwornia;
                where += wh;
                from += join;
                flag = true;
            }

            if (krajCheckBox.isSelected()) {
                String kraj = " distinct (K.Kraje) as Kraj";
                String wh = "";//" and K.Kraje is not null";
                String join = " left join Kraje as K on P.IDKraju = K.IDKraju";

                if (flag) {
                    kraj = kraj.replace("distinct", "");
                    temp += ",";
                }

                if (krajTextField.getText().length() > 0) {
                    wh += " and K.Kraje like ?";
                    StringChecker s = new StringChecker(krajTextField.getText());
                    if (!s.stringCheck())
                        isEverythingGood = false;
                }

                temp += kraj;
                where += wh;
                from += join;
                flag = true;
            }

            if (gatunekCheckBox.isSelected()) {
                String gatunek = " distinct (G.Gatunki) as Gatunek";
                String wh = "";//" and G.Gatunki is not null ";
                String join = " left join Gatunki as G on P.IDGatunku = G.IDGatunku";

                if (flag) {
                    gatunek = gatunek.replace("distinct", "");
                    temp += ",";
                }

                if (gatunekTextField.getText().length() > 0) {
                    wh += " and G.Gatunki like ?";
                    StringChecker s = new StringChecker(gatunekTextField.getText());
                    if (!s.stringCheck())
                        isEverythingGood = false;
                }
                temp += gatunek;
                where += wh;
                from += join;
                flag = true;
            }

            String ready = null;
            if(tytulCheckBox.isSelected() && wykonawcaCheckBox.isSelected())
                ready = select + temp + ", P.IDPłyty " + from + where;
            else
                ready = select + temp + from + where;


            System.out.println(ready);

            PreparedStatement statemet = null;
            try {
                statemet = connection.prepareStatement(ready);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            int index = 1;

            index = IsTextFieldUsed(statemet, index, tytulCheckBox, tytulTextField);

            index = IsTextFieldUsed(statemet, index, wykonawcaCheckBox, wykonawcaTextField);

            index = IsTextFieldUsed(statemet, index, rokCheckBox, rokTextField);

            index = IsTextFieldUsed(statemet, index, rodzajCheckBox, rodzajTextField);

            index = IsTextFieldUsed(statemet, index, wytworniaCheckBox, wytworniaTextField);

            index = IsTextFieldUsed(statemet, index, krajCheckBox, krajTextField);

            if (gatunekCheckBox.isSelected() && gatunekTextField.getText().length() > 0) {
                try {
                    String temporary = gatunekTextField.getText() + "%";
                    statemet.setString(index, temporary);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (flag && isEverythingGood) {
                try {
                    data = FXCollections.observableArrayList();
                    count = 0;

                    ResultSet rs = statemet.executeQuery();

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
                    if(tytulCheckBox.isSelected() && wykonawcaCheckBox.isSelected())
                        table.getColumns().get(rs.getMetaData().getColumnCount() - 1).setVisible(false);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (!flag)
                table.setPlaceholder(new Label("Nie wybrano żadnego kryterium względem którego chcesz wyszukać dane!"));
            else if (!isEverythingGood)
                table.setPlaceholder(new Label("Popraw dane!"));
            else if(count == 0)
                table.setPlaceholder(new Label("Nie wyszukało nic!"));

            //tableColumn1.setCellValueFactory(new PropertyValueFactory<>("tytulPlyty"));

            reaserchAmount.textProperty().set("Wyszukano " + count + " pozycji");
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

            Node source = (Node)  event.getSource();
            Stage stage2  = (Stage) source.getScene().getWindow();
            stage2.close();
        }
    }

    private int IsTextFieldUsed(PreparedStatement statemet, int index, CheckBox tytulCheckBox, TextField tytulTextField) {
        if (tytulCheckBox.isSelected() && tytulTextField.getText().length() > 0) {
            try {
                String temporary = tytulTextField.getText() + "%";
                statemet.setString(index, temporary);
                index++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return index;
    }


    public void tytulKeyTapedAction(KeyEvent keyEvent) {
        if (tytulTextField.getText().equalsIgnoreCase(""))
            tytulLabel.textProperty().set("");

        if(tytulTextField.getText().length() > 0){
            StringChecker s = new StringChecker(tytulTextField.getText());
            if(!s.stringCheck()){
                tytulLabel.setText("Błędne znaki");
            }else
                tytulLabel.setText("");
        }
    }

    public void wykonawcaKeyTapedAction(KeyEvent keyEvent) {
        if (wykonawcaTextField.getText().equalsIgnoreCase(""))
            wykonawcaLabel.textProperty().set("");

        if (wykonawcaTextField.getText().length() > 0) {
            StringChecker s = new StringChecker(wykonawcaTextField.getText());
            if (!s.stringCheck()) {
                wykonawcaLabel.setText("Błędne znaki");
            } else
                wykonawcaLabel.setText("");
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
            StringChecker s = new StringChecker(wytworniaTextField.getText());
            if (!s.stringCheck()) {
                wytworniaLabel.setText("Błędne znaki");
            } else
                wytworniaLabel.setText("");
        }
    }

    public void krajKeyTapedAction(KeyEvent keyEvent) {
        if (krajTextField.getText().equalsIgnoreCase(""))
            krajLabel.textProperty().set("");

        if (krajTextField.getText().length() > 0) {
            StringChecker s = new StringChecker(krajTextField.getText());
            if (!s.stringCheck()) {
                krajLabel.setText("Błędne znaki");
            } else
                krajLabel.setText("");
        }
    }

    public void gatunekKeyTapedAction(KeyEvent keyEvent) {
        if (gatunekTextField.getText().equalsIgnoreCase(""))
            gatunekLabel.textProperty().set("");

        if (gatunekTextField.getText().length() > 0) {
            StringChecker s = new StringChecker(gatunekTextField.getText());
            if (!s.stringCheck()) {
                gatunekLabel.setText("Błędne znaki");
            } else
                gatunekLabel.setText("");
        }
    }

    public void rowClicked(MouseEvent mouseEvent) {
        String tytul = null, wykonawca = null, rok = null, rodzaj = null, wytwornia = null, kraj = null, gatunek = null;
        int IDPlyty = 0;
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {

            System.out.println(table.getSelectionModel().isEmpty());

            if (!table.getSelectionModel().isEmpty()) {
                int i = 0;
                if (tytulCheckBox.isSelected() && wykonawcaCheckBox.isSelected() && (table.getSelectionModel().getSelectedItem().get(i) != null) && (table.getSelectionModel().getSelectedItem().get(i + 1) != null)) {
                    tytul = table.getSelectionModel().getSelectedItem().get(i).toString();
                    i++;
                    wykonawca = table.getSelectionModel().getSelectedItem().get(i).toString();
                    i++;
                    if (rokCheckBox.isSelected() && table.getSelectionModel().getSelectedItem().get(i) != null) {
                        rok = table.getSelectionModel().getSelectedItem().get(i).toString();
                        i++;
                    }
                    if (rodzajCheckBox.isSelected() && table.getSelectionModel().getSelectedItem().get(i) != null) {
                        rodzaj = table.getSelectionModel().getSelectedItem().get(i).toString();
                        i++;
                    }
                    if (wytworniaCheckBox.isSelected() && table.getSelectionModel().getSelectedItem().get(i) != null) {
                        System.out.println(table.getSelectionModel().getSelectedItem().get(i));
                        wytwornia = table.getSelectionModel().getSelectedItem().get(i).toString();
                        i++;
                    }

                    if (krajCheckBox.isSelected() && table.getSelectionModel().getSelectedItem().get(i) != null) {
                        kraj = table.getSelectionModel().getSelectedItem().get(i).toString();
                        i++;
                    }

                    if (gatunekCheckBox.isSelected() && table.getSelectionModel().getSelectedItem().get(i) != null)
                        gatunek = table.getSelectionModel().getSelectedItem().get(i).toString();

                    IDPlyty = Integer.parseInt(table.getSelectionModel().getSelectedItem().get(table.getColumns().size() - 1).toString());
                }

                if (tytulCheckBox.isSelected() && wykonawcaCheckBox.isSelected()) {
                    FXMLLoader updateController = new FXMLLoader();
                    updateController.setLocation(getClass().getResource("scenes/update.fxml"));
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
                    stage.setResizable(false);
                    stage.show();
                    UpdateController send = updateController.getController();
                    if (tytulCheckBox.isSelected())
                        send.setTytul(tytul);
                    if (wykonawcaCheckBox.isSelected())
                        send.setWykonawca(wykonawca);
                    if (rokCheckBox.isSelected())
                        send.setRok(rok);
                    if (rodzajCheckBox.isSelected())
                        send.setRodzaj(rodzaj);
                    if (wytworniaCheckBox.isSelected())
                        send.setWytwornia(wytwornia);
                    if (krajCheckBox.isSelected())
                        send.setKraj(kraj);
                    if (gatunekCheckBox.isSelected())
                        send.setGatunek(gatunek);

                    send.setIDPlyty(IDPlyty);
                    send.loadFromDataBase();

                    stage.setOnHidden(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            dbLoad.fire();
                        }
                    });

                } else {
                    FXMLLoader errorController = new FXMLLoader();
                    errorController.setLocation(getClass().getResource("scenes/error.fxml"));
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
            }else
                System.out.println("Działa!");
        }
    }

    public void test(SortEvent<TableView<ObservableList>> tableViewSortEvent) {
        System.out.println("On sort");
    }

    public void tytulCheckBoxAction(ActionEvent actionEvent) {
    }

    public void wykonawcaCheckBoxAction(ActionEvent actionEvent) {
    }

    public void rokCheckBoxAction(ActionEvent actionEvent) {
    }

    public void rodzajCheckBoxAction(ActionEvent actionEvent) {
    }

    public void wytworniaCheckBoxAction(ActionEvent actionEvent) {
    }

    public void krajCheckBoxAction(ActionEvent actionEvent) {
    }

    public void gatunekCheckBoxAction(ActionEvent actionEvent) {
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
}

