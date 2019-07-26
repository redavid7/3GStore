package com.QUeM.TreGStore.DatabaseClass;

import java.util.ArrayList;

//https://firebase.google.com/docs/firestore/manage-data/add-data#update_elements_in_an_array
/*
City city = new City("Los Angeles", "CA", "USA",
        false, 5000000L, Arrays.asList("west_coast", "sorcal"));
        db.collection("cities").document("LA").set(city);
*/

public class Carrello {

    //inserisco qui solo i codici dei prodotti
    public ArrayList<Prodotti> prodotti;

    public Carrello(){
        prodotti=new ArrayList<Prodotti>();
    }

    public Carrello(ArrayList<Prodotti> nuovo){
        this.prodotti=nuovo;
    }

    public ArrayList<Prodotti> getProdotti() {
        return prodotti;
    }


    public String size() {
       return String.valueOf(prodotti.size());
    }
}
