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
	private static final int NOTIFICAR_NUEVO_JUGADOR = 7;
	private static final int NOTIFICAR_DESARROLLO_TURNO = 8;
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
		partidaActual = new Partida(); //creacion de partida
		partidaActual.agregarJugador(nombreVista);
		partidaActual.setEstadoPartida();
		partidaActual.setCantJugadoresDeseada(cantJugadores);
		notificarObservadores(NUEVA_PARTIDA); //el ctrl setea la partida
		notificarObservadores(nombreVista); //avisa que el jugador x creo una partida
	}

	public void iniciarCartasPartida() throws RemoteException{
		partidaActual.crearMazo();
		partidaActual.repartirCartas();
		partidaActual.iniciarPozo();
		partidaActual.setRonda(1);
	}

	public void partidaFinRonda() throws RemoteException {
		partidaActual.incrementarRonda();
		partidaActual.resetearJuegosJugadores();
		partidaActual.sumarPuntos();
		notificarPuntos();
	}

	public ifJugador getJugadorQueLeToca() throws RemoteException {
		return partidaActual.getJugadorQueLeToca();
	}

	public boolean isPozoEmpty() throws RemoteException {
		return partidaActual.getPozo().isEmpty();
	}

	public void setTurno(int numJugador) throws RemoteException {
		partidaActual.getJugadoresActuales().get(numJugador).setTurnoActual();
	}

	public void notificarDesarrolloTurno() throws RemoteException {
		notificarObservadores(NOTIFICAR_DESARROLLO_TURNO);
	}

	public void determinarGanador() throws RemoteException {
		jugadorActual ganador = partidaActual.determinarGanador();
		ganador.setPuntosAlFinalizar(ganador.getPuntos());
		notificarGanador(ganador.getNombre());
	}

	public void agregarJugadorAPartidaActual(String nombreJugador) throws RemoteException {
		partidaActual.agregarJugador(nombreJugador);
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

	public boolean notificarRoboConCastigo(int iJugador, int numJNoPuedeRobar) throws RemoteException{
		boolean puedeBajar = false;
		if (partidaActual.getJugadoresActuales().get(iJugador).getPuedeBajar()) { //si no puede bajar es porque ya bajo por lo que no puede robar con castigo
			puedeBajar = true;
			int[] cambio = new int[3];
			cambio[0] = iJugador;
			cambio[1] = NOTIFICAR_PUEDE_ROBO_CASTIGO;
			cambio[2] = numJNoPuedeRobar;
			notificarObservadores(cambio); //notifica a la vista que i que puede robar con castigo
		}
		return puedeBajar;
	}

	public void notificarHaRobadoConCastigo(int numJ) throws RemoteException {
		int[] cambio = new int[2];
		cambio[0] = numJ;
		cambio[1] = NOTIFICAR_HUBO_ROBO_CASTIGO;
		notificarObservadores(cambio); //notifica que la vista i robo con castigo
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

	public void guardarPartida() throws RemoteException {
		srl.addOneObject(partidaActual);
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

	public int getCantJugadoresPartida() throws RemoteException {
		return partidaActual.getNumJugadores();
	}
}