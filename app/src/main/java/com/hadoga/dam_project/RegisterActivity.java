package com.hadoga.dam_project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.hadoga.data.AppDatabase;
import com.hadoga.data.model.User;

public class RegisterActivity extends AppCompatActivity {
    // Declaraciones
    EditText editTextUsuario, editTextEmail, editTextPassword, editTextConfirmar;
    Button botonGuardar, botonRegresar;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // MODO OSCURO
        SharedPreferences themePrefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean isDarkMode = themePrefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        setContentView(R.layout.activity_register);

        // Vistas referencias
        editTextUsuario = findViewById(R.id.edittext_usuario);
        editTextEmail = findViewById(R.id.edittext_email);
        editTextPassword = findViewById(R.id.edittext_password);
        editTextConfirmar = findViewById(R.id.edittext_confirmar);
        botonGuardar = findViewById(R.id.button_guardar);
        botonRegresar = findViewById(R.id.button_regresar);

        // Base de datos
        db = AppDatabase.getInstance(this);


        // Acción del botón guardar
        botonGuardar.setOnClickListener(v -> {
            String username = editTextUsuario.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmarPassword = editTextConfirmar.getText().toString().trim();

            // Validaciones
            if (username.isEmpty()) {
                editTextUsuario.setError("Campo obligatorio");
                return;
            } else if (username.length() <= 3) {
                editTextUsuario.setError("Mínimo de 4 caracteres");
                return;
            }

            if (email.isEmpty()) {
                editTextEmail.setError("Campo obligatorio");
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Correo no válido");
                return;
            }

            if (password.isEmpty()) {
                editTextPassword.setError("Campo obligatorio");
                return;
            }

            if (password.length() <= 4) {
                editTextPassword.setError("La contraseña debe ser mayor a 4 caracteres");
                return;
            }

            if (confirmarPassword.isEmpty()) {
                editTextConfirmar.setError("Campo obligatorio");
                return;
            } else if (!password.equals(confirmarPassword)) {
                editTextConfirmar.setError("Las contraseñas no coinciden");
                return;
            }

            // Cierra el teclado
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View view = this.getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            // Verificar si ya existe el email
            if (db.userDao().getUserByEmail(email) != null) {
                // Snackbar
                Snackbar snackbar = Snackbar.make(findViewById(R.id.main), "Este correo ya está registrado", Snackbar.LENGTH_LONG);

                View snackbarView = snackbar.getView();
                TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);

                snackbar.show();
                return;
            }

            // Registrar nuevo usuario luego de validaciones exitosas
            User nuevoUsuario = new User(username, email, password);
            db.userDao().insertUser(nuevoUsuario);

            // Alerta
            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();

            // // Cierra esta pantalla
            finish();
        });

        // Accion del boton regresar
        botonRegresar.setOnClickListener(v -> {
            // Cierra esta pantalla y regresa a la anterior
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}