package com.gruixuts.cocobe25;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class act_alu_llista_ViewHolder extends RecyclerView.ViewHolder implements View.
        OnClickListener {

    private final TextView ael_curs;
    private final TextView ael_nom;
    private final TextView ael_cognom1;
    private String LlistaTxt;

    act_alu_llista_ViewHolder(@NonNull View itemView, String LlistaCodis) {
        super(itemView);
        ael_curs = itemView.findViewById(R.id.ael_curs);
        ael_nom = itemView.findViewById(R.id.ael_nom);
        ael_cognom1 = itemView.findViewById(R.id.ael_cognom1);
        itemView.setOnClickListener(this);
        LlistaTxt = LlistaCodis;
    }

    void ompleFila(@NonNull classAlumne item) {
        ael_curs.setText(item.getCurs());
        ael_nom.setText(item.getNom());
        ael_cognom1.setText(item.getCognom1());
    }

    @Override
    public void onClick(View v) {
        //listener.m_onClick(v, getAdapterPosition());

        int position = getBindingAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            String text = "Click element de la posició " + position + "   id=" + getItemId();
            Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
            Log.d("act_alu_llista_ViewHolder.onClick", "position = " + position);

            Intent myIntent = new Intent(v.getContext(), act_alu_edita.class);
            myIntent.putExtra(act_alu_edita.ARG_ITEM_ID, "" + position);
            myIntent.putExtra(act_alu_edita.ARG_ITEM_LLISTA,LlistaTxt);
            v.getContext().startActivity(myIntent);
        } else {
            Log.e("act_alu_llista_ViewHolder.onClick", "position == RecyclerView.NO_POSITION");
        }
    }

}
