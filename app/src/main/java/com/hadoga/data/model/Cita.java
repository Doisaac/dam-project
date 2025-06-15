package com.hadoga.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "citas",
        foreignKeys = @ForeignKey(
                entity = Expediente.class,
                parentColumns = "id",
                childColumns = "paciente_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class Cita {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "paciente_id", index = true)
    public int pacienteId;

    @ColumnInfo(name = "fecha_hora")
    public String fechaHora;

    public String motivo;
    public String notas;

    // Pendiente o completada
    @ColumnInfo(defaultValue = "pendiente")
    public String estado;
}
