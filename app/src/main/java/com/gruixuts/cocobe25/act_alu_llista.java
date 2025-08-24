package com.gruixuts.cocobe25;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class act_alu_llista extends AppCompatActivity {

    public static final String ARG_SQL = "sql";
    ArrayList<classAlumne> LlistaTrobats;
    GestorDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alu_llista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db =  new GestorDB(getApplicationContext());
        LlistaTrobats = db.selAluSQL(getIntent().getStringExtra(ARG_SQL));
        // Cridar als mètodes per configurar la Toolbar i el RecyclerView
        setupToolbar();
        setupRecyclerView();
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.ll_alu_toolbar);
        toolbar.setTitle("Alumnes (" + LlistaTrobats.size() + ")");
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.ll_alu_recyclerView);
        db=  new GestorDB(getApplicationContext());
        db.open();
        String LlistaTxt = db.LlistaAlu2Cadena(LlistaTrobats);
        recyclerView.setAdapter(new act_alu_llista_Adapter(LlistaTrobats,LlistaTxt));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

}