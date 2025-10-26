package com.boxvisoft.motos.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boxvisoft.motos.R;
import com.boxvisoft.motos.controller.Persona;
import com.boxvisoft.motos.model.Motos;
import com.boxvisoft.motos.ui.CrudMotoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MotosAdapter extends RecyclerView.Adapter<MotosAdapter.MotoViewHolder> {

    private List<Motos> motosList;

    public MotosAdapter(List<Motos> motosList) {
        this.motosList = motosList != null ? motosList : new ArrayList<>();
    }

    @NonNull
    @Override
    public MotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_moto_detalle, parent, false); // ✅ CAMBIO IMPORTANTE
        return new MotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MotoViewHolder holder, int position) {
        Motos moto = motosList.get(position);
        holder.bind(moto);

        // Opcional: agregar click listener a cada moto
        holder.itemView.setOnClickListener(v -> {
            Log.d("APP", "Moto clickeada: " + moto.getPlaca());
            // Aquí puedes abrir detalles de la moto, etc.
        });
    }

    @Override
    public int getItemCount() {
        return motosList.size();
    }

    public class MotoViewHolder extends RecyclerView.ViewHolder {

        private TextView txtSticker, txtNombre, txtResponsable, txtPlaca, txtColor, txtMoto, txtTelefono;

        public MotoViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSticker = itemView.findViewById(R.id.txtSticker);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPlaca = itemView.findViewById(R.id.txtPlaca);
            txtColor = itemView.findViewById(R.id.txtColor);
            txtMoto = itemView.findViewById(R.id.txtMoto);
            txtTelefono = itemView.findViewById(R.id.txtTelefono);
            txtResponsable = itemView.findViewById(R.id.txtResponsable);


        }

        public void bind(Motos moto) {

            txtSticker.setText(moto.getSticker());
            txtNombre.setText("Nombre: " + moto.getNombre());
            txtPlaca.setText("Placa: " + moto.getPlaca());
            txtColor.setText("Color: " + moto.getColor());
            txtMoto.setText("Moto: " + moto.getMoto());
            txtResponsable.setText("Responsable: " + moto.getResponsable());
            txtTelefono.setText("Telefono: " + moto.getTelefono());
        }
    }

    // Método para actualizar datos
    public void updateMotos(List<Motos> nuevasMotos) {
        this.motosList = nuevasMotos != null ? nuevasMotos : new ArrayList<>();
        notifyDataSetChanged();
    }
}