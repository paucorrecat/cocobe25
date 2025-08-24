package com.gruixuts.cocobe25;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;


public class act_acc_edita extends AppCompatActivity {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_LLISTA = "item_llista";
    private int numIndex; // Index 0..N del item que s'està veient dins la llista @objLlistaTrobades
    private classActuacio mItem;

    GestorDB db;
    ArrayList<classActuacio> llistaActuacions;


    private boolean CanviantManuelment = true;
    private AutoCompleteTextView txtAlumnes;
    private AutoCompleteTextView txtProfessors;
    private AutoCompleteTextView txtTipus;

    private ActivityResultLauncher<Intent> TitolSpeechRecLauncher;
    private ActivityResultLauncher<Intent> DescripSpeechRecLauncher;
    private EditText txtTitol;
    private EditText txtDescrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acc_edita);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new GestorDB(getApplicationContext());
        Log.d("act_acc_edita.OnCreate", "Entra");
        numIndex = Integer.parseInt(getIntent().getStringExtra(ARG_ITEM_ID));
        String mcadena = getIntent().getStringExtra(ARG_ITEM_LLISTA);
        Log.d("act_acc_edita.OnCreate", "cadena = '" + mcadena + "'");

        txtProfessors = findViewById(R.id.edtAccEdtProfessors);

        txtProfessors.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() > 0) && CanviantManuelment) {
//                if (s.length() > 0) {
                    ActualitzaLlistaProfessors(s.toString());
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        txtProfessors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            classProfessor tut;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
            }
        });

        txtAlumnes = findViewById(R.id.edtAccEdtAlumnes);

        txtAlumnes.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() > 0)  && CanviantManuelment) {
                    ActualitzaLlistaAlumnes(s.toString());
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        txtAlumnes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            classProfessor tut;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
            }
        });

        txtTipus = findViewById(R.id.edtAccEdtTipus);

        txtTipus.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() > 0) && CanviantManuelment) {
//                if (s.length() > 0) {
                    ActualitzaLlistaTipus(s.toString());
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        txtTipus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            classProfessor tut;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
            }
        });

        txtTitol = findViewById(R.id.edtAccEdtTitol);

        ImageButton voiceButton = findViewById(R.id.cmdAccVeuTitol);
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

        txtDescrip = findViewById(R.id.edtAccEdtDescripcio);

        ImageButton voiceButtonDesc = findViewById(R.id.cmdAccVeuDesc);
        voiceButtonDesc.setOnClickListener(this::cmd_VeuDescrip);

        DescripSpeechRecLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            if (matches != null) {
                                String txtAnt = txtDescrip.getText().toString();
                                String txtAdd = Global.Veu2String( matches);
//                                String txtAdd = TextUtils.join(" ", matches);
                                if (txtAnt.length() > 0) {
                                    txtAnt += "\n";
                                }
                                txtAnt += txtAdd;
                                txtDescrip.setText(txtAnt);
                            }
                        }
                    }
                }
        );




        llistaActuacions = db.Cadena2LlistyaAcc(mcadena);
        assert (numIndex < llistaActuacions.size());
        assert (numIndex >= 0);
        mItem = llistaActuacions.get(numIndex);
        CarregaItem(mItem);
    }

    private void CarregaItem(classActuacio item) {

        assert (item != null);
        CanviantManuelment = false;
        ((TextView) findViewById(R.id.txtAccEdtId)).setText("" + item.getId());
        ((TextView) findViewById(R.id.edtAccEdtData)).setText(item.getData());
        ((Switch) findViewById(R.id.schedtAccEdtPendent)).setChecked(item.getPendent());
        ((AutoCompleteTextView) findViewById(R.id.edtAccEdtTipus)).setText(item.getTipus());
        ((TextView) findViewById(R.id.edtAccEdtTitol)).setText(item.getTitol());
        ((AutoCompleteTextView) findViewById(R.id.edtAccEdtProfessors)).setText(item.getProfessors());
        ((AutoCompleteTextView) findViewById(R.id.edtAccEdtAlumnes)).setText(item.getAlumnes());
        ((TextView) findViewById(R.id.edtAccEdtPares)).setText(item.getPares());
        ((TextView) findViewById(R.id.edtAccEdtDescripcio)).setText(item.getDescripcio());
        CanviantManuelment = true;


    }

    private void DescarregaItem(classActuacio item) {
        item.setId(((TextView) findViewById(R.id.txtAccEdtId)).getText().toString());
        item.setData(((TextView) findViewById(R.id.edtAccEdtData)).getText().toString());
        item.setPendent(((Switch) findViewById(R.id.schedtAccEdtPendent)).isChecked());
        item.setTipus(((TextView) findViewById(R.id.edtAccEdtTipus)).getText().toString());
        item.setTitol(((TextView) findViewById(R.id.edtAccEdtTitol)).getText().toString());
        item.setProfessors(((TextView) findViewById(R.id.edtAccEdtProfessors)).getText().toString());
        item.setAlumnes(((TextView) findViewById(R.id.edtAccEdtAlumnes)).getText().toString());
        item.setPares(((TextView) findViewById(R.id.edtAccEdtPares)).getText().toString());
        item.setDescripcio(((TextView) findViewById(R.id.edtAccEdtDescripcio)).getText().toString());

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
        db.actActuacio(mItem);
    }

    public void cmd_Acc_Ok(View view) {
        SalvaItem();
        setResult(RESULT_OK);
        finish();
    }

    public void cmd_Acc_Cancella(View view) {
        CarregaItem(mItem);
    }

    public void cmd_Acc_Elimina(View view) {
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
                        db.delActuacio(mItem.getId());
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        // Fi MsgBox
    }

    public void cmd_Acc_Ant(View view) {
        SalvaItem();
        if (numIndex>0) {
            numIndex--;
            mItem = llistaActuacions.get(numIndex);
        }
        CarregaItem(mItem);
    }

    public void cmd_Acc_Seg(View view) {
        SalvaItem();
        if (numIndex< llistaActuacions.size()-1) {
            numIndex++;
            mItem = llistaActuacions.get(numIndex);
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

    private void ActualitzaLlistaAlumnes(String input) {
        ArrayList<String> suggestions = db.PossiblesAlumnes(input);
        if (suggestions.size()>0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
            txtAlumnes.setAdapter(adapter);
            txtAlumnes.showDropDown();
        }
    }

    private void ActualitzaLlistaProfessors(String input) {
        ArrayList<String> suggestions = db.PossiblesProfessors(input);
        if (suggestions.size()>0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
            txtProfessors.setAdapter(adapter);
            txtProfessors.showDropDown();
        }
    }

    private void ActualitzaLlistaTipus(String input) {
        ArrayList<String> suggestions = classActuacio.llistaTipusActuacions;
        if (suggestions.size()>0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
            txtTipus.setAdapter(adapter);
            txtTipus.showDropDown();
        }
    }

    public void cmd_VeuTitol(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Títol:");
        TitolSpeechRecLauncher.launch(intent);
    }

    public void cmd_VeuDescrip(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Descripció:");
        DescripSpeechRecLauncher.launch(intent);
    }




}


