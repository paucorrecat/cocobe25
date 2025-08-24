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

public class act_acc_sel extends AppCompatActivity {

    GestorDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acc_sel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db =  new GestorDB(getApplicationContext());

        ((TextView) findViewById(R.id.selAccId)).setText("");
        ((TextView) findViewById(R.id.selAccDataDesde)).setText("2024-09-01");
        ((CheckBox) findViewById(R.id.selAccDesdeSi)).setChecked(false);
        ((TextView) findViewById(R.id.selAccDataFins)).setText("2025-06-30");
        ((CheckBox) findViewById(R.id.selAccFinsSi)).setChecked(false);
        ((CheckBox) findViewById(R.id.selAccPendentNo)).setChecked(false);
        ((CheckBox) findViewById(R.id.selAccPendentSi)).setChecked(false);
        ((TextView) findViewById(R.id.selAccTipus)).setText("");
        ((TextView) findViewById(R.id.selAccTitol)).setText("");
        ((TextView) findViewById(R.id.selAccProfessor)).setText("");
        ((TextView) findViewById(R.id.selAccPares)).setText("");
        ((TextView) findViewById(R.id.selAccDescripcio)).setText("");
        ((TextView) findViewById(R.id.selAccAlumne)).setText("");
        ((TextView) findViewById(R.id.selAccDescripcio)).setText("");
        ((RadioGroup) findViewById(R.id.grpOrdre)).clearCheck();

    }

    public void AccBusca(View view) {
        String Filtre = "";
        if (((TextView) findViewById(R.id.selAccId)).getText().length()!=0) {
            Filtre += " and (Id = '%" + ((TextView) findViewById(R.id.selAccId)).getText() + "%' )";
        }
        if (((CheckBox) findViewById(R.id.selAccDesdeSi)).isChecked()) {
            Filtre += " and (Data >= '" + ((TextView) findViewById(R.id.selAccDataDesde)).getText() + "' )";
        }
        if (((CheckBox) findViewById(R.id.selAccFinsSi)).isChecked()) {
            Filtre += " and (Data >= '" + ((TextView) findViewById(R.id.selAccDataFins)).getText() + "' )";
        }
        if (((CheckBox) findViewById(R.id.selAccPendentSi)).isChecked()) {
            Filtre += " and (Pendent = '" + classActuacio.PENDENT_PENDENT + "' )";
        }
        if (((CheckBox) findViewById(R.id.selAccPendentNo)).isChecked()) {
            Filtre += " and (Pendent = '" + classActuacio.PENDENT_FET + "' )";
        }
        if (((TextView) findViewById(R.id.selAccTipus)).getText().length()!=0) {
            Filtre += " and (Tipus like '" + ((TextView) findViewById(R.id.selAccTipus)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.selAccTitol)).getText().length()!=0) {
            Filtre += " and (Titol like '%" + ((TextView) findViewById(R.id.selAccTitol)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.selAccProfessor)).getText().length()!=0) {
            Filtre += " and (Professors like '%" + ((TextView) findViewById(R.id.selAccProfessor)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.selAccAlumne)).getText().length()!=0) {
            Filtre += " and (Alumnes like '%" + ((TextView) findViewById(R.id.selAccAlumne)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.selAccPares)).getText().length()!=0) {
            Filtre += " and (Pares like '%" + ((TextView) findViewById(R.id.selAccPares)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.selAccDescripcio)).getText().length()!=0) {
            Filtre += " and (Descripcio like '%" + ((TextView) findViewById(R.id.selAccDescripcio)).getText() + "%' )";
        }

        int rb = ((RadioGroup) findViewById(R.id.grpOrdre)).getCheckedRadioButtonId();
        String Ordre;
        if (rb==R.id.radData ) {
            Ordre = "Data, Id";
        } else if (rb==R.id.radId ) {
            Ordre = "Id, Data";
        } else if (rb==R.id.radTipus ) {
            Ordre = "Tipus, Data, Id";
        } else {
            Ordre = "";
        }

        if (Filtre.length()>4) {
            Filtre=Filtre.substring(4);
        } else {
            Filtre = "";
        }

        String SQLtxt;

        SQLtxt = "Select " + db.ActuacioLlistaCamps() + " from " + db.ActuacioTableName();
        if (Filtre.length() > 0) {
            SQLtxt += " where " + Filtre;
        }
        if (Ordre.length() > 0) {
            SQLtxt += " order by " + Ordre;
        }
        SQLtxt += " ;";
        Log.d("selActuacions","SQL:"+ SQLtxt);


        //objLlistaTrobats.NouSQLtxt(Filtre,Ordre,getApplicationContext());

        Intent myIntent = new Intent(act_acc_sel.this, act_acc_llista.class);
        myIntent.putExtra(act_acc_llista.ARG_SQL,SQLtxt);
        startActivity(myIntent);
    }
    public void AccCrea(View view) {
//        objLlistaTrobats.ITEMS.clear();
        String NouCodi = ((TextView) findViewById(R.id.selAccId)).getText().toString();
        if (NouCodi.length()==0) {
            Log.d("AccCrea","Codi en blanc");
            Global.MissatgeError("Cal posar el codi",getApplicationContext());
            return;
        } else {
            classActuacio item = new classActuacio(NouCodi);
            Log.d("AccCrea","Actuacio '"+ NouCodi +"' creat");
            db.insActuacio(item);
            Log.d("AccCrea","Actuacio '"+ NouCodi +"' insertat a la base de dades");
        }
        Intent myIntent = new Intent(act_acc_sel.this, act_acc_edita.class);
        myIntent.putExtra(act_acc_edita.ARG_ITEM_ID, "0");
        myIntent.putExtra(act_acc_edita.ARG_ITEM_LLISTA, NouCodi);
        Log.d("AccCrea","myIntent creat");
        startActivity(myIntent);

    }

}