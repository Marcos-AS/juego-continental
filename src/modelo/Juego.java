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

	public boolean crearPartida(String nombreVista) throws RemoteException{
		boolean creada = false;
		if (this.jugadores.size()>=2) {
			creada = true;
			this.partidaActual = new Partida();
			this.partidaActual.agregarJugador(nombreVista);
			this.srl.writeOneObject(this.partidaActual);
			notificarSrl(this.srl, false);
			notificarObservadores(nombreVista); //avisa que el jugador x creo una partida
			//iniciarPartida(partidaActual);
		}
		return creada;
	}

	public void agregarJugadorAPartidaActual(String nombreJugador) throws RemoteException {
		this.partidaActual.agregarJugador(nombreJugador);
	}

	public void iniciarPartida() throws RemoteException {
		try {
			this.partidaActual.setRonda(1);
			int rondaActual = this.partidaActual.getRonda();

			ArrayList<jugadorActual> jugadoresActuales = this.partidaActual.getJugadoresActuales();

			//empiezan las rondas
			while (rondaActual <= this.partidaActual.getTotalRondas()) {
				this.partidaActual.crearMazo();
				this.partidaActual.repartirCartas();
				this.partidaActual.iniciarPozo();
				this.srl.writeOneObject(this.partidaActual);
				notificarSrl(this.srl, false); //muestra combinacion requerida y pozo
				boolean corte = false;
				int i = 0;
				desarrolloTurno(this.partidaActual, i, corte); //supongo que tiene que mantenerse en esta funcion y desp volver
				this.partidaActual.incrementarRonda();
				this.partidaActual.resetearJuegosJugadores();
				this.partidaActual.sumarPuntos();
				int[] puntos = this.partidaActual.getPuntosJugadores();
				int m = 0;
				for (jugadorActual j : jugadoresActuales) {
					//vista.mostrarPuntosJugador(j.getNombre(), puntos[m]);
					m++;
				}
			}
			//String ganador = p.determinarGanador();
			//vista.mostrarGanador(ganador);
		} catch(RemoteException e){
			throw new RuntimeException(e);
		}
	}

	private void desarrolloTurno(Partida p, int i, boolean corte) throws RemoteException {
		if (!corte) {
			if (i > p.getJugadoresActuales().size() - 1) i = 0;
			jugadorActual j = p.getJugadoresActuales().get(i);
			j.setTurno();
			notificarObservadores(j);

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
		}
	}

	public void finalizoTurno(Partida p, int numJugador, boolean corte) throws RemoteException {
		this.srl.writeOneObject(p);
		notificarSrl(srl, false);
		desarrolloTurno(p,numJugador+1, corte);
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
				this.srl.writeOneObject(this.partidaActual);
				notificarSrl(this.srl, true);
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
		this.partidaActual = (Partida) srl.readFirstObject();
	}
}