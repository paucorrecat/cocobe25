package com.gruixuts.cocobe25;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class act_inc_llista_ViewHolder extends RecyclerView.ViewHolder implements View.
        OnClickListener {

    private final TextView iel_data;
    private final TextView iel_curs;
    private final TextView iel_alumne;
    private final TextView iel_titol;

    act_inc_llista_ViewHolder(@NonNull View itemView) {
        super(itemView);
        iel_data = itemView.findViewById(R.id.iel_data);
        iel_curs = itemView.findViewById(R.id.iel_curs);
        iel_alumne = itemView.findViewById(R.id.iel_alumne);
        iel_titol = itemView.findViewById(R.id.iel_titol);
        itemView.setOnClickListener(this);
    }

    void ompleFila(@NonNull classIncidencia item, String alumne) {
        classAlumne itemAlu;
//        itemAlu = db.Possi
        iel_data.setText(item.getData());
        iel_curs.setText(item.getCurs());
        iel_alumne.setText(alumne);
        iel_titol.setText(item.getTitol());
    }

    @Override
    public void onClick(View v) {
        //listener.m_onClick(v, getAdapterPosition());
        int position = getBindingAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            String text = "Click element de la posició " + position + "   id=" + getItemId();
            Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(v.getContext(), act_inc_edita.class);
            myIntent.putExtra(act_inc_edita.ARG_ITEM_ID, "" + position);
            v.getContext().startActivity(myIntent);
        }
    }

}
