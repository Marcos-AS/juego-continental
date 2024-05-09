package src.modelo;

import rmimvc.src.observer.ObservableRemoto;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

public class Partida extends ObservableRemoto implements ifPartida, Serializable {
    private int ronda;
    private final ArrayList<jugadorActual> jugadoresActuales = new ArrayList<>();
    private ArrayList<Carta> mazo;
    private ArrayList<Carta> pozo;
    private static final int BARAJAS_HASTA_4_JUGADORES = 2;
    private static final int BARAJAS_MAS_4_JUGADORES = 3;
    //private static final int BARAJAS_MAS_6_JUGADORES = 4;
    private static final int NUM_COMODINES_POR_BARAJA = 2;
    private static final int CANT_TOTAL_RONDAS = 1; //para probar
    private boolean enCurso = false;
    private boolean corteRonda = false;
    @Serial
    private static final long serialVersionUID = 1L;
    private int cantJugadoresDeseada;

    public Partida() {}

//PRIVATE ----------------------------------------------------

    //PUBLIC ----------------------------------------------------
    @Override
    public int determinarNumBarajas() {
        int cantBarajas = BARAJAS_HASTA_4_JUGADORES;
        if (jugadoresActuales.size() >= 4 && jugadoresActuales.size() <= 6) {
            cantBarajas = BARAJAS_MAS_4_JUGADORES;
        //} else if(this.jugadoresActuales.size() >= 6 && this.jugadoresActuales.size() <= 8) {
          //  cantBarajas = BARAJAS_MAS_6_JUGADORES;
        }
        return cantBarajas;
    }

    @Override
    public void iniciarMazo(int numBarajas) {
        mazo = new ArrayList<>();
        int i = 0;
        while(i < numBarajas) {
            for(int j = 1; j < 14; j++)
                mazo.add(new Carta(j, Palo.PICAS));
            for(int j = 1; j < 14; j++)
                mazo.add(new Carta(j, Palo.DIAMANTES));
            for(int j = 1; j < 14; j++)
                mazo.add(new Carta(j, Palo.TREBOL));
            for(int j = 1; j < 14; j++)
                mazo.add(new Carta(j, Palo.CORAZONES));
            for(int j = 0; j < NUM_COMODINES_POR_BARAJA; j++)
                mazo.add(new Carta(-1, Palo.COMODIN));
            i++;
        }
    }

    @Override
    public void mezclarCartas() {
        ArrayList<Carta> mazoMezclado = new ArrayList<>();
        Random random = new Random();
        while(!mazo.isEmpty()) {
            Carta c = mazo.remove(random.nextInt(mazo.size()));
            mazoMezclado.add(c);
        }
        mazo = mazoMezclado;
	}

    @Override
    public void iniciarPozo() {
        pozo = new ArrayList<>();
		pozo.add(sacarPrimeraDelMazo());
		mazo.remove(mazo.size()-1);
	}

    @Override
    public Carta sacarPrimeraDelPozo() {
        return pozo.get(pozo.size()-1);
    }

    private Carta sacarPrimeraDelMazo() {
        return mazo.get(mazo.size()-1);
    }

    @Override
    public void agregarJugador(String nombre) throws RemoteException {
		jugadorActual nuevoJugador = new jugadorActual();
		nuevoJugador.setNombre(nombre);
        nuevoJugador.sumarPartida(this);
        nuevoJugador.setNumeroJugador(jugadoresActuales.size());
		jugadoresActuales.add(nuevoJugador);
        notificarObservadores(8);
	}

    public jugadorActual getJugadorQueLeToca() throws RemoteException {
        int i = 0;
        boolean encontrado = false;
        while (!encontrado) {
            if (jugadoresActuales.get(i).isTurnoActual()) {
                encontrado = true;
            } else {
                i++;
            }
        }
        return jugadoresActuales.get(i);
    }

    @Override
    public Carta eliminarDelMazo() {
		Carta c = sacarPrimeraDelMazo();
		mazo.remove(mazo.size()-1);
		return c;
	}

    @Override
    public Carta eliminarDelPozo() {
		Carta c = sacarPrimeraDelPozo();
		pozo.remove(pozo.size()-1);
		return c;
	}

    @Override
    public void agregarAlPozo(Carta c) {
		pozo.add(c);
	}

    @Override
    public void crearMazo() throws RemoteException {
        iniciarMazo(determinarNumBarajas());
        mezclarCartas();
    }

	@Override
    public void repartirCartas() throws RemoteException {
//		int numCartasARepartir = ifJuego.cartasPorRonda(this.ronda);
//		for(jugadorActual j: this.jugadoresActuales) {
//			for(int i = 0; i < numCartasARepartir; i++) {
//			    Carta c = this.eliminarDelMazo();
//			 	j.agregarCarta(c);
//			}
//        }
             jugadoresActuales.get(0).agregarCarta(new Carta(-1, Palo.COMODIN));
             jugadoresActuales.get(0).agregarCarta(new Carta(5, Palo.TREBOL));
             jugadoresActuales.get(0).agregarCarta(new Carta(5, Palo.PICAS));
             jugadoresActuales.get(0).agregarCarta(new Carta(6, Palo.PICAS));
             jugadoresActuales.get(0).agregarCarta(new Carta(6, Palo.TREBOL));
             jugadoresActuales.get(0).agregarCarta(new Carta(6, Palo.DIAMANTES));
             jugadoresActuales.get(1).agregarCarta(new Carta(3, Palo.PICAS));
             jugadoresActuales.get(1).agregarCarta(new Carta(3, Palo.DIAMANTES));
             jugadoresActuales.get(1).agregarCarta(new Carta(3, Palo.TREBOL));
             jugadoresActuales.get(1).agregarCarta(new Carta(-1, Palo.COMODIN));
             jugadoresActuales.get(1).agregarCarta(new Carta(8, Palo.PICAS));
             jugadoresActuales.get(1).agregarCarta(new Carta(8, Palo.TREBOL));
	}

    @Override
    public void resetearJuegosJugadores() {
        for (jugadorActual jugadorActual : jugadoresActuales) {
            jugadorActual.setTriosBajados(0);
            jugadorActual.setEscalerasBajadas(0);
            jugadorActual.setPuedeBajar();
        }
    }

    @Override
    public void sumarPuntos() throws RemoteException {
		int n = 0;
		int puntos;
		while(n < jugadoresActuales.size()) {
			jugadorActual j = jugadoresActuales.get(n);
			puntos = 0;
            for(Carta c: j.getMano()) {
                int num = c.getNumero();
                switch(num) {
                case 1:
                    puntos += ifJuego.getAs();
                    break;
                case 11, 12, 13:
                    puntos += ifJuego.getFigura();
                    break;
                case -1:
                    puntos += ifJuego.getPuntosComodin();
                    break;
                case 2,3,4,5,6,7,8,9,10:
                    puntos += num;
                }
            }
            j.setPuntos(puntos);
            n++;
        }
	}

	@Override
    public jugadorActual determinarGanador() {
		jugadorActual ganador = jugadoresActuales.get(0);
		int menosPuntos = ganador.getPuntos();
		for(jugadorActual j: jugadoresActuales) {
			if(j.getPuntos() < menosPuntos) {
				menosPuntos = j.getPuntos();
				ganador = j;				
			}
		}
		return ganador;
	}

    @Override
    public int[] getPuntosJugadores() {
        int[] puntos = new int[jugadoresActuales.size()+1];
        int i = 0;
        puntos[i] = -1;
        i++;
        for (jugadorActual jugadorActual : jugadoresActuales) {
            puntos[i] = jugadorActual.getPuntos();
            i++;
        }
        return puntos;
    }

//SETTERS Y GETTERS------------
    @Override
    public int getNumJugadores() {
        return jugadoresActuales.size();
    }

    @Override
    public ArrayList<jugadorActual> getJugadoresActuales() {
        return jugadoresActuales;
    }

    @Override
    public int getRonda() {
        return ronda;
    }

    public void setRonda(int i) {
        ronda = i;
    }

    public void incrementarRonda() {
        ronda++;
    }

    @Override
    public jugadorActual getJugador(String nombreJugador) {
        jugadorActual j = null;
        for (jugadorActual jugadorActual : jugadoresActuales) {
            if (jugadorActual.getNombre().equals(nombreJugador)) {
                j = jugadorActual;
            }
        }
        return j;
    }

    @Override
    public int getTotalRondas() {
        return CANT_TOTAL_RONDAS;
    }

    @Override
    public ArrayList<Carta> getMazo() {
        return mazo;
    }

    @Override
    public ArrayList<Carta> getPozo() {
        return pozo;
    }

    @Override
    public boolean getEstadoPartida() {
        return enCurso;
    }

    @Override
    public void setEstadoPartida() {
        enCurso = !enCurso;
    }

    public void setCantJugadoresDeseada(int cant) {
        cantJugadoresDeseada = cant;
    }

    public int getCantJugadoresDeseada() {
        return cantJugadoresDeseada;
    }

    public void setCorteRonda() {
        corteRonda = !corteRonda;
    }

    public boolean getCorteRonda() {
        return corteRonda;
    }
}