package com.banas.liczkalorie;

public class Product{
    private int id;
    private String nazwa;
    private int kcal;
    private double weglowodany;
    private double tluszcz;
    private double bialko;
    private int added;

    public Product(int id, String nazwa, int kcal, double weglowodany, double tluszcz, double bialko, int added){
        this.id=id;
        this.nazwa=nazwa;
        this.kcal=kcal;
        this.bialko=bialko;
        this.tluszcz=tluszcz;
        this.weglowodany=weglowodany;
        this.added=added;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getKcal() { return kcal; }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public double getWeglowodany() {
        return weglowodany;
    }

    public void setWeglowodany(double weglowodany) {
        this.weglowodany = weglowodany;
    }

    public double getTluszcz() {
        return tluszcz;
    }

    public void setTluszcz(double tluszcz) {
        this.tluszcz = tluszcz;
    }

    public double getBialko() {
        return bialko;
    }

    public void setBialko(double bialko) {
        this.bialko = bialko;
    }

    public int isAdded() {
        return added;
    }

    public void setAdded(int added) {
        this.added = added;
    }
}
