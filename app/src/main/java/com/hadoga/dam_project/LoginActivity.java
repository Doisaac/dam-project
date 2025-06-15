package com.hadoga.dam_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hadoga.data.AppDatabase;
import com.hadoga.data.model.User;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button btnIngresar, btnSalir;
    AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Conectar vistas
        editTextEmail = findViewById(R.id.textview_usuario);
        editTextPassword = findViewById(R.id.edittext_password);
        btnIngresar = findViewById(R.id.btn_ingresar);
        btnSalir = findViewById(R.id.btn_salir);

        // Inicializar base de datos
        db = AppDatabase.getInstance(this);
        btnIngresar.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty()) {
                editTextEmail.setError("Campo obligatorio");
                return;
            }

            if (password.isEmpty()) {
                editTextPassword.setError("Campo obligatorio");
                return;
            }

            User user = db.userDao().login(email, password);
            if (user != null) {
                Toast.makeText(this, "¡Bienvenido " + user.username + "!", Toast.LENGTH_LONG).show();

                // Redirigir a HomeActivity (Navigation Drawer)
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);

                // cierra LoginActivity para no volver (cambiable)
                finish();
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        btnSalir.setOnClickListener(v -> finish());


        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Opciones del menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuitem_register) {

            // Ir a RegisterActivity
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menuitem_salir) {
            finish(); // Cierra la app o actividad actual
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

