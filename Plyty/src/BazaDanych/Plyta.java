package BazaDanych;

import javafx.beans.property.SimpleStringProperty;

public class Plyta {

    private SimpleStringProperty tytulPlyty = new SimpleStringProperty();
    private SimpleStringProperty rodzajPlyty = new SimpleStringProperty();
    private SimpleStringProperty rok = new SimpleStringProperty();
    private SimpleStringProperty wykonawca = new SimpleStringProperty();
    private SimpleStringProperty wytwornia = new SimpleStringProperty();
    private SimpleStringProperty kraj = new SimpleStringProperty();
    private SimpleStringProperty gatunek = new SimpleStringProperty();


    public Plyta( String tytulPlyty, String rodzajPlyty, String rok, String wykonawca, String wytwornia, String kraj, String gatunek) {
        this.tytulPlyty = new SimpleStringProperty(tytulPlyty);
        this.rodzajPlyty = new SimpleStringProperty(rodzajPlyty);
        this.rok = new SimpleStringProperty(rok);
        this.wykonawca = new SimpleStringProperty(wykonawca);
        this.wytwornia = new SimpleStringProperty(wytwornia);
        this.kraj = new SimpleStringProperty(kraj);
        this.gatunek = new SimpleStringProperty(gatunek);
    }

    public String getTytulPlyty() {
        return tytulPlyty.get();
    }

    public SimpleStringProperty tytulPlytyProperty() {
        return tytulPlyty;
    }

    public void setTytulPlyty(String tytulPlyty) {
        this.tytulPlyty.set(tytulPlyty);
    }

    public String getRodzajPlyty() {
        return rodzajPlyty.get();
    }

    public SimpleStringProperty rodzajPlytyProperty() {
        return rodzajPlyty;
    }

    public void setRodzajPlyty(String rodzajPlyty) {
        this.rodzajPlyty.set(rodzajPlyty);
    }

    public String getRok() {
        return rok.get();
    }

    public SimpleStringProperty rokProperty() {
        return rok;
    }

    public void setRok(String rok) {
        this.rok.set(rok);
    }

    public String getWykonawca() {
        return wykonawca.get();
    }

    public SimpleStringProperty wykonawcaProperty() {
        return wykonawca;
    }

    public void setWykonawca(String wykonawca) {
        this.wykonawca.set(wykonawca);
    }

    public String getWytwornia() {
        return wytwornia.get();
    }

    public SimpleStringProperty wytworniaProperty() {
        return wytwornia;
    }

    public void setWytwornia(String wytwornia) {
        this.wytwornia.set(wytwornia);
    }

    public String getKraj() {
        return kraj.get();
    }

    public SimpleStringProperty krajProperty() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj.set(kraj);
    }

    public String getGatunek() {
        return gatunek.get();
    }

    public SimpleStringProperty gatunekProperty() {
        return gatunek;
    }

    public void setGatunek(String gatunek) {
        this.gatunek.set(gatunek);
    }

}
