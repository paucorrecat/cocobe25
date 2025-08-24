package com.gruixuts.cocobe25;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

// Sistema d'arxius. S'organitza en arxius i dins els arxius, carpetes (individuals)



public class Global {

    // Dialegs
    public static final String ARXIU_ALUMNES = "Alumnes";
    public static final String ARXIU_PROFESSORS = "Professors";
    public static final String ARXIU_INCIDENCIES = "Incidencies";


    public static void Missatge(String Missatge, String Titol, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(Missatge);
        builder.setTitle(Titol);
        builder.setCancelable(false);
        builder.setNeutralButton("Entesos",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void MissatgeError2(String Missatge, Context context) {
        Missatge(Missatge, "Error!!!",context);
    }

    public static void MissatgeError(String Missatge, Context context) {
        Toast.makeText(context,Missatge,Toast.LENGTH_SHORT).show();
    }

    public static boolean MissatgeSiNo(String Missatge, String Titol, Context context) {
        // MsgBox ¿?
        final boolean[] Resposta = new boolean[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(Missatge);
        builder.setTitle(Titol);
        builder.setCancelable(false);
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Resposta[0] =false;
                    }
                });
        builder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Resposta[0] =true;
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        return Resposta[0];
        // Fi MsgBox
    }

    // Paràmetres
    public static final String ARG_PREG_AVAL_ITEM_ID = "item_id";
    public static final String ARG_PREG_AVAL_RESP = "resposta";
    public static final String ARG_PREG_AVAL_AVAL = "avaluacio";

    public static String convertArrayListToString(ArrayList<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        for (String item : list) {
            result.append(item).append(" ");
        }

        // Elimina l'últim espai afegit

        return result.toString().trim();
    }

    public static String Veu2String(ArrayList<String> list) {
        String Result = "";
        boolean Maj = true;
        String[] paraules;
        if (list == null || list.isEmpty()) {
            return "";
        }
        for (Integer j=0;j<list.size();j++) {
            paraules = list.get(j).split(" ");
            for (Integer i=0; i<paraules.length; i++) {
                if (Maj) {
                    Result += FirstUpper(paraules[i]);
                    Maj=false;
                } else if (paraules[i].equals("coma")) {
                    Result += ",";
                } else if (paraules[i].equals("punt")) {
                    Result += ". ";
                    Maj=true;
                } else {
                    Result += " "+ paraules[i];
                }
            }
        }
        return Result;
    }

    private static String FirstUpper(String paraula) {
        if (paraula == null || paraula.isEmpty()) {
            return paraula; // Retornar el mateix string si és nul o buit
        }
        return paraula.substring(0, 1).toUpperCase() + paraula.substring(1);
    }
}
