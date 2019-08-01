package com.QUeM.TreGStore.DatabaseClass;

import java.io.Serializable;

public class Domanda implements Serializable {
    public String testo;
    public String risposta1;
    public String risposta2;
    public String risposta3;
    public String risposta4;
    public String corretta;
    public  Domanda(String t,String r1,String r2, String r3,String r4, String c){
        this.testo=t;
        this.risposta1=r1;
        this.risposta2=r2;
        this.risposta3=r3;
        this.risposta4=r4;
        this.corretta=c;
    }

}
