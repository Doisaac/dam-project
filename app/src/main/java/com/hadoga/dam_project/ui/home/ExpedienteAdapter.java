package com.hadoga.dam_project.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.hadoga.dam_project.R;
import com.hadoga.data.AppDatabase;
import com.hadoga.data.model.Expediente;

import java.io.File;
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

        // Cargar foto
        if (expediente.fotoUri != null && !expediente.fotoUri.isEmpty()) {
            File file = new File(Uri.parse(expediente.fotoUri).getPath());
            if (file.exists()) {
                holder.imageExpediente.setImageURI(Uri.parse(expediente.fotoUri));
            } else {
                holder.imageExpediente.setImageResource(R.drawable.ic_person_placeholder);
            }
        } else {
            holder.imageExpediente.setImageResource(R.drawable.ic_person_placeholder);
        }

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
            new AlertDialog.Builder(activity)
                    .setTitle("Confirmar eliminación de expediente")
                    .setMessage("¿Estás seguro que deseas eliminar este expediente?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        AppDatabase db = AppDatabase.getInstance(activity);
                        db.expedienteDao().delete(expediente);

                        lista.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, lista.size());

                        // Snackbar
                        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), "¡Expediente eliminado correctamente!", Snackbar.LENGTH_LONG);

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

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageExpediente;
        TextView textNombre, textNacimiento;
        Button btnEditar, btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);
            imageExpediente = itemView.findViewById(R.id.imageExpediente);
            textNombre = itemView.findViewById(R.id.textNombre);
            textNacimiento = itemView.findViewById(R.id.textNacimiento);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}