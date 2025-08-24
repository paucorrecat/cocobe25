package com.gruixuts.cocobe25;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
// TODO later: Això és una txapussa usant una classe amb static, s'hauria de fer més simila a les llistes d'alumnes i professors

public class incLlistaTrobades {

    public static final List<classIncidencia> ITEMS = new ArrayList<classIncidencia>();
    public static final List<String> ITEMS_ALU = new ArrayList<String>();
//    public static final Map<String, classIncidencia> ITEM_MAP = new HashMap<String, classIncidencia>();
    public static String Filtre;

    public static void NouSQLtxt (String filtre, String ordre, Context c) {
        ITEMS.clear();
        ITEMS_ALU.clear();
        GestorDB db;
        classAlumne Alu;
        db= new GestorDB(c);
        ArrayList<classIncidencia> Llista;
        db.open();
        Llista = db.selIncidencies(filtre,ordre);
        for (Integer i = 0; i < Llista.size(); i++) {
            Alu = db.Possibles2objAlumne(Llista.get(i).getAlumne());
            ITEMS.add(Llista.get(i));
            if (Alu != null) {
                ITEMS_ALU.add(Alu.getNom() + " " + Alu.getCognom1());
            } else {
                ITEMS_ALU.add("");
            }
        }
         db.close();
        Filtre = filtre;
    }


}
