package com.banas.liczkalorie;

public class Eaten {
    private int id_eaten;
    private int id_date;
    private int id_product;
    private double amount;
    private String nazwa;
    private double kcal;
    private double weglowodany;
    private double tluszcz;
    private double bialko;
    private String data;

    public Eaten(int id_eaten, int id_date, int id_product, double amount, String nazwa, double kcal, double weglowodany, double tluszcz, double bialko, String data) {
        this.id_eaten = id_eaten;
        this.id_date = id_date;
        this.id_product = id_product;
        this.amount = amount;
        this.nazwa = nazwa;
        this.kcal = kcal;
        this.weglowodany = weglowodany;
        this.tluszcz = tluszcz;
        this.bialko = bialko;
        this.data = data;
    }

    public int getId_eaten() {
        return id_eaten;
    }

    public void setId_eaten(int id_eaten) {
        this.id_eaten = id_eaten;
    }

    public int getId_date() {
        return id_date;
    }

    public void setId_date(int id_date) {
        this.id_date = id_date;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

