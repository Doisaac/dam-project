package com.hadoga.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hadoga.data.model.Expediente;

import java.util.List;

@Dao
public interface ExpedienteDao {
    @Insert
    void insert(Expediente expediente);

    @Update
    void update(Expediente expediente);

    @Delete
    void delete(Expediente expediente);

    @Query("SELECT * FROM expedientes")
    List<Expediente> getAll();

    @Query("SELECT * FROM expedientes WHERE id = :id")
    Expediente getById(int id);
}
