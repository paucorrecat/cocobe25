package com.gruixuts.cocobe25;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class act_alu_llista_Adapter extends RecyclerView.Adapter<act_alu_llista_ViewHolder> {
    private List<classAlumne> dades;
    private String LlistaClaus;
    GestorDB db;
    public act_alu_llista_Adapter(@NonNull List<classAlumne> data, String llistaClaus) {
        this.dades = data;
        LlistaClaus=llistaClaus;
    }

    @Override
    @NonNull
    public act_alu_llista_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_alu_llista_elem, parent, false);
        return new act_alu_llista_ViewHolder(row,LlistaClaus);
    }

     @Override
    public void onBindViewHolder(act_alu_llista_ViewHolder holder, int position) {
        holder.ompleFila(dades.get(position));
    }

    @Override
    public int getItemCount() {
        return dades.size();
    }
}
