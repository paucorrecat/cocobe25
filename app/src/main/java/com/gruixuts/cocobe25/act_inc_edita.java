package com.gruixuts.cocobe25;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


//public class act_inc_edita extends AppCompatActivity {
public class act_inc_edita extends AppCompatActivity {



    public static final String ARG_ITEM_ID = "item_id";
    private int numIndex; // Index 0..N del item que s'està veient dins la llista @incLlistaTrobades
    private classIncidencia mItem;
    private boolean CanviantManuelment = true;
    GestorDB db;

    private AutoCompleteTextView txtAlumne;
    private EditText txtCurs;
    private EditText txtTutor;
    private EditText txtTitol;
    private AutoCompleteTextView txtProfessor;

//    private pgadContinguts pageAdapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private frg_inc_descripcio fragment_desc;
    private frg_inc_imatges fragment_imatges;
    private frg_acc_llista fragment_acc_llista;
    private ActivityResultLauncher<Intent> editAccLauncher; // Per refrescar després d'haver modificat amb acc_edita
    private ActivityResultLauncher<Intent> TitolSpeechRecLauncher;
    //ContextWrapper contextWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String NouId;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inc_edita);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        numIndex = Integer.parseInt(getIntent().getStringExtra(ARG_ITEM_ID));
        //contextWrapper = new ContextWrapper(getApplicationContext());

        //La carpeta On hi ha els documents ha d'estar a 'Emmagatzematge intern compartit/Android/data/com.gruixuts.cocobe_dev/files/Documents
        txtAlumne = findViewById(R.id.edtIncAlumne);
        txtCurs = findViewById(R.id.edtIncCurs);
        txtTutor = findViewById(R.id.edtIncTutor);
        txtProfessor=findViewById(R.id.edtIncProfessor);

