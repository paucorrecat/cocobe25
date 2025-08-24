package com.gruixuts.cocobe25;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/////////////////////////////////////////////////////////////////////////////////////////////
//
// Quan importes fa una copia a
// Emmagatzematge intern compartit/Android/data/com.gruixuts.geniuscares9/files/Copies
// NOTA: aquest lloc s'obté
//            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
//            File mm=contextWrapper.getExternalFilesDir("Copies");
//            mm.toString();
//
//   Importar db no funciona i no sé perquè
//   Cal importar en format fitxaer.txt
//   Ara per ara funciona cpompiant del fitxer AImportar.txt a la carpeta compartit/Android/data/com.gruixuts.cocobe_dev/files/Dades
// I fent anar "IMPORTAR COPIA"
//
// Exemple de fitxer:
//        V,9
//        A,1,3A01,Júlia,Bolivar,Tinoco,3A,3A01,RP22-23,,,a,,true
//        A,2,3A02,Marco,Bonilla,Guevara,3A,3A02,RP22-23,,,a,,true
//        A,3,3A03,Tarik,Boukhadra,,3A,3A03,RP22-23,,,a,,true
//        A,4,3A04,Joel,Bueno,Donat,3A,3A04,RP22-23,,,a,,true
//        A,5,3A05,Ainhoa,Domínguez,Molina,3A,3A05,RP22-23,,,a,,true
//        A,6,3A06,Hèctor,Elena,Rodriguez,3A,3A06,RP22-23,,,a,,true



//

public class act_import_export  extends AppCompatActivity {

    SimpleDateFormat frmtData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static String Separador = ";";
    TextView estat;
    static String CarpetaExport; //= Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pau/GeniusCares/Export";
    static String CarpetaImport; //= Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pau/GeniusCares/Import";
    static String CarpetaCopies; // = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pau/GeniusCares/Copies";
    static String CarpetaDades; // = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pau/GeniusCares/Copies";
    static String CarpetaDb;

    CheckBox chkImpAlumnes;
    CheckBox chkImpProfessors;
    CheckBox chkImpIncidencies;
    CheckBox chkImpActuacions;
    View ledView;
    GradientDrawable dibuixLED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        estat = findViewById(R.id.txtEstat);
        estat.setText("preparat");
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        CarpetaCopies=contextWrapper.getExternalFilesDir("Copies").toString();//  o Environment.DIRECTORY_DOCUMENTS enlloc de Copies
        CarpetaExport=contextWrapper.getExternalFilesDir("Export").toString();//  o Environment.DIRECTORY_DOCUMENTS enlloc de Dades
        CarpetaImport=contextWrapper.getExternalFilesDir("Import").toString();//  o Environment.DIRECTORY_DOCUMENTS enlloc de Dades
        CarpetaDades=contextWrapper.getExternalFilesDir("Dades").toString();//  o Environment.DIRECTORY_DOCUMENTS enlloc de Dades

        isStoragePermissionGranted();

        ((TextView) findViewById(R.id.txtImpCarpetes)).setText(contextWrapper.getExternalFilesDir("").toString());
        chkImpAlumnes = findViewById(R.id.chkImpAlumnes);
        chkImpProfessors = findViewById(R.id.chkImpProfessors);
        chkImpIncidencies = findViewById(R.id.chkImpIncidencies);
        chkImpActuacions = findViewById(R.id.chkImpActuacions);
        ledView = findViewById(R.id.ledView);
        dibuixLED = (GradientDrawable) ledView.getBackground();


    }

    // Funció actualitzada per Android 11+ - No necessita permisos especials per directoris de l'app
    public boolean isStoragePermissionGranted() {
        // A partir d'Android 11, l'accés als directoris específics de l'app no requereix permisos especials
        // Els directoris getExternalFilesDir() són accessibles sense permisos
        return true;
    }

    public void ImportarCSV(View view) {
        chkImpAlumnes.setTextColor(Color.BLACK);
        chkImpProfessors.setTextColor(Color.BLACK);
        chkImpIncidencies.setTextColor(Color.BLACK);
        chkImpActuacions.setTextColor(Color.BLACK);
        if (chkImpAlumnes.isChecked()) {
            if (ImportarCSV("Alumnes")) {
                chkImpAlumnes.setTextColor(Color.GREEN);
            } else {
                chkImpAlumnes.setTextColor(Color.RED);
            }
        }
        if (chkImpProfessors.isChecked()) {
            if (ImportarCSV("Professors")) {
                chkImpProfessors.setTextColor(Color.GREEN);
            } else {
                chkImpProfessors.setTextColor(Color.RED);
            }
        }

        if (chkImpIncidencies.isChecked()) {
            if (ImportarCSV("Incidencies")) {
                chkImpIncidencies.setTextColor(Color.GREEN);
            } else {
                chkImpIncidencies.setTextColor(Color.RED);
            }
        }
        if (chkImpActuacions.isChecked()) {
            if (ImportarCSV("Actuacions")) {
                chkImpActuacions.setTextColor(Color.GREEN);
            } else {
                chkImpActuacions.setTextColor(Color.RED);
            }
        }

    }

    public void ExportarCSV(View view) {
        if (chkImpAlumnes.isChecked()) {
            if (ExportAlumnes(false)) {
                chkImpAlumnes.setTextColor(Color.GREEN);
            } else {
                chkImpAlumnes.setTextColor(Color.RED);
            }
        }
        if (chkImpProfessors.isChecked()) {
            if (ExportProfessors(false)) {
                chkImpProfessors.setTextColor(Color.GREEN);
            } else {
                chkImpProfessors.setTextColor(Color.RED);
            }
        }

        if (chkImpIncidencies.isChecked()) {
            if (ExportIncidencies(false)) {
                chkImpIncidencies.setTextColor(Color.GREEN);
            } else {
                chkImpIncidencies.setTextColor(Color.RED);
            }
        }
        if (chkImpActuacions.isChecked()) {
            if (ExportActuacions(false)) {
                chkImpActuacions.setTextColor(Color.GREEN);
            } else {
                chkImpActuacions.setTextColor(Color.RED);
            }
        }

    }

    public boolean ImportarCSV(String fit) {
        GestorDB db = new GestorDB(getApplicationContext());

        //String[] camps;
        Integer NumCamps;
        File fitxer;
        BufferedReader Buf; // Buffer del fitxer
        String txt; // On es llegeix cada línia
        long NumLin = 0;
        String nomfit = fit+".csv";

        try {
            // Controlem que el fitxer existeix
            fitxer = new File(CarpetaImport,nomfit);
            if (!fitxer.exists()) {
                Log.d("ImportarCSV", "El fitxer '" + nomfit + "' no existeix");
                estat.setText("El fitxer " + nomfit + " no existeix a " + CarpetaImport);
                return false;
            }
        } catch (Exception e) {
            Log.e("ImportarCSV", "Excepció: "+ e.getMessage());
            estat.setText("Error al mirar si el fitxer '" + nomfit + "' existeix: " + e.getMessage());
            return false;
        }
        estat.setText("Important de " + nomfit);
        Log.d("ImportarCSV", "Fitxer obert: "+ nomfit);

        try {
            // Llegim les dades
            Buf = new BufferedReader((new InputStreamReader(new FileInputStream(fitxer))));
            db.open();
            Log.d("ImportarCSV","db oberta");
            switch (fit) {
                case "Alumnes":
                    if (ExportAlumnes(true)) {
                        Log.d("ImportarCSV", fit+ ": Copia de seguretat feta");
                        db.delAlumnes();  //Buidem tot lo anterior
                        Log.d("ImportarCSV", fit+ ": Dades actuals esborrades");
                        NumCamps = classAlumne.NumCamps();
                    } else {
                        Log.d("ImportarCSV", nomfit + ": No pot fer copia. No esborra, no importa");
                        return false;
                    }
                    break;
                case "Professors":
                    if (ExportProfessors(true)) {
                        Log.d("ImportarCSV", fit+ ": Copia de seguretat feta");
                        db.delProfessors();  //Buidem tot lo anterior
                        Log.d("ImportarCSV", fit+ ": Dades actuals esborrades");
                        NumCamps = classProfessor.NumCamps();
                    } else {
                        Log.d("ImportarCSV", nomfit + ": No pot fer copia. No esborra, no importa");
                        return false;
                    }
                    break;
                case "Incidencies":
                    if (ExportIncidencies(true)) {
                        Log.d("ImportarCSV", fit+ ": Copia de seguretat feta");
                        db.delIncidencies();  //Buidem tot lo anterior
                        Log.d("ImportarCSV", fit+ ": Dades actuals esborrades");
                        NumCamps = classProfessor.NumCamps();
                    } else {
                        Log.d("ImportarCSV", nomfit + ": No pot fer copia. No esborra, no importa");
                        return false;
                    }
                    break;
                case "Actuacions":
                    if (ExportActuacions(true)) {
                        Log.d("ImportarCSV", fit+ ": Copia de seguretat feta");
                        db.delActuacions();  //Buidem tot lo anterior
                        Log.d("ImportarCSV", fit+ ": Dades actuals esborrades");
                        NumCamps = classProfessor.NumCamps();
                    } else {
                        Log.d("ImportarCSV", nomfit + ": No pot fer copia. No esborra, no importa");
                        return false;
                    }
                    break;
                default:
                    Log.d("ImportarCSV", nomfit + ": No existeix aquesta opció");
                    return false;

            }
            Log.d("ImportarCSV","elimina la taula antiga");
            String txtSQL;
            while ((txt = Buf.readLine()) != null) {
                NumLin++;
              //  txt += ";Fi"; // per assegurarme de que agafa tots els camps (inclús els buits), després surt un camp més però no importa
                String[] camps = new String[NumCamps];
                String[] llegit;
                llegit = txt.split(Separador);
                System.arraycopy(llegit,0,camps,0, llegit.length);
                for (int i=llegit.length; i<NumCamps;i++) {
                    camps[i]="";
                }
                switch (fit) {
                    case "Alumnes":
                        db.insAlumne(new classAlumne(camps));
                        break;
                    case "Professors":
                        db.insProfessor(new classProfessor(camps));
                        break;
                    case "Incidencies":
                        db.insIncidencia(new classIncidencia(camps));
                        break;
                    case "Actuacions":
                        db.insActuacio(new classActuacio(camps));
                        break;
                }

//                db.debug_MiraAlumnes();
            }
            db.close();

        } catch (Exception e) {
            String text= "Dades no carregades: NumLin=" + NumLin + " " + e.getMessage();
            Log.d("ImportarCSV",text);
            Toast.makeText(act_import_export.this, text, Toast.LENGTH_LONG).show();
            estat.setText(text);
            return false;
        }

        //db.debug_MiraAlumnes();
        estat.setText("Tot importat correctament de " + nomfit);
        return true;
    }

    public boolean ExportAlumnes(boolean EsCopiaSeg) {

        GestorDB db = new GestorDB(getApplicationContext());
        ArrayList<classAlumne> LlistaAlumnes;
        OutputStreamWriter fout;
        String nomfit;
        String Carpeta;

        if (EsCopiaSeg) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            nomfit = "Alumnes_bak_" + timeStamp + ".csv";
            Carpeta = CarpetaCopies;
            Log.d("ExportAlumne","Es fa copia de seguretat d'Alumnes a "+ nomfit);
        } else {
            nomfit = "Alumnes.csv";
            Carpeta = CarpetaExport;
        }

        LlistaAlumnes = db.selAlumnes("","Curs, Cognom1, Cognom2");
        Log.d("ExportAlumne: ","Hi ha " + LlistaAlumnes.size() + " alumnes\n a " + nomfit);

        try {
            File fitxer = new File(Carpeta, nomfit);
//            if (fitxer.exists()) {
//                Log.e("ExportAlumne","Fitxer ja existeix. No es pot fer la copia de seguretat. \n" +
//                        fitxer.getAbsolutePath());
//                return false;
//            }
            fout = new OutputStreamWriter(new FileOutputStream(fitxer));
            for (int n = 0; n < LlistaAlumnes.size(); n++) {
                try {
                    fout.write(LlistaAlumnes.get(n).toCSV() + "\n");
                } catch (Exception e) {
                    Log.e("ExportAlumne","Error al registre " + n +": "+LlistaAlumnes.get(n).toCSV());
                }
            }
            fout.close();
        } catch (Exception e) {
            Log.e("ExportAlumne", "Error al obrir el fitxer \n" + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean ExportProfessors(boolean EsCopiaSeg) {

        GestorDB db = new GestorDB(getApplicationContext());
        ArrayList<classProfessor> LlistaProfessors;
        OutputStreamWriter fout;
        String nomfit;
        String Carpeta;

        if (EsCopiaSeg) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            nomfit = "Professors_bak_" + timeStamp + ".csv";
            Carpeta = CarpetaCopies;
        } else {
            nomfit = "Professors.csv";
            Carpeta = CarpetaExport;
        }

        LlistaProfessors = db.selProfessors("","Cognom1, Nom");
        Log.d("ExportProfessor","Hi ha " + LlistaProfessors.size() + " Professors \n a " + nomfit);

        try {
            File fitxer = new File(Carpeta, nomfit);
//            if (fitxer.exists()) {
//                Log.e("ExportProfessor","Fitxer ja existeix. No es pot fer la copia de seguretat. \n" +
//                        fitxer.getAbsolutePath());
//                return false;
//            }
            fout = new OutputStreamWriter(new FileOutputStream(fitxer));
            for (int n = 0; n < LlistaProfessors.size(); n++) {
                try {
                    fout.write(LlistaProfessors.get(n).toCSV() + "\n");
                } catch (Exception e) {
                    Log.e("ExportProfessor","Error al registre " + n +": "+LlistaProfessors.get(n).toCSV());
                }
            }
            fout.close();
        } catch (Exception e) {
            Log.e("ExportProfessor", "Error al obrir el fitxer \n" + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean ExportIncidencies(boolean EsExport) {

        GestorDB db = new GestorDB(getApplicationContext());
        ArrayList<classIncidencia> LlistaIncidencies;
        OutputStreamWriter fout;
        String nomfit;
        String Carpeta;

        if (EsExport) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            nomfit = "Incidencies_bak_" + timeStamp + ".csv";
            Carpeta = CarpetaCopies;
        } else {
            nomfit = "Incidencies.csv";
            Carpeta = CarpetaExport;
        }

        LlistaIncidencies = db.selIncidencies("","Id");
        Log.d("ExportIncidencia","Hi ha " + LlistaIncidencies.size() + " Incidencies \n a " + nomfit);

        try {
            File fitxer = new File(Carpeta, nomfit);
//            if (fitxer.exists()) {
//                Log.e("ExportIncidencia","Fitxer ja existeix. No es pot fer la copia de seguretat. \n" +
//                        fitxer.getAbsolutePath());
//                return false;
//            }
            fout = new OutputStreamWriter(new FileOutputStream(fitxer));
            for (int n = 0; n < LlistaIncidencies.size(); n++) {
                try {
                    fout.write(LlistaIncidencies.get(n).toCSV() + "\n");
                } catch (Exception e) {
                    Log.e("ExportIncidencia","Error al registre " + n +": "+LlistaIncidencies.get(n).toCSV());
                }
            }
            fout.close();
        } catch (Exception e) {
            Log.e("ExportIncidencia", "Error al obrir el fitxer \n" + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean ExportActuacions(boolean EsCopiaSeg) {

        GestorDB db = new GestorDB(getApplicationContext());
        ArrayList<classActuacio> LlistaActuacions;
        OutputStreamWriter fout;
        String nomfit;
        String Carpeta;

        if(EsCopiaSeg) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            nomfit = "Actuacions_bak_" + timeStamp + ".csv";
            Carpeta = CarpetaCopies;
        } else {
            nomfit = "Actuacions.csv";
            Carpeta = CarpetaExport;
        }


        LlistaActuacions = db.selActuacions("","Id");
        Log.d("ExportActuacio","Hi ha " + LlistaActuacions.size() + " Actuacions \n a " + nomfit);

        try {
            File fitxer = new File(Carpeta, nomfit);
//            if (fitxer.exists()) {
//                Log.e("ExportActuacio","Fitxer ja existeix. No es pot fer la copia de seguretat. \n" +
//                        fitxer.getAbsolutePath());
//                return false;
//            }
            fout = new OutputStreamWriter(new FileOutputStream(fitxer));
            for (int n = 0; n < LlistaActuacions.size(); n++) {
                try {
                    fout.write(LlistaActuacions.get(n).toCSV() + "\n");
                } catch (Exception e) {
                    Log.e("ExportActuacio","Error al registre " + n +": "+LlistaActuacions.get(n).toCSV());
                }
            }
            fout.close();
        } catch (Exception e) {
            Log.e("ExportActuacio", "Error al obrir el fitxer \n" + e.getMessage());
            return false;
        }
        return true;
    }

    public void ImportarDb_ant(View view) {
        // Ara copiem la base de dades

        //String Dades = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pau/GeniusCares/Dades/AImportar.db";
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        String Dades=contextWrapper.getExternalFilesDir("Dades").toString() + "/AImportar.db";

        File origin = new File(Dades);
        File destination = new File(getDatabasePath("Gestor.db").getAbsolutePath());

        if (origin.exists()) {
          //  Exportar(view);
            try {
                InputStream in = new FileInputStream(origin);
                OutputStream out = new FileOutputStream(destination);
                // We use a buffer for the copy (Usamos un buffer para la copia).
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                MissatgeError("Base de dades no copiada: " + ioe.getMessage(),view.getContext());
            }
        } else {
            MissatgeError("Base de dades AImportar.db no trobada",view.getContext());
        }
    }

    public void ImportaDb(View view) {
        // Origen: fitxer extern
        // Abans podria fer una copia

        CopiaSegDb(view);
        Log.d("ImportarDb","Copia soguretat dades feta");

        File src = new File(CarpetaImport + "/cocobe.db");

        if (!src.exists()) {
            MissatgeError("Base de dades cocobe.db no trobada a /Import/",view.getContext());
            dibuixLED.setColor(Color.RED);
        }
        // Destinació: directori de bases de dades de l'aplicació
        File dst = new File(getDatabasePath("cocobe.db").getAbsolutePath());

        try (FileChannel inChannel = new FileInputStream(src).getChannel();
             FileChannel outChannel = new FileOutputStream(dst).getChannel()) {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            dibuixLED.setColor(Color.GREEN);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            Log.e("ImportaDb","Copia base de dades no feta " );
            dibuixLED.setColor(Color.RED);
        }
    }

    public void CopiaSegDb(View view)  {
        // Origen: base de dades de l'aplicació
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File src = new File(getDatabasePath("cocobe.db").getAbsolutePath());

        // Destinació: directori accessible
        File dst = new File(CarpetaCopies + "/cocobe_bak_" + timeStamp + ".db");

        try (FileChannel inChannel = new FileInputStream(src).getChannel();
             FileChannel outChannel = new FileOutputStream(dst).getChannel()) {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            dibuixLED.setColor(Color.GREEN);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            Log.e("CopiaSegDb","Copia base de dades no feta " );
            dibuixLED.setColor(Color.RED);
        }
    }

    public void ExportaDb(View view)  {
        // Origen: base de dades de l'aplicació
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File src = new File(getDatabasePath("cocobe.db").getAbsolutePath());

        // Destinació: directori accessible
        File dst = new File(CarpetaExport + "/cocobe.db");

        try (FileChannel inChannel = new FileInputStream(src).getChannel();
             FileChannel outChannel = new FileOutputStream(dst).getChannel()) {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            dibuixLED.setColor(Color.GREEN);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            Log.e("ExportaDb","Copia base de dades no feta " );
            dibuixLED.setColor(Color.RED);
        }
    }


    static private void MissatgeError(String Missatge, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(Missatge);
        builder.setTitle("Error!!!");
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


}
