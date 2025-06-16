package com.hadoga.dam_project.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.TextView;

import com.hadoga.dam_project.R;
import com.hadoga.dam_project.ui.expediente.AddExpedienteFragment;
import com.hadoga.data.AppDatabase;
import com.hadoga.data.model.Expediente;

import java.util.List;

public class ExpedienteAdapter extends RecyclerView.Adapter<ExpedienteAdapter.ViewHolder> {

    private final List<Expediente> lista;
    private final LayoutInflater inflater;
    private final AppCompatActivity activity;

    public ExpedienteAdapter(AppCompatActivity activity, List<Expediente> lista) {
        this.lista = lista;
        this.inflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_expediente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expediente expediente = lista.get(position);

        holder.textNombre.setText(expediente.nombre + " " + expediente.apellido);
        holder.textNacimiento.setText("Nacimiento: " + expediente.fechaNacimiento);

        // Botón editar
        holder.btnEditar.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_home);
            Bundle args = new Bundle();
            args.putInt("expediente_id", expediente.id);
            navController.navigate(R.id.nav_add_expediente, args);
        });

        // Botón eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(activity);
            db.expedienteDao().delete(expediente);

            lista.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, lista.size());
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textNacimiento;
        Button btnEditar, btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textNombre);
            textNacimiento = itemView.findViewById(R.id.textNacimiento);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}