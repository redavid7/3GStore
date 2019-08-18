package com.QUeM.TreGStore.DatabaseClass;

public class Conti {

    private int coinAmount;
    private double currentDiscount;
    private double totaleCarrello;
    private double saldoDisponibile;

    public Conti(){
        this.coinAmount=0;
        this.currentDiscount=0.0;
        this.saldoDisponibile=10000.0;
        this.totaleCarrello=0;
    }

    public Conti(int coinAmount, double currentDiscount, double totCarrello, double saldoCorrente){
        this.coinAmount=coinAmount;
        this.currentDiscount=currentDiscount;
        this.saldoDisponibile=saldoCorrente;
        this.totaleCarrello=totCarrello;
    }

    public int getCoinAmount(){
        return this.coinAmount;
    }

    public double getCurrentDiscount(){
        return this.currentDiscount;
    }

    public void setCoinAmount(int newCoinAmount){
        this.coinAmount=newCoinAmount;
    }

    public void setCurrentDiscount(double newCurrentDiscount){
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

    @Override
    public String toString() {
        return "Conti{" +
                "coinAmount=" + coinAmount +
                ", currentDiscount=" + currentDiscount +
                ", totaleCarrello=" + totaleCarrello +
                ", saldoDisponibile=" + saldoDisponibile +
                '}';
    }
}
