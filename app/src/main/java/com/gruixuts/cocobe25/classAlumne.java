package com.gruixuts.cocobe25;

import android.database.Cursor;

public class classAlumne {
// TODO later: Afegir e-mails i potser un altre telèfon.
//  A més un boolean per a dir si es pot usar o no tal o qual canal

    private String Codi;
    private String Nom;
    private String Cognom1;
    private String Cognom2;
    private String Curs;
    private String Observacions;
    private String Cntct1Tel;
    private String Cntct1Nom;
    private String Cntct1Rel;
    private String Cntct2Tel;
    private String Cntct2Nom;
    private String Cntct2Rel;
    private String Imatges;


    public static final int LongNomImg = 4;  // Longitud del nom de la carpeta que conté les imatges


    public classAlumne(String codi, String nom, String cognom1, String cognom2, String curs, String observacions, String cntct1Tel, String cntct1Nom, String cntct1Rel, String cntct2Tel, String cntct2Nom, String cntct2Rel, String imatges) {

        Codi = codi;  //Id del e-mail
        Nom = nom;
        Cognom1 = cognom1;
        Cognom2 = cognom2;
        Curs = curs;
        Observacions = observacions;
        Cntct1Tel = cntct1Tel;
        Cntct1Nom = cntct1Nom;
        Cntct1Rel = cntct1Rel;
        Cntct2Tel = cntct2Tel;
        Cntct2Nom = cntct2Nom;
        Cntct2Rel = cntct2Rel;
        Imatges = Imatges;
    }   

    public classAlumne(Cursor cursor) {

        Codi = cursor.getString(0);
        Nom = cursor.getString(1);
        Cognom1 = cursor.getString(2);
        Cognom2 = cursor.getString(3);
        Curs = cursor.getString(4);
        Observacions = cursor.getString(5);
        Cntct1Tel = cursor.getString(6);
        Cntct1Nom = cursor.getString(7);
        Cntct1Rel = cursor.getString(8);
        Cntct2Tel = cursor.getString(9);
        Cntct2Nom = cursor.getString(10);
        Cntct2Rel = cursor.getString(11);
        Imatges = cursor.getString(12);
    }

    public classAlumne(String noucodi) {
        Codi = noucodi;
        Nom = "";
        Cognom1 = "";
        Cognom2 = "";
        Curs = "";
        Observacions = "";
        Cntct1Tel = "";
        Cntct1Nom = "";
        Cntct1Rel = "";
        Cntct2Tel = "";
        Cntct2Nom = "";
        Cntct2Rel = "";
        Imatges = "";
    }

    public classAlumne(String[] camps) {
        Codi = camps[0];
        Nom = camps[1];
        Cognom1 = camps[2];
        Cognom2 = camps[3];
        Curs = camps[4];
        Observacions = camps[5];
        Cntct1Tel = camps[6];
        Cntct1Nom = camps[7];
        Cntct1Rel = camps[8];
        Cntct2Tel = camps[9];
        Cntct2Nom = camps[10];
        Cntct2Rel = camps[11];
        Imatges = camps[12];
    }

    public String toCSV() {
        String ret;
        ret =  Codi + ";";
        ret +=  Nom + ";";
        ret +=  Cognom1 + ";";
        ret +=  Cognom2 + ";";
        ret +=  Curs + ";";
        ret +=  Observacions + ";";
        ret +=  Cntct1Tel + ";";
        ret +=  Cntct1Nom + ";";
        ret +=  Cntct1Rel + ";";
        ret +=  Cntct2Tel + ";";
        ret +=  Cntct2Nom + ";";
        ret +=  Cntct2Rel + ";";
        ret +=  Imatges ;
        return ret;
    }

    public static Integer NumCamps() {
        return 13;
    }

    // Getters

    public String getCodi() {
        return Codi;
    }

    public String getNom() {
        return Nom;
    }

    public String getCognom1() {
        return Cognom1;
    }

    public String getCognom2() {
        return Cognom2;
    }

    public String getCurs() {
        return Curs;
    }

    public String getObservacions() {
        return Observacions;
    }

    public String getCntct1Tel() {
        return Cntct1Tel;
    }

    public String getCntct1Nom() {
        return Cntct1Nom;
    }

    public String getCntct1Rel() {
        return Cntct1Rel;
    }

    public String getCntct2Tel() {
        return Cntct2Tel;
    }

    public String getCntct2Nom() {
        return Cntct2Nom;
    }

    public String getCntct2Rel() {
        return Cntct2Rel;
    }

    public String getImatges() {
        return Imatges;
    }
    // Setters

    public void setCodi(String codi) {
        Codi = codi;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public void setCognom1(String cognom1) {
        Cognom1 = cognom1;
    }

    public void setCognom2(String cognom2) {
        Cognom2 = cognom2;
    }

    public void setCurs(String curs) {
        Curs = curs;
    }

    public void setObservacions(String observacions) {
        Observacions = observacions;
    }

    public void setCntct1Tel(String cntct1Tel) {
        Cntct1Tel = cntct1Tel;
    }

    public void setCntct1Nom(String cntct1Nom) {
        Cntct1Nom = cntct1Nom;
    }

    public void setCntct1Rel(String cntct1Rel) {
        Cntct1Rel = cntct1Rel;
    }

    public void setCntct2Tel(String cntct2Tel) {
        Cntct2Tel = cntct2Tel;
    }

    public void setCntct2Nom(String cntct2Nom) {
        Cntct2Nom = cntct2Nom;
    }

    public void setCntct2Rel(String cntct2Rel) {
        Cntct2Rel = cntct2Rel;
    }

    public void setImatges(String imatges) {
        Imatges = imatges;
    }

}

