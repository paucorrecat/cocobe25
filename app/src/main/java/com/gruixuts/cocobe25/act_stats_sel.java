package com.gruixuts.cocobe25;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class act_stats_sel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_sel);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((TextView) findViewById(R.id.edtStatsCurs)).setText("");
        ((TextView) findViewById(R.id.edtStatsSiCodi)).setText("");
        ((TextView) findViewById(R.id.edtStatsNoCodi)).setText("");
        ((TextView) findViewById(R.id.edtStatsSiGrup)).setText("");
        ((TextView) findViewById(R.id.edtStatsNoGrup)).setText("");
    }


    public void GoStats(View view) {
        //Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        String Filtre = "";
        if (((TextView) findViewById(R.id.edtStatsCurs)).getText().length()!=0) {
            Filtre += " and (Curs like '" + ((TextView) findViewById(R.id.edtStatsCurs)).getText() + "%' )";
        }
        if (((TextView) findViewById(R.id.edtStatsSiCodi)).getText().length()!=0) {
            Filtre += " and (Codi like '" + ((TextView) findViewById(R.id.edtStatsSiCodi)).getText() + "' )";
        }
        if (((TextView) findViewById(R.id.edtStatsNoCodi)).getText().length()!=0) {
            Filtre += " and not(Codi like '" + ((TextView) findViewById(R.id.edtStatsNoCodi)).getText() + "' )";
        }
        if (((TextView) findViewById(R.id.edtStatsSiGrup)).getText().length()!=0) {
            Filtre += " and (Grup like '" + ((TextView) findViewById(R.id.edtStatsSiGrup)).getText() + "' )";
        }
        if (((TextView) findViewById(R.id.edtStatsNoGrup)).getText().length()!=0) {
            Filtre += " and not(Grup like '" + ((TextView) findViewById(R.id.edtStatsNoGrup)).getText() + "' )";
        }
        Filtre += " and (AMemoritzar <>0 )";
//        Filtre += " and (NextTipus <> 'a')  and (NextTipus <> 't') and (NextTipus <> '')";


//        if (Filtre.length()>4) Filtre=Filtre.substring(4);
//        objLlistaTrobats.NouSQLtxt(Filtre,"",getApplicationContext());

        Intent myIntent = new Intent(act_stats_sel.this, act_stats.class);
        myIntent.putExtra(act_stats.ARG_FILTRE, Filtre);
        startActivity(myIntent);

    }


}
