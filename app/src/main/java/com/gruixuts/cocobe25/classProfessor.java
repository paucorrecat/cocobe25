package com.gruixuts.cocobe25;

import android.database.Cursor;

public class classProfessor {
    private String Codi;
    private String Nom;
    private String Cognom1;
    private String Cognom2;
    private String Rols;
    private String Estat;
    private String Observacions;
    private String eMail;
    private String Imatges;


    public classProfessor(String codi, String nom, String cognom1, String cognom2, String rols, String estat, String observacions, String email, String imatges) {
        Codi = codi;  //Id del e-mail
        Nom = nom;
        Cognom1 = cognom1;
        Cognom2 = cognom2;
        Rols = rols;
        Estat = estat;
        Observacions = observacions;
        eMail = email;
        Imatges = Imatges;
    }

    public classProfessor(Cursor cursor) {
        Codi = cursor.getString(0);
        Nom = cursor.getString(1);
        Cognom1 = cursor.getString(2);
        Cognom2 = cursor.getString(3);
        Rols = cursor.getString(4);
        Estat = cursor.getString(5);
        Observacions = cursor.getString(6);
        eMail = cursor.getString(7);
        Imatges = cursor.getString(8);
    }

    public classProfessor(String noucodi) {
        Codi = noucodi;
        Nom = "";
        Cognom1 = "";
        Cognom2 = "";
        Rols = "";
        Estat = "";
        Observacions = "";
        eMail = "";
        Imatges = "";
    }



    public classProfessor(String[] camps) {
        Codi = camps[0];
        Nom = camps[1];
        Cognom1 = camps[2];
        Cognom2 = camps[3];
        Rols = camps[4];
        Estat = camps[5];
        Observacions = camps[6];
        eMail = camps[7];
        Imatges = camps[8];
    }

    public String toCSV() {
        String ret;
        ret =  Codi + ";";
        ret +=  Nom + ";";
        ret +=  Cognom1 + ";";
        ret +=  Cognom2 + ";";
        ret +=  Rols + ";";
        ret +=  Estat + ";";
        ret +=  Observacions + ";";
        ret +=  eMail + ";";
        ret +=  Imatges + ";";
        return ret;
        
    }

    public static Integer NumCamps() {
        return 9;
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

    public String getRols() {
        return Rols;
    }

    public String getEstat() {
        return Estat;
    }

    public String getObservacions() {
        return Observacions;
    }

    public String geteMail() {
        return eMail;
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

    public void setRols(String rols) {
        Rols = rols;
    }

    public void setObservacions(String observacions) {
        Observacions = observacions;
    }

    public void setEstat(String estat) {
        Estat = estat;
    }

    public void seteMail(String email) {
        eMail = email;
    }

    public void setImatges(String imatges) {
        Imatges = imatges;
    }

}

