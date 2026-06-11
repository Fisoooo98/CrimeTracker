package Model.Entities;
import java.util.ArrayList;
import java.util.List;

public class Caso implements Comparable<Caso>{
    private int id_caso;
    private String nombre;
    private String descripcion;
    private List<Sospechoso> sospechosos;
    private List<Evidencia> evidencias;
    private List<Pista> pistas;
    private int probEvidencia;
    private String notas;
    private Estado estado;
    private Dificultad  dificultad;
    private boolean correcto;
    private int contador_preguntas;
    public Caso(int id_caso, String nombre, String descripcion,String notas) {
        this.id_caso = id_caso;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = Estado.NORESUELTO;
        this.notas = notas;
        this.correcto = false;
        this.sospechosos = new ArrayList<>();
        this.evidencias = new ArrayList<>();
        this.pistas = new ArrayList<>();

    }


    //Getters and Setters
    public boolean isCorrecto() {
        return correcto;
    }

    public void setCorrecto(boolean correcto) {
        this.correcto = correcto;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<Evidencia> getEvidencias() {
        return evidencias;
    }

    public void setEvidencias(List<Evidencia> evidencias) {
        this.evidencias = evidencias;
    }

    public int getId_caso() {
        return id_caso;
    }

    public void setId_caso(int id_caso) {
        this.id_caso = id_caso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public List<Pista> getPistas() {
        return pistas;
    }

    public void setPistas(List<Pista> pistas) {
        this.pistas = pistas;
    }

    public List<Sospechoso> getSospechosos() {
        return sospechosos;
    }

    public void setSospechosos(List<Sospechoso> sospechosos) {
        this.sospechosos = sospechosos;
    }

    public int getContador_preguntas() {
        return contador_preguntas;
    }

    public void setContador_preguntas(int contador_preguntas) {
        this.contador_preguntas = contador_preguntas;
    }

    public int getProbEvidencia() {
        return probEvidencia;
    }

    public void setProbEvidencia(int probEvidencia) {
        this.probEvidencia = probEvidencia;
    }

    public Dificultad getDificultad() {
        return dificultad;
    }

    public void setDificultad(Dificultad dificultad) {
        this.dificultad = dificultad;
    }

    @Override
    public String toString() {
        return String.format(
                """
                ==================================================
                📂 CASO Nº %d: %s
                ==================================================
                📝 DESCRIPCIÓN: %s
                🚦 ESTADO:      [%s]
                🎯 ¿ACERTADO?:  %s
                📌 NOTAS:       %s
                👥 SOSPECHOSOS: %d cargados en memoria
                ==================================================
                """,
                this.id_caso,
                this.nombre != null ? this.nombre.toUpperCase() : "SIN TÍTULO",
                this.descripcion != null ? this.descripcion : "Sin descripción disponible.",
                this.estado,
                this.correcto ? "✅ SÍ" : "❌ NO",
                (this.notas != null && !this.notas.isEmpty()) ? this.notas : "Ninguna nota guardada.",
                this.sospechosos != null ? this.sospechosos.size() : 0
        );
    }



    @Override
    public int compareTo(Caso o) {
        if (id_caso > o.id_caso) return 1;
        else if (id_caso < o.id_caso) return -1;
        else return 0;
    }
}
