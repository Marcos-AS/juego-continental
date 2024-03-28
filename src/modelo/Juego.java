package src.modelo;

import rmimvc.src.observer.ObservableRemoto;
import src.serializacion.Serializador;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Juego extends ObservableRemoto implements ifJuego {
	protected static final int FIGURA = 10;
	protected static final int AS = 20;
	protected static final int PUNTOS_COMODIN = 50;
	protected static final int COMODIN = -1; //este valor para que al ordenar cartas queden los comodines primero
	protected static final int CANT_CARTAS_INICIAL = 6;
	private static final int NOTIFICAR_NUEVO_JUGADOR = 7;
	private static final int NOTIFICAR_PUEDE_ROBO_CASTIGO = 11;
	private static final int NOTIFICAR_HUBO_ROBO_CASTIGO = 12;
	private static final int NOTIFICAR_RONDA_FINALIZADA = 14;
	private static final int NOTIFICAR_VENTANA_NUEVA_PARTIDA = 27;
	private static final int NUEVA_PARTIDA = 6;
	private static final int ACTUALIZAR_PARTIDA = 2;
	private static final int ENVIAR_RANKING = 3;

	private ArrayList<Jugador> jugadores = new ArrayList<>();
	private static Juego instancia;
	private Partida partidaActual;
	private final Serializador srl = new Serializador("partidas.dat");
	private final Serializador srlRanking = new Serializador("jugadores.dat");

	//singleton
	public static Juego getInstancia() throws RemoteException {
		if (instancia == null) instancia = new Juego();
		return instancia;
	}

	private Juego() {
	}

	public void agregarJugador(Jugador j) throws RemoteException {
		jugadores.add(j);
		notificarObservadores(NOTIFICAR_NUEVO_JUGADOR);
	}

	public void crearPartida(String nombreVista, int cantJugadores) throws RemoteException{
		//boolean creada = false;
		//if (jugadores.size()>=cantJugadores) {
		//	creada = true;
			partidaActual = new Partida(); //creacion de partida
			partidaActual.agregarJugador(nombreVista);
			partidaActual.setEstadoPartida();
			partidaActual.setCantJugadoresDeseada(cantJugadores);
			notificarObservadores(NUEVA_PARTIDA); //el ctrl setea la partida
			notificarObservadores(nombreVista); //avisa que el jugador x creo una partida
		//}
		//return creada;
	}

	public void iniciarCartasPartida() throws RemoteException{
		partidaActual.crearMazo();
		partidaActual.repartirCartas();
		partidaActual.iniciarPozo();
	}

	public void partidaFinRonda() throws RemoteException {
		partidaActual.incrementarRonda();
		partidaActual.resetearJuegosJugadores();
		partidaActual.sumarPuntos();
		notificarPuntos();
	}

	public void determinarGanador() throws RemoteException {
		jugadorActual ganador = partidaActual.determinarGanador();
		ganador.setPuntosAlFinalizar(ganador.getPuntos());
		notificarGanador(ganador.getNombre());
	}

	public void agregarJugadorAPartidaActual(String nombreJugador) throws RemoteException {
		partidaActual.agregarJugador(nombreJugador);
	}

	public jugadorActual notificarTurno(int numJugador) throws RemoteException {
		jugadorActual j = partidaActual.getJugadoresActuales().get(numJugador);
		j.setTurnoActual();
		notificarObservadores(j);
		return j;
	}

	public void finalizoTurno(int numJugador, boolean corte) throws RemoteException {
		if (corte) partidaActual.setEstadoPartida();
		if (partidaActual.getEstadoPartida()) {
			notificarObservadores(NUEVA_PARTIDA);
		} else {
			notificarObservadores(ACTUALIZAR_PARTIDA);
		}
	}

	public void notificarRondaFinalizada() throws RemoteException {
		notificarObservadores(NOTIFICAR_RONDA_FINALIZADA);
	}

	public void notificarPuntos() throws RemoteException {
		int[] puntos = partidaActual.getPuntosJugadores();
		notificarObservadores(puntos);
		notificarObservadores();
	}

	public void notificarGanador(String nombreGanador) throws RemoteException {
		notificarObservadores(nombreGanador);
		if (srlRanking.readFirstObject()==null) {
			srlRanking.writeOneObject(nombreGanador);
		} else {
			srlRanking.addOneObject(nombreGanador); //revisar tema cabecera
		}

	}

	public void roboConCastigo(String nombreJugador) throws RemoteException{
		int numJNoPuedeRobar = partidaActual.getJugador(nombreJugador).getNumeroJugador();
		int i = 0;
		desarrolloRoboConCastigo(i, numJNoPuedeRobar, false);
	}

	public void desarrolloRoboConCastigo(int iJugador, int numJNoPuedeRobar, boolean robo) throws RemoteException {
		if (iJugador < partidaActual.getJugadoresActuales().size()) {
			if (iJugador == numJNoPuedeRobar) {
				iJugador++;
			}
			if (iJugador < partidaActual.getJugadoresActuales().size()) {
				if (partidaActual.getJugadoresActuales().get(iJugador).getPuedeBajar()) { //si no puede bajar es porque ya bajo por lo que no puede robar con castigo
					int[] cambio = new int[3];
					cambio[0] = iJugador;
					cambio[1] = NOTIFICAR_PUEDE_ROBO_CASTIGO;
					cambio[2] = numJNoPuedeRobar;
					notificarObservadores(cambio); //notifica a la vista que i que puede robar con castigo
				}
			}
		}
	}

	public void haRobadoConCastigo(int numJ, int numJNoPuedoRobar, boolean robo) throws RemoteException {
		if (!robo) {
			desarrolloRoboConCastigo(numJ+1, numJNoPuedoRobar, robo);
		} else {
			notificarObservadores(ACTUALIZAR_PARTIDA);
			int[] cambio = new int[2];
			cambio[0] = numJ;
			cambio[1] = NOTIFICAR_HUBO_ROBO_CASTIGO;
			notificarObservadores(cambio); //notifica que la vista i robo con castigo
		}
	}

	private void notificarSrl(Serializador srl, int situacion) throws RemoteException {
		Object[] cambio = new Object[2];
		cambio[0] = srl;
		if (situacion == ENVIAR_RANKING) {
			cambio[1] = ENVIAR_RANKING;
		}
		notificarObservadores(cambio);
	}

	public void nuevaVentana() throws RemoteException {
		notificarObservadores(NOTIFICAR_VENTANA_NUEVA_PARTIDA);
	}

	public void getRanking() throws RemoteException {
		notificarSrl(srlRanking, ENVIAR_RANKING);
	}

	//GETTERS Y SETTERS------------------------------------------------------------
	public Jugador getJugador(String nombreJugador) throws RemoteException {
		boolean encontrado = false;
		int i = 0;
		while (i < jugadores.size() && !encontrado) {
			if (jugadores.get(i).getNombre().equals(nombreJugador)) {
				encontrado = true;
			} else {
				i++;
			}
		}
		return jugadores.get(i);
	}

	public ArrayList<Jugador> getJugadores() throws RemoteException {
		return jugadores;
	}

	public Partida getPartidaActual() throws RemoteException {
		return partidaActual;
	}

	@Override
	public void setPartidaActual(Serializador srl) throws RemoteException {}

	public void setCorteRonda() throws RemoteException{
		partidaActual.setCorteRonda();
	}

	public boolean getCorteRonda() throws RemoteException {
		return partidaActual.getCorteRonda();
	}

	public int getCantJugadores() throws RemoteException {
		return partidaActual.getNumJugadores();
	}
}