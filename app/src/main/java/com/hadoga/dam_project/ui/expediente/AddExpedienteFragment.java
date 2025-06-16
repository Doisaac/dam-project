package com.hadoga.dam_project.ui.expediente;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.hadoga.dam_project.R;
import com.hadoga.data.AppDatabase;
import com.hadoga.data.model.Expediente;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExpedienteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddExpedienteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddExpedienteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_addExpediente.
     */
    // TODO: Rename and change types and number of parameters
    public static AddExpedienteFragment newInstance(String param1, String param2) {
        AddExpedienteFragment fragment = new AddExpedienteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expediente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Calendario
        EditText editTextFecha = view.findViewById(R.id.editTextFechaNacimiento);

        editTextFecha.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String fecha = selectedYear + "-" +
                                String.format("%02d", selectedMonth + 1) + "-" +
                                String.format("%02d", selectedDay);
                        editTextFecha.setText(fecha);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });

        // Agregar expediente
        Button btnGuardar = view.findViewById(R.id.btnGuardarExpediente);
        btnGuardar.setOnClickListener(v -> {
            // Obtener referencias
            // Obligatirios
            EditText editTextNombre = view.findViewById(R.id.editTextNombre);
            EditText editTextApellido = view.findViewById(R.id.editTextApellido);
            EditText editTextFechaNacimiento = view.findViewById(R.id.editTextFechaNacimiento);

            String nombre = editTextNombre.getText().toString().trim();
            String apellido = editTextApellido.getText().toString().trim();
            String fechaNacimiento = editTextFechaNacimiento.getText().toString().trim();

            // Validación de campos obligatorios
            if (nombre.isEmpty()) {
                editTextNombre.setError("El nombre es obligatorio");
                editTextNombre.requestFocus();
                return;
            }

            if (apellido.isEmpty()) {
                editTextApellido.setError("El apellido es obligatorio");
                editTextApellido.requestFocus();
                return;
            }

            if (fechaNacimiento.isEmpty()) {
                editTextFechaNacimiento.setError("La fecha de nacimiento es obligatoria");
                editTextFechaNacimiento.requestFocus();
                return;
            }

            // Resto de campos
            String telefono = ((EditText) view.findViewById(R.id.editTextTelefono)).getText().toString().trim();
            String correo = ((EditText) view.findViewById(R.id.editTextCorreo)).getText().toString().trim();
            String direccion = ((EditText) view.findViewById(R.id.editTextDireccion)).getText().toString().trim();
            String observaciones = ((EditText) view.findViewById(R.id.editTextObservaciones)).getText().toString().trim();

            // genero
            RadioButton radioMasculino = view.findViewById(R.id.radioMasculino);
            String genero = radioMasculino.isChecked() ? "Masculino" : "Femenino";

            // Fecha de creacion se crea automaticamente
            String fechaCreacion = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Checkboxes
            boolean diabetes = ((CheckBox) view.findViewById(R.id.checkboxDiabetes)).isChecked();
            boolean anemia = ((CheckBox) view.findViewById(R.id.checkboxAnemia)).isChecked();
            boolean gastritis = ((CheckBox) view.findViewById(R.id.checkboxGastritis)).isChecked();
            boolean hta = ((CheckBox) view.findViewById(R.id.checkboxHTA)).isChecked();
            boolean hemorragias = ((CheckBox) view.findViewById(R.id.checkboxHemorragias)).isChecked();
            boolean asma = ((CheckBox) view.findViewById(R.id.checkboxAsma)).isChecked();
            boolean trastornosCardiacos = ((CheckBox) view.findViewById(R.id.checkboxCardiacos)).isChecked();
            boolean convulsiones = ((CheckBox) view.findViewById(R.id.checkboxConvulsiones)).isChecked();
            boolean tiroides = ((CheckBox) view.findViewById(R.id.checkboxTiroides)).isChecked();

            // Crear objeto Expediente
            Expediente expediente = new Expediente();
            expediente.nombre = nombre;
            expediente.apellido = apellido;
            expediente.fechaNacimiento = fechaNacimiento;
            expediente.genero = genero;
            expediente.telefono = telefono;
            expediente.correo = correo;
            expediente.direccion = direccion;
            expediente.observaciones = observaciones;
            expediente.fechaCreacion = fechaCreacion;
            expediente.diabetes = diabetes;
            expediente.anemia = anemia;
            expediente.gastritis = gastritis;
            expediente.hta = hta;
            expediente.hemorragias = hemorragias;
            expediente.asma = asma;
            expediente.trastornosCardiacos = trastornosCardiacos;
            expediente.convulsiones = convulsiones;
            expediente.tiroides = tiroides;

            // Guardar en base de datos
            AppDatabase db = AppDatabase.getInstance(requireContext());
            db.expedienteDao().insert(expediente);

            Toast.makeText(requireContext(), "Expediente guardado correctamente", Toast.LENGTH_SHORT).show();

            // Regresar al fragmento anterior
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_home);
            navController.popBackStack();
        });

    }
}