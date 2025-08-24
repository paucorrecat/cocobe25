package com.gruixuts.cocobe25;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class act_acc_llista_Adapter extends RecyclerView.Adapter<act_acc_llista_ViewHolder> {
    private List<classActuacio> dades;
    private String LlistaClaus;
    private ActivityResultLauncher<Intent> editLauncher;


    GestorDB db;

    public act_acc_llista_Adapter(@NonNull List<classActuacio> data, String llistaClaus, ActivityResultLauncher<Intent> editLauncher) {
        this.dades = data;
        LlistaClaus=llistaClaus;
        this.editLauncher = editLauncher;
    }

    @Override
    @NonNull
    public act_acc_llista_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_acc_llista_elem, parent, false);
        return new act_acc_llista_ViewHolder(row,LlistaClaus, editLauncher);
    }

     @Override
    public void onBindViewHolder(act_acc_llista_ViewHolder holder, int position) {
        holder.ompleFila(dades.get(position));
    }

    @Override
    public int getItemCount() {
        return dades.size();
    }

    public void updateList(List<classActuacio> newDades, String newLlistaClaus) {
        this.dades = newDades;
        this.LlistaClaus = newLlistaClaus;
        notifyDataSetChanged();
    }
}


