package com.gruixuts.cocobe25;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import android.content.Intent;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frg_acc_llista#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frg_acc_llista extends Fragment {

    public static final String ARG_SQL = "sql";
    public static final String ARG_ID = "id";
    Toolbar toolbar;

    private View view;
    GestorDB db;
    ArrayList<classActuacio> LlistaTrobats;
    String llistaClaus;
    String txtSql;
    String incIdActual;

    private ActivityResultLauncher<Intent> editLauncher;

    public frg_acc_llista() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param txtsql Parameter 1.
     * @return A new instance of fragment frg_acc_llista.
     */

    public static frg_acc_llista newInstance(String txtsql, String incId) {
        frg_acc_llista fragment = new frg_acc_llista();
        Bundle args = new Bundle();
        args.putString(ARG_SQL, txtsql);
        args.putString(ARG_ID, incId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            txtSql = getArguments().getString(ARG_SQL);
            incIdActual = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inc_acc_llista, container, false);

        // Aquí busquem la vista "main" dins el layout del fragment
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Context context = getActivity();
        if (context != null) {
            db = new GestorDB(context);
        } else {
            // Manejar l'error si el context és null
            Log.e("frg_acc_llista","Contex = null");
            return view;
        }

        LlistaTrobats = db.selAccSQL(txtSql);
        // Cridar als mètodes per configurar la Toolbar i el RecyclerView
        setupToolbar();
        setupRecyclerView(view);
        return view;
   ///     return inflater.inflate(R.layout.fragment_acc_llista, container, false);
        // Altres configuracions i inicialitzacions del fragment
    }

    public void setEditLauncher(ActivityResultLauncher<Intent> editLauncher) {
        this.editLauncher = editLauncher;
    }

    public void setSQL(String txtSQL, String  incId) {
        Log.d("frg_acc_llista.setSQL", "SQL= " + txtSQL);

        LlistaTrobats = db.selAccSQL(txtSQL);
        llistaClaus = db.LlistaAcc2Cadena(LlistaTrobats);
        incIdActual = incId;
        Log.d("frg_acc_llista.setSQL", "Trobats= " + LlistaTrobats.size() + ": " + llistaClaus);

        RecyclerView recyclerView = view.findViewById(R.id.ll_acc_recyclerView);
        act_acc_llista_Adapter adapter = (act_acc_llista_Adapter) recyclerView.getAdapter();
        if (toolbar != null) {
            toolbar.setTitle("Actuacions (" + LlistaTrobats.size() + ")");
        }
        if (adapter != null) {
            adapter.updateList(LlistaTrobats,llistaClaus);
        } else {
            Log.e("frg_acc_llista.setSQL", "adapter == null");
        }

    }

    private void setupToolbar() {//ll_acc_toolbar
        toolbar = view.findViewById(R.id.ll_acc_toolbar);
        Log.d("frg_acc_llista.setupToolbar", "Entra");
        toolbar.setTitle("Actuacions (" + LlistaTrobats.size() + ")");
        // Verifiquem que l'activitat és una instància de AppCompatActivity
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
        } else {
            Log.e("frg_acc_llista.setupToolbar", "No funciona");
            throw new IllegalStateException("L'activitat que conté aquest fragment no és una AppCompatActivity");
        }

        // Configura l'ImageButton
        ImageButton myImageButton = toolbar.findViewById(R.id.cmd_acc_crea);
        myImageButton.setOnClickListener(v -> {

        String NouId = db.insActuacio(incIdActual);
        Intent myIntent = new Intent(getActivity(), act_acc_edita.class);
        myIntent.putExtra(act_acc_edita.ARG_ITEM_ID, "0");
        myIntent.putExtra(act_acc_edita.ARG_ITEM_LLISTA, NouId);
        startActivity(myIntent);

        });

    }

    private void AccCrea(View view) {
//        objLlistaTrobats.ITEMS.clear();


    }


    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.ll_acc_recyclerView);
        llistaClaus = db.LlistaAcc2Cadena(LlistaTrobats);

        Log.d("frg_acc_llista.setupRecyclerView", "LlistaClaus = " + llistaClaus);
        // Comprova si l'adaptador està configurat abans d'assignar-lo
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(new act_acc_llista_Adapter(LlistaTrobats, llistaClaus, editLauncher));
            Log.d("frg_acc_llista.setupRecyclerView", "Adaptador creat");
        }

        // Configura el LayoutManager si encara no ho està
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            Log.d("frg_acc_llista.setupRecyclerView", "Layout Mgr creat");
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation()
        );
        recyclerView.addItemDecoration(dividerItemDecoration);
        Log.d("frg_acc_llista.setupRecyclerView", "Sortida");

    }
}