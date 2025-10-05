package com.boxvisoft.motos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boxvisoft.motos.R;
import com.boxvisoft.motos.model.Motos;

import java.util.ArrayList;

public class ListaMotosAdapter extends RecyclerView.Adapter<ListaMotosAdapter.MotoViewHolder> {

    private ArrayList<Motos> motosArrayList;
    private ArrayList<Motos> motosArrayListOriginal;


    public ListaMotosAdapter(ArrayList<Motos> motosArrayList) {
        this.motosArrayList = motosArrayList;
        motosArrayListOriginal = new ArrayList<>(motosArrayList);
        motosArrayListOriginal.addAll(motosArrayList);
    }

    @NonNull
    @Override
    public MotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_motos, parent, false);
        return new MotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MotoViewHolder holder, int position) {

        holder.txtSticker.setText(motosArrayList.get(position).getSticker());
        holder.txtNombre.setText("Nombre: " + motosArrayList.get(position).getNombre());
        holder.txtResponsable.setText("Trabajador de: " + motosArrayList.get(position).getResponsable());
        holder.txtPlaca.setText("Placa: " + motosArrayList.get(position).getPlaca());
        holder.txtColor.setText("Color: " + motosArrayList.get(position).getColor());
        holder.txtMoto.setText("Moto: " + motosArrayList.get(position).getMoto());
        holder.txtTelefono.setText("Telefono: " + motosArrayList.get(position).getTelefono());

    }

    @Override
    public int getItemCount() {
        return motosArrayList.size();
    }

    public class MotoViewHolder extends RecyclerView.ViewHolder {

        private TextView txtSticker, txtNombre, txtResponsable, txtPlaca, txtColor, txtMoto, txtTelefono;


        public MotoViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSticker = itemView.findViewById(R.id.txtSticker);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtResponsable = itemView.findViewById(R.id.txtResponsable);
            txtPlaca = itemView.findViewById(R.id.txtPlaca);
            txtColor = itemView.findViewById(R.id.txtColor);
            txtMoto = itemView.findViewById(R.id.txtMoto);
            txtTelefono = itemView.findViewById(R.id.txtTelefono);

        }
    }
}
