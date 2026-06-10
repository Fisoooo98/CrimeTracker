package Model.Entities;

public class ResultadoPregunta {
    private String nombreSospechoso;
    private final String respuesta;
    private final boolean pistaObtenida;
    private final String textoPista;
    private int id_pista;

    public ResultadoPregunta(String respuesta, boolean pistaObtenida, String textoPista,String nombreSospechoso,int id_pista) {
        this.respuesta = respuesta;
        this.pistaObtenida = pistaObtenida;
        this.textoPista = textoPista;
        this.nombreSospechoso = nombreSospechoso;
        this.id_pista = id_pista;
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

    @Override
    public String toString() {
        return "ResultadoPregunta{" +
                "pistaObtenida=" + pistaObtenida +
                ", respuesta='" + respuesta + '\'' +
                ", textoPista='" + textoPista + '\'' +
                '}';
    }
}