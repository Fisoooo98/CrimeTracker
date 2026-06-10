package Model.Entities;

import java.util.ArrayList;
import java.util.List;

public class Detective {
    private String nombre;
    private List<Caso> casos;
    private double porcentaje;

    public Detective(String nombre, double porcentaje) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.casos = new ArrayList<>();
    }

    public List<Caso> getCasos() {
        return casos;
    }


    public void setCasos(List<Caso> casos) {
        this.casos = casos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
}
