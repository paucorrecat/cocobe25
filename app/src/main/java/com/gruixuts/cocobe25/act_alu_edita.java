package com.gruixuts.cocobe25;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class act_alu_edita extends AppCompatActivity {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_LLISTA = "item_llista";
    private int numIndex; // Index 0..N del item que s'està veient dins la llista @objLlistaTrobades
    private classAlumne mItem;
    GestorDB db;
    ArrayList<classAlumne> llistaALumnes;


    // Imatges
    private String CarpetaImatges;
    private String CarpetaImatgesItem;  // La carpeta del Item actual
    private frg_inc_imatges fragment_imatges;
    private Integer numImatge;
    private String nomsImatge[] = {};
    static final int REQUEST_IMAGE_CAPTURE = 17;

    protected static final int REQUEST_CODE = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alu_edita);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new GestorDB(getApplicationContext());
        Log.d("act_alu_edita.OnCreate", "Entra");
        numIndex = Integer.parseInt(getIntent().getStringExtra(ARG_ITEM_ID));
        String mcadena = getIntent().getStringExtra(ARG_ITEM_LLISTA);
        Log.d("act_alu_edita.OnCreate", "cadena = '" + mcadena + "'");
        llistaALumnes = db.Cadena2LlistyaAlu(mcadena);
        assert (numIndex < llistaALumnes.size());

        //        CarpetaImatges = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pau/GeniusCares/Imatges";  // Es veu que això no està permès ja ¿¿??
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());

        // Fragment de gestió de les imatges
        // Comprova que només s'afegeixi el fragment si l'activity es crea per primera vegada
        // CarpetaImatges=contextWrapper.getExternalFilesDir("Imatges").toString();//  o Environment.DIRECTORY_DOCUMENTS enlloc de Copies
        assert (numIndex >= 0);
        mItem = llistaALumnes.get(numIndex);
        CarregaItem(mItem);
        if (mItem.getImatges().length() == 0) {
            CarpetaImatgesItem = mItem.getCodi();
            mItem.setImatges(CarpetaImatgesItem);
        } else {
            CarpetaImatgesItem = mItem.getImatges();
        }
        if (savedInstanceState == null) {
            // Crea una instància del fragment
            fragment_imatges = frg_inc_imatges.newInstance(Global.ARXIU_ALUMNES,CarpetaImatgesItem);

            // Obté el FragmentManager i inicia una transacció
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Afegeix el fragment al contenidor definit en el layout de l'activity
            fragmentTransaction.add(R.id.FragmentImatges, fragment_imatges);

            // Confirma la transacció
            fragmentTransaction.commit();
        }

