package com.QUeM.TreGStore.DatabaseClass;

import java.util.Date;

public class Prodotti {

    public String id;
    public String nome;
    public boolean disponibile;
    public int ndisp;
    public double prezzo;
    public boolean promozione;
    public Date tempoPromozione;
    public int totalePezziCarrello;



    public Prodotti(){
        //chiamate da dataSnapshot
    }

    public Prodotti(String codiceProdotto){
        this.id=codiceProdotto;
    }

    public String toString(){
        String text="Codice="+this.id+", Nome= "+this.nome+", Disponibile? "+this.disponibile+ ", Numero pezzi= "+this.ndisp+", Prezzo Singolo= "+
                this.prezzo+", Totale pezzi nel carrello= "+this.totalePezziCarrello+", Promozione? "+this.promozione+ ", Tempo Promozione Rimanente= "+this.tempoPromozione;
        return text;
    }

    public boolean compareTo(Prodotti controllo){
        boolean risposta=false;
        if(this.id.equals(controllo.id)){
            risposta=true;
        }
        return risposta;
    }

}
