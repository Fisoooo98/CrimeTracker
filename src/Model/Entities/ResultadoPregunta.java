package Model.Entities;

public class ResultadoPregunta {
    private String nombreSospechoso;
    private final String respuesta;
    private final boolean pistaObtenida;
    private boolean evidenciaObtenida;
    private final String textoPista;


    public ResultadoPregunta(String respuesta, boolean pistaObtenida, String textoPista,String nombreSospechoso,boolean evidenciaObtenida) {
        this.respuesta = respuesta;
        this.pistaObtenida = pistaObtenida;
        this.textoPista = textoPista;
        this.nombreSospechoso = nombreSospechoso;
        this.evidenciaObtenida = evidenciaObtenida;
    }

    public String getRespuesta() {
        return respuesta;
    }
    public boolean isPistaObtenida() {
        return pistaObtenida;
    }

    public String getNombreSospechoso() {
        return nombreSospechoso;
    }

    public boolean isEvidenciaObtenida() {
        return evidenciaObtenida;
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