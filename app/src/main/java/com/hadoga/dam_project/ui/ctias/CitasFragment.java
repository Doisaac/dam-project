package com.hadoga.dam_project.ui.ctias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hadoga.dam_project.R;
import com.hadoga.dam_project.databinding.FragmentCitasBinding;
import com.hadoga.data.AppDatabase;
import com.hadoga.data.model.Cita;
import com.hadoga.data.model.Expediente;

import java.util.List;

public class CitasFragment extends Fragment {

    private FragmentCitasBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCitasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnNuevaCita = view.findViewById(R.id.button_nueva_cita);
        btnNuevaCita.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_home);
            navController.navigate(R.id.nav_add_cita);
        });

        // RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCitas);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        AppDatabase db = AppDatabase.getInstance(requireContext());
        List<Cita> citas = db.citaDao().getAll();
        List<Expediente> pacientes = db.expedienteDao().getAll();

        CitaAdapter adapter = new CitaAdapter((AppCompatActivity) requireActivity(), citas, pacientes);
        recyclerView.setAdapter(adapter);
    }
}