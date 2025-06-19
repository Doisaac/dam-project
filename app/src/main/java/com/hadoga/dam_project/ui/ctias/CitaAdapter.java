package com.hadoga.dam_project.ui.ctias;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hadoga.dam_project.R;
import com.hadoga.data.AppDatabase;
import com.hadoga.data.model.Cita;
import com.hadoga.data.model.Expediente;

import java.util.List;

public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.ViewHolder> {

    private final List<Cita> lista;
    private final AppCompatActivity activity;
    private final List<Expediente> pacientes;

    public CitaAdapter(AppCompatActivity activity, List<Cita> lista, List<Expediente> pacientes) {
        this.lista = lista;
        this.activity = activity;
        this.pacientes = pacientes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_cita, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cita cita = lista.get(position);
        Expediente paciente = getPacientePorId(cita.pacienteId);

        holder.textPaciente.setText(paciente != null ? paciente.nombre + " " + paciente.apellido : "Paciente");
        holder.textFechaHora.setText("Fecha: " + cita.fechaHora);
        holder.textMotivo.setText("Motivo: " + cita.motivo);
        holder.textEstado.setText("Estado: " + cita.estado);

        holder.btnEditar.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_home);
            Bundle args = new Bundle();
            args.putInt("cita_id", cita.id);
            navController.navigate(R.id.nav_add_cita, args);
        });

        holder.btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(activity, R.style.alertDialogPersonalizado)
                    .setTitle("Confirmar eliminación de cita")
                    .setMessage("¿Estás seguro que deseas eliminar esta cita?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        AppDatabase db = AppDatabase.getInstance(activity);
                        db.citaDao().delete(cita);
                        lista.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, lista.size());

                        // Snackbar
                        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), "¡Cita eliminada correctamente!", Snackbar.LENGTH_LONG);

                        View snackbarView = snackbar.getView();
                        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

                        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);

                        snackbar.show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    private Expediente getPacientePorId(int id) {
        for (Expediente p : pacientes) {
            if (p.id == id) return p;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textPaciente, textFechaHora, textMotivo, textEstado;
        Button btnEditar, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textPaciente = itemView.findViewById(R.id.textPaciente);
            textFechaHora = itemView.findViewById(R.id.textFechaHora);
            textMotivo = itemView.findViewById(R.id.textMotivo);
            textEstado = itemView.findViewById(R.id.textEstado);
            btnEditar = itemView.findViewById(R.id.btnEditarCita);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCita);
        }
    }
}