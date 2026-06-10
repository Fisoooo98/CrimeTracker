package Model.Entities;

public class Pista {
    private int id_pista;
    private String texto;
    private int id_caso;
    private int id_sospechoso;
    private int id_pregunta;


    //Constructor
    public Pista(int id_caso, int id_pista, String nombre, int id_sospechoso, int id_pregunta) {
        this.id_caso = id_caso;
        this.id_pista = id_pista;
        this.texto = nombre;
        this.id_sospechoso = id_sospechoso;
        this.id_pregunta = id_pregunta;
    }

    public Pista(int id_pista, String texto) {
        this.id_pista = id_pista;
        this.texto = texto;
    }

    //Getters and Setters
    public int getId_caso() {
        return id_caso;
    }

    public void setId_caso(int id_caso) {
        this.id_caso = id_caso;
    }

    public int getId_pista() {
        return id_pista;
    }

    public void setId_pista(int id_pista) {
        this.id_pista = id_pista;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(int id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

    public int getId_sospechoso() {
        return id_sospechoso;
    }

    public void setId_sospechoso(int id_sospechoso) {
        this.id_sospechoso = id_sospechoso;
    }
}
