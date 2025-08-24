package com.gruixuts.cocobe25;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class act_prf_sel extends AppCompatActivity {

    GestorDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prf_sel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db =  new GestorDB(getApplicationContext());

        ((TextView) findViewById(R.id.edtPrfCodi)).setText("");
        ((TextView) findViewById(R.id.edtPrfNom)).setText("");
        ((TextView) findViewById(R.id.edtPrfCognom1)).setText("");
        ((TextView) findViewById(R.id.edtPrfCognom2)).setText("");
        ((TextView) findViewById(R.id.edtPrfRols)).setText("");
        ((TextView) findViewById(R.id.edtPrfObs)).setText("");
        ((TextView) findViewById(R.id.edtPrfEstat)).setText("");
        ((TextView) findViewById(R.id.edtPrfeMail)).setText("");
        ((RadioGroup) findViewById(R.id.grpOrdre)).clearCheck();

    }

    public void PrfBusca(View view) {
        String Filtre = "";
        if (((TextView) findViewById(R.id.edtPrfCodi)).getText().length()!=0) {
            Filtre += " and (Codi = '" + ((TextView) findViewById(R.id.edtPrfCodi)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtPrfNom)).getText().length()!=0) {
            Filtre += " and (Nom like '" + ((TextView) findViewById(R.id.edtPrfNom)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtPrfCognom1)).getText().length()!=0) {
            Filtre += " and (Cognom1 like '" + ((TextView) findViewById(R.id.edtPrfCognom1)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtPrfCognom2)).getText().length()!=0) {
            Filtre += " and (Cognom2 like '" + ((TextView) findViewById(R.id.edtPrfCognom2)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtPrfRols)).getText().length()!=0) {
            Filtre += " and (Rols like '%" + ((TextView) findViewById(R.id.edtPrfRols)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtPrfEstat)).getText().length()!=0) {
            Filtre += " and (Estat like '%" + ((TextView) findViewById(R.id.edtPrfEstat)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtPrfObs)).getText().length()!=0) {
            Filtre += " and (Observacions like '%" + ((TextView) findViewById(R.id.edtPrfObs)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtPrfeMail)).getText().length()!=0) {
            Filtre += " and (eMail like '%" + ((TextView) findViewById(R.id.edtPrfeMail)).getText() + "%' )";
        }

        int rb = ((RadioGroup) findViewById(R.id.grpOrdre)).getCheckedRadioButtonId();
        String Ordre;
        if (rb==R.id.radNom ) {
            Ordre = "Nom, Cognom1, Cognom2";
        } else if (rb==R.id.radCognom ) {
            Ordre = "Cognom1, Cognom2, Nom";
        } else {
            Ordre = "";
        }

        if (Filtre.length()>4) {
            Filtre=Filtre.substring(4);
        } else {
            Filtre = "";
        }

        String SQLtxt;

        SQLtxt = "Select " + db.ProfessorLlistaCamps() + " from " + db.ProfessorTableName();
        if (Filtre.length() > 0) {
            SQLtxt += " where " + Filtre;
        }
        if (Ordre.length() > 0) {
            SQLtxt += " order by " + Ordre;
        }
        SQLtxt += " ;";
        Log.d("selProfessors","SQL:"+ SQLtxt);


        //objLlistaTrobats.NouSQLtxt(Filtre,Ordre,getApplicationContext());

        Intent myIntent = new Intent(act_prf_sel.this, act_prf_llista.class);
        myIntent.putExtra(act_prf_llista.ARG_SQL,SQLtxt);
        startActivity(myIntent);
    }
    public void PrfCrea(View view) {
//        objLlistaTrobats.ITEMS.clear();
        String NouCodi = ((TextView) findViewById(R.id.edtPrfCodi)).getText().toString();
        if (NouCodi.length()==0) {
            Log.d("PrfCrea","Codi en blanc");
            Global.MissatgeError("Cal posar el codi",getApplicationContext());
            return;
        } else {
            classProfessor item = new classProfessor(NouCodi);
            Log.d("PrfCrea","Professor '"+ NouCodi +"' creat");
            db.insProfessor(item);
            Log.d("PrfCrea","Professor '"+ NouCodi +"' insertat a la base de dades");
        }
        Intent myIntent = new Intent(act_prf_sel.this, act_prf_edita.class);
        myIntent.putExtra(act_prf_edita.ARG_ITEM_ID, "0");
        myIntent.putExtra(act_prf_edita.ARG_ITEM_LLISTA, NouCodi);
        Log.d("PrfCrea","myIntent creat");
        startActivity(myIntent);

    }


}