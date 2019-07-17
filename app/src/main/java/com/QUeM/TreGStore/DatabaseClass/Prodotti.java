package com.QUeM.TreGStore.DatabaseClass;

public class Prodotti {

    public int id;
    public String nome;
    public boolean disponibile;
    public int ndisp;

    public Prodotti(){
        //chiamate da dataSnapshot
    }

    public Prodotti(int fid, String fnome, boolean fdisp, int fndisp){
        this.id=fid;
        this.nome=fnome;
        this.ndisp=fndisp;
        this.disponibile=fdisp;
    }

}
