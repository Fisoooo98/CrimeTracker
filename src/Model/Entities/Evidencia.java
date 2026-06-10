package Model.Entities;

public class Evidencia {
    private int id_evidencia;
    private String texto;
    private int id_caso;

    //Constructor
    public Evidencia(int id_caso, int id_evidencia, String texto) {
        this.id_caso = id_caso;
        this.id_evidencia = id_evidencia;
        this.texto = texto;
    }


    //Getters and Setters
    public int getId_caso() {
        return id_caso;
    }

    public void setId_caso(int id_caso) {
        this.id_caso = id_caso;
    }

    public int getId_evidencia() {
        return id_evidencia;
    }

    public void setId_evidencia(int id_evidencia) {
        this.id_evidencia = id_evidencia;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