        txtAlumne.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() > 0) && CanviantManuelment) {
                    ActualitzaLlistaAlumnes(s.toString());
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        txtAlumne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            classAlumne alu;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                alu = db.Possibles2objAlumne(selectedItem);
                txtCurs.setText(alu.getCurs());
                txtTutor.setText((db.TutorDe(alu.getCurs())));
            }

        });

        txtProfessor.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() > 0) && CanviantManuelment) {
                    ActualitzaLlistaProfessors(s.toString());
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        txtProfessor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            classProfessor tut;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
            }
        });

        editAccLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Actualitzar la llista
                        if (mItem!=null) {
                            fragment_acc_llista.setSQL(Id2SQL(mItem.getId()),mItem.getId());
                        }
                    }
                });

        txtTitol = findViewById(R.id.edtIncTitol);

        ImageButton voiceButton = findViewById(R.id.cmdIncVeuTitol);
        voiceButton.setOnClickListener(this::cmd_VeuTitol);

        TitolSpeechRecLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            if (matches != null) {
                                String txtAdd = Global.Veu2String( matches);
                                //String txtAdd = TextUtils.join(" ", matches);
                                txtTitol.setText(txtAdd);
                            }
                        }
                    }
                }
        );

        tabLayout = findViewById(R.id.tabIncLayout);
        viewPager=findViewById(R.id.vp2Continguts);

        db=  new GestorDB(getApplicationContext());
        if (numIndex < 0) {
            db.open();
            NouId= db.NouIdIncidencia();
            mItem = new classIncidencia(NouId,NouId);
            db.insIncidencia(mItem);
            db.close();
        } else {
            mItem = incLlistaTrobades.ITEMS.get(numIndex);
        }
        CarregaItem(mItem);

        Log.d("act_inc_edita", "Initializing adapter");
        pgadContinguts adapter= new pgadContinguts(this);
        viewPager.setAdapter(adapter);

        String[] labels = new String[]{"Descripció", "Imatges", "Actuacions"};
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(labels[position]);
        }).attach();

        Log.d("act_inc_edita.onCreate", "Tot fet");

    }

    private void CarregaItem(classIncidencia item) {
        if (item != null) {
            CanviantManuelment = false;
            ((TextView) findViewById(R.id.txtIncId)).setText("" + item.getId());
            ((TextView) findViewById(R.id.txtIncGrup)).setText(item.getGrup());
            ((TextView) findViewById(R.id.edtIncData)).setText(item.getData());
            ((TextView) findViewById(R.id.edtIncProfessor)).setText(item.getProfessor());
            ((AutoCompleteTextView) findViewById(R.id.edtIncAlumne)).setText(item.getAlumne()); //Versió Bona
            ((TextView) findViewById(R.id.edtIncCurs)).setText(item.getCurs());
            ((TextView) findViewById(R.id.edtIncTutor)).setText(item.getTutor());
            ((TextView) findViewById(R.id.edtIncEstat)).setText(item.getEstat());
            ((TextView) findViewById(R.id.edtIncTitol)).setText(item.getTitol());
//            txt_Descripcio= item.getDescripcio();
            if (fragment_desc != null) {
                fragment_desc.setDescripcio(item.getDescripcio());
            }

            CanviantManuelment = true;
            // Imatges:
            if (fragment_imatges != null) {
                if (item.getImatges().length() > 0) {
                    Log.d("act_inc_edita", "CarregaItem SetCarpeta amb '" + mItem.getImatges() + "'");
                    fragment_imatges.SetCarpeta(mItem.getImatges());
                } else {
                    Log.d("act_inc_edita", "CarregaItem SetCarpeta imatge amb '" + "Docs_" + mItem.getId() + "'");
                    fragment_imatges.SetCarpeta("Docs_" + mItem.getId());
                }
            }
            if (fragment_acc_llista != null) {
                fragment_acc_llista.setSQL(Id2SQL(mItem.getId()),mItem.getId());
            }

        } else {
            //MissatgeError("item=null, i no hauria de ser");


//            ((TextView) findViewById(R.id.txtModId)).setText("");
//            ((TextView) findViewById(R.id.edtModNom)).setText("");
//            ((TextView) findViewById(R.id.edtIncData)).setText("");
//            ((TextView) findViewById(R.id.edtModCognom2)).setText("");
//            ((TextView) findViewById(R.id.edtModCurs)).setText("");
//            ((TextView) findViewById(R.id.edtModCodi)).setText("");
//            ((Switch) findViewById(R.id.schTraduible)).setChecked(false);
//            ((TextView) findViewById(R.id.edtModGrup)).setText("");
//            ((RadioButton) findViewById(R.id.rdT)).setChecked(true);
//            ((TextView) findViewById(R.id.edtModNextData)).setText("");
//            ((ImageView) findViewById(R.id.imgImatges)).setImageResource(R.mipmap.ic_launcher);

            Global.MissatgeError("No hi ha res",getApplicationContext());
//            Toast toast = Toast.makeText(getApplicationContext(), "No hi ha res" , Toast.LENGTH_LONG);
//            toast.show();
        }
    }

    private void DescarregaItem(classIncidencia item) {
        item.setId(((TextView) findViewById(R.id.txtIncId)).getText().toString());
        item.setGrup(((TextView) findViewById(R.id.txtIncGrup)).getText().toString());
        item.setData(((TextView) findViewById(R.id.edtIncData)).getText().toString());
        item.setProfessor(((TextView) findViewById(R.id.edtIncProfessor)).getText().toString());
        item.setAlumne(((TextView) findViewById(R.id.edtIncAlumne)).getText().toString());
        item.setCurs(((TextView) findViewById(R.id.edtIncCurs)).getText().toString());
        item.setTutor(((TextView) findViewById(R.id.edtIncTutor)).getText().toString());
        item.setEstat(((TextView) findViewById(R.id.edtIncEstat)).getText().toString());
        item.setTitol(((TextView) findViewById(R.id.edtIncTitol)).getText().toString());
        //pass item.setDescripcio(txt_Descripcio);
        if (fragment_desc != null) {
            item.setDescripcio(fragment_desc.getDescripcio());
        }
    }

    public class pgadContinguts extends FragmentStateAdapter {


        public pgadContinguts(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Log.d("pgadContinguts", "Creating fragment for position: " + position);
            switch (position) {
                case 0:
                    fragment_desc = frg_inc_descripcio.newInstance(mItem.getDescripcio());
                    return fragment_desc;
                case 1:
                    if (mItem.getImatges().length()==0) {
                        fragment_imatges = frg_inc_imatges.newInstance(Global.ARXIU_INCIDENCIES,"Docs_" + mItem.getId());
                    } else {
                        fragment_imatges = frg_inc_imatges.newInstance(Global.ARXIU_INCIDENCIES,mItem.getImatges());
                    }
                    return fragment_imatges;
                case 2:
                    fragment_acc_llista = frg_acc_llista.newInstance(Id2SQL(mItem.getId()),mItem.getId());
                    fragment_acc_llista.setEditLauncher(editAccLauncher);
                    return fragment_acc_llista;
                default:
                    return frg_inc_descripcio.newInstance("");
            }
        }

        @Override
        public int getItemCount() {
            return 3; // Nombre de fragments
        }
    }



    /********************************************************************/
    /**************  Comandaments botons  *******************************/
    /********************************************************************/

    private void SalvaItem() {
        db.open();
        DescarregaItem(mItem);
        if (mItem.getId() == "") {
            db.insIncidencia(mItem);
        } else {
            db.actIncidencia(mItem);
        }
        db.close();
    }

    public void cmd_Ok(View view) {
        SalvaItem();
        finish();
    }

    public void cmd_Cancella(View view) {
        CarregaItem(mItem);
    }

    public void cmd_Elimina(View view) {
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
                        db.open();
                        if (mItem.getId() != "") {
                            db.delIncidencia(mItem.getId());
                        }
                        db.close();
                        // Todo later: Eliminar les imatges

                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        // Fi MsgBox
    }

    public void cmd_Ant(View view) {
        SalvaItem();
        if (numIndex>0) {
            numIndex--;
            mItem = incLlistaTrobades.ITEMS.get(numIndex);
        }
        CarregaItem(mItem);
    }

    public void cmd_Seg(View view) {
        SalvaItem();
        if (numIndex<incLlistaTrobades.ITEMS.size()-1) {
            numIndex++;
            mItem = incLlistaTrobades.ITEMS.get(numIndex);
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


    /*
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

     */

    private void ActualitzaLlistaAlumnes(String input) {
        ArrayList<String> suggestions = db.PossiblesAlumnes(input);
        if (suggestions.size()>0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
            txtAlumne.setAdapter(adapter);
            txtAlumne.showDropDown();
        }
    }

    private void ActualitzaLlistaProfessors(String input) {
        ArrayList<String> suggestions = db.PossiblesProfessors(input);
        if (suggestions.size()>0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
            txtProfessor.setAdapter(adapter);
            txtProfessor.showDropDown();
        }
    }

    private String Id2SQL(String id) {
        String txtSQL="select " + db.ActuacioLlistaCamps() + " from " + db.ActuacioTableName() + " where Id like '" +mItem.getId() + "-%' order by Id";
        return txtSQL;
    }

    public void cmd_VeuTitol(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Títol:");
        TitolSpeechRecLauncher.launch(intent);
    }


}



