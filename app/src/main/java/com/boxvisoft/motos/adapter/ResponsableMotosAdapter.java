package com.boxvisoft.motos.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boxvisoft.motos.R;
import com.boxvisoft.motos.controller.Persona;
import com.boxvisoft.motos.model.Motos;
import com.boxvisoft.motos.ui.CrudMotoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponsableMotosAdapter extends RecyclerView.Adapter<ResponsableMotosAdapter.ViewHolderResponsable> {

    private List<Persona.ResposableConMotos> personasList;
    private Map<Integer, Boolean> expandedState;

    public ResponsableMotosAdapter(List<Persona.ResposableConMotos> personasList) {
        this.personasList = personasList;
        this.expandedState = new HashMap<>();

        for (int i = 0; i < personasList.size(); i++) {
            expandedState.put(i, false);
        }
    }

    @NonNull
    @Override
    public ViewHolderResponsable onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_item_personas, parent, false);
        return new ViewHolderResponsable(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderResponsable holder, int position) {
        Persona.ResposableConMotos responsable = personasList.get(position);
        boolean isExpanded = expandedState.get(position);

        holder.bind(responsable, isExpanded, position);
    }

    @Override
    public int getItemCount() {
        return personasList.size();
    }

    public class ViewHolderResponsable extends RecyclerView.ViewHolder {

        private TextView tvNombre, tvCantidadMotos;
        private Button btVerMas;
        private LinearLayout layoutMotosDetalles;
        private RecyclerView recyclerMotos;

        public ViewHolderResponsable(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCantidadMotos = itemView.findViewById(R.id.tvCantidadMotos);
            btVerMas = itemView.findViewById(R.id.btVerMas);

            layoutMotosDetalles = itemView.findViewById(R.id.layoutMotosDetalles);
            recyclerMotos = itemView.findViewById(R.id.recyclerMotos);
//
//            // Configurar LayoutManager
            recyclerMotos.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }

        public void bind(Persona.ResposableConMotos persona, boolean isExpanded, int position) {
            tvNombre.setText(persona.getNombre());
            tvCantidadMotos.setText("Número de motos: " + persona.getCantidadMotos());

            // Configurar texto del botón
            btVerMas.setText(isExpanded ? "Ver menos" : "Ver más");

            Log.d("APP", "Bind - Posición: " + position +
                    " | Nombre: " + persona.getNombre() +
                    " | isExpanded: " + isExpanded +
                    " | Visibilidad: " + (isExpanded ? "VISIBLE" : "GONE"));

            // Mostrar/ocultar detalles
            layoutMotosDetalles.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            Log.d("APP", "Visibilidad real: " + layoutMotosDetalles.getVisibility());


//            // Configurar RecyclerView de motos si está expandido
            if (isExpanded) {
                Log.d("APP", "Configurando RecyclerView para motos...");
                MotosAdapter motosAdapter = new MotosAdapter(persona.getMotos());
                recyclerMotos.setAdapter(motosAdapter);
            } else {
                recyclerMotos.setAdapter(null); // Limpiar adapter cuando no está expandido
            }
//
//            // **CORRECCIÓN PRINCIPAL: Agregar listener al botón**
            btVerMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPosition = getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        boolean currentState = expandedState.get(currentPosition);
                        boolean newState = !currentState;

                        Log.d("APP", "Botón clickeado - Posición: " + currentPosition +
                                " | Estado actual: " + currentState +
                                " | Nuevo estado: " + newState);

                        expandedState.put(currentPosition, newState);
                        notifyItemChanged(currentPosition);
                    }
                }
            });
//                @Override
//                public void onClick(View v) {
//                    // Obtener la posición actual (importante por el reciclaje)
//                    int currentPosition = getAdapterPosition();
//                    if (currentPosition != RecyclerView.NO_POSITION) {
//                        // Alternar estado
//                        boolean currentState = expandedState.get(currentPosition);
//                        expandedState.put(currentPosition, !currentState);
//
//                        // Notificar cambio
//                        notifyItemChanged(currentPosition);
//

