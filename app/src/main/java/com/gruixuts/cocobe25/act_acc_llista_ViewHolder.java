package com.gruixuts.cocobe25;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class act_acc_llista_ViewHolder extends RecyclerView.ViewHolder implements View.
        OnClickListener {

    private final TextView cel_id;
    private final TextView cel_data;
    private final TextView cel_tipus;
    private final TextView cel_titol;
    private String LlistaTxt;
    private ActivityResultLauncher<Intent> editLauncher;

    act_acc_llista_ViewHolder(@NonNull View itemView, String LlistaCodis, ActivityResultLauncher<Intent> editlauncher) {
        super(itemView);
        cel_id = itemView.findViewById(R.id.cel_id);
        cel_data = itemView.findViewById(R.id.cel_data);
        cel_tipus = itemView.findViewById(R.id.cel_tipus);
        cel_titol = itemView.findViewById(R.id.cel_titol);
        itemView.setOnClickListener(this);
        LlistaTxt = LlistaCodis;
        this.editLauncher = editlauncher;
    }

    void ompleFila(@NonNull classActuacio item) {
        cel_id.setText(item.getId());
        if (item.getPendent()) {
            cel_id.setBackgroundColor(Color.CYAN);
        } else {
            cel_id.setBackgroundColor(Color.WHITE);
        }
        cel_data.setText(item.getData().toString());
        cel_tipus.setText(item.getTipus());
        cel_titol.setText(item.getTitol());
    }

    @Override
    public void onClick(View v) {
        //listener.m_onClick(v, getAdapterPosition());

        int position = getBindingAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            String text = "Click element de la posició " + position + "   id=" + getItemId();
            Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
            Log.d("act_acc_llista_ViewHolder.onClick", "position = " + position);

            Intent myIntent = new Intent(v.getContext(), act_acc_edita.class);
            myIntent.putExtra(act_acc_edita.ARG_ITEM_ID, "" + position);
            myIntent.putExtra(act_acc_edita.ARG_ITEM_LLISTA,LlistaTxt);
            editLauncher.launch(myIntent);
            //v.getContext().startActivity(myIntent);
        } else {
            Log.e("act_acc_llista_ViewHolder.onClick", "position == RecyclerView.NO_POSITION");
        }
    }

}
