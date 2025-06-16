package com.hadoga.dam_project;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hadoga.data.adapter.ExpedienteAdapter;
import com.hadoga.data.model.Expediente;
import java.util.ArrayList;
import java.util.List;

public class ExpedienteListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExpedienteAdapter adapter;
    private List<Expediente> listaDeExpedientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expediente_lista);

        recyclerView = findViewById(R.id.recyclerViewExpedientes);

        // Para mientras, luego reemplazar por datos reales (Room)
        listaDeExpedientes = new ArrayList<>();
        listaDeExpedientes.add(new Expediente("Juan", "Pérez", "01/01/2000"));
        listaDeExpedientes.add(new Expediente("Ana", "López", "02/02/2001"));

        adapter = new ExpedienteAdapter(listaDeExpedientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
