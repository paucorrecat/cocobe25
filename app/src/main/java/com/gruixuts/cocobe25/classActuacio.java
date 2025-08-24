package com.gruixuts.cocobe25;

import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class classActuacio {


    // Constants
    public static final String DATA_MAX = "2025-06-30";
    public static final String PENDENT_PENDENT = "Pendent";
    public static final String PENDENT_FET = "Fet";

    private String Id; // Incidència & Comptador
    private String Data;
    private boolean Pendent;
    private String Tipus; // Cal anar-ho definint
    private String Titol;
    private String Professors;
    private String Alumnes;
    private String Pares;
    private String Descripcio;


    public static final ArrayList<String> llistaTipusActuacions;

    // Bloc d'inicialització estàtic per afegir valors a la llista
    static {
        llistaTipusActuacions = new ArrayList<>();
        llistaTipusActuacions.add("Pares-trucada");
        llistaTipusActuacions.add("Pares-reunió");
        llistaTipusActuacions.add("Alum-Preguntar");
        llistaTipusActuacions.add("Alum-Demanar perdó");
        llistaTipusActuacions.add("Alum-Activ Restauradora");
        llistaTipusActuacions.add("Alum-Expulsió");
        llistaTipusActuacions.add("Profe-Aclarir");
        llistaTipusActuacions.add("Profe-Informar");
    }

    public classActuacio(String id, String data, boolean pendent, String tipus, String titol, String professors, String alumnes, String pares, String descripcio) {
        Id = id;
        Data = data;
        Pendent = pendent;
        Tipus = tipus;
        Titol = titol;
        Professors = professors;
        Alumnes = alumnes;
        Pares = pares;
        Descripcio = descripcio;
    }

    public classActuacio(Cursor cursor) {
        Id = cursor.getString(0);
        Data = cursor.getString(1);
        if (!cursor.getString(2).equals(PENDENT_FET)) {
            Pendent = true;
        } else {
            Pendent=false;
        }
        Tipus = cursor.getString(3);
        Titol = cursor.getString(4);
        Professors = cursor.getString(5);
        Alumnes = cursor.getString(6);
        Pares = cursor.getString(7);
        Descripcio = cursor.getString(8);
    }

    public classActuacio(String id) {
        Id = id;
        Data = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Pendent = true;
        Tipus = "";
        Titol = "";
        Professors = "";
        Alumnes = "";
        Pares = "";
        Descripcio = "";
    }

    public classActuacio(String[] camps) {
        Id = camps[0];
        Data = camps[1];
        if (camps[2] == "Fet") {
            Pendent = false;
        } else {
            Pendent=true;
        }
        Tipus = camps[3];
        Titol = camps[4];
        Professors = camps[5];
        Alumnes = camps[6];
        Pares = camps[7];
        Descripcio = camps[8];
    }

    public String toCSV() {
    String ret;
        ret = Id + ";";
        ret += Data + ";";
        ret += (Pendent ? PENDENT_PENDENT : PENDENT_FET) + ";";
        ret += Tipus + ";";
        ret += Titol + ";";
        ret += Professors + ";";
        ret += Alumnes + ";";
        ret += Pares + ";";
        ret += Descripcio ;
        return ret;
    }

    public static Integer NumCamps() {
        return 9;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public boolean getPendent() {
        return Pendent;
    }

    public void setPendent(boolean pendent) {
        Pendent = pendent;
    }

    public String getTipus() {
        return Tipus;
    }

    public void setTipus(String tipus) {
        Tipus = tipus;
    }

    public String getTitol() {
        return Titol;
    }

    public void setTitol(String titol) {
        Titol = titol;
    }

    public String getProfessors() {
        return Professors;
    }

    public void setProfessors(String professors) {
        Professors = professors;
    }

    public String getAlumnes() {
        return Alumnes;
    }

    public void setAlumnes(String alumnes) {
        Alumnes = alumnes;
    }

    public String getPares() {
        return Pares;
    }

    public void setPares(String pares) {
        Pares = pares;
    }

    public String getDescripcio() {
        return Descripcio;
    }

    public void setDescripcio(String descripcio) {
        Descripcio = descripcio;
    }
}
    
    
