package com.hadoga.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hadoga.data.dao.CitaDao;
import com.hadoga.data.dao.ExpedienteDao;
import com.hadoga.data.dao.UserDao;
import com.hadoga.data.model.Cita;
import com.hadoga.data.model.Expediente;
import com.hadoga.data.model.User;

@Database(entities = {User.class, Expediente.class, Cita.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    // Métodos DAO
    public abstract UserDao userDao();
    public abstract ExpedienteDao expedienteDao();
    public abstract CitaDao citaDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "dam_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}