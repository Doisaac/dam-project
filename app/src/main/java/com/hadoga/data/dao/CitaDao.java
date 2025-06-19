package com.hadoga.data.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hadoga.data.model.Cita;

import java.util.List;

@Dao
public interface CitaDao {
    @Query("SELECT * FROM citas")
    List<Cita> getAll();

    @Query("SELECT * FROM citas WHERE id = :id LIMIT 1")
    Cita getById(int id);
    @Insert
    void insert(Cita cita);

    @Update
    void update(Cita cita);

    @Delete
    void delete(Cita cita);

    @Query("SELECT * FROM citas WHERE datetime(fecha_hora) BETWEEN datetime(:fechaHora) AND datetime(:fechaHora, '+30 minutes')")
    List<Cita> getCitasEnRango(String fechaHora);

    // Posible implementación
    @Query("SELECT * FROM citas WHERE paciente_id = :expedienteId")
    List<Cita> getCitasByExpedienteId(int expedienteId);
}