//        db=  new GestorDB(getApplicationContext());
//        if (numIndex < 0) {
//            mItem = new classAlumne();
//        } else {
//            mItem = objLlistaTrobats.ITEMS.get(numIndex);
//        }
//        CarregaItem(mItem);

    }

    private void CarregaItem(classAlumne item) {

        assert (item != null);
        ((TextView) findViewById(R.id.txtAluEdtCodi)).setText("" + item.getCodi());
        ((TextView) findViewById(R.id.edtAluEdtNom)).setText(item.getNom());
        ((TextView) findViewById(R.id.edtAluEdtCurs)).setText(item.getCurs());
        ((TextView) findViewById(R.id.edtAluEdtCognom1)).setText(item.getCognom1());
        ((TextView) findViewById(R.id.edtAluEdtCognom2)).setText(item.getCognom2());
        ((TextView) findViewById(R.id.edtAluEdtObservacions)).setText(item.getObservacions());
        ((TextView) findViewById(R.id.edtAluEdtCntct2Nom)).setText(item.getCntct1Nom());
        ((TextView) findViewById(R.id.edtAluEdtCntct2Tel)).setText(item.getCntct1Tel());
        ((TextView) findViewById(R.id.edtAluEdtCntct2Rel)).setText(item.getCntct1Rel());
        ((TextView) findViewById(R.id.edtAluEdtCntct2Nom)).setText(item.getCntct2Nom());
        ((TextView) findViewById(R.id.edtAluEdtCntct2Tel)).setText(item.getCntct2Tel());
        ((TextView) findViewById(R.id.edtAluEdtCntct2Rel)).setText(item.getCntct2Rel());

        if (fragment_imatges != null) {
            if (item.getImatges().length() > 0) {
                Log.d("act_inc_edita", "CarregaItem SetCarpeta amb '" + mItem.getImatges() + "'");
                fragment_imatges.SetCarpeta(mItem.getImatges());
            } else {
                Log.d("act_inc_edita", "CarregaItem SetCarpeta imatge amb '" + "Docs_" + mItem.getCodi() + "'");
                fragment_imatges.SetCarpeta("Docs_" + mItem.getCodi());
                mItem.setImatges(mItem.getCodi());
            }
        }
    }

    private void DescarregaItem(classAlumne item) {
        item.setCodi(((TextView) findViewById(R.id.txtAluEdtCodi)).getText().toString());
        item.setNom(((TextView) findViewById(R.id.edtAluEdtNom)).getText().toString());
        item.setCurs(((TextView) findViewById(R.id.edtAluEdtCurs)).getText().toString());
        item.setCognom1(((TextView) findViewById(R.id.edtAluEdtCognom1)).getText().toString());
        item.setCognom2(((TextView) findViewById(R.id.edtAluEdtCognom2)).getText().toString());
        item.setObservacions(((TextView) findViewById(R.id.edtAluEdtObservacions)).getText().toString());
        item.setCntct2Nom(((TextView) findViewById(R.id.edtAluEdtCntct1Nom)).getText().toString());
        item.setCntct2Tel(((TextView) findViewById(R.id.edtAluEdtCntct1Tel)).getText().toString());
        item.setCntct2Rel(((TextView) findViewById(R.id.edtAluEdtCntct1Rel)).getText().toString());
        item.setCntct2Nom(((TextView) findViewById(R.id.edtAluEdtCntct2Nom)).getText().toString());
        item.setCntct2Tel(((TextView) findViewById(R.id.edtAluEdtCntct2Tel)).getText().toString());
        item.setCntct2Rel(((TextView) findViewById(R.id.edtAluEdtCntct2Rel)).getText().toString());

        }



    /********************************************************************/
    /**************  Comandaments botons  *******************************/
    /********************************************************************/

    private void SalvaItem() {

        DescarregaItem(mItem);
        db.actAlumne(mItem);
    }

    public void cmd_Alu_Ok(View view) {
        SalvaItem();
        finish();
    }

    public void cmd_Alu_Cancella(View view) {
        CarregaItem(mItem);
    }

    public void cmd_Alu_Elimina(View view) {
        // MsgBox ¿?
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Segur que vols esborrar?");
        builder.setTitle("Atenció!!");
        builder.setCancelable(false);
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.delAlumne(mItem.getCodi());
                        // Todo later: Eliminar les imatges quan s'elimina un alumne
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        // Fi MsgBox
    }

    public void cmd_Alu_Ant(View view) {
        SalvaItem();
        if (numIndex>0) {
            numIndex--;
            mItem =llistaALumnes.get(numIndex);
        }
        CarregaItem(mItem);
    }

    public void cmd_Alu_Seg(View view) {
        SalvaItem();
        if (numIndex<llistaALumnes.size()-1) {
            numIndex++;
            mItem =llistaALumnes.get(numIndex);
        }
        CarregaItem(mItem);
    }

    /***************************************************************************************/
    /************************  Utilitats  **************************************************/
    /***************************************************************************************/



    private void MissatgeError(String Missatge) {
        MissatgeError(Missatge, "Error!!!");
    }

    private void MissatgeError(String Missatge, String Titol) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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



}