package com.hadoga.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hadoga.data.model.Expediente;
import java.util.List;
import com.hadoga.dam_project.R;

public class ExpedienteAdapter extends RecyclerView.Adapter<ExpedienteAdapter.ExpedienteViewHolder> {

    private List<Expediente> expedientes;

    // Constructor
    public ExpedienteAdapter(List<Expediente> expedientes) {
        this.expedientes = expedientes;
    }

    @NonNull
    @Override
    public ExpedienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expediente, parent, false);
        return new ExpedienteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpedienteViewHolder holder, int position) {
        Expediente expediente = expedientes.get(position);
        holder.nombreApellido.setText(expediente.nombre + " " + expediente.apellido);
        holder.fechaNacimiento.setText(expediente.fechaNacimiento);
    }

    @Override
    public int getItemCount() {
        return expedientes.size();
    }

    // ViewHolder interno
    public static class ExpedienteViewHolder extends RecyclerView.ViewHolder {
        TextView nombreApellido, fechaNacimiento;

        public ExpedienteViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreApellido = itemView.findViewById(R.id.nombreApellido);
            fechaNacimiento = itemView.findViewById(R.id.fechaNacimiento);
        }
    }

    // Actualizar lista expedientes
    public void setExpedientes(List<Expediente> expedientes) {
        this.expedientes = expedientes;
        notifyDataSetChanged();
    }
}
