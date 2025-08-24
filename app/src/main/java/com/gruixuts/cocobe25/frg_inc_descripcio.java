package com.gruixuts.cocobe25;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frg_inc_descripcio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frg_inc_descripcio extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    protected static final int SPEECH_REQUEST_CODE = 13;

    private ActivityResultLauncher<Intent> DescripSpeechRecLauncher;
    private View view;
    private String text_Descripcio;
    private EditText txtDescrip;

    public String getDescripcio() {
        if (txtDescrip != null) {
            return txtDescrip.getText().toString();
        }
        return "";
    }

    public void setDescripcio(String txt) {
        if (txtDescrip != null) {
            txtDescrip.setText(txt);
        }
    }

    public frg_inc_descripcio() {
        // Required empty public constructor
    }

    public static frg_inc_descripcio newInstance(String txtDescrip) {
        frg_inc_descripcio fragment = new frg_inc_descripcio();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, txtDescrip);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            text_Descripcio = getArguments().getString(ARG_PARAM1);
        }
        Log.d("frgmIncDescripcio", "onCreate: text_Descripcio = " + text_Descripcio);

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
                                //String txtAdd = TextUtils.join(" ", matches);
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
    }

    public void cmd_Veu(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Descripció:");
        DescripSpeechRecLauncher.launch(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inc_descripcio, container, false);
        txtDescrip = view.findViewById(R.id.edtIncDescripcio);
        txtDescrip.setText(text_Descripcio);
        Log.d("frgmIncDescripcio", "onCreateView: text set to " + text_Descripcio);

        // Afegir el listener al botó o a l'element que llança el reconeixement de veu
        ImageButton voiceButton = view.findViewById(R.id.cmdIncVeuDesc);
        voiceButton.setOnClickListener(this::cmd_Veu);

        return view;
    }
}