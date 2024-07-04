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
	private static final int NOTIFICAR_GANADOR = 10;
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

	public void agregarJugador(String nombreJugador) throws RemoteException {
		jugadores.add(new Jugador(nombreJugador));
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
	}

	public void partidaFinRonda() throws RemoteException {
		partidaActual.incrementarRonda();
		partidaActual.resetearJuegosJugadores();
		partidaActual.sumarPuntos();
		partidaActual.setCorteRonda();
		notificarPuntos();
	}

	public boolean isPozoEmpty() throws RemoteException {
		return partidaActual.getPozo().isEmpty();
	}

	public void ordenarCartasEnMano(int numJugador, int[] cartas) throws RemoteException {
		partidaActual.getJugadoresActuales().get(numJugador).moverCartaEnMano(cartas[0],cartas[1]);
	}

	public void robarDelPozo(int numJugador) throws RemoteException {
		partidaActual.getJugadoresActuales().get(numJugador).addCarta(partidaActual.eliminarDelPozo());
	}

	public void robarDelMazo(int numJugador) throws RemoteException {
		partidaActual.getJugadoresActuales().get(numJugador).addCarta(partidaActual.eliminarDelMazo());
	}

	public void tirarAlPozo(int numJugador, int eleccionCarta) throws RemoteException {
		partidaActual.agregarAlPozo(partidaActual.getJugadoresActuales().get(numJugador).getCartaFromMano(eleccionCarta));
	}

	public void bajarJuego(int numJugador, int[] cartasABajar, int tipoJuego) throws RemoteException {
		ifJugador j = partidaActual.getJugadoresActuales().get(numJugador);
		j.addJuego(cartasABajar);
		j.eliminarDeLaMano(j.getJuegos().get(j.getJuegos().size() - 1));
		if (tipoJuego == TRIO) {
			j.incrementarTriosBajados();
		} else if (tipoJuego == ESCALERA) {
			j.incrementarEscalerasBajadas();
		}
	}

	public boolean acomodarCartaJuegoPropio(int iCarta, int numJugador, int numJuego, int ronda) throws RemoteException {
		return partidaActual.getJugadoresActuales().get(numJugador).acomodarCartaJuegoPropio(iCarta,numJuego,ronda);
	}

	public boolean acomodarCartaJuegoAjeno(int iCarta, int numCarta, Palo paloCarta, int numJugador, int numJugadorAcomodar, int numJuego, int ronda) throws RemoteException {
		boolean acomodo = partidaActual.getJugadoresActuales().get(numJugadorAcomodar).comprobarAcomodarCarta(numCarta,paloCarta,numJuego,ronda);
		if (acomodo) {
			Carta c = partidaActual.getJugadoresActuales().get(numJugador).getCartaFromMano(iCarta);
			partidaActual.getJugadoresActuales().get(numJugadorAcomodar).getJuegos().get(numJuego).add(c);
		}
		return acomodo;
	}

	public void notificarDesarrolloTurno(int numJugador) throws RemoteException {
		int[] numJugadorYNotif = new int[2];
		numJugadorYNotif[0] = numJugador;
		numJugadorYNotif[1] = NOTIFICAR_DESARROLLO_TURNO;
		notificarObservadores(numJugadorYNotif);
	}

	public void determinarGanador() throws RemoteException {
		jugadorActual ganador = partidaActual.determinarGanador();
		ganador.setPuntosAlFinalizar(ganador.getPuntos());
		Object[] notif = new Object[2];
		notif[0] = ganador.getNombre();
		notif[1] = NOTIFICAR_GANADOR;
		notificarGanador(notif);
	}

	public void agregarJugadorAPartidaActual(String nombreJugador) throws RemoteException {
		partidaActual.agregarJugador(nombreJugador);
	}

	public void notificarRondaFinalizada() throws RemoteException {
		notificarObservadores(NOTIFICAR_RONDA_FINALIZADA);
	}

	public void notificarPuntos() throws RemoteException {
		int[] puntos = partidaActual.getPuntosJugadores();
		notificarObservadores(puntos);
		notificarObservadores();
	}

	private void notificarGanador(Object[] notif) throws RemoteException {
		notificarObservadores(notif);
		if (srlRanking.readFirstObject()==null) {
			srlRanking.writeOneObject((String)notif[0]);
		} else {
			srlRanking.addOneObject((String)notif[0]); //revisar tema cabecera
		}

	}

	public void notificarRoboConCastigo(int iJugador) throws RemoteException{
		Object[] cambio = new Object[2];
		cambio[0] = getPartidaActual().getJugadoresActuales().get(iJugador);
		cambio[1] = NOTIFICAR_PUEDE_ROBO_CASTIGO;
		notificarObservadores(cambio);
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

	public void setTurno(int numJugador, boolean valor) throws RemoteException {
		partidaActual.getJugadoresActuales().get(numJugador).setTurnoActual(valor);
	}

	public boolean getTurno(int numJugador) throws RemoteException {
		return partidaActual.getJugadoresActuales().get(numJugador).isTurnoActual();
	}

	public void setCorteRonda() throws RemoteException{
		partidaActual.setCorteRonda();
	}

	public boolean getCorteRonda() throws RemoteException {
		return partidaActual.getCorteRonda();
	}

	public void setPuedeBajar(int numJugador) throws RemoteException {
		partidaActual.getJugadoresActuales().get(numJugador).setPuedeBajar();
	}

	public int getCantJugadoresPartida() throws RemoteException {
		return partidaActual.getNumJugadores();
	}

	public boolean getRoboConCastigo(int numJugador) throws RemoteException {
		return partidaActual.getJugadoresActuales().get(numJugador).getRoboConCastigo();
	}

	public boolean getPuedeBajar(int numJugador) throws RemoteException {
		return partidaActual.getJugadoresActuales().get(numJugador).getPuedeBajar();
	}

	public boolean isManoEmpty(int numJugador) throws RemoteException {
		return partidaActual.getJugadoresActuales().get(numJugador).isManoEmpty();
	}
}