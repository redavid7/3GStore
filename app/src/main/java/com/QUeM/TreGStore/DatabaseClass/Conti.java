package com.QUeM.TreGStore.DatabaseClass;

public class Conti {

    private int coinAmount;
    private float currentDiscount;
    private double totaleCarrello;
    private double saldoDisponibile;

    public Conti(){
        this.coinAmount=0;
        this.currentDiscount=0;
        this.saldoDisponibile=10000;
        this.totaleCarrello=0;
    }

    public Conti(int coinAmount, float currentDiscount, double totCarrello, double saldoCorrente){
        this.coinAmount=coinAmount;
        this.currentDiscount=currentDiscount;
        this.saldoDisponibile=saldoCorrente;
        this.totaleCarrello=totCarrello;
    }

    public int getCoinAmount(){
        return this.coinAmount;
    }

    public float getCurrentDiscount(){
        return this.currentDiscount;
    }

    public void setCoinAmount(int newCoinAmount){
        this.coinAmount=newCoinAmount;
    }

    public void setCurrentDiscount(int newCurrentDiscount){
        this.currentDiscount=newCurrentDiscount;
    }

    public double getTotaleCarrello() {
        return totaleCarrello;
    }

    public double getSaldoDisponibile() {
        return saldoDisponibile;
    }


    public void setTotaleCarrello(double totaleCarrello) {
        this.totaleCarrello = totaleCarrello;
    }

    public void setSaldoDisponibile(double saldoDisponibile) {
        this.saldoDisponibile = saldoDisponibile;
    }
}
