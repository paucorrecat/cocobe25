package com.gruixuts.cocobe25;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class act_inc_llista_Adapter extends RecyclerView.Adapter<act_inc_llista_ViewHolder> {
    private List<classIncidencia> dades;
    private List<String> Alumnes;

    public act_inc_llista_Adapter(@NonNull List<classIncidencia> data) {
        this.dades = data;
        this.Alumnes = incLlistaTrobades.ITEMS_ALU;
    }

    @Override
    @NonNull
    public act_inc_llista_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_inc_llista_elem, parent, false);
        return new act_inc_llista_ViewHolder(row);
    }

     @Override
    public void onBindViewHolder(act_inc_llista_ViewHolder holder, int position) {
        holder.ompleFila(dades.get(position),Alumnes.get(position));
    }

    @Override
    public int getItemCount() {
        return dades.size();
    }
}
