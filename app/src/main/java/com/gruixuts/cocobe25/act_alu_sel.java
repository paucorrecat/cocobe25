package com.gruixuts.cocobe25;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class act_alu_sel extends AppCompatActivity {

    GestorDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alu_sel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db =  new GestorDB(getApplicationContext());

        ((TextView) findViewById(R.id.edtAluCodi)).setText("");
        ((TextView) findViewById(R.id.edtAluNom)).setText("");
        ((TextView) findViewById(R.id.edtAluCognom1)).setText("");
        ((TextView) findViewById(R.id.edtAluCognom2)).setText("");
        ((TextView) findViewById(R.id.edtAluCurs)).setText("");
        ((TextView) findViewById(R.id.edtAluObs)).setText("");
        ((TextView) findViewById(R.id.edtAluCont)).setText("");
        ((CheckBox) findViewById(R.id.chkAluSiInc)).setChecked(false);
        ((CheckBox) findViewById(R.id.chkAluNoInc)).setChecked(false);
        ((RadioGroup) findViewById(R.id.grpOrdre)).clearCheck();

    }

    public void AluBusca(View view) {
        String Filtre = "";
        if (((TextView) findViewById(R.id.edtAluCodi)).getText().length()!=0) {
            Filtre += " and (Codi = '" + ((TextView) findViewById(R.id.edtAluCodi)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtAluNom)).getText().length()!=0) {
            Filtre += " and (Nom like '" + ((TextView) findViewById(R.id.edtAluNom)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtAluCognom1)).getText().length()!=0) {
            Filtre += " and (Cognom1 like '" + ((TextView) findViewById(R.id.edtAluCognom1)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtAluCognom2)).getText().length()!=0) {
            Filtre += " and (Cognom2 like '" + ((TextView) findViewById(R.id.edtAluCognom2)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtAluCurs)).getText().length()!=0) {
            Filtre += " and (Curs like '" + ((TextView) findViewById(R.id.edtAluCurs)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtAluObs)).getText().length()!=0) {
            Filtre += " and (Observacions like '%" + ((TextView) findViewById(R.id.edtAluObs)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtAluCont)).getText().length()!=0) {
            Filtre += " and ((Cntct1Nom like '" + ((TextView) findViewById(R.id.edtAluCont)).getText() + "%' )";
            Filtre += " or (Cntct2Nom like '" + ((TextView) findViewById(R.id.edtAluCont)).getText() + "%' ))";
        }

        int rb = ((RadioGroup) findViewById(R.id.grpOrdre)).getCheckedRadioButtonId();
        String Ordre;
        if (rb==R.id.radNom ) {
            Ordre = "Nom, Cognom1, Cognom2";
        } else if (rb==R.id.radCognom ) {
            Ordre = "Cognom1, Cognom2";
        } else if (rb==R.id.radCurs ) {
            Ordre = "Curs, Cognom1, Cognom2";
        } else {
            Ordre = "";
        }

        if (Filtre.length()>4) {
            Filtre=Filtre.substring(4);
        } else {
            Filtre = "";
        }

        String SQLtxt;

        SQLtxt = "Select " + db.AlumneLlistaCamps() + " from " + db.AlumneTableName();
        if (Filtre.length() > 0) {
            SQLtxt += " where " + Filtre;
        }
        if (Ordre.length() > 0) {
            SQLtxt += " order by " + Ordre;
        }
        SQLtxt += " ;";
        Log.d("selAlumnes","SQL:"+ SQLtxt);


        //objLlistaTrobats.NouSQLtxt(Filtre,Ordre,getApplicationContext());

        Intent myIntent = new Intent(act_alu_sel.this, act_alu_llista.class);
        myIntent.putExtra(act_alu_llista.ARG_SQL,SQLtxt);
        startActivity(myIntent);
    }
    public void AluCrea(View view) {
//        objLlistaTrobats.ITEMS.clear();
        String NouCodi = ((TextView) findViewById(R.id.edtAluCodi)).getText().toString();
        if (NouCodi.length()==0) {
            Log.d("AluCrea","Codi en blanc");
            Global.MissatgeError("Cal posar el codi",getApplicationContext());
            return;
        } else {
            classAlumne item = new classAlumne(NouCodi);
            Log.d("AluCrea","Alumne '"+ NouCodi +"' creat");
            db.insAlumne(item);
            Log.d("AluCrea","Alumne '"+ NouCodi +"' insertat a la base de dades");
        }
        Intent myIntent = new Intent(act_alu_sel.this, act_alu_edita.class);
        myIntent.putExtra(act_alu_edita.ARG_ITEM_ID, "0");
        myIntent.putExtra(act_alu_edita.ARG_ITEM_LLISTA, NouCodi);
        Log.d("AluCrea","myIntent creat");
        startActivity(myIntent);

    }

}