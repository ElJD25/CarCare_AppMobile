package com.example.prueba1.presenters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba1.R;
import com.example.prueba1.model.Mantenimiento;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class MantenimientoAdapter extends FirestoreRecyclerAdapter<Mantenimiento, MantenimientoAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MantenimientoAdapter(@NonNull FirestoreRecyclerOptions<Mantenimiento> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Mantenimiento mantenimiento) {
        viewHolder.fecha_actual.setText(mantenimiento.getFecha());
        viewHolder.fecha_prox.setText(mantenimiento.getFecha_prox());
        viewHolder.km_actual.setText(mantenimiento.getKm_actual());
        viewHolder.km_prox.setText(mantenimiento.getKm_prox());
        viewHolder.notas.setText(mantenimiento.getNotas());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_mantenimiento_single, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fecha_actual, fecha_prox, km_actual, km_prox, notas;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fecha_actual = itemView.findViewById(R.id.fecha_actual);
            fecha_prox = itemView.findViewById(R.id.fecha_prox);
            km_actual = itemView.findViewById(R.id.km_actual);
            km_prox = itemView.findViewById(R.id.km_prox);
            notas = itemView.findViewById(R.id.notas);
        }
    }
}
