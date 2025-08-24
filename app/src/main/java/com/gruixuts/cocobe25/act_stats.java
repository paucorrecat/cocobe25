package com.gruixuts.cocobe25;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

public class act_stats extends AppCompatActivity {
    public static final String ARG_FILTRE = "filtre";
    String Filtre;

    SimpleDateFormat frmtData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Filtre = getIntent().getStringExtra(ARG_FILTRE);

        GestorDB db;
//        ArrayList<classDiccionari> Llista;
//        Integer N1;
//        Integer N2;
//
//        db = new GestorDB(getApplicationContext());
//        db.open();
//        // Total
//        N1=db.selDiccionari(Filtre,"").size();
//        ((TextView) findViewById(R.id.TauTotal)).setText(""+N1);
//
//        if (Filtre.length()>0) {
//            Filtre = " and " + Filtre;
//        }
//
//        // A Memoritzar (abans hi havia conceptes que no eren per memoritzar, només per agrupar conceptes)
//        N2=db.selDiccionari("(AMemoritzar)" + Filtre,"").size();
//        ((TextView) findViewById(R.id.TauAMemSi)).setText(""+N2);
//        ((TextView) findViewById(R.id.TauAMemNo)).setText(""+(N1-N2));
//
//        // Té foto? (ant: traduida)
//        N1=db.selDiccionari("(NextTipus<>'t')  and (AMemoritzar)" + Filtre,"").size();
//        ((TextView) findViewById(R.id.TauTraSi)).setText(""+N1);
//        ((TextView) findViewById(R.id.TauTraNo)).setText(""+(N2-N1));
//
//        // Apreses
//        N2=db.selDiccionari("(NextTipus<>'a')  and (NextTipus<>'t')  and (AMemoritzar)" + Filtre,"").size();
//        ((TextView) findViewById(R.id.TauAprSi)).setText(""+N2);
//        ((TextView) findViewById(R.id.TauAprNo)).setText(""+(N1-N2));
//
//
//        /*
//        // Errors
//        N2=db.selDiccionari("(Not(Traduible) and (Basc<>''))","").size();
//        N2=db.selDiccionari("(Not(Traduible) and (NextTipus<>''))","").size();
//        N2=db.selDiccionari("((Traduible) and (NextTipus=''))","").size();
//        N2=db.selDiccionari("((Basc<>'') and (NextTipus=''))","").size();
//        N2=db.selDiccionari("((Basc<>'') and (NextTipus='t'))","").size();
//        N2=db.selDiccionari("((Traduible) and (Basc='') and (NextTipus<>'t'))","").size();
//
//        */
//
//        N1=db.selDiccionari("(NextTipus='1h')"  + Filtre,"").size();
//        N2=db.selDiccionari("((NextTipus='1h') and (NextData < '" + frmtData.format(new Date()) + "'))" + Filtre,"").size();
//        ((TextView) findViewById(R.id.TauRp1hM)).setText(""+N2);
//        ((TextView) findViewById(R.id.TauRp1hB)).setText(""+(N1-N2));
//
//        N1=db.selDiccionari("(NextTipus='1d')","").size();
//        N2=db.selDiccionari("((NextTipus='1d') and (NextData < '" + frmtData.format(new Date()) + "'))" + Filtre,"").size();
//        ((TextView) findViewById(R.id.TauRp1dM)).setText(""+N2);
//        ((TextView) findViewById(R.id.TauRp1dB)).setText(""+(N1-N2));
//
//        N1=db.selDiccionari("(NextTipus='1s')","").size();
//        N2=db.selDiccionari("((NextTipus='1s') and (NextData < '" + frmtData.format(new Date()) + "'))" + Filtre,"").size();
//        ((TextView) findViewById(R.id.TauRp1sM)).setText(""+N2);
//        ((TextView) findViewById(R.id.TauRp1sB)).setText(""+(N1-N2));
//
//        N1=db.selDiccionari("(NextTipus='1m')","").size();
//        N2=db.selDiccionari("((NextTipus='1m') and (NextData < '" + frmtData.format(new Date()) + "'))" + Filtre,"").size();
//        ((TextView) findViewById(R.id.TauRp1mM)).setText(""+N2);
//        ((TextView) findViewById(R.id.TauRp1mB)).setText(""+(N1-N2));
//
//        N1=db.selDiccionari("(NextTipus='6m')","").size();
//        N2=db.selDiccionari("((NextTipus='6m') and (NextData < '" + frmtData.format(new Date()) + "'))" + Filtre,"").size();
//        ((TextView) findViewById(R.id.TauRp6mM)).setText(""+N2);
//        ((TextView) findViewById(R.id.TauRp6mB)).setText(""+(N1-N2));
//
//        db.close();

    }
}
