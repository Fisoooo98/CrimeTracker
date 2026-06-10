package Model.Entities;

import java.util.HashMap;

public class Sospechoso {
    private int id_sospechoso;
    private String nombre;
    private boolean es_culpable;


    //Constructor
    public Sospechoso(int id_sospechoso, String nombre, boolean es_culpable) {
        this.id_sospechoso = id_sospechoso;
        this.nombre = nombre;
        this.es_culpable = es_culpable;
    }

    //Getters and Setters
    public boolean isEs_culpable() {
        return es_culpable;
    }

    public void setEs_culpable(boolean es_culpable) {
        this.es_culpable = es_culpable;
    }

    public int getId_sospechoso() {
        return id_sospechoso;
    }

    public void setId_sospechoso(int id_sospechoso) {
        this.id_sospechoso = id_sospechoso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Sospechoso{" +
                "es_culpable=" + es_culpable +
                ", id_sospechoso=" + id_sospechoso +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
