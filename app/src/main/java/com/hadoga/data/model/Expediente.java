package com.hadoga.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expedientes")
public class Expediente {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String apellido;

    @ColumnInfo(name = "fecha_nacimiento")
    public String fechaNacimiento;

    public String genero;
    public String telefono;
    public String correo;
    public String direccion;
    public String observaciones;

    @ColumnInfo(name = "fecha_creacion", defaultValue = "CURRENT_TIMESTAMP")
    public String fechaCreacion;

    // Historia Clínica
    public boolean diabetes;
    public boolean anemia;
    public boolean gastritis;
    public boolean hta;
    public boolean hemorragias;
    public boolean asma;

    @ColumnInfo(name = "trastornos_cardiacos")
    public boolean trastornosCardiacos;

    public boolean convulsiones;
    public boolean tiroides;
}
