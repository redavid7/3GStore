package com.QUeM.TreGStore.DatabaseClass;

import java.util.ArrayList;
import java.util.Iterator;

//https://firebase.google.com/docs/firestore/manage-data/add-data#update_elements_in_an_array
/*
City city = new City("Los Angeles", "CA", "USA",
        false, 5000000L, Arrays.asList("west_coast", "sorcal"));
        db.collection("cities").document("LA").set(city);
*/

public class Carrello {

    //inserisco qui solo i codici dei prodotti
    private ArrayList<Prodotti> prodotti;

    public Carrello(){
        prodotti=new ArrayList<Prodotti>();
    }

    public Carrello(ArrayList<Prodotti> nuovo){
        this.prodotti=nuovo;
    }

    public ArrayList<Prodotti> getProdotti() {
        return prodotti;
    }

    public int size(){
        return prodotti.size();
    }

    public boolean controllaProdottoNelCarrello(Prodotti controllo){
        boolean risposta=false;
        Iterator<Prodotti> iter=this.getProdotti().iterator();
        while(iter.hasNext()){
            Prodotti it=iter.next();
            if(it.compareTo(controllo)){
                risposta=true;
            }
        }
        return risposta;
    }

    public void incrementaProdotto(Prodotti incremento){
        for(int i=0; i<this.getProdotti().size(); i++){
            if(this.getProdotti().get(i).compareTo(incremento)){
                this.getProdotti().get(i).totalePezziCarrello++;
            }
        }
    }

}
