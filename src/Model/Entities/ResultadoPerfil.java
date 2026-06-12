package Model.Entities;

public class ResultadoPerfil {
    private int totalCasosFaciles;
    private int totalCasosNormales;
    private int totalCasosDificiles;
    private int casosFacilesResueltos;
    private int casosNormalesResueltos;
    private int casosDificilesResueltos;
    private Tier tier;
    private int puntuacion;

    public ResultadoPerfil(int casosDificilesResueltos, int casosFacilesResueltos, int casosNormalesResueltos,
                           int puntuacion, Tier tier, int totalCasosDificiles, int totalCasosFaciles, int totalCasosNormales) {
        this.casosDificilesResueltos = casosDificilesResueltos;
        this.casosFacilesResueltos = casosFacilesResueltos;
        this.casosNormalesResueltos = casosNormalesResueltos;
        this.puntuacion = puntuacion;
        this.tier = tier;
        this.totalCasosDificiles = totalCasosDificiles;
        this.totalCasosFaciles = totalCasosFaciles;
        this.totalCasosNormales = totalCasosNormales;
    }

    public int getCasosDificilesResueltos() {
        return casosDificilesResueltos;
    }

    public int getCasosFacilesResueltos() {
        return casosFacilesResueltos;
    }

    public int getCasosNormalesResueltos() {
        return casosNormalesResueltos;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public Tier getTier() {
        return tier;
    }

    public int getTotalCasosDificiles() {
        return totalCasosDificiles;
    }

    public int getTotalCasosFaciles() {
        return totalCasosFaciles;
    }

    public int getTotalCasosNormales() {
        return totalCasosNormales;
    }
}
