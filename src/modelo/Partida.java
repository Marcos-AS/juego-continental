package src.modelo;

import java.util.ArrayList;
import java.util.Random;

public class Partida {
    private int ronda;
    private ArrayList<jugadorActual> jugadoresActuales = new ArrayList<>();
    private ArrayList<Carta> mazo = new ArrayList<>();
    private ArrayList<Carta> pozo = new ArrayList<>();
    private static final int BARAJAS_HASTA_4_JUGADORES = 2;
    private static final int BARAJAS_MAS_4_JUGADORES = 3;
    private static final int BARAJAS_MAS_6_JUGADORES = 4;
    private static final int NUM_COMODINES_POR_BARAJA = 2;
    private static final int CANT_TOTAL_RONDAS = 7;
//PRIVATE ----------------------------------------------------

    private int determinarNumBarajas() {
        int cantBarajas = BARAJAS_HASTA_4_JUGADORES;
        if (this.jugadoresActuales.size() >= 4 && this.jugadoresActuales.size() <= 6) {
            cantBarajas = BARAJAS_MAS_4_JUGADORES;
        } else if(this.jugadoresActuales.size() >= 6 && this.jugadoresActuales.size() <= 8) {
            cantBarajas = BARAJAS_MAS_6_JUGADORES;
        }
        return cantBarajas;
    }

    private void iniciarMazo() {
        int numBarajas = determinarNumBarajas();
        int i = 0;
        while(i < numBarajas) {
            for(Palo p: Palo.values()) {
                for(int j = 1; j < 14; j++) {
                    Carta c = new Carta(j, p);
                    this.mazo.add(c);
                }
            }
            for(int j = 0; j < NUM_COMODINES_POR_BARAJA; j++) {
                Carta joker = new Carta();
                this.mazo.add(joker);
            }
            i++;
        }
    }

    private void mezclarCartas() {
        ArrayList<Carta> mazoMezclado = new ArrayList<>();
        Random random = new Random();
        while(this.mazo.size() > 0) {
            Carta c = this.mazo.remove(random.nextInt(mazo.size()));
            mazoMezclado.add(c);
        }
        this.mazo = mazoMezclado;
	}

    private Carta sacarPrimeraDelMazo() {
        return this.mazo.get(this.mazo.size()-1);
    }

    private Carta sacarPrimeraDelPozo() {
        return this.pozo.get(this.pozo.size()-1);
    }

    private void iniciarPozo() {
		this.pozo.add(this.sacarPrimeraDelMazo());
		this.mazo.remove(this.mazo.size()-1);
	}

    //PUBLIC ----------------------------------------------------
    public Partida() {}

    public void agregarJugador(String nombre) {
		jugadorActual nuevoJugador = new jugadorActual();
		nuevoJugador.setNombre(nombre);
		this.jugadoresActuales.add(nuevoJugador);
		nuevoJugador.setNumJugador(this.jugadoresActuales.size());
		this.jugadoresActuales.get(this.jugadoresActuales.size()-1).sumarPartida(this);	
		//notificarJugadorNuevo(); HACER					
	}

    public Carta eliminarDelMazo() {
		Carta c = sacarPrimeraDelMazo();
		this.mazo.remove(mazo.size()-1);
		return c;
	}

    public Carta eliminarDelPozo() {
		Carta c = sacarPrimeraDelPozo();
		this.pozo.remove(this.pozo.size()-1);
		return c;
	}

    public void agregarAlPozo(Carta c) {
		this.pozo.add(c);
	}

    public void crearMazo() {
        iniciarMazo();
        mezclarCartas();
    }

	public void repartirCartas() {
		int numCartasARepartir = Juego.cartasPorRonda(this.ronda);
		for(jugadorActual j: this.jugadoresActuales) {
			for(int i = 0; i < numCartasARepartir; i++) {
				Carta c = this.eliminarDelMazo();
				j.agregarCarta(c);		
			}
		}
        this.iniciarPozo();
        this.ronda = 1;
	}

    public void incrementarRonda() {
        this.ronda++;
    } 

//SETTERS Y GETTERS------------
    public int getNumJugadores() {
        return this.jugadoresActuales.size();
    }

    public ArrayList<jugadorActual> getJugadoresActuales() {
        return this.jugadoresActuales;
    }

    public int getRonda() {
        return this.ronda;
    }

    public jugadorActual getJugador(String nombreJugador) {
        jugadorActual j = null;
        for (jugadorActual jugadorActual : jugadoresActuales) {
            if (jugadorActual.getNombre() == nombreJugador) {
                j = jugadorActual;
            }
        }
        return j;
    }

    public int getTotalRondas() {
        return CANT_TOTAL_RONDAS;
    }
}