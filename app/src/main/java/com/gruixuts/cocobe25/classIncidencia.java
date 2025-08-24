package com.gruixuts.cocobe25;

import android.database.Cursor;
import java.text.SimpleDateFormat;
import java.util.Date;

public class classIncidencia {

    private String Id;
    private String Grup;
    private String Data;
    private String Estat;
    private String CodiAl;
    private String Alumne;
    private String Curs;
    private String Professor;
    private String Tutor;
    private String Titol;
    private String Descripcio;
    private String Imatges;


    public static final int LongNomImg = 4;  // Longitud del nom de la carpeta que conté les imatges

//    public static String NovaImatge() {
//        // Torna una cadena aleatoria formada per digits, majúscules i minúscules
//        String rslt="";
//        int n_aleat;
//        Random aleatori = new Random(System.currentTimeMillis());
//        for ( int n=0;n<LongNomImg;n++) {
//            n_aleat=aleatori.nextInt(61);
//            char c;
//            if (n_aleat < 10) {
//                c=(char) (n_aleat+48);  // Torna un dógit del 0 al 9
//            } else if (n_aleat< 36) {
//                c=(char) (n_aleat+55); // Torna majúscula de A a Z
//            } else {
//                c=(char) (n_aleat+61); // Torna minúscula de a a z
//            }
//            rslt +=c;
//        }
//        return rslt;
//    }

    public classIncidencia(String id, String grup, String data, String estat, String codiAl, String alumne, String curs, String professor, String tutor, String titol, String descripcio, String imatges ) {

        Id=id;
        Grup=grup;
        Data=data;
        Estat=estat;
        CodiAl=codiAl;
        Alumne=alumne;
        Curs=curs;
        Professor=professor;
        Tutor=tutor;
        Titol=titol;
        Descripcio=descripcio;
        Imatges=imatges;
    }

    public classIncidencia(Cursor cursor) {
        Id  = cursor.getString(0);
        Grup  = cursor.getString(1);
        Data  = cursor.getString(2);
        Estat  = cursor.getString(3);
        Professor  = cursor.getString(4);
        CodiAl  = cursor.getString(5);
        Alumne  = cursor.getString(6);
        Curs  = cursor.getString(7);
        Tutor  = cursor.getString(8);
        Titol  = cursor.getString(9);
        Descripcio  = cursor.getString(10);
        Imatges  = cursor.getString(11);

    }

    public classIncidencia(String[] camps) {
        Id  = camps[0];
        Grup  = camps[1];
        Data  = camps[2];
        Estat  = camps[3];
        Professor  = camps[4];
        CodiAl  = camps[5];
        Alumne  = camps[6];
        Curs  = camps[7];
        Tutor  = camps[8];
        Titol  = camps[9];
        Descripcio  = camps[10];
        Imatges  = camps[11];

    }

    public String toCSV() {
        String ret;
        ret =  Id+ ";";
        ret +=  Grup+ ";";
        ret +=  Data+ ";";
        ret +=  Estat+ ";";
        ret +=  Professor+ ";";
        ret +=  CodiAl+ ";";
        ret +=  Alumne+ ";";
        ret +=  Curs+ ";";
        ret +=  Tutor+ ";";
        ret +=  Titol+ ";";
        ret +=  Descripcio+ ";";
        ret +=  Imatges+ ";";
        return ret;
    }

    
    
    public classIncidencia(String id, String grup) {
        Id = id;
        Grup = grup;
//        Data = frmtData.format(cal.getTime());
        Data = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Estat = "O";
        Professor = "";
        CodiAl = "";
        Alumne = "";
        Curs = "";
        Tutor = "";
        Titol = "";
        Descripcio = "";
        Imatges = "";
    }



    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }

    public String getGrup() {
        return Grup;
    }
    public void setGrup(String grup) {
        Grup = grup;
    }

    public String getData() {
        return Data;
    }
    public void setData(String data) {
        Data = data;
    }

    public String getEstat() {
        return Estat;
    }

    public void setEstat(String estat) {
        Estat = estat;
    }

    public String getCodiAl() {
        return CodiAl;
    }
    public void setCodiAl(String codiAl) {
        CodiAl = codiAl;
    }

    public void setAlumne(String alumne) { Alumne = alumne; }

    public String getAlumne() { return Alumne; }

    public String getCurs() { return Curs; }

    public void setCurs(String curs) { Curs = curs; }

    public String getProfessor() {
        return Professor;
    }

    public void setProfessor(String professor) { Professor = professor; }

    public String getTutor() {
        return Tutor;
    }
    public void setTutor(String tutor) {
        Tutor = tutor;
    }

    public String getTitol() {
        return Titol;
    }
    public void setTitol(String titol) {
        Titol = titol;
    }

    public String getDescripcio() {
        return Descripcio;
    }
    public void setDescripcio(String descripcio) {
        Descripcio = descripcio;
    }

    public String getImatges() {
        return Imatges;
    }
    public void setImatges(String imatges) {
        Imatges = imatges;
    }

}

