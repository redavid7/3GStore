package com.QUeM.TreGStore.DatabaseClass;

public class Conti {

    private int coinAmount;
    private float currentDiscount;

    public Conti(){

    }

    public Conti(int coinAmount, float currentDiscount){
        this.coinAmount=coinAmount;
        this.currentDiscount=currentDiscount;
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


}
