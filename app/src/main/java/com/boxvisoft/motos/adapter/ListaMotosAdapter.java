package com.boxvisoft.motos.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boxvisoft.motos.R;
import com.boxvisoft.motos.model.Motos;
import com.boxvisoft.motos.ui.CrudMotoActivity;

import java.util.ArrayList;
import java.util.List;

public class ListaMotosAdapter extends RecyclerView.Adapter<ListaMotosAdapter.MotoViewHolder> {

    private List<Motos> motosList;
    private List<Motos> motosListOriginal;
    private Intent intent;
    private Activity activity;

    public ListaMotosAdapter(List<Motos> motosList, Activity activity) {
        this.motosList = new ArrayList<>(motosList);
        this.motosListOriginal = new ArrayList<>(motosList);
        this.activity = activity;
    }

    @NonNull
    @Override
    public MotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_motos, parent, false);
        return new MotoViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull MotoViewHolder holder, int position) {

        holder.txtSticker.setText(motosList.get(position).getSticker());
        holder.txtNombre.setText("Nombre: " + motosList.get(position).getNombre());
        holder.txtResponsable.setText("Trabajador de: " + motosList.get(position).getResponsable());
        holder.txtPlaca.setText("Placa: " + motosList.get(position).getPlaca());
        holder.txtColor.setText("Color: " + motosList.get(position).getColor());
        holder.txtMoto.setText("Moto: " + motosList.get(position).getMoto());
        holder.txtTelefono.setText("Telefono: " + motosList.get(position).getTelefono());

    }

    public void filtrado(String textoBusqueda) {
        textoBusqueda = textoBusqueda.toLowerCase().trim();
        List<Motos> listaFiltrada = new ArrayList<>();

        if (textoBusqueda.isEmpty()) {
            listaFiltrada.addAll(motosListOriginal);
        } else {
            for (Motos moto : motosListOriginal) {
                if (moto.getSticker() != null &&
                        moto.getSticker().toLowerCase().contains(textoBusqueda)) {
                    listaFiltrada.add(moto);
                }
            }
        }

        motosList.clear();
        motosList.addAll(listaFiltrada);
        notifyDataSetChanged();
    }

    public void actualizarDatos(List<Motos> nuevosDatos) {
        motosListOriginal.clear();
        motosListOriginal.addAll(nuevosDatos);

        motosList.clear();
        motosList.addAll(nuevosDatos);
        notifyDataSetChanged();
    }




    @Override
    public int getItemCount() {
        return motosList.size();
    }

    public class MotoViewHolder extends RecyclerView.ViewHolder {

        private TextView txtSticker, txtNombre, txtResponsable, txtPlaca, txtColor, txtMoto, txtTelefono;
        private Activity activity;

        public MotoViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;

            txtSticker = itemView.findViewById(R.id.txtSticker);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtResponsable = itemView.findViewById(R.id.txtResponsable);
            txtPlaca = itemView.findViewById(R.id.txtPlaca);
            txtColor = itemView.findViewById(R.id.txtColor);
            txtMoto = itemView.findViewById(R.id.txtMoto);
            txtTelefono = itemView.findViewById(R.id.txtTelefono);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Context context = view.getContext();

                    intent = new Intent(context, CrudMotoActivity.class);
                    intent.putExtra("TITULO", "EDITAR MOTOS");
                    intent.putExtra("ID", motosList.get(getAdapterPosition()).getIdColeccion());

                    context.startActivity(intent);
                    activity.finish();

                }
            });


        }
    }
}
