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
import android.widget.TextView;

import com.hadoga.dam_project.R;
import com.hadoga.dam_project.ui.expediente.AddExpedienteFragment;
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
        View view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expediente expediente = lista.get(position);
        holder.text1.setText(expediente.nombre + " " + expediente.apellido);
        holder.text2.setText("Nacimiento: " + expediente.fechaNacimiento);

        holder.itemView.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_home);
            Bundle args = new Bundle();
            args.putInt("expediente_id", expediente.id);
            navController.navigate(R.id.nav_add_expediente, args);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}