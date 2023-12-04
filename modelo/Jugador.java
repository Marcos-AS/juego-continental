import java.util.ArrayList;

public class Jugador {
    private String nombre;
    protected ArrayList<Partida> partidas;
    private int puestoRanking;
    private int puntosTotales;

    public Jugador() {}

    public Jugador(String nombre) {
        this.nombre = nombre;
    }
}
