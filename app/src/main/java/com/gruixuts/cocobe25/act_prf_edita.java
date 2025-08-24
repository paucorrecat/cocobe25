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

public class act_prf_edita extends AppCompatActivity {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_LLISTA = "item_llista";
    private int numIndex; // Index 0..N del item que s'està veient dins la llista @objLlistaTrobades
    private classProfessor mItem;
    GestorDB db;
    ArrayList<classProfessor> llistaProfessors;

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
        setContentView(R.layout.activity_prf_edita);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new GestorDB(getApplicationContext());
        Log.d("act_prf_edita.OnCreate", "Entra");
        numIndex = Integer.parseInt(getIntent().getStringExtra(ARG_ITEM_ID));
        String mcadena = getIntent().getStringExtra(ARG_ITEM_LLISTA);
        Log.d("act_prf_edita.OnCreate", "cadena = '" + mcadena + "'");
        llistaProfessors = db.Cadena2LlistyaPrf(mcadena);
        assert (numIndex < llistaProfessors.size());

        //        CarpetaImatges = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pau/GeniusCares/Imatges";  // Es veu que això no està permès ja ¿¿??
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());

        // Fragment de gestió de les imatges
        // Comprova que només s'afegeixi el fragment si l'activity es crea per primera vegada
        // CarpetaImatges=contextWrapper.getExternalFilesDir("Imatges").toString();//  o Environment.DIRECTORY_DOCUMENTS enlloc de Copies
        assert (numIndex >= 0);
        mItem = llistaProfessors.get(numIndex);
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

    }

    private void CarregaItem(classProfessor item) {

        assert (item != null);
        ((TextView) findViewById(R.id.txtPrfEdtCodi)).setText("" + item.getCodi());
        ((TextView) findViewById(R.id.edtPrfEdtNom)).setText(item.getNom());
        ((TextView) findViewById(R.id.edtPrfEdtCognom1)).setText(item.getCognom1());
        ((TextView) findViewById(R.id.edtPrfEdtCognom2)).setText(item.getCognom2());
        ((TextView) findViewById(R.id.edtPrfEdtRols)).setText(item.getRols());
        ((TextView) findViewById(R.id.edtPrfEdtEstat)).setText(item.getEstat());
        ((TextView) findViewById(R.id.edtPrfEdtObservacions)).setText(item.getObservacions());
        ((TextView) findViewById(R.id.edtPrfEdteMail)).setText(item.geteMail());

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

        /*
        if (item.getImatges()!=null) {
            assert (item.getImatges().length()>0);
            CarpetaImatgesItem = CarpetaImatges + "/" + item.getImatges();
            File carpeta = new File(CarpetaImatgesItem);
            if (!carpeta.exists()) { // Crear-la
                //MissatgeError("La cerpeta d'immatges " + CarpetaImatgesItem + " no existeix");
                numImatge = 0;
                //carpeta.mkdirs();
                nomsImatge = new String[0];

            } else {
                nomsImatge = carpeta.list();
            }
            if (nomsImatge.length != 0) {
                numImatge = 1;
                Drawable d = Drawable.createFromPath(CarpetaImatgesItem + "/" + nomsImatge[numImatge - 1]);
                ((ImageView) findViewById(R.id.imgImatges)).setImageDrawable(d);
            } else {
                numImatge = 0;
            }
        } else {
            CarpetaImatgesItem="";
            numImatge = 0;
            nomsImatge = new String[0];
        }
        if (numImatge==0) {
            ((ImageView) findViewById(R.id.imgImatges)).setImageResource(R.mipmap.ic_launcher);
            if (!(mItem.getNextTipus().equals("t"))) {
                mItem.setNextTipus("t");
                ((RadioButton) findViewById(R.id.rdT)).setChecked(true);
            }
        } else {
            if (mItem.getNextTipus().equals("t")) {
                mItem.setNextTipus("a");
                ((RadioButton) findViewById(R.id.rdA)).setChecked(true);
            }
        }

         */

    }

    private void DescarregaItem(classProfessor item) {
        item.setCodi(((TextView) findViewById(R.id.txtPrfEdtCodi)).getText().toString());
        item.setNom(((TextView) findViewById(R.id.edtPrfEdtNom)).getText().toString());
        item.setCognom1(((TextView) findViewById(R.id.edtPrfEdtCognom1)).getText().toString());
        item.setCognom2(((TextView) findViewById(R.id.edtPrfEdtCognom2)).getText().toString());
        item.setRols(((TextView) findViewById(R.id.edtPrfEdtRols)).getText().toString());
        item.setEstat(((TextView) findViewById(R.id.edtPrfEdtEstat)).getText().toString());
        item.setObservacions(((TextView) findViewById(R.id.edtPrfEdtObservacions)).getText().toString());
        item.seteMail(((TextView) findViewById(R.id.edtPrfEdteMail)).getText().toString());

    }



    /********************************************************************/
    /**************  Comandaments botons  *******************************/
    /********************************************************************/

    /**
     * Passa les dades que es veuen a mItem i després desa mItem a la base de dades
     * @param
     *
     */
    private void SalvaItem() {

        DescarregaItem(mItem);
        db.actProfessor(mItem);
    }

    public void cmd_Prf_Ok(View view) {
        SalvaItem();
        finish();
    }

    public void cmd_Prf_Cancella(View view) {
        CarregaItem(mItem);
    }

    public void cmd_Prf_Elimina(View view) {
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
                        db.delProfessor(mItem.getCodi());
                        // Todo: Eliminar les imatges quan s'elimina un prfmne
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        // Fi MsgBox
    }

    public void cmd_Prf_Ant(View view) {
        SalvaItem();
        if (numIndex>0) {
            numIndex--;
            mItem = llistaProfessors.get(numIndex);
        }
        CarregaItem(mItem);
    }

    public void cmd_Prf_Seg(View view) {
        SalvaItem();
        if (numIndex< llistaProfessors.size()-1) {
            numIndex++;
            mItem = llistaProfessors.get(numIndex);
        }
        CarregaItem(mItem);
    }

    /***************************************************************************************/
    /************************  Utilitats  **************************************************/
    /***************************************************************************************/
//
//    public void AutomTraduible(View view) {
//        if (((Switch) findViewById(R.id.schTraduible)).isChecked()) {
//            ((RadioButton) findViewById(R.id.rdT)).setChecked(false);
//            ((RadioButton) findViewById(R.id.rdA)).setChecked(false);
//            ((RadioButton) findViewById(R.id.rd1h)).setChecked(false);
//            ((RadioButton) findViewById(R.id.rd1d)).setChecked(false);
//            ((RadioButton) findViewById(R.id.rd1s)).setChecked(false);
//            ((RadioButton) findViewById(R.id.rd1m)).setChecked(false);
//            ((RadioButton) findViewById(R.id.rd6m)).setChecked(false);
//            ((TextView) findViewById(R.id.edtModNextData)).setText("");
//        } else {
//            ((RadioButton) findViewById(R.id.rdA)).setChecked(true);
//        }
//    }
//
    public void BuscaCodi(View view) {
//        Intent intent = new Intent(act_manteniment_modificar.this, act_aux_Codi.class);

        // Passem les cadenes a comparar ja normalitzades
//        intent.putExtra("Codi", ((TextView) findViewById(R.id.edtModCodi)).getText());
//        intent.putExtra("Codi", ((TextView) findViewById(R.id.edtModCodi)).getText());
//        startActivityForResult(intent, REQUEST_CODE);

    }

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