package com.gruixuts.cocobe25;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class act_inc_llista extends AppCompatActivity  {

    GestorDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inc_llista);

        setupToolbar();
        setupRecyclerView();

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.ill_toolbar);
        toolbar.setTitle("Incidències (" + incLlistaTrobades.ITEMS.size() + ")");
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.ill_recyclerView);
//        db=  new GestorDB(getApplicationContext());
//        db.open();
        recyclerView.setAdapter(new act_inc_llista_Adapter(incLlistaTrobades.ITEMS));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

}