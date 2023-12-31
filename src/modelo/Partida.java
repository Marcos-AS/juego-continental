package src.modelo;

import java.util.ArrayList;
import java.util.Random;

import src.main.Observer;

public class Partida {
    private int ronda;
    private ArrayList<jugadorActual> jugadoresActuales = new ArrayList<>();
    private ArrayList<Carta> mazo = new ArrayList<>();
    private ArrayList<Carta> pozo = new ArrayList<>();
    private static final int BARAJAS_HASTA_4_JUGADORES = 2;
    private static final int BARAJAS_MAS_4_JUGADORES = 3;
    private static final int BARAJAS_MAS_6_JUGADORES = 4;
    private static final int NUM_COMODINES_POR_BARAJA = 2;
    private static final int CANT_TOTAL_RONDAS = 1; //prueba
    private ArrayList<Observer> observadores = new ArrayList<>();
    
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
            for(int j = 1; j < 14; j++) {
                Carta c = new Carta(j, Palo.PICAS);
                this.mazo.add(c);
            }
            for(int j = 1; j < 14; j++) {
                Carta c = new Carta(j, Palo.DIAMANTES);
                this.mazo.add(c);
            }
            for(int j = 1; j < 14; j++) {
                Carta c = new Carta(j, Palo.TREBOL);
                this.mazo.add(c);
            }
            for(int j = 1; j < 14; j++) {
                Carta c = new Carta(j, Palo.CORAZONES);
                this.mazo.add(c);
            }
            for(int j = 0; j < NUM_COMODINES_POR_BARAJA; j++) {
                Carta c = new Carta(-1, Palo.COMODIN);
                this.mazo.add(c);
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

    private void iniciarPozo() {
		this.pozo.add(this.sacarPrimeraDelMazo());
		this.mazo.remove(this.mazo.size()-1);
	}

    //PUBLIC ----------------------------------------------------
    public Partida() {}

    public Carta sacarPrimeraDelPozo() {
        return this.pozo.get(this.pozo.size()-1);
    }

    public Carta sacarPrimeraDelMazo() {
        return this.mazo.get(this.mazo.size()-1);
    }

    public void agregarJugador(String nombre) {
		jugadorActual nuevoJugador = new jugadorActual();
		nuevoJugador.setNombre(nombre);
		this.jugadoresActuales.add(nuevoJugador);
		//nuevoJugador.setNumeroJugador(this.jugadoresActuales.size());
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
            // Carta c = new Carta(-1, Palo.COMODIN);
            // Carta c1 = new Carta(5, Palo.TREBOL);
            // Carta c2 = new Carta(5, Palo.PICAS);
            // Carta c3 = new Carta(6, Palo.PICAS);
            // Carta c4 = new Carta(6, Palo.TREBOL);
            // Carta c5 = new Carta(6, Palo.DIAMANTES);
            // this.jugadoresActuales.get(0).agregarCarta(c);
            // this.jugadoresActuales.get(0).agregarCarta(c1);
            // this.jugadoresActuales.get(0).agregarCarta(c2);
            // this.jugadoresActuales.get(0).agregarCarta(c3);
            // this.jugadoresActuales.get(0).agregarCarta(c4);
            // this.jugadoresActuales.get(0).agregarCarta(c5);
            // Carta c6 = new Carta(3, Palo.PICAS);
            // Carta c7 = new Carta(3, Palo.DIAMANTES);
            // Carta c8 = new Carta(3, Palo.TREBOL);
            // Carta c9 = new Carta(-1, Palo.COMODIN);
            // Carta c10 = new Carta(8, Palo.PICAS);
            // Carta c11 = new Carta(8, Palo.TREBOL);
            // this.jugadoresActuales.get(1).agregarCarta(c6);
            // this.jugadoresActuales.get(1).agregarCarta(c7);
            // this.jugadoresActuales.get(1).agregarCarta(c8);
            // this.jugadoresActuales.get(1).agregarCarta(c9);
            // this.jugadoresActuales.get(1).agregarCarta(c10);
            // this.jugadoresActuales.get(1).agregarCarta(c11);
		}
        this.iniciarPozo();
        this.ronda = 1;
	}

    public void incrementarRonda() {
        this.ronda++;
    } 

    public void resetearJuegosJugadores() {
        for (jugadorActual jugadorActual : this.jugadoresActuales) {
            jugadorActual.setTriosBajados(0);
            jugadorActual.setEscalerasBajadas(0);
            jugadorActual.setPuedeBajar();
        }
    }

    public void sumarPuntos() {
		int n = 0;
		int puntos = 0;
		while(n < this.jugadoresActuales.size()) {
			jugadorActual j = this.jugadoresActuales.get(n);
			puntos = 0;
            for(Carta c: j.getMano()) {
                int num = c.getNumero();
                switch(num) {
                case 1:
                    puntos += Juego.getAs();
                    break;
                case 11, 12, 13:
                    puntos += Juego.getFigura();
                    break;
                case -1:
                    puntos += Juego.getPuntosComodin();
                    break;
                case 2,3,4,5,6,7,8,9,10:
                    puntos += num;
                }
            }
            j.setPuntos(puntos);
            n++;
        }
	}

	public String determinarGanador() {
		jugadorActual ganador = this.jugadoresActuales.get(0);
		int menosPuntos = ganador.getPuntos();
		for(jugadorActual j: this.jugadoresActuales) {
			if(j.getPuntos() < menosPuntos) {
				menosPuntos = j.getPuntos();
				ganador = j;				
			}
		}
		return ganador.getNombre();
	}

    public int[] getPuntosJugadores() {
        int[] puntos = new int[this.jugadoresActuales.size()];
        int i = 0;
        for (jugadorActual jugadorActual : this.jugadoresActuales) {
            puntos[i] = jugadorActual.getPuntos();
            i++;
        }
        return puntos;
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

    public ArrayList<Carta> getMazo() {
        return this.mazo;
    }

    public ArrayList<Carta> getPozo() {
        return this.pozo;
    }
}