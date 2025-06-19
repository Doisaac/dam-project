package com.hadoga.dam_project.ui.ctias;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.hadoga.dam_project.R;
import com.hadoga.data.AppDatabase;
import com.hadoga.data.model.Cita;
import com.hadoga.data.model.Expediente;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCitaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCitaFragment extends Fragment {

    private static final String ARG_CITA_ID = "cita_id";
    private Cita citaEditando = null;
    private int citaId = -1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCitaFragment() {
        // Required empty public constructor
    }

    public static AddCitaFragment newInstance(int citaId) {
        AddCitaFragment fragment = new AddCitaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CITA_ID, citaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            citaId = getArguments().getInt("cita_id", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_cita, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinnerPacientes = view.findViewById(R.id.spinnerPacientes);
        Spinner spinnerEstado = view.findViewById(R.id.spinnerEstado);
        EditText editTextFechaHora = view.findViewById(R.id.editTextFechaHora);
        EditText editTextMotivo = view.findViewById(R.id.editTextMotivo);
        EditText editTextNotas = view.findViewById(R.id.editTextNotas);
        Button btnGuardar = view.findViewById(R.id.btnGuardarCita);

        AppDatabase db = AppDatabase.getInstance(requireContext());
        List<Expediente> pacientes = db.expedienteDao().getAll();

        // Pacientes al spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                pacientes.stream().map(p -> p.nombre + " " + p.apellido).collect(Collectors.toList())
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPacientes.setAdapter(adapter);

        // Estado al spinner
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                Arrays.asList("pendiente", "cancelada", "completada")
        );
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(estadoAdapter);

        // Fecha y hora con pickers
        editTextFechaHora.setOnClickListener(v -> mostrarDateTimePicker(editTextFechaHora));

        // Si es edición, precargar los datos
        if (citaId != -1) {
            citaEditando = db.citaDao().getById(citaId);
            if (citaEditando != null) {
                editTextFechaHora.setText(citaEditando.fechaHora);
                editTextMotivo.setText(citaEditando.motivo);
                editTextNotas.setText(citaEditando.notas);

                for (int i = 0; i < pacientes.size(); i++) {
                    if (pacientes.get(i).id == citaEditando.pacienteId) {
                        spinnerPacientes.setSelection(i);
                        break;
                    }
                }

                for (int i = 0; i < spinnerEstado.getCount(); i++) {
                    if (spinnerEstado.getItemAtPosition(i).toString().equalsIgnoreCase(citaEditando.estado)) {
                        spinnerEstado.setSelection(i);
                        break;
                    }
                }
            }
        }

        // Guardar cita (crear o actualizar)
        btnGuardar.setOnClickListener(v -> {
            int indexSeleccionado = spinnerPacientes.getSelectedItemPosition();
            if (indexSeleccionado == -1) {
                Toast.makeText(requireContext(), "Debe seleccionar un paciente", Toast.LENGTH_SHORT).show();
                return;
            }

            String fechaHora = editTextFechaHora.getText().toString().trim();
            String motivo = editTextMotivo.getText().toString().trim();
            String notas = editTextNotas.getText().toString().trim();

            if (fechaHora.isEmpty()) {
                editTextFechaHora.setError("La fecha y hora es obligatoria");
                editTextFechaHora.requestFocus();
                return;
            }

            if (motivo.isEmpty()) {
                editTextMotivo.setError("El motivo es obligatorio");
                editTextMotivo.requestFocus();
                return;
            }

            String estadoSeleccionado = spinnerEstado.getSelectedItem().toString();

            if (citaEditando == null) {
                citaEditando = new Cita();
            }

            citaEditando.pacienteId = pacientes.get(indexSeleccionado).id;
            citaEditando.fechaHora = fechaHora;
            citaEditando.motivo = motivo;
            citaEditando.notas = notas;
            citaEditando.estado = estadoSeleccionado;

            // Validar si no hay cita en ese rango
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Date fechaSeleccionada = sdf.parse(fechaHora);
                Calendar calInicio = Calendar.getInstance();
                Calendar calFin = Calendar.getInstance();

                calInicio.setTime(fechaSeleccionada);
                // 30 minutos antes
                calInicio.add(Calendar.MINUTE, -30);

                calFin.setTime(fechaSeleccionada);
                // 30 minutos después
                calFin.add(Calendar.MINUTE, 30);

                String inicio = sdf.format(calInicio.getTime());
                String fin = sdf.format(calFin.getTime());

                List<Cita> citasConflicto = db.citaDao().getCitasEnRango(inicio, fin);

                // Filtrar solo las que no están canceladas
                List<Cita> citasNoCanceladas = citasConflicto.stream()
                        .filter(c -> !c.estado.equalsIgnoreCase("cancelada"))
                        .collect(Collectors.toList());

                if (citaId == -1 && !citasNoCanceladas.isEmpty()) {
                    mostrarSnackbar(view, "Ya existe una cita activa en ese rango de tiempo");
                    return;
                } else if (citaId != -1 && citasNoCanceladas.stream().anyMatch(c -> c.id != citaId)) {
                    mostrarSnackbar(view, "Conflicto con otra cita activa en ese horario.");
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                mostrarSnackbar(view, "Error en el formato de fecha");
                return;
            }

            if (citaId != -1) {
                db.citaDao().update(citaEditando);
                // Snackbar
                Snackbar snackbar = Snackbar.make(view, "¡Cita actualizada correctamente!", Snackbar.LENGTH_LONG);

                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundTintList(null);
                snackbarView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.verde_menta));
                TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);

                snackbar.show();
            } else {
                db.citaDao().insert(citaEditando);
                // Snackbar
                Snackbar snackbar = Snackbar.make(view, "¡Cita guardada correctamente!", Snackbar.LENGTH_LONG);

                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundTintList(null);
                snackbarView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.verde_menta));
                TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);

                snackbar.show();
            }

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_home);
            navController.popBackStack();
        });
    }

    // Método selector de fecha y hora
    private void mostrarDateTimePicker(EditText editTextFechaHora) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccionar fecha")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                    .setMinute(Calendar.getInstance().get(Calendar.MINUTE))
                    .setTitleText("Seleccionar hora")
                    .build();

            timePicker.show(getParentFragmentManager(), "TIME_PICKER");

            timePicker.addOnPositiveButtonClickListener(v -> {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                Calendar finalCalendar = Calendar.getInstance();
                finalCalendar.set(year, month, day, hour, minute);

                String fechaHora = String.format(Locale.getDefault(), "%04d-%02d-%02d %02d:%02d",
                        year, month + 1, day, hour, minute);

                editTextFechaHora.setText(fechaHora);
            });
        });
    }

    private void mostrarSnackbar(View view, String mensaje) {
        Snackbar snackbar = Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();
    }
}