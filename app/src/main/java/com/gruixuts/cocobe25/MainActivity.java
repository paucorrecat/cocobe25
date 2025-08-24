package com.gruixuts.cocobe25;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

/*    public void goToColors(View view) {
        Intent myIntent = new Intent(MainActivity.this, act_colors.class);
        MainActivity.this.startActivity(myIntent);
    }

 */

    public void goToImportExport(View view) {
        Intent myIntent = new Intent(MainActivity.this, act_import_export.class);
        MainActivity.this.startActivity(myIntent);
    }


    public void goToIncidencies(View view) {
        Intent myIntent = new Intent(MainActivity.this, act_inc_sel.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void goToAlumnes(View view) {
        Intent myIntent = new Intent(MainActivity.this, act_alu_sel.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void goToProfessors(View view) {
        Intent myIntent = new Intent(MainActivity.this, act_prf_sel.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void goToActuacions(View view) {
        Intent myIntent = new Intent(MainActivity.this, act_acc_sel.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void goToStats(View view) {
        Intent myIntent = new Intent(MainActivity.this, act_stats_sel.class);
        MainActivity.this.startActivity(myIntent);
    }
/*
    public void goToTest(View view) {
        Intent myIntent = new Intent(MainActivity.this, act_mnt_llista.class);
        MainActivity.this.startActivity(myIntent);
    }
*/



}