//                    }
//                }
//            });

            // **OPCIONAL: Remover el listener del itemView si no lo necesitas**
            // itemView.setOnClickListener(null);
        }
    }
}

//public class ResponsableMotosAdapter extends RecyclerView.Adapter<ResponsableMotosAdapter.ViewHolderResponsable> {
//
//    private List<Persona.ResposableConMotos> personasList;
//    private Map<Integer, Boolean> expandedState;
//
//    public ResponsableMotosAdapter(List<Persona.ResposableConMotos> personasList) {
//        this.personasList = personasList;
//        this.expandedState = new HashMap<>();
//
//        // Inicializar el estado de expansión usando la posición (índice)
//        for (int i = 0; i < personasList.size(); i++) {
//            expandedState.put(i, false);
//        }
//    }
//
//    @NonNull
//    @Override
//    public ViewHolderResponsable onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                // NOTA: Asegúrate de que R.layout.lista_item_personas exista y contenga los IDs
//                .inflate(R.layout.lista_item_personas, parent, false);
//        return new ViewHolderResponsable(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolderResponsable holder, int position) {
//        Persona.ResposableConMotos responsable = personasList.get(position);
//
//// Uso de la posición para obtener el estado
//        boolean isExpanded = expandedState.get(position);
//        holder.bind(responsable, isExpanded);
//
//        // Manejo del click para alternar el estado
//        holder.itemView.setOnClickListener(v -> {
//            boolean currentState = expandedState.get(position);
//            expandedState.put(position, !currentState);
//
//            // Crucial: notificar al adaptador para que re-enlace el ítem en su nueva posición/estado
//            notifyItemChanged(position);
//            Log.d("APP", "ResponsableMotosAdapter::onBindViewHolder: Click en " + responsable.getNombre() + " | Nueva posición: " + position);
//        });
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return personasList.size();
//    }
//
//    public class ViewHolderResponsable extends RecyclerView.ViewHolder {
//
//        private TextView tvNombre, tvCantidadMotos, tvVerMas;
//        private Button btVerMas;
//        private LinearLayout layoutMotosDetalles;
//        private RecyclerView recyclerMotos;
//
//
//        public ViewHolderResponsable(@NonNull View itemView) {
//            super(itemView);
//
//            tvNombre = itemView.findViewById(R.id.tvNombre);
//            tvCantidadMotos = itemView.findViewById(R.id.tvCantidadMotos);
//            btVerMas = itemView.findViewById(R.id.btVerMas);
//            layoutMotosDetalles = itemView.findViewById(R.id.layoutMotosDetalles);
//            recyclerMotos = itemView.findViewById(R.id.recyclerMotos);
//
//            // OPTIMIZACIÓN: Configurar LayoutManager una sola vez
//            if (recyclerMotos != null) {
//                recyclerMotos.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
//            }
//
//        }
//
//        public void bind(Persona.ResposableConMotos persona, boolean isExpanded) {
//            tvNombre.setText(persona.getNombre());
//            tvCantidadMotos.setText("Número de motos: " + persona.getCantidadMotos());
//
//            btVerMas.setText(isExpanded ? "Ver menos" : "Ver mas :3");
//
//            layoutMotosDetalles.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
//
//            if (isExpanded && recyclerMotos != null) {
//
//                // MEJORA: Solo crea un nuevo adaptador si es nulo (primera vez o reciclado sin adaptador)
//                if (recyclerMotos.getAdapter() == null) {
//                    // ASUMO que tienes una clase MotosAdapter implementada
//                    MotosAdapter motosAdapter = new MotosAdapter(persona.getMotos());
//                    recyclerMotos.setAdapter(motosAdapter);
//                }
//                // Si el adaptador ya existe, puedes tener un método para actualizar los datos si es necesario.
//
//            } else if (!isExpanded && recyclerMotos != null) {
//                // OPTIMIZACIÓN: Opcional, puedes quitar el adaptador al colapsar para liberar recursos.
//                // recyclerMotos.setAdapter(null);
//            }
//        }
//    }
//}


