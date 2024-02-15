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

	public void agregarJugador(Jugador j) throws RemoteException {
		this.jugadores.add(j);
		notificarObservadores(7); //llama al actualizar del ctrl con (this, 1)
	}

	public boolean crearPartida(String nombreVista, int cantJugadores) throws RemoteException{
		boolean creada = false;
		if (this.jugadores.size()>=cantJugadores) {
			creada = true;
			this.partidaActual = new Partida(); //creacion de partida
			this.partidaActual.agregarJugador(nombreVista);
			this.partidaActual.setEstadoPartida();
			this.partidaActual.setCantJugadoresDeseada(cantJugadores);
			this.srl.writeOneObject(this.partidaActual);
			notificarSrl(this.srl, false);
			notificarObservadores(nombreVista); //avisa que el jugador x creo una partida
		}
		return creada;
	}

	public void agregarJugadorAPartidaActual(String nombreJugador) throws RemoteException {
		this.partidaActual.agregarJugador(nombreJugador);
	}

	public void iniciarPartida() throws RemoteException {
		try {
			this.partidaActual.setRonda(1);

			//empiezan las rondas
			while (this.partidaActual.getRonda() <= this.partidaActual.getTotalRondas()) {
				this.partidaActual.crearMazo();
				this.partidaActual.repartirCartas();
				this.partidaActual.iniciarPozo();
				this.srl.writeOneObject(this.partidaActual);
				notificarSrl(this.srl, false); //muestra combinacion requerida y pozo
				int i = 0;
				desarrolloTurno(this.partidaActual, i); //supongo que tiene que mantenerse en esta funcion y desp volver
				notificarObservadores(14);
				this.partidaActual.incrementarRonda();
				this.partidaActual.resetearJuegosJugadores();
				this.partidaActual.sumarPuntos();
				int[] puntos = this.partidaActual.getPuntosJugadores();
				notificarObservadores(puntos);
			}
			String ganador = this.partidaActual.determinarGanador();
			notificarObservadores(ganador);
		} catch(RemoteException e){
			throw new RuntimeException(e);
		}
	}

	private void desarrolloTurno(Partida p, int i) throws RemoteException {
		if (i > p.getJugadoresActuales().size() - 1) i = 0;
		jugadorActual j = p.getJugadoresActuales().get(i);
		j.setTurno();
		notificarObservadores(j);
	}

	public void finalizoTurno(Partida p, int numJugador, boolean corte) throws RemoteException {
		this.partidaActual = p;
		if (corte) p.setEstadoPartida();
		this.srl.writeOneObject(p);
		if (!corte) {
			notificarSrl(srl, false);
			desarrolloTurno(p,numJugador+1);
		} else {
			notificarSrl(srl, true);
		}
	}

	public void roboConCastigo(String nombreJugador) throws RemoteException{
		int numJNoPuedeRobar = this.partidaActual.getJugador(nombreJugador).getNumeroJugador();
		int i = 0;
		desarrolloRoboConCastigo(i, numJNoPuedeRobar, false);
	}

	public void desarrolloRoboConCastigo(int i, int numJNoPuedeRobar, boolean robo) throws RemoteException {
		if (i < this.partidaActual.getJugadoresActuales().size()) {
			if (i == numJNoPuedeRobar) {
				i++;
			}
			if (i < this.partidaActual.getJugadoresActuales().size()) {
				int[] cambio = new int[3];
				cambio[0] = i;
				cambio[1] = 11;
				cambio[2] = numJNoPuedeRobar;
				notificarObservadores(cambio); //notifica a la vista que i que puede robar con castigo
			}
		}
		if (!robo) {
			int[] cambio = new int[2];
			cambio[0] = numJNoPuedeRobar;
			cambio[1] = 13;
			notificarObservadores(cambio); //notifica que la vista n puede continuar con su turno
		}
	}

	public void haRobadoConCastigo(int numJ, int numJNoPuedoRobar, boolean robo, Partida p) throws RemoteException {
		if (!robo) {
			desarrolloRoboConCastigo(numJ+1, numJNoPuedoRobar, robo);
		} else {
			this.srl.writeOneObject(p);
			notificarSrl(this.srl, true);
			int[] cambio = new int[2];
			cambio[0] = numJ;
			cambio[1] = 12;
			notificarObservadores(cambio); //notifica que la vista i robo con castigo
		}
	}

	private void notificarSrl(Serializador srl, boolean especial) throws RemoteException {
		Object[] cambio = new Object[2];
		cambio[0] = srl;
		if (!especial) {
			cambio[1] = null;
		} else {
			cambio[1] = 1;
		}
		notificarObservadores(cambio);
	}

	public void cambioPartida(Partida p) throws RemoteException {
		this.partidaActual = p;
		this.srl.writeOneObject(p);
		notificarSrl(srl, true);
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

	public Partida getPartidaActual() throws RemoteException {
		return this.partidaActual;
	}

	@Override
	public void setPartidaActual(Serializador srl) throws RemoteException {

	}

}