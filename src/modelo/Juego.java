package src.modelo;

import rmimvc.src.observer.IObservadorRemoto;
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
	private static final int NOTIFICAR_HUBO_ROBO_CASTIGO = 12;
	private static final int NOTIFICAR_RONDA_FINALIZADA = 14;
	private static final int NOTIFICAR_ROBO = 18;
	private static final int NOTIFICAR_CORTE_RONDA = 21;
	private static final int NOTIFICAR_FIN_TURNO = 24;
	private static final int NOTIFICAR_VENTANA_NUEVA_PARTIDA = 27;
	private static final int NOTIFICAR_NUEVA_PARTIDA = 6;
	private static final String NOMBRE_ARCHIVO = "ranking.dat";

	private ArrayList<Jugador> jugadores = new ArrayList<>();
	private static Juego instancia;
	private Partida partidaActual;
	private final Serializador srl = new Serializador("partidas.dat");
	private final Serializador srlRanking = new Serializador(NOMBRE_ARCHIVO);
	private int numJugadorRoboCastigo;

	private int numJugadorQueEmpiezaRonda;
	private int numTurno;

	//singleton
	public static Juego getInstancia() throws RemoteException {
		if (instancia == null) instancia = new Juego();
		return instancia;
	}

	private Juego() {
	}

	public void removerObservadores() throws RemoteException {
		int cantObservadores = getObservadores().size();
		for (int i = cantObservadores-1; i >= 0; i--) {
			removerObservador(getObservadores().get(i));
		}
	}

	public int getObservadorIndex(IObservadorRemoto o) throws RemoteException {
		return getObservadores().indexOf(o);
	}

	public void finTurno(int numJugador) throws RemoteException {
		numTurno++;
		if (numTurno>getCantJugadoresPartida()-1) {
			numTurno = 0;
		}
		Object[] notif = new Object[2];
		notif[0] = numJugador;
		notif[1] = NOTIFICAR_FIN_TURNO;
		notificarObservadores(notif);

	}

	public void setNumJugador(int numJugadorActual, int numJugadorNuevo) throws RemoteException {
		partidaActual.getJugadoresActuales().get(numJugadorActual).setNumeroJugador(numJugadorNuevo);
	}

	public void agregarJugador(String nombreJugador) throws RemoteException {
		jugadores.add(new Jugador(nombreJugador));
		notificarObservadores(NOTIFICAR_NUEVO_JUGADOR);
	}

	public void crearPartida(String nombreVista, int cantJugadoresDeseada, int numJugador) throws RemoteException{
		partidaActual = new Partida(cantJugadoresDeseada); //creacion de partida
		partidaActual.agregarJugador(nombreVista);
		partidaActual.setEstadoPartida();
		//partidaActual.setCantJugadoresDeseada(cantJugadores);
		notificarObservadores(NOTIFICAR_NUEVA_PARTIDA); //el ctrl setea la partida
		Object[] notif = new Object[2];
		notif[0] = nombreVista;
		notif[1] = NOTIFICAR_NUEVA_PARTIDA;
		notificarObservador(numJugador, notif);
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
		notificarObservadores(partidaActual.getPuntosJugadores());
	}

	public void notificarCorteRonda(int numJugador) throws RemoteException {
		Object[] cambio = new Object[2];
		cambio[0] = numJugador;
		cambio[1] = NOTIFICAR_CORTE_RONDA;
		notificarObservadores(cambio);
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
		j.addJuego(cartasABajar, tipoJuego);
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

	public void determinarGanador() throws RemoteException {
		jugadorActual ganador = partidaActual.determinarGanador();
		ganador.setPuntosAlFinalizar(ganador.getPuntos());
		Object[] notif = new Object[3];
		notif[0] = ganador.getNombre();
		notif[1] = NOTIFICAR_GANADOR;
		notif[2] = ganador.getPuntos();
		notificarGanador(notif);
	}

	public void agregarJugadorAPartidaActual(String nombreJugador) throws RemoteException {
		partidaActual.agregarJugador(nombreJugador);
	}

	private void notificarGanador(Object[] notif) throws RemoteException {
		Object guardar = notif[0] + " --- puntos: " + notif[2];
		if (srlRanking.readFirstObject()==null) {
			srlRanking.writeOneObject(guardar);
		} else {
			Object[] jugadores = srlRanking.readObjects();
			ArrayList<String> listaJugadores = new ArrayList<>();

			for (Object jugador : jugadores) {
				listaJugadores.add(jugador.toString());
			}
			listaJugadores.add(guardar.toString());

			listaJugadores.sort((j1, j2) -> {
				int puntos1 = Integer.parseInt(j1.split(" --- puntos: ")[1]);
				int puntos2 = Integer.parseInt(j2.split(" --- puntos: ")[1]);
				return Integer.compare(puntos2, puntos1); // Orden descendente
			});

			int i = 0;
			srlRanking.writeOneObject(listaJugadores.get(i));
			for (i = 1; i < listaJugadores.size(); i++) {
				srlRanking.addOneObject(listaJugadores.get(i)); //revisar tema cabecera
			}
		}

		notificarObservadores(notif);
	}

	public void notificarHaRobadoConCastigo(int numJ) throws RemoteException {
		Object[] cambio = new Object[2];
		cambio[0] = numJ;
		cambio[1] = NOTIFICAR_HUBO_ROBO_CASTIGO;
		notificarObservadores(cambio); //notifica que la vista i robo con castigo
	}

	public void nuevaVentana() throws RemoteException {
		notificarObservadores(NOTIFICAR_VENTANA_NUEVA_PARTIDA);
	}

	public Serializador getRanking() throws RemoteException {
		//notificarSrl(srlRanking, ENVIAR_RANKING);
		return srlRanking;
	}

	public void guardarPartida() throws RemoteException {
		srl.addOneObject(partidaActual);
	}

	public void ponerJugadoresEnOrden() throws RemoteException {
		ArrayList<jugadorActual> jugadores = partidaActual.getJugadoresActuales();
		ArrayList<jugadorActual> jugadoresNuevo = new ArrayList<>();
		int[] numJugadores = new int[getCantJugadoresPartida()];
		int i = 0;
		for (jugadorActual j : jugadores) {
			int numJugador = j.getNumeroJugador();
			numJugadores[i] = numJugador;
			i++;
		}

		for (int num : numJugadores) {
			jugadoresNuevo.add(jugadores.get(num));
		}
		partidaActual.setJugadoresActuales(jugadoresNuevo);
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

	public int getNumJugadorQueEmpiezaRonda() {
		return numJugadorQueEmpiezaRonda;
	}

	public void setNumJugadorQueEmpiezaRonda(int numJugadorQueEmpiezaRonda) {
		this.numJugadorQueEmpiezaRonda = numJugadorQueEmpiezaRonda;
	}

	public void setCorteRonda() throws RemoteException{
		partidaActual.setCorteRonda();
	}

	public boolean getCorteRonda() throws RemoteException {
		return partidaActual.getCorteRonda();
	}

	public boolean isRondaEmpezada() throws RemoteException {
		return partidaActual.isRondaEmpezada();
	}

	public void setRondaEmpezada(boolean rondaEmpezada) throws RemoteException{
		partidaActual.setRondaEmpezada(rondaEmpezada);
	}

	public void incrementarPuedeBajar(int numJugador) throws RemoteException {
		partidaActual.getJugadoresActuales().get(numJugador).incrementarPuedeBajar();
	}

	public void setRoboConCastigo(int numJugador, boolean valor) throws RemoteException {
		partidaActual.getJugadoresActuales().get(numJugador).setRoboConCastigo(valor);
	}

	public void resetearRoboConCastigo() throws RemoteException {
		for (jugadorActual j : partidaActual.getJugadoresActuales()) {
			j.setRoboConCastigo(false);
		}
	}

	public int getCantJugadoresPartida() throws RemoteException {
		return partidaActual.getNumJugadores();
	}

	public boolean getRoboConCastigo(int numJugador) throws RemoteException {
		return partidaActual.getJugadoresActuales().get(numJugador).getRoboConCastigo();
	}

	public int getPuedeBajar(int numJugador) throws RemoteException {
		return partidaActual.getJugadoresActuales().get(numJugador).getPuedeBajar();
	}

	public boolean isManoEmpty(int numJugador) throws RemoteException {
		return partidaActual.getJugadoresActuales().get(numJugador).isManoEmpty();
	}

	public void setRoboDelMazo(int numJugador, boolean valor) throws RemoteException {
		partidaActual.getJugadoresActuales().get(numJugador).setRoboDelMazo(valor);
	}

	public boolean getRoboDelMazo(int numJugador) throws RemoteException {
		return partidaActual.getJugadoresActuales().get(numJugador).getRoboDelMazo();
	}

	public int getNumJugadorRoboCastigo() throws RemoteException {
		return numJugadorRoboCastigo;
	}

	public void setNumJugadorRoboCastigo(int numJugador) throws RemoteException {
		numJugadorRoboCastigo = numJugador;
	}

	public void setNumTurno(int num) throws RemoteException {
		numTurno = num;
	}

	public int getNumTurno() throws RemoteException {
		return numTurno;
	}

	public void incNumJugadorQueEmpiezaRonda() throws RemoteException {
		int num = numJugadorQueEmpiezaRonda+1;
		if (num>getCantJugadoresPartida()-1) num = 0;
		numJugadorQueEmpiezaRonda = num;
	}
}