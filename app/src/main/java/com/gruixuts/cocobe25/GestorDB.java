package com.gruixuts.cocobe25;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class GestorDB {


    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "cocobe.db";

    private final Context context;
    private SQLiteDatabase db;
    private final DBHandler openHelper; //Gestor de base de datos


    public static SimpleDateFormat frmtData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String SeparadorClaus = "//";

    public static Date AData(String DataTxt) {
        return frmtData.parse(DataTxt, new ParsePosition(0));
    }

    public GestorDB(Context context) {
        this.context = context;
        this.openHelper = new DBHandler(this.context);
    }

    private static class DBHandler extends SQLiteOpenHelper {

        public DBHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("DBHandler.onCreate", "Es creen les noves taules");
            db.execSQL(Incidencia_TABLE_CREATE);
            db.execSQL(Alumne_TABLE_CREATE);
            db.execSQL(Professor_TABLE_CREATE);
            db.execSQL(Actuacio_TABLE_CREATE);
            Log.d("DBHandler", "S'han creat les noves taules");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //act_import_export.Exportar("Ultima_V" + oldVersion + "abans_de_V" + newVersion,context);
            db.execSQL("ALTER TABLE " + IncidenciaDef.TABLE_NAME + " RENAME TO " + IncidenciaDef.TABLE_NAME + "V" + oldVersion);
            db.execSQL("ALTER TABLE " + AlumneDef.TABLE_NAME + " RENAME TO " + AlumneDef.TABLE_NAME + "V" + oldVersion);
            db.execSQL("ALTER TABLE " + ProfessorDef.TABLE_NAME + " RENAME TO " + ProfessorDef.TABLE_NAME + "V" + oldVersion);
            //db.execSQL("DROP TABLE IF EXISTS " + DiccionariDef.TABLE_NAME);
            //db.execSQL("DROP TABLE IF EXISTS " + ProvesDef.TABLE_NAME);
            //db.execSQL("DROP TABLE IF EXISTS " + ResultatsDef.TABLE_NAME);
            onCreate(db);
        }
    }

    public GestorDB open() {
        try {
            this.db = openHelper.getWritableDatabase(); //Crea/abre la base de datos para la lectura/escritura
            return this;
        } catch (Exception ex) {
            Log.e("GestorDB", "Error al obrir la base de dades");
            // Todo later: Ser més clar, ara log.e queda amagat i no t'enteres, potser AlarmDialog
            Global.MissatgeError("GestorDB: No s'ha pogut obrir la base de dades " + ex.getMessage(), context);
            return null;
        }

    }

    public void close() {
        this.db.close();
    }

    //public void fesSQL(String txtSQL) {
    //    db.execSQL(txtSQL);
    //}

/************************************************************************************/
/*****************   Incidencies  ********************************************************/
    /************************************************************************************/

// <editor-fold Incidències>
    // Definició de les taules
    private static abstract class IncidenciaDef implements BaseColumns {
        public static final String TABLE_NAME = "Incidencies";
        // Pendent
        public static final String LLISTA_CAMPS = "Id, Grup, Data, Estat, Professor, CodiAl, Alumne, Curs, Tutor, Titol, Descripcio, Imatges";
        public static final String Id = "Id";
        public static final String Grup = "Grup";
        public static final String Data = "Data";
        public static final String Estat = "Estat";  //"T"=Tancada; "O"=Oberta
        public static final String Professor = "Professor";
        public static final String CodiAl = "CodiAl";  // Prioritari
        public static final String Alumne = "Alumne"; // Redundant
        public static final String Curs = "Curs";
        public static final String Tutor = "Tutor";
        public static final String Titol = "Titol";
        public static final String Descripcio = "Descripcio";
        public static final String Imatges = "Imatges";

    }

    // Sentencies per a la creació de taules
    private static final String Incidencia_TABLE_CREATE = "create table " + IncidenciaDef.TABLE_NAME
            + " (" + IncidenciaDef.Id + " text primary key, "
            + IncidenciaDef.Grup + " text, "
            + IncidenciaDef.Data + " text not null, "
            + IncidenciaDef.Estat + " text, "
            + IncidenciaDef.Professor + " text not null, "
            + IncidenciaDef.CodiAl + " text, "
            + IncidenciaDef.Alumne + " text not null, "
            + IncidenciaDef.Curs + " text, "
            + IncidenciaDef.Tutor + " text, "
            + IncidenciaDef.Titol + " text, "
            + IncidenciaDef.Descripcio + " text, "
            + IncidenciaDef.Imatges + " text ); ";

    public ArrayList<classIncidencia> selIncidencies(String Filtre, String Ordre) {
//    public ArrayList<classIncidencia> selIncidencies(String Filtre, String Ordre) {
        ArrayList<classIncidencia> list = new ArrayList<classIncidencia>();
        String SQLtxt;

        SQLtxt = "Select " + IncidenciaDef.LLISTA_CAMPS + " from " + IncidenciaDef.TABLE_NAME;
        if (Filtre.length() > 0) {
            SQLtxt += " where " + Filtre;
        }
        if (Ordre.length() > 0) {
            SQLtxt += " order by " + Ordre;
        }
        SQLtxt += " ;";

        return selIncSQL(SQLtxt);
    }

    public ArrayList<classIncidencia> selIncSQL(String SQLtxt) {
        ArrayList<classIncidencia> list = new ArrayList<classIncidencia>();
        boolean EstavaOberta = ObreDb();
        Cursor cursor = this.db.rawQuery(SQLtxt, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    classIncidencia entrada = new classIncidencia(cursor);
                    list.add(entrada);
                } catch (Exception ex) {
                    Log.e("GestorDB", "selIncidencia: Error al crear la incidència");
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {//Se cierra el cursor si no está cerrado ya
            cursor.close();
        }
        TancaDb(EstavaOberta);
        return list;
    }

    public String NouIdIncidencia() {
        String NouId = null;
        //open();
        Cursor cursor;
        String SQLtxt = "select max(Id) from " + IncidenciaDef.TABLE_NAME;
        cursor = this.db.rawQuery(SQLtxt, null);
        if (cursor.moveToFirst()) {
            NouId = cursor.getString(0);
        }
        if (NouId == null) {
            NouId = "000";
        }
        //db.close();
        return String.format("%03d", Integer.parseInt(NouId) + 1);

    }

    public void insIncidencia(classIncidencia ent) {
        ContentValues values = new ContentValues();

        values.put(IncidenciaDef.Id, ent.getId());
        values.put(IncidenciaDef.Grup, ent.getGrup());
        values.put(IncidenciaDef.Data, ent.getData());
        values.put(IncidenciaDef.Estat, ent.getEstat());
        values.put(IncidenciaDef.Professor, ent.getProfessor());
        values.put(IncidenciaDef.CodiAl, ent.getCodiAl());
        values.put(IncidenciaDef.Alumne, ent.getAlumne());
        values.put(IncidenciaDef.Curs, ent.getCurs());
        values.put(IncidenciaDef.Tutor, ent.getTutor());
        values.put(IncidenciaDef.Titol, ent.getTitol());
        values.put(IncidenciaDef.Descripcio, ent.getDescripcio());
        values.put(IncidenciaDef.Imatges, ent.getImatges());
        // Insertar...
        db.insert(IncidenciaDef.TABLE_NAME, null, values);
    }

    public void actIncidencia(classIncidencia ent) {
        ContentValues values = new ContentValues();
        values.put(IncidenciaDef.Id, ent.getId());
        values.put(IncidenciaDef.Grup, ent.getGrup());
        values.put(IncidenciaDef.Data, ent.getData());
        values.put(IncidenciaDef.Estat, ent.getEstat());
        values.put(IncidenciaDef.Professor, ent.getProfessor());
        values.put(IncidenciaDef.CodiAl, ent.getCodiAl());
        values.put(IncidenciaDef.Alumne, ent.getAlumne());
        values.put(IncidenciaDef.Curs, ent.getCurs());
        values.put(IncidenciaDef.Tutor, ent.getTutor());
        values.put(IncidenciaDef.Titol, ent.getTitol());
        values.put(IncidenciaDef.Descripcio, ent.getDescripcio());
        values.put(IncidenciaDef.Imatges, ent.getImatges());

        // Ens assegurem que la base de dades està oberta
        if (db == null || !db.isOpen()) {
            Log.e("GestorDB", "actIncidencia: La base de dades no està oberta");
            Global.MissatgeError("GestorDB: La base de dades no està oberta", context);
            return;
        }

        /* V01
        // Comprovem que el registre existeix
        Cursor cursor = db.query(IncIdenciaDef.TABLE_NAME, new String[]{IncIdenciaDef.Id}, "Id=?", new String[]{String.valueOf(ent.getId())}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            // Realitzem l'update
            int rowsAffected = db.update(IncIdenciaDef.TABLE_NAME, values, "Id=?", new String[]{String.valueOf(ent.getId())});
            if (rowsAffected > 0) {
                Log.i("GestorDB", "Incidencia: Registre actualitzat correctament");
            } else {
                Log.e("GestorDB", "Incidencia: No s'ha pogut actualitzar el registre");
                Global.MissatgeError("GestorDB. Incidència: No s'ha pogut actualitzar el registre" + String[]{String.valueOf(ent.getId())}, context);
            }
        } else {
            if (cursor != null) {
                cursor.close();
            }
            Log.e("GestorDB", "actIncidencia: El registre no existeix");
            Global.MissatgeError("GestorDB: El registre no existeix", context);
        }

         */
        // Comprovem que el registre existeix
        Cursor cursor = db.query(IncidenciaDef.TABLE_NAME, new String[]{IncidenciaDef.Id}, "Id=?", new String[]{ent.getId()}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            // Realitzem l'update
            int rowsAffected = db.update(IncidenciaDef.TABLE_NAME, values, "Id=?", new String[]{ent.getId()});
            if (rowsAffected > 0) {
                Log.i("GestorDB", "Incidencia: Registre actualitzat correctament");
            } else {
                Log.e("GestorDB", "Incidencia: No s'ha pogut actualitzar el registre");
                Global.MissatgeError("GestorDB. Incidència: No s'ha pogut actualitzar el registre" + ent.getId(), context);
            }
        } else {
            if (cursor != null) {
                cursor.close();
            }
            Log.e("GestorDB", "actIncidencia: El registre no existeix");
            Global.MissatgeError("GestorDB: El registre no existeix", context);
        }

    }

    public void delIncidencia(String Id) {
        Cursor cursor = db.rawQuery("SELECT Id FROM " + IncidenciaDef.TABLE_NAME + " WHERE " + IncidenciaDef.Id + "='" + Id + "'", null);
        if (cursor.moveToFirst()) {
            db.delete(IncidenciaDef.TABLE_NAME, "Id='" + Id + "'", null);
        }
    }

    public void delIncidencies() {
        boolean EstavaOberta = ObreDb();
        db.delete(IncidenciaDef.TABLE_NAME, "-1", null);
        TancaDb(EstavaOberta);
    }

    public String LlistaInc2Cadena(ArrayList<classIncidencia> llista) {
        String res = "";
        for (int i = 0; i < llista.size(); i++) {
            res = SeparadorClaus + llista.get(i).getId();
        }
        if (res.length() > 0) {
            res = res.substring(2);
        }
        return res;
    }

    public ArrayList<classIncidencia> Cadena2LlistyaInc(String cadena) {
        Cursor cursor;
        String[] claus;
        classIncidencia incidencia;
        ArrayList<classIncidencia> llista = new ArrayList<>();
        claus = cadena.split(SeparadorClaus);
        for (int i = 0; i < claus.length; i++) {
            cursor = db.rawQuery("SELECT " + IncidenciaDef.LLISTA_CAMPS + " FROM " + IncidenciaDef.TABLE_NAME + " WHERE " + IncidenciaDef.Id + "=" + claus[i], null);
            if (cursor.moveToFirst()) {
                incidencia = new classIncidencia(cursor);
                llista.add(incidencia);
            } else {
                Log.e("Cadena2LlistyaInc", "La clau[" + i + "]='" + claus[i] + "' no trobada");
            }
        }
        return llista;

    }

    //</editor-fold>

/***********************************************************************************************/
/*************************  Alumnes  ********************************************************/
    /***********************************************************************************************/

// <editor-fold Alumnes>
    private static abstract class AlumneDef implements BaseColumns {
        public static final String TABLE_NAME = "Alumnes";
        // Pendent
        public static final String LLISTA_CAMPS = "Codi, Nom, Cognom1, Cognom2, Curs, Observacions, Cntct1Tel, Cntct1Nom, Cntct1Rel, Cntct2Tel, Cntct2Nom, Cntct2Rel, Imatges";


        public static final String Codi = "Codi";  //Id del e-mail
        public static final String Nom = "Nom";
        public static final String Cognom1 = "Cognom1";
        public static final String Cognom2 = "Cognom2";
        public static final String Curs = "Curs";
        public static final String Observacions = "Observacions";
        public static final String Cntct1Tel = "Cntct1Tel";
        public static final String Cntct1Nom = "Cntct1Nom";
        public static final String Cntct1Rel = "Cntct1Rel";
        public static final String Cntct2Tel = "Cntct2Tel";
        public static final String Cntct2Nom = "Cntct2Nom";
        public static final String Cntct2Rel = "Cntct2Rel";
        public static final String Imatges = "Imatges";
    }

    public static String AlumneLlistaCamps() {
        return AlumneDef.LLISTA_CAMPS;
    }

    public static String AlumneTableName() {
        return AlumneDef.TABLE_NAME;
    }

    private static final String Alumne_TABLE_CREATE = "create table " + AlumneDef.TABLE_NAME + " ("
            + AlumneDef.Codi + " text  primary key, "
            + AlumneDef.Nom + " text, "
            + AlumneDef.Cognom1 + " text, "
            + AlumneDef.Cognom2 + " text, "
            + AlumneDef.Curs + " text, "
            + AlumneDef.Observacions + " text, "
            + AlumneDef.Cntct1Tel + " text, "
            + AlumneDef.Cntct1Nom + " text, "
            + AlumneDef.Cntct1Rel + " text, "
            + AlumneDef.Cntct2Tel + " text, "
            + AlumneDef.Cntct2Nom + " text, "
            + AlumneDef.Cntct2Rel + " text, "
            + AlumneDef.Imatges + " text ); ";


    public ArrayList<classAlumne> selAlumnes(String Filtre, String Ordre) {
        String SQLtxt;

        SQLtxt = "Select " + AlumneDef.LLISTA_CAMPS + " from " + AlumneDef.TABLE_NAME;
        if (Filtre.length() > 0) {
            SQLtxt += " where " + Filtre;
        }
        if (Ordre.length() > 0) {
            SQLtxt += " order by " + Ordre;
        }
        SQLtxt += " ;";
        Log.d("selAlumnes", "SQL:" + SQLtxt);
        return selAluSQL(SQLtxt);
    }

    public ArrayList<classAlumne> selAluSQL(String SQLtxt) {
        ArrayList<classAlumne> list = new ArrayList<classAlumne>();
        Log.d("selAluSQL", "Entra");
        boolean EstavaOberta = false;
        if (db == null || !db.isOpen()) {
            open();
            EstavaOberta = false;
        }
        Cursor cursor = this.db.rawQuery(SQLtxt, null);
        Log.d("selAluSQL", "cursor Ok");
        if (cursor.moveToFirst()) {
            Log.d("selAluSQL", "cursor Move to First Ok");
            do {
                try {
                    classAlumne entrada = new classAlumne(cursor);
                    list.add(entrada);
                } catch (Exception ex) {
                    Log.e("GestorDB", "selAlumne: Error al crear la incidència");
                }
            } while (cursor.moveToNext());
            Log.d("selAluSQL", "Llegides " + list.size() + " files");
        }

        if (cursor != null && !cursor.isClosed()) {//Se cierra el cursor si no está cerrado ya
            cursor.close();
        }
        if (!EstavaOberta) {
            db.close();
        }
        return list;
    }


    public void actAlumne(classAlumne alumne) {
        ContentValues values = new ContentValues();

        boolean EstavaOberta = ObreDb();

        values.put(AlumneDef.Codi, alumne.getCodi());
        values.put(AlumneDef.Nom, alumne.getNom());
        values.put(AlumneDef.Cognom1, alumne.getCognom1());
        values.put(AlumneDef.Cognom2, alumne.getCognom2());
        values.put(AlumneDef.Curs, alumne.getCurs());
        values.put(AlumneDef.Observacions, alumne.getObservacions());
        values.put(AlumneDef.Cntct1Tel, alumne.getCntct1Tel());
        values.put(AlumneDef.Cntct1Nom, alumne.getCntct1Nom());
        values.put(AlumneDef.Cntct1Rel, alumne.getCntct1Rel());
        values.put(AlumneDef.Cntct2Tel, alumne.getCntct2Tel());
        values.put(AlumneDef.Cntct2Nom, alumne.getCntct2Nom());
        values.put(AlumneDef.Cntct2Rel, alumne.getCntct2Rel());
        values.put(AlumneDef.Imatges, alumne.getImatges());

        // Comprovem que el registre existeix
        Cursor cursor = db.query(AlumneDef.TABLE_NAME, new String[]{AlumneDef.Codi}, "Codi=?", new String[]{alumne.getCodi()}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            // Realitzem l'update
            int rowsAffected = db.update(AlumneDef.TABLE_NAME, values, "Codi=?", new String[]{alumne.getCodi()});
            if (rowsAffected > 0) {
                Log.i("GestorDB", "Alumne: Registre actualitzat correctament");
            } else {
                Log.e("GestorDB", "Alumne: No s'ha pogut actualitzar el registre");
                Global.MissatgeError("GestorDB. Incidència: No s'ha pogut actualitzar el registre" + alumne.getCodi(), context);
            }
        } else {
            if (cursor != null) {
                cursor.close();
            }
            Log.e("GestorDB", "actAlumne: El registre no existeix");
            Global.MissatgeError("GestorDB: El registre no existeix", context);
        }
        TancaDb(EstavaOberta);

    }

    private boolean ObreDb() {
        if (db == null || !db.isOpen()) {
            open();
            return false;
        }
        return true;
    }

    private void TancaDb(boolean EstavaOberta) {
        if (!EstavaOberta) {
            db.close();
        }
    }

    // Versió Jul 2024
    public void insAlumne(classAlumne ent) {
        boolean EstavaOberta = ObreDb();
        String Valors = "'";
        Valors += ent.getCodi() + "','";
        Valors += ent.getNom() + "','";
        Valors += ent.getCognom1() + "','";
        Valors += ent.getCognom2() + "','";
        Valors += ent.getCurs() + "','";
        Valors += ent.getObservacions() + "','";
        Valors += ent.getCntct1Tel() + "','";
        Valors += ent.getCntct1Nom() + "','";
        Valors += ent.getCntct1Rel() + "','";
        Valors += ent.getCntct2Tel() + "','";
        Valors += ent.getCntct2Nom() + "','";
        Valors += ent.getCntct2Rel() + "','";
        Valors += ent.getImatges() + "'";

        String txtSQL = "INSERT INTO " + AlumneDef.TABLE_NAME + " (" + AlumneDef.LLISTA_CAMPS + ") values (" + Valors + ")";
        db.execSQL(txtSQL);
        TancaDb(EstavaOberta);
    }
    /* Versió original
    public void insAlumne(classAlumne ent) {
        ContentValues values = new ContentValues();
        values.put(AlumneDef.Codi, ent.getCodi());  //Id del e-mail
        values.put(AlumneDef.Nom, ent.getNom());
        values.put(AlumneDef.Cognom1, ent.getCognom1());
        values.put(AlumneDef.Cognom2, ent.getCognom2());
        values.put(AlumneDef.Curs, ent.getCurs());  // 3C   5 = 1r Btx  6 = 2n Btx
        values.put(AlumneDef.Observacions, ent.getObservacions());
        values.put(AlumneDef.Cntct1Tel, ent.getCntct1Tel());
        values.put(AlumneDef.Cntct1Nom, ent.getCntct1Nom());
        values.put(AlumneDef.Cntct1Rel, ent.getCntct1Rel());  //Pare, Mare, avi...
        values.put(AlumneDef.Cntct2Tel, ent.getCntct2Tel());
        values.put(AlumneDef.Cntct2Nom, ent.getCntct2Nom());
        values.put(AlumneDef.Cntct2Rel, ent.getCntct2Rel());
        values.put(AlumneDef.Imatges, ent.getImatges());
        db.insert(AlumneDef.TABLE_NAME, null, values);
    }
    
     */


    public ArrayList<String> PossiblesAlumnes(String Entrat) {
        String SQLtxt;
        ArrayList<String> suggeriments = new ArrayList<>();

        SQLtxt = "Select Nom || ' ' || Cognom1 || ' ' || Cognom2 || ' (' || Curs || ')' as NomComplert from Alumnes where (( Nom like '%" + Entrat + "%' ) OR ( Cognom1 like '%" + Entrat + "%' )) order by Curs, Cognom1, Cognom2, Nom";

        boolean EstavaOberta = ObreDb();

        Cursor cursor = this.db.rawQuery(SQLtxt, null);
        if (cursor.moveToFirst()) {
            do {
                suggeriments.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {//Se cierra el cursor si no está cerrado ya
            cursor.close();
        }
        TancaDb(EstavaOberta);

        return suggeriments;
    }

    public classAlumne Possibles2objAlumne(String Possible) {
        // Del String que surt de "PossiblesAlumnes" a l'objecte classAlumne
        String SQLtxt;
        classAlumne Resultat;
        SQLtxt = "Select " + AlumneDef.LLISTA_CAMPS + " from " + AlumneDef.TABLE_NAME + " where (Nom || ' ' || Cognom1 || ' ' || Cognom2 || ' (' || Curs || ')' = '" + Possible + "' )";
        boolean EstavaOberta = ObreDb();

        Cursor cursor = this.db.rawQuery(SQLtxt, null);
        if (cursor.moveToFirst()) {
            Resultat = new classAlumne(cursor); // Se suposa que només n'hi ha un, i si no (2 alumnes mateix nom, mateixos 2 cognoms al mateix curs), agafa el primer
        } else {
            Resultat = null;

        }
        if (cursor != null && !cursor.isClosed()) {//Se cierra el cursor si no está cerrado ya
            cursor.close();
        }
        TancaDb(EstavaOberta);
        return Resultat;

    }

    public void delAlumne(String codi) {
        boolean EstavaOberta = ObreDb();
        db.delete(AlumneDef.TABLE_NAME, "Codi='" + codi + "'", null);
        TancaDb(EstavaOberta);

    }

    public void delAlumnes() {
        boolean EstavaOberta = ObreDb();
        db.delete(AlumneDef.TABLE_NAME, "-1", null);
        TancaDb(EstavaOberta);
    }


    public void debug_MiraAlumnes() {
        String SQLtxt;
        ArrayList<String> suggeriments = new ArrayList<>();

        SQLtxt = "Select Codi, Nom, Cognom1, Cognom2, Curs from Alumnes";

        Cursor cursor = this.db.rawQuery(SQLtxt, null);
        if (cursor.moveToFirst()) {
            do {
                suggeriments.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {//Se cierra el cursor si no está cerrado ya
            cursor.close();
        }

    }

    public static String LlistaAlu2Cadena(ArrayList<classAlumne> llista) {
        String res = "";
        for (int i = 0; i < llista.size(); i++) {
            res += SeparadorClaus + llista.get(i).getCodi();
        }
        if (res.length() > 0) {
            res = res.substring(2);
        }
        return res;
    }

    public ArrayList<classAlumne> Cadena2LlistyaAlu(String cadena) {
        Cursor cursor;
        String[] claus;
        classAlumne Alumne;
        ArrayList<classAlumne> llista = new ArrayList<>();
        boolean EstavaOberta = ObreDb();

        claus = cadena.split(SeparadorClaus);
        for (int i = 0; i < claus.length; i++) {
            cursor = db.rawQuery("SELECT " + AlumneDef.LLISTA_CAMPS + " FROM " + AlumneDef.TABLE_NAME + " WHERE " + AlumneDef.Codi + "='" + claus[i] + "'", null);
            if (cursor.moveToFirst()) {
                Alumne = new classAlumne(cursor);
                llista.add(Alumne);
            } else {
                Log.e("Cadena2LlistyaInc", "La clau[" + i + "]='" + claus[i] + "' no trobada");
            }
        }
        TancaDb(EstavaOberta);
        return llista;

    }

    //</editor-fold>

/***********************************************************************************************/
/*************************  Professors  ********************************************************/
    /***********************************************************************************************/

// <editor-fold Professors>

    private static abstract class ProfessorDef implements BaseColumns {
        public static final String TABLE_NAME = "Professors";
        // Pendent
        public static final String LLISTA_CAMPS = "Codi, Nom, Cognom1, Cognom2, Rols, Estat, Observacions, eMail, Imatges";


        public static final String Codi = "Codi";  //Id del e-mail
        public static final String Nom = "Nom";
        public static final String Cognom1 = "Cognom1";
        public static final String Cognom2 = "Cognom2";
        public static final String Rols = "Rols";  // Rols codificats: T3C= Tutor de 3C; D3C= Equip Docent de 3C  1rBtx = 5è
        public static final String Estat = "Estat";
        public static final String Observacions = "Observacions";
        public static final String eMail = "eMail";
        public static final String Imatges = "Imatges";
    }

    public static String ProfessorLlistaCamps() {
        return ProfessorDef.LLISTA_CAMPS;
    }

    public static String ProfessorTableName() {
        return ProfessorDef.TABLE_NAME;
    }


    private static final String Professor_TABLE_CREATE = "create table " + ProfessorDef.TABLE_NAME + " ("
            + ProfessorDef.Codi + " text  primary key, "
            + ProfessorDef.Nom + " text, "
            + ProfessorDef.Cognom1 + " text, "
            + ProfessorDef.Cognom2 + " text, "
            + ProfessorDef.Rols + " text, "
            + ProfessorDef.Estat + " text, "
            + ProfessorDef.Observacions + " text, "
            + ProfessorDef.eMail + " text, "
            + ProfessorDef.Imatges + " text ); ";


    public ArrayList<classProfessor> selProfessors(String Filtre, String Ordre) {
        String SQLtxt;

        SQLtxt = "Select " + ProfessorDef.LLISTA_CAMPS + " from " + ProfessorDef.TABLE_NAME;
        if (Filtre.length() > 0) {
            SQLtxt += " where " + Filtre;
        }
        if (Ordre.length() > 0) {
            SQLtxt += " order by " + Ordre;
        }
        SQLtxt += " ;";

        return selPrfSQL(SQLtxt);
    }

    public void actProfessor(classProfessor profe) {
        ContentValues values = new ContentValues();

        boolean EstavaOberta = ObreDb();

        values.put(ProfessorDef.Codi, profe.getCodi());
        values.put(ProfessorDef.Nom, profe.getNom());
        values.put(ProfessorDef.Cognom1, profe.getCognom1());
        values.put(ProfessorDef.Cognom2, profe.getCognom2());
        values.put(ProfessorDef.Rols, profe.getRols());
        values.put(ProfessorDef.Estat, profe.getEstat());
        values.put(ProfessorDef.Observacions, profe.getObservacions());
        values.put(ProfessorDef.eMail, profe.geteMail());
        values.put(ProfessorDef.Imatges, profe.getImatges());

        // Comprovem que el registre existeix
        Cursor cursor = db.query(ProfessorDef.TABLE_NAME, new String[]{ProfessorDef.Codi}, "Codi=?", new String[]{profe.getCodi()}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            // Realitzem l'update
            int rowsAffected = db.update(ProfessorDef.TABLE_NAME, values, "Codi=?", new String[]{profe.getCodi()});
            if (rowsAffected > 0) {
                Log.i("GestorDB", "Professor: Registre actualitzat correctament");
            } else {
                Log.e("GestorDB", "Professor: No s'ha pogut actualitzar el registre");
                Global.MissatgeError("GestorDB. Incidència: No s'ha pogut actualitzar el registre" + profe.getCodi(), context);
            }
        } else {
            if (cursor != null) {
                cursor.close();
            }
            Log.e("GestorDB", "actProfessor: El registre no existeix");
            Global.MissatgeError("GestorDB: El registre no existeix", context);
        }
        TancaDb(EstavaOberta);

    }


    // Versió Jul 2024
    public void insProfessor(classProfessor ent) {
        boolean EstavaOberta = ObreDb();

        String Valors = "'";
        Valors += ent.getCodi() + "','";
        Valors += ent.getNom() + "','";
        Valors += ent.getCognom1() + "','";
        Valors += ent.getCognom2() + "','";
        Valors += ent.getRols() + "','";
        Valors += ent.getEstat() + "','";
        Valors += ent.getObservacions() + "','";
        Valors += ent.geteMail() + "','";
        Valors += ent.getImatges() + "'";

        String txtSQL = "INSERT INTO " + ProfessorDef.TABLE_NAME + " (" + ProfessorDef.LLISTA_CAMPS + ") values (" + Valors + ");";
        db.execSQL(txtSQL);
        TancaDb(EstavaOberta);
    }

    public ArrayList<classProfessor> selPrfSQL(String SQLtxt) {
        ArrayList<classProfessor> list = new ArrayList<classProfessor>();
        boolean EstavaOberta = ObreDb();
        Cursor cursor = this.db.rawQuery(SQLtxt, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    classProfessor entrada = new classProfessor(cursor);
                    list.add(entrada);
                } catch (Exception ex) {
                    Log.e("GestorDB.selProSQL", "selProfesor: Error al crear el professor");
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {//Se cierra el cursor si no está cerrado ya
            cursor.close();
        }
        TancaDb(EstavaOberta);
        return list;
    }

    public ArrayList<String> PossiblesProfessors(String Entrat) {
        String SQLtxt;
        ArrayList<String> suggeriments = new ArrayList<>();

        SQLtxt = "Select Nom || ' ' || Cognom1 as NomComplert from Professors where (( Nom like '%" + Entrat + "%' ) OR ( Cognom1 like '%" + Entrat + "%' ) OR ( Cognom2 like '%" + Entrat + "%' )OR ( Rols like '%" + Entrat + "%' )) order by Cognom1, Nom";

        boolean EstavaOberta = ObreDb();

        Cursor cursor = this.db.rawQuery(SQLtxt, null);
        if (cursor.moveToFirst()) {
            do {
                suggeriments.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {//Se cierra el cursor si no está cerrado ya
            cursor.close();
        }
        close();
        TancaDb(EstavaOberta);
        return suggeriments;
    }

    public String TutorDe(String curs) {
        String SQLtxt;
        String Tutor;

        SQLtxt = "Select Nom || ' ' || Cognom1 as NomComplert from Professors where  instr(Rols,'T" + curs + "') >0";

        boolean EstavaOberta = ObreDb();

        Cursor cursor = this.db.rawQuery(SQLtxt, null);
        if (cursor.moveToFirst()) {
            Tutor = cursor.getString(0);
        } else {
            Tutor = "";
        }
        if (cursor != null && !cursor.isClosed()) {//Se cierra el cursor si no está cerrado ya
            cursor.close();
        }
        TancaDb(EstavaOberta);

        return Tutor;
    }


    public void delProfessor(String codi) {
        boolean EstavaOberta = ObreDb();
        db.delete(ProfessorDef.TABLE_NAME, "Codi='" + codi + "'", null);
        TancaDb(EstavaOberta);

    }

    public void delProfessors() {

        boolean EstavaOberta = ObreDb();
        db.delete(ProfessorDef.TABLE_NAME, "-1", null);
        TancaDb(EstavaOberta);
    }


    /*
    public void debug_MiraProfessors() {
        String SQLtxt;
        ArrayList<String> suggeriments = new ArrayList<>();

        SQLtxt = "Select Codi, Nom, Cognom1, Cognom2, Curs from Professors";

        Cursor cursor = this.db.rawQuery(SQLtxt, null);
        if (cursor.moveToFirst()) {
            do {
                suggeriments.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {//Se cierra el cursor si no está cerrado ya
            cursor.close();
        }

    }

     */

    public static String LlistaPrf2Cadena(ArrayList<classProfessor> llista) {
        String res = "";
        for (int i = 0; i < llista.size(); i++) {
            res += SeparadorClaus + llista.get(i).getCodi();
        }
        if (res.length() > 0) {
            res = res.substring(2);
        }
        return res;
    }

    public ArrayList<classProfessor> Cadena2LlistyaPrf(String cadena) {
        Cursor cursor;
        String[] claus;
        classProfessor Professor;
        ArrayList<classProfessor> llista = new ArrayList<>();
        boolean EstavaOberta = ObreDb();

        claus = cadena.split(SeparadorClaus);
        for (int i = 0; i < claus.length; i++) {
            cursor = db.rawQuery("SELECT " + ProfessorDef.LLISTA_CAMPS + " FROM " + ProfessorDef.TABLE_NAME + " WHERE " + ProfessorDef.Codi + "='" + claus[i] + "'", null);
            if (cursor.moveToFirst()) {
                Professor = new classProfessor(cursor);
                llista.add(Professor);
            } else {
                Log.e("Cadena2LlistyaInc", "La clau[" + i + "]='" + claus[i] + "' no trobada");
            }
        }
        TancaDb(EstavaOberta);
        return llista;

    }

    //</editor-fold>

/***********************************************************************************************/
/*************************  Actuacions  ********************************************************/
    /***********************************************************************************************/

// <editor-fold Actuacions>

    private static abstract class ActuacioDef implements BaseColumns {
        public static final String TABLE_NAME = "Actuacions";
        // Pendent
        public static final String LLISTA_CAMPS = "Id, Data, Pendent, Tipus, Titol, Professors, Alumnes, Pares, Descripcio";

        public static final String Id = "Id";
        public static final String Data = "Data";
        public static final String Pendent = "Pendent";
        public static final String Tipus = "Tipus";
        public static final String Titol = "Titol";
        public static final String Professors = "Professors";
        public static final String Alumnes = "Alumnes";
        public static final String Pares = "Pares";
        public static final String Descripcio = "Descripcio";

    }


    private static final String Actuacio_TABLE_CREATE = "create table " + ActuacioDef.TABLE_NAME + " ("
            + ActuacioDef.Id + " text primary key, "
            + ActuacioDef.Data + " text, "
            + ActuacioDef.Pendent + " integer, "
            + ActuacioDef.Tipus + " text, "
            + ActuacioDef.Titol + " text, "
            + ActuacioDef.Professors + " text, "
            + ActuacioDef.Alumnes + " text, "
            + ActuacioDef.Pares + " text, "
            + ActuacioDef.Descripcio + " text ); ";


    public static String ActuacioLlistaCamps() {
        return ActuacioDef.LLISTA_CAMPS;
    }

    public static String ActuacioTableName() {
        return ActuacioDef.TABLE_NAME;
    }


    public ArrayList<classActuacio> selActuacions(String Filtre, String Ordre) {
        String SQLtxt;

        SQLtxt = "Select " + ActuacioDef.LLISTA_CAMPS + " from " + ActuacioDef.TABLE_NAME;
        if (Filtre.length() > 0) {
            SQLtxt += " where " + Filtre;
        }
        if (Ordre.length() > 0) {
            SQLtxt += " order by " + Ordre;
        }
        SQLtxt += " ;";

        return selAccSQL(SQLtxt);
    }

    public void actActuacio(classActuacio profe) {
        ContentValues values = new ContentValues();

        boolean EstavaOberta = ObreDb();

        values.put(ActuacioDef.Id, profe.getId());
        values.put(ActuacioDef.Data, profe.getData());
        values.put(ActuacioDef.Pendent, profe.getPendent() ? classActuacio.PENDENT_PENDENT : classActuacio.PENDENT_FET);
        values.put(ActuacioDef.Tipus, profe.getTipus());
        values.put(ActuacioDef.Titol, profe.getTitol());
        values.put(ActuacioDef.Professors, profe.getProfessors());
        values.put(ActuacioDef.Alumnes, profe.getAlumnes());
        values.put(ActuacioDef.Pares, profe.getPares());
        values.put(ActuacioDef.Descripcio, profe.getDescripcio());

        // Comprovem que el registre existeix
        Cursor cursor = db.query(ActuacioDef.TABLE_NAME, new String[]{ActuacioDef.Id}, "Id=?", new String[]{profe.getId()}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            // Realitzem l'update
            int rowsAffected = db.update(ActuacioDef.TABLE_NAME, values, "Id=?", new String[]{profe.getId()});
            if (rowsAffected > 0) {
                Log.i("GestorDB", "Actuacio: Registre actualitzat correctament");
            } else {
                Log.e("GestorDB", "Actuacio: No s'ha pogut actualitzar el registre");
                Global.MissatgeError("GestorDB. Incidència: No s'ha pogut actualitzar el registre" + profe.getId(), context);
            }
        } else {
            if (cursor != null) {
                cursor.close();
            }
            Log.e("GestorDB", "actActuacio: El registre no existeix");
            Global.MissatgeError("GestorDB: El registre no existeix", context);
        }
        TancaDb(EstavaOberta);

    }

    public String insActuacio(String IdInc) {
        boolean EstavaOberta = ObreDb();
        String nouId = null;
        //open();
        Cursor cursor;
        String SQLtxt = "select max(Id) from " + ActuacioDef.TABLE_NAME + " where Id like '" + IdInc + "-%'";
        cursor = this.db.rawQuery(SQLtxt, null);
        nouId = "000";
        if (cursor.moveToFirst()) {
//        if (cursor.getString(0) != null) {
            String IdMax = cursor.getString(0);
            if (IdMax != null && cursor.getString(0).length() == 7) {
                nouId = cursor.getString(0).substring(5);
            }
        }
        //db.close();
        nouId = IdInc + "-" + String.format("%03d", Integer.parseInt(nouId) + 1);
        classActuacio item = new classActuacio(nouId);
        insActuacio(item);
        TancaDb(EstavaOberta);
        return nouId;

    }


    // Versió Jul 2024
    public void insActuacio(classActuacio ent) {
        String Valors = "'";
        Valors += ent.getId() + "','";
        Valors += ent.getData() + "','";
        Valors += (ent.getPendent() ? classActuacio.PENDENT_PENDENT : classActuacio.PENDENT_FET) + "','";
        Valors += ent.getTipus() + "','";
        Valors += ent.getTitol() + "','";
        Valors += ent.getProfessors() + "','";
        Valors += ent.getAlumnes() + "','";
        Valors += ent.getPares() + "','";
        Valors += ent.getDescripcio() + "'";

        String txtSQL = "INSERT INTO " + ActuacioDef.TABLE_NAME + " (" + ActuacioDef.LLISTA_CAMPS + ") values (" + Valors + ")";
        boolean EstavaOberta = ObreDb();
        db.execSQL(txtSQL);
        TancaDb(EstavaOberta);

    }

    public ArrayList<classActuacio> selAccSQL(String SQLtxt) {
        ArrayList<classActuacio> list = new ArrayList<classActuacio>();
        boolean EstavaOberta = ObreDb();
        Cursor cursor = this.db.rawQuery(SQLtxt, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    classActuacio entrada = new classActuacio(cursor);
                    list.add(entrada);
                } catch (Exception ex) {
                    Log.e("GestorDB.selActSQL", "selActuació: Error al crear el Actuacio");
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {//Se cierra el cursor si no está cerrado ya
            cursor.close();
        }
        TancaDb(EstavaOberta);
        return list;
    }

    public void delActuacio(String id) {
        boolean EstavaOberta = ObreDb();
        db.delete(ActuacioDef.TABLE_NAME, "Id='" + id + "'", null);
        TancaDb(EstavaOberta);
    }

    public void delActuacions() {
        boolean EstavaOberta = ObreDb();
        db.delete(ActuacioDef.TABLE_NAME, "-1", null);
        TancaDb(EstavaOberta);
    }

    public static String LlistaAcc2Cadena(ArrayList<classActuacio> llista) {
        String res = "";
        for (int i = 0; i < llista.size(); i++) {
            res += SeparadorClaus + llista.get(i).getId();
        }
        if (res.length() > 0) {
            res = res.substring(2);
        }
        return res;
    }

    public ArrayList<classActuacio> Cadena2LlistyaAcc(String cadena) {
        Cursor cursor;
        String[] claus;
        classActuacio Actuacio;
        ArrayList<classActuacio> llista = new ArrayList<>();
        boolean EstavaOberta = ObreDb();

        claus = cadena.split(SeparadorClaus);
        for (int i = 0; i < claus.length; i++) {
            cursor = db.rawQuery("SELECT " + ActuacioDef.LLISTA_CAMPS + " FROM " + ActuacioDef.TABLE_NAME + " WHERE " + ActuacioDef.Id + "='" + claus[i] + "'", null);
            if (cursor.moveToFirst()) {
                Actuacio = new classActuacio(cursor);
                llista.add(Actuacio);
            } else {
                Log.e("Cadena2LlistyaAcc", "La clau[" + i + "]='" + claus[i] + "' no trobada");
            }
        }
        TancaDb(EstavaOberta);
        return llista;

    }
}

// </editor-fold>


