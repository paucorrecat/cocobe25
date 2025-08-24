package com.gruixuts.cocobe25;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class act_prf_llista_ViewHolder extends RecyclerView.ViewHolder implements View.
        OnClickListener {

    private final TextView pel_curs;
    private final TextView pel_nom;
    private final TextView pel_cognom1;
    private String LlistaTxt;

    act_prf_llista_ViewHolder(@NonNull View itemView, String LlistaCodis) {
        super(itemView);
        pel_curs = itemView.findViewById(R.id.pel_rols);
        pel_nom = itemView.findViewById(R.id.pel_nom);
        pel_cognom1 = itemView.findViewById(R.id.pel_cognom1);
        itemView.setOnClickListener(this);
        LlistaTxt = LlistaCodis;
    }

    void ompleFila(@NonNull classProfessor item) {
        pel_curs.setText(item.getRols());
        pel_nom.setText(item.getNom());
        pel_cognom1.setText(item.getCognom1());
    }

    @Override
    public void onClick(View v) {
        //listener.m_onClick(v, getAdapterPosition());

        int position = getBindingAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            String text = "Click element de la posició " + position + "   id=" + getItemId();
            Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
            Log.d("act_prf_llista_ViewHolder.onClick", "position = " + position);

            Intent myIntent = new Intent(v.getContext(), act_prf_edita.class);
            myIntent.putExtra(act_prf_edita.ARG_ITEM_ID, "" + position);
            myIntent.putExtra(act_prf_edita.ARG_ITEM_LLISTA,LlistaTxt);
            v.getContext().startActivity(myIntent);
        } else {
            Log.e("act_prf_llista_ViewHolder.onClick", "position == RecyclerView.NO_POSITION");
        }
    }

}
