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
	private static final int NOTIFICAR_PUEDE_ROBO_CASTIGO = 11;
	private static final int NOTIFICAR_HUBO_ROBO_CASTIGO = 12;
	private static final int NOTIFICAR_RONDA_FINALIZADA = 14;
	private static final int NOTIFICAR_VENTANA_JUEGO = 27;
	private static final int EMPIEZA_PARTIDA = 1;
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
		this.jugadores.add(j);
		notificarObservadores(NOTIFICAR_NUEVO_JUGADOR);
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
			notificarSrl(this.srl, EMPIEZA_PARTIDA); //el ctrl setea la partida
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
				notificarSrl(this.srl, EMPIEZA_PARTIDA); //muestra combinacion requerida y pozo
				int i = 0;
				desarrolloTurno(this.partidaActual, i); //supongo que tiene que mantenerse en esta funcion y desp volver
				notificarObservadores(NOTIFICAR_RONDA_FINALIZADA);
				this.partidaActual.incrementarRonda();
				this.partidaActual.resetearJuegosJugadores();
				this.partidaActual.sumarPuntos();
				int[] puntos = this.partidaActual.getPuntosJugadores();
				notificarObservadores(puntos);
			}
			jugadorActual ganador = this.partidaActual.determinarGanador();
			ganador.setPuntosAlFinalizar(ganador.getPuntos());
			notificarObservadores(ganador.getNombre());
			if (srlRanking.readFirstObject()==null) {
				srlRanking.writeOneObject(ganador);
			} else {
				srlRanking.addOneObject(ganador); //revisar tema cabecera
			}
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
			notificarSrl(srl, EMPIEZA_PARTIDA);
			desarrolloTurno(p,numJugador+1);
		} else {
			notificarSrl(srl, ACTUALIZAR_PARTIDA);
		}
	}

	public void roboConCastigo(String nombreJugador) throws RemoteException{
		int numJNoPuedeRobar = this.partidaActual.getJugador(nombreJugador).getNumeroJugador();
		int i = 0;
		desarrolloRoboConCastigo(i, numJNoPuedeRobar, false);
	}

	public void desarrolloRoboConCastigo(int iJugador, int numJNoPuedeRobar, boolean robo) throws RemoteException {
		if (iJugador < this.partidaActual.getJugadoresActuales().size()) {
			if (iJugador == numJNoPuedeRobar) {
				iJugador++;
			}
			if (iJugador < this.partidaActual.getJugadoresActuales().size()) {
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

	public void haRobadoConCastigo(int numJ, int numJNoPuedoRobar, boolean robo, Partida p) throws RemoteException {
		if (!robo) {
			desarrolloRoboConCastigo(numJ+1, numJNoPuedoRobar, robo);
		} else {
			this.srl.writeOneObject(p);
			notificarSrl(this.srl, ACTUALIZAR_PARTIDA);
			int[] cambio = new int[2];
			cambio[0] = numJ;
			cambio[1] = NOTIFICAR_HUBO_ROBO_CASTIGO;
			notificarObservadores(cambio); //notifica que la vista i robo con castigo
		}
	}

	private void notificarSrl(Serializador srl, int situacion) throws RemoteException {
		Object[] cambio = new Object[2];
		cambio[0] = srl;
		if (situacion == EMPIEZA_PARTIDA) {
			cambio[1] = null;
		} else if (situacion == ACTUALIZAR_PARTIDA){
			cambio[1] = ACTUALIZAR_PARTIDA;
		} else if (situacion == ENVIAR_RANKING) {
			cambio[1] = ENVIAR_RANKING;
		}
		notificarObservadores(cambio);
	}

	public void nuevaVentana() throws RemoteException {
		notificarObservadores(NOTIFICAR_VENTANA_JUEGO);
	}

	public void getRanking() throws RemoteException {
		notificarSrl(srlRanking, ENVIAR_RANKING);
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