package src.modelo;

import rmimvc.src.observer.ObservableRemoto;
import src.serializacion.Serializador;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Juego extends ObservableRemoto implements ifJuego {
	protected static final int FIGURA = 10;
	protected static final int AS = 20;
	protected static final int PUNTOS_COMODIN = 50;
	protected static final int COMODIN = -1; //este valor para que al ordenar cartas queden los comodines primero
	protected static final int CANT_CARTAS_INICIAL = 6;
	private ArrayList<Jugador> jugadores = new ArrayList<>();
	private static Juego instancia;
	private Partida partidaActual;
	private final Serializador srl = new Serializador("partidas.dat");

	//singleton
	public static Juego getInstancia() {
		if (instancia == null) instancia = new Juego();
		return instancia;
	}

	private Juego() {
	}


	//OBSERVER--------------------------------------------------
//	public void setValorAccion1(Integer valor) {
//
//	}

	//PRIVATE----------------------------------------------------

	//PUBLIC----------------------------------------------------------------------


	public int cartasPorRonda(int ronda) throws RemoteException {
		int cantCartas = Juego.CANT_CARTAS_INICIAL;
		switch (ronda) {
			case 2:
				cantCartas = 7;
				break;
			case 3:
				cantCartas = 8;
				break;
			case 4:
				cantCartas = 9;
				break;
			case 5:
				cantCartas = 10;
				break;
			case 6:
				cantCartas = 11;
				break;
			case 7:
				cantCartas = 12;
				break;
		}
		return cantCartas;
	}

	public void agregarJugador(Jugador j) throws RemoteException {
		this.jugadores.add(j);
		notificarObservadores(7); //llama al actualizar del ctrl con (this, 1)
	}

	public void crearPartida() throws RemoteException{
		this.partidaActual = new Partida();
		for (Jugador j : this.jugadores)
			this.partidaActual.agregarJugador(j.getNombre());
		//Serializador srl = new Serializador("partidas.dat");
		this.srl.writeOneObject(this.partidaActual);
		notificarObservadores(this.srl);
		iniciarPartida(partidaActual);
	}

	private void iniciarPartida(Partida p) throws RemoteException {
		try {
			p.setRonda(1);
			desarrolloPartida(p);
		} catch(RemoteException e){
			throw new RuntimeException(e);
		}
	}

	private void desarrolloPartida(Partida p) throws RemoteException {
		int rondaActual = p.getRonda();
		boolean corte = false;
		int eleccion;
		ArrayList<jugadorActual> jugadoresActuales = p.getJugadoresActuales();

		//empiezan las rondas
		while (rondaActual <= p.getTotalRondas()) {
			p.crearMazo();
			p.repartirCartas();
			p.iniciarPozo();
			this.srl.writeOneObject(this.partidaActual);
			notificarObservadores(this.srl); //muestra combinacion requerida y pozo
			int i = 0;
			while (!corte) {
				jugadorActual j = jugadoresActuales.get(i);
				this.partidaActual.avisarTurno(j);
				//desarrolloTurno(j, i);

				//si no roba del pozo, los demas pueden hacerlo, con "castigo"
                    /*if (eleccion != consola.getEleccionRobarDelPozo()) {
                        ArrayList<jugadorActual> jugadoresRoboCastigo = new ArrayList<>();
                        jugadoresRoboCastigo.addAll(jugadoresActuales);
                        jugadoresRoboCastigo.remove(i);
                        jugadorActual jugadorR = null;
                        int k;
                        int l = 0;
                        int eleccionR = eleccion;
                        while (eleccionR != consola.getEleccionRobarDelPozo() && l < jugadoresRoboCastigo.size()) {
                            k = i+1;
                            if (k > jugadoresRoboCastigo.size()-1) k = 0;
                            jugadorR = jugadoresRoboCastigo.get(k);
                            consola.jugadorPuedeRobarConCastigo(jugadorR.getNombre());
                            eleccionR = consola.menuRobarDelPozo();
                            l++;
                        }
                        if (eleccionR == consola.getEleccionRobarDelPozo()) {
                            jugadorActual jugador = partidaNueva.getJugador(jugadorR.getNombre());
                            jugador.robarConCastigo();
                            mano = consola.getCartasJugador(jugador.getNombre());
                            consola.mostrarCartas(mano);
                        }
                        consola.mostrarContinuaTurno(j.getNombre());
                    }*/
				//todo lo de robo con castigo me falta pasarlo


				//lo de acomodar me falta pasarlo
				//acomodar en un juego
                    /*while (eleccion == consola.getEleccionAcomodarJuegoPropio()) {
                        int iCarta = consola.preguntarCartaParaAcomodar();
                        ArrayList<ArrayList<String>> juegos = consola.getJuegosJugador(partidaNueva, j.getNombre());
                        if (!juegos.isEmpty()) {
                            int numJuego = 1;
                            for (ArrayList<String> juego : juegos) {
                                consola.mostrarJuego(numJuego);
                                consola.mostrarCartas(juego);
                                numJuego++;
                            }
                            int e = consola.preguntarEnQueJuegoQuiereAcomodar();
                            if(j.acomodarCartaJuegoPropio(iCarta, e, partidaNueva.getRonda())) {
                                juegos = consola.getJuegosJugador(partidaNueva,j.getNombre());
                                ArrayList<String> juego = juegos.get(e);
                                consola.mostrarCartas(juego);
                            }
                        } else {
                            consola.mostrarNoPuedeAcomodarJuegoPropio();
                        }
                        mano = consola.getCartasJugador(j.getNombre());
                        consola.mostrarCartas(mano);
                        eleccion = consola.menuBajar();
                    }*/


				i++;
				if (i>jugadoresActuales.size()-1) i = 0;
			}//while ronda
			p.incrementarRonda();
			p.resetearJuegosJugadores();
			p.sumarPuntos();
			int[] puntos = p.getPuntosJugadores();
			int m = 0;
			for (jugadorActual j : jugadoresActuales) {
				//vista.mostrarPuntosJugador(j.getNombre(), puntos[m]);
				m++;
			}
		}//while partida
		String ganador = p.determinarGanador();
		//vista.mostrarGanador(ganador);
	}

	//GETTERS Y SETTERS------------------------------------------------------------
	public Jugador getJugador(String nombreJugador) throws RemoteException {
		boolean encontrado = false;
		int i = 0;
		while (i < this.jugadores.size() && !encontrado) {
			if (this.jugadores.get(i).getNombre().equals(nombreJugador)) {
				encontrado = true;
			} else {
				i++;
			}
		}
		return this.jugadores.get(i);
	}

	public ArrayList<Jugador> getJugadores() throws RemoteException {
		return this.jugadores;
	}


	public int getFigura() throws RemoteException {
		return FIGURA;
	}

	public int getAs() throws RemoteException {
		return AS;
	}

	public int getPuntosComodin() throws RemoteException {
		return PUNTOS_COMODIN;
	}

	public Partida getPartidaActual() throws RemoteException {
		return this.partidaActual;
	}

	public void setPartidaActual(Serializador srl) throws RemoteException{
		Partida p = (Partida) srl.readFirstObject();
		this.partidaActual = p;
	}
}