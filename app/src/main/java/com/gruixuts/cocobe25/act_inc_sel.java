package com.gruixuts.cocobe25;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

public class act_inc_sel extends AppCompatActivity {

    SimpleDateFormat frmtData = new SimpleDateFormat("yy-MM-dd");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inc_sel);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((TextView) findViewById(R.id.edtAluCodi)).setText("");
        ((TextView) findViewById(R.id.edtIncGrup)).setText("");
        ((TextView) findViewById(R.id.edtIncAlumne)).setText("");
        ((TextView) findViewById(R.id.edtIncCurs)).setText("");
        ((TextView) findViewById(R.id.edtIncProfessor)).setText("");
        ((TextView) findViewById(R.id.edtIncTutor)).setText("");
        ((TextView) findViewById(R.id.edtIncDescripcio)).setText("");
        ((TextView) findViewById(R.id.edtIncDataDesde)).setText("");
        ((TextView) findViewById(R.id.edtIncDataFins)).setText("");
        ((TextView) findViewById(R.id.chkIncSiTancada)).setText("");
        ((TextView) findViewById(R.id.chkIncNoTancada)).setText("");
        ((RadioGroup) findViewById(R.id.grpOrdre)).clearCheck();
    }


    public void IncBusca(View view) {
        String Filtre = "";
        if (((TextView) findViewById(R.id.edtAluCodi)).getText().length()!=0) {
            Filtre += " and (Id like '%" + ((TextView) findViewById(R.id.edtAluCodi)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtIncGrup)).getText().length()!=0) {
            Filtre += " and (Grup like '%" + ((TextView) findViewById(R.id.edtIncGrup)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtIncAlumne)).getText().length()!=0) {
            Filtre += " and (Alumne like '%" + ((TextView) findViewById(R.id.edtIncAlumne)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtIncCurs)).getText().length()!=0) {
            Filtre += " and (Curs like '%" + ((TextView) findViewById(R.id.edtIncCurs)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtIncProfessor)).getText().length()!=0) {
            Filtre += " and (Professor like '%" + ((TextView) findViewById(R.id.edtIncProfessor)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtIncTutor)).getText().length()!=0) {
            Filtre += " and (Tutor like '%" + ((TextView) findViewById(R.id.edtIncTutor)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtIncDescripcio)).getText().length()!=0) {
            Filtre += " and (Descripcio like '%" + ((TextView) findViewById(R.id.edtIncDescripcio)).getText() + "%' )";
        }
        if (((CheckBox) findViewById(R.id.chkIncSiTancada)).isChecked()) {
            Filtre += " and (Estat = 'T' )";
        }
        if (((CheckBox) findViewById(R.id.chkIncNoTancada)).isChecked()) {
            Filtre += " and (Estat = 'O' )";
        }

        //( NextData < '" +  frmtData.format(cal.getTime()) + "')
        if (((TextView) findViewById(R.id.edtIncDataDesde)).getText().length()!=0) {
            Filtre += " and (Data >= '" + ((TextView) findViewById(R.id.edtIncDataDesde)).getText() + "' )";
        }

        if (((TextView) findViewById(R.id.edtIncDataFins)).getText().length()!=0) {
            Filtre += " and (Data <= '" + ((TextView) findViewById(R.id.edtIncDataFins)).getText() + "' )";
        }

        int rb = ((RadioGroup) findViewById(R.id.grpOrdre)).getCheckedRadioButtonId();
        String Ordre;
        if (rb==R.id.radData ) {
            Ordre = "Data";
        } else if (rb==R.id.radDataInv ) {
            Ordre = "Data DESC";
        } else if (rb==R.id.radDataCurs ) {
            Ordre = "Curs";
        } else {
            Ordre = "Data";
        }

        if (Filtre.length()>4) {
            Filtre=Filtre.substring(4);
        } else {
            Filtre = "";
        }
        incLlistaTrobades.NouSQLtxt(Filtre,Ordre,getApplicationContext());

        Intent myIntent = new Intent(act_inc_sel.this, act_inc_llista.class);
        startActivity(myIntent);
    }
    public void IncCrea(View view) {
        incLlistaTrobades.ITEMS.clear();
//        Intent myIntent = new Intent(act_inc_sel.this, act_inc_edita.class);
//        myIntent.putExtra(act_inc_edita.ARG_ITEM_ID, "-1");
        Intent myIntent = new Intent(act_inc_sel.this, act_inc_edita.class);
        myIntent.putExtra(act_inc_edita.ARG_ITEM_ID, "-1");
        startActivity(myIntent);

    }

}
