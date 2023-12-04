import java.util.ArrayList;

public class jugadorActual extends Jugador {
    private int numeroJugador;
    private ArrayList<Carta> mano;
    private int puntosPartida;
    private ArrayList<ArrayList<Carta>> juegos;

    public jugadorActual() {}
    
    private Partida getPartidaActual() {
        Partida partActual = null;
        if (!this.partidas.isEmpty()) {
            partActual = this.partidas.get(this.partidas.size()-1);
        }
        return partActual;
    }

    private void robarDelMazo() {
		Carta c = getPartidaActual().eliminarDelMazo();
		this.mano.add(c);
	}
	
	private void robarDelPozo() {
		Carta c = this.partActual.sacarDelPozo();
		this.cartas.add(c);
	}
    
}
