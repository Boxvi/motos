package com.boxvisoft.motos.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boxvisoft.motos.R;
import com.boxvisoft.motos.controller.Persona;
import com.boxvisoft.motos.model.Motos;

import java.util.ArrayList;

public class ListaPersonasAdapter extends RecyclerView.Adapter<ListaPersonasAdapter.PersonasViewHolder> {

    private ArrayList<Persona.PersonaMotos> personasArrayList;
    private ArrayList<Persona.PersonaMotos> personasArrayListOriginal;
    private Intent intent;
    private Activity activity;

    public ListaPersonasAdapter(ArrayList<Persona.PersonaMotos> personasArrayList) {
        this.personasArrayList = personasArrayList;
        personasArrayListOriginal = new ArrayList<>(personasArrayList);
        personasArrayListOriginal.addAll(personasArrayList);
    }

    @NonNull
    @Override
    public PersonasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_personas, parent, false);
        return new PersonasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonasViewHolder holder, int position) {
        holder.txvNombres.setText("Nombre: " + personasArrayList.get(position).getNombre());
        holder.txvNumMotos.setText("Numero de motos: " + String.valueOf(personasArrayList.get(position).getNumMotos()));

    }

    @Override
    public int getItemCount() {
        return personasArrayList.size();
    }

    public class PersonasViewHolder extends RecyclerView.ViewHolder {

        private TextView txvNombres, txvNumMotos;

        public PersonasViewHolder(@NonNull View itemView) {
            super(itemView);

            txvNombres = itemView.findViewById(R.id.tvNombre);
            txvNumMotos = itemView.findViewById(R.id.tvCantidadMotos);
        }
    }
}
