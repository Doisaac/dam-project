package com.hadoga.dam_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.hadoga.dam_project.databinding.ActivityHomeBinding;
import com.hadoga.data.AppDatabase;
import com.hadoga.data.model.User;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // MODO OSCURO
        /// Leer el estado actual del modo oscuro
        SharedPreferences themePrefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean isDarkMode = themePrefs.getBoolean("dark_mode", false);

        // Referencia al Switch
        Switch switchDarkMode = binding.appBarHome.switchDarkMode;
        switchDarkMode.setChecked(isDarkMode);

        // Escuchar cambios del Switch
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = themePrefs.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );

            recreate();
        });

        setSupportActionBar(binding.appBarHome.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_expediente, R.id.nav_cita, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Accede a los TextViews del header
        View headerView = navigationView.getHeaderView(0);
        TextView textUsername = headerView.findViewById(R.id.text_username);
        TextView textEmail = headerView.findViewById(R.id.text_email);

        AppDatabase db = AppDatabase.getInstance(this);
        SharedPreferences prefs = getSharedPreferences("nav_header", MODE_PRIVATE);
        String email_nav = prefs.getString("email", null);
        User user = db.userDao().getUserByEmail(email_nav);

        if (user != null) {
            textUsername.setText(user.username);
            textEmail.setText(user.email);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cerrar_sesion) {
            // Eliminar datos de sesión guardados
            getSharedPreferences("user_login_data", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            // Volver al LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_salir) {
            // Salir de la aplicación (continua guardando los datos de login en sharedPreferences)
            new AlertDialog.Builder(this)
                    .setTitle("Salir de la aplicación")
                    .setMessage("¿Estás seguro que deseas salir?")
                    .setPositiveButton("Salir", (dialog, which) -> {
                        finishAffinity();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}