package com.QUeM.TreGStore.DatabaseClass;

import java.util.Date;

public class Prodotti {

    private String id;
    private String nome;
    private boolean disponibile;
    private int ndisp;
    private double prezzo;
    private boolean promozione;
    private Date tempoPromozione;
    private int totalePezziCarrello;



    public Prodotti(){
        //chiamate da dataSnapshot
    }

    public Prodotti(String codiceProdotto){
        this.id=codiceProdotto;
    }

    public String toString(){
        String text="Nome: "+this.nome+"\nPrezzo Singolo: "+
                this.prezzo+"€"+"\nNumero articoli comprati: "+this.totalePezziCarrello;
        return text;
    }

    public boolean compareTo(Prodotti controllo){
        boolean risposta=false;
        if(this.id.equals(controllo.id)){
            risposta=true;
        }
        return risposta;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public int getNdisp() {
        return ndisp;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public boolean isPromozione() {
        return promozione;
    }

    public Date getTempoPromozione() {
        return tempoPromozione;
    }

    public int getTotalePezziCarrello() {
        return totalePezziCarrello;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public void setNdisp(int ndisp) {
        this.ndisp = ndisp;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public void setPromozione(boolean promozione) {
        this.promozione = promozione;
    }

    public void setTempoPromozione(Date tempoPromozione) {
        this.tempoPromozione = tempoPromozione;
    }

    public void setTotalePezziCarrello(int totalePezziCarrello) {
        this.totalePezziCarrello = totalePezziCarrello;
    }
}
