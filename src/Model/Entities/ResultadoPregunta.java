package Model.Entities;

public class ResultadoPregunta {
    private String nombreSospechoso;
    private final String respuesta;
    private final boolean pistaObtenida;
    private boolean evidenciaObtenida;
    private final String textoPista;
    private int id_pista;


    public ResultadoPregunta(String respuesta, boolean pistaObtenida, String textoPista,String nombreSospechoso,int id_pista,boolean evidenciaObtenida) {
        this.respuesta = respuesta;
        this.pistaObtenida = pistaObtenida;
        this.textoPista = textoPista;
        this.nombreSospechoso = nombreSospechoso;
        this.id_pista = id_pista;
        this.evidenciaObtenida = evidenciaObtenida;
    }

    public String getRespuesta() {
        return respuesta;
    }
    public boolean isPistaObtenida() {
        return pistaObtenida;
    }
    public String getTextoPista() {
        return textoPista;
    }
    public String getNombreSospechoso() {
        return nombreSospechoso;
    }

    public int getId_pista() {
        return id_pista;
    }

    public boolean isEvidenciaObtenida() {
        return evidenciaObtenida;
    }

    public void setEvidenciaObtenida(boolean evidenciaObtenida) {
        this.evidenciaObtenida = evidenciaObtenida;
    }

    @Override
    public String toString() {
        return "ResultadoPregunta{" +
                "pistaObtenida=" + pistaObtenida +
                ", respuesta='" + respuesta + '\'' +
                ", textoPista='" + textoPista + '\'' +
                '}';
    }
}