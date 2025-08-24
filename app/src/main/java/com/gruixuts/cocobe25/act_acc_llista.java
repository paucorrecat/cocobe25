package com.gruixuts.cocobe25;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class act_acc_llista extends AppCompatActivity {

    public static final String ARG_SQL = "sql";
    ArrayList<classActuacio> LlistaTrobats;
    GestorDB db;

    private ActivityResultLauncher<Intent> editLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acc_llista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db =  new GestorDB(getApplicationContext());
        LlistaTrobats = db.selAccSQL(getIntent().getStringExtra(ARG_SQL));
        // Cridar als mètodes per configurar la Toolbar i el RecyclerView

        // Per fer que actualitzi quan torna de _edita
        editLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Actualitza la llista
                        LlistaTrobats = db.selAccSQL(getIntent().getStringExtra(ARG_SQL));
                        setupRecyclerView();
                    }
                });

        setupToolbar();
        setupRecyclerView();

    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.ll_acc_toolbar);
        toolbar.setTitle("Actuacions (" + LlistaTrobats.size() + ")");
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.ll_acc_recyclerView);
        db=  new GestorDB(getApplicationContext());
        String LlistaTxt = db.LlistaAcc2Cadena(LlistaTrobats);
        recyclerView.setAdapter(new act_acc_llista_Adapter(LlistaTrobats,LlistaTxt, editLauncher));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }


}