package src.controlador;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rmimvc.src.observer.IObservableRemoto;
import src.modelo.Carta;
import src.modelo.Palo;
import src.modelo.ifJuego;
import src.modelo.ifJugador;
import src.modelo.ifCarta;
import src.modelo.ifPartida;
import rmimvc.src.cliente.IControladorRemoto;
import src.serializacion.Serializador;
import src.vista.ifVista;


public class Controlador implements IControladorRemoto {
    ifJuego juego;
    ifVista vista;
    private static final int NOTIFICACION_RECIBIDA_RANKING = 3;
    private static final int NOTIFICACION_DESARROLLO_TURNO = 8;
    private static final int NOTIFICACION_NUEVO_JUGADOR = 7;
    private static final int NOTIFICACION_NUEVA_PARTIDA = 6;
    private static final int NOTIFICACION_GANADOR = 10;
    private static final int NOTIFICACION_PUEDE_ROBO_CASTIGO = 11;
    private static final int NOTIFICACION_HUBO_ROBO_CASTIGO = 12;
    private static final int NOTIFICACION_RONDA_FINALIZADA = 14;
    private static final int NOTIFICACION_PUNTOS = 15;
    private static final int NOTIFICACION_RANKING = 16;
    private static final int NOTIFICACION_JUGADOR_INICIO_PARTIDA = 17;
    private static final int NOTIFICACION_ROBO = 18;
    private static final int NOTIFICACION_COMIENZO_PARTIDA = 19;
    private static final int JUEGO_INVALIDO = 2;
    private static final int YA_NO_PUEDE_BAJAR = 1;
    private static final int CANT_MIN_JUGADORES = 0;
    private static final int CANT_MAX_JUGADORES = 5;


    public Controlador(ifVista vista) {
        this.vista = vista;
    }

    //jugador---------------------------
    public ArrayList<String> enviarManoJugador(int numJugador) throws RemoteException {
        ArrayList<String> manoString = new ArrayList<>();
        try {
            ifJugador jA = getJugadorPartida(numJugador);
            ArrayList<ifCarta> cs = new ArrayList<>(jA.getMano());
            manoString = ifVista.cartasToStringArray(cs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return manoString;
    }

    public ArrayList<ArrayList<String>> enviarJuegosJugador(int numJugador) throws RemoteException {
        ifJugador j = getJugadorPartida(numJugador);
        ArrayList<ArrayList<Carta>> juegos = j.getJuegos();
        ArrayList<ArrayList<String>> juegosString = new ArrayList<>();
        for (ArrayList<Carta> juego : juegos) {
            ArrayList<ifCarta> cs = new ArrayList<>(juego);
            juegosString.add(ifVista.cartasToStringArray(cs));
        }
        return juegosString;
    }

    public void enviarJuegosEnMesa(int numJugador) throws RemoteException {
        if (numJugador>getCantJugadoresPartida()-1) {
            numJugador = 0;
        }
        for (int j = 0; j < getCantJugadoresPartida()-1; j++) {
            vista.mostrarJuegosJugador(numJugador);
            vista.mostrarJuegos(enviarJuegosJugador(numJugador));
        }
    }

    private boolean hayJuegosEnMesa(int numJugador) throws RemoteException {
        boolean hay = false;
        int i = 0;
        if (numJugador > getCantJugadoresPartida()-1) {
            numJugador = 0;
        }
        while (!hay && i < getCantJugadoresPartida()-1) {
            hay = !getJugadorPartida(numJugador).getPuedeBajar();
            i++;
        }
        return hay;
    }

    public void agregarNuevoJugador(String nombreJugador) throws RemoteException {
        vista.setNombreVista(nombreJugador);
        juego.agregarJugador(nombreJugador);
    }


    //partida-------------------------------------------------------------

    //crea la partida y la inicia si hay al menos 2 jugadores
    public void crearPartida(ifVista vista, int cantJugadores) throws RemoteException {
        juego.crearPartida(vista.getNombreVista(), cantJugadores); //setea partida en juego y en ctrl
    }

    public void guardarPartida() throws RemoteException {
        juego.guardarPartida();
    }

    public boolean bajarse(int numJugador, int [] cartasABajar) throws RemoteException {
        boolean bajo = false;
        int tipoJuego =
        ifJuego.comprobarJuego(getJugadorPartida(numJugador).seleccionarCartasABajar(cartasABajar),getRonda());
        if(tipoJuego != JUEGO_INVALIDO) {
            juego.bajarJuego(numJugador, cartasABajar, tipoJuego);
            vista.mostrarJuegos(enviarJuegosJugador(numJugador));
            bajo = true;
        } else {
             vista.mostrarNoPuedeBajarJuego(JUEGO_INVALIDO);
        }
        return bajo;
    }

    public void acomodarEnJuegoPropio(int iCarta, int numJugador, ArrayList<ArrayList<String>> juegos, int numJuego) throws RemoteException {
        if(juego.acomodarCartaJuegoPropio(iCarta, numJugador, numJuego, getRonda())) {
            vista.mostrarAcomodoCarta();
            vista.mostrarCartas(enviarJuegosJugador(numJugador).get(numJuego));
        } else {
            vista.mostrarNoPuedeAcomodarJuegoPropio();
        }
    }

    public void acomodarEnJuegoAjeno(int iCarta, int numCarta, Palo paloCarta, int numJugador, int numJugadorAcomodar, int numJuego) throws RemoteException {
        if (juego.acomodarCartaJuegoAjeno(iCarta, numCarta, paloCarta, numJugador, numJugadorAcomodar, numJuego, getRonda())) {
            vista.mostrarAcomodoCarta();
            vista.mostrarJuegosJugador(numJugadorAcomodar);
            vista.mostrarJuegos(enviarJuegosJugador(numJugadorAcomodar));
        } else {
            vista.mostrarNoPuedeAcomodarJuegoPropio();
        }
    }

    public boolean cortarTurno(int numJugador, int ronda, boolean corte) throws RemoteException {
        ifJugador j = getJugadorPartida(numJugador);
        if (!corte) {
            int[] triosYEscalerasQueFaltan = j.comprobarQueFaltaParaCortar(ronda);
            vista.mostrarLoQueFaltaParaCortar(triosYEscalerasQueFaltan);
        }
        return corte;
    }

    public void tirarAlPozoTurno(int numJugador, ArrayList<String> mano) throws RemoteException {
        int eleccion = vista.preguntarQueBajarParaPozo(mano.size());
        juego.tirarAlPozo(numJugador, eleccion);
    }

    public void notificarTurno(int numJugador) throws RemoteException {
        juego.notificarObservadores(juego.getPartidaActual().getJugadoresActuales().get(numJugador).getNumeroJugador());
    }

    public void setTurno(int numJugador, boolean valor) throws RemoteException {
        juego.setTurno(numJugador, valor);
    }

    public void setRoboConCastigo(int numJugador, boolean valor) throws RemoteException {
        juego.setRoboConCastigo(numJugador, valor);
    }

    public void notificarRobo(int numJugador) throws RemoteException {
        juego.notificarRobo(numJugador);
    }

    public void notificarDesarrolloTurno(int numJugador) throws RemoteException {
        juego.notificarDesarrolloTurno(numJugador);
    }

    public void notificarComienzoPartida() throws RemoteException {
        juego.notificarObservadores(NOTIFICACION_COMIENZO_PARTIDA);
    }

    public void desarrolloRobo(int numJugador) throws RemoteException {
        vista.mostrarCartas(enviarManoJugador(numJugador));
        int eleccion = vista.menuRobar();
        if (eleccion == ifVista.ELECCION_ROBAR_DEL_POZO) {
            juego.robarDelPozo(numJugador);
        } else if(eleccion == ifVista.ELECCION_ROBAR_DEL_MAZO) {
            juego.robarDelMazo(numJugador);
            juego.setRoboDelMazo(numJugador, true);
        }

    }

    public void notificarRoboConCastigo(int numJugador) throws RemoteException {
        int numJugadorRoboCastigo = numJugador+1;
        if (numJugadorRoboCastigo > getCantJugadoresPartida()-1) {
            numJugadorRoboCastigo = 0;
        }
        juego.setNumJugadorRoboCastigo(numJugadorRoboCastigo);
        int[] jugadoresQuePuedenRobarConCastigo = new int[getCantJugadoresPartida()-1];
        for (int i = 0; i < getCantJugadoresPartida()-1; i++) {
            jugadoresQuePuedenRobarConCastigo[i] = numJugadorRoboCastigo;
            numJugadorRoboCastigo++;
            if (numJugadorRoboCastigo>getCantJugadoresPartida()-1) numJugadorRoboCastigo = 0;
        }
        juego.notificarObservadores(jugadoresQuePuedenRobarConCastigo);
    }

    public void resetearRoboConCastigo() throws RemoteException {
        juego.resetearRoboConCastigo();
    }
    
    public void desarrolloTurno(int numJugador) throws RemoteException {
        vista.mostrarCartas(enviarManoJugador(numJugador));
        while(juego.getTurno(numJugador)) {
            int eleccion = vista.menuBajar();
            boolean corte;
            switch (eleccion) {
                case ifVista.ELECCION_ORDENAR_CARTAS:
                    int manoSize = juego.getPartidaActual().getJugadoresActuales().get(numJugador).getMano().size();
                    juego.ordenarCartasEnMano(numJugador, vista.preguntarParaOrdenarCartas(manoSize));
                    break;
                case ifVista.ELECCION_ACOMODAR_JUEGO_PROPIO:
                    ArrayList<ArrayList<String>> juegos = enviarJuegosJugador(numJugador);
                    if (!juegos.isEmpty()) {
                        int cartaAcomodar = vista.preguntarCartaParaAcomodar();
                        vista.mostrarJuegos(juegos);
                        acomodarEnJuegoPropio(cartaAcomodar, numJugador, juegos, vista.preguntarEnQueJuegoQuiereAcomodar()-1);
                    } else {
                        vista.mostrarNoPuedeAcomodarJuegoPropio();
                    }
                    break;
                case ifVista.ELECCION_ACOMODAR_JUEGO_AJENO:
                    if (hayJuegosEnMesa(numJugador+1)) {
                        enviarJuegosEnMesa(numJugador+1);
                        int iCartaAcomodar = vista.preguntarCartaParaAcomodar();
                        ifCarta c = getJugadorPartida(numJugador).getMano().get(iCartaAcomodar);
                        int numJugadorAcomodar = vista.preguntarEnLosJuegosDeQueJugadorAcomodar();
                        vista.mostrarJuegos(enviarJuegosJugador(numJugadorAcomodar));
                        acomodarEnJuegoAjeno(iCartaAcomodar, c.getNumero(), c.getPalo(), numJugador, numJugadorAcomodar, vista.preguntarEnQueJuegoQuiereAcomodar()-1);
                    }
                    break;
                case ifVista.ELECCION_BAJARSE:
                    if (juego.getPuedeBajar(numJugador)) {
                        vista.mostrarAdvertenciaBajarse();
                        int juegosBajados = 0;
                        while (vista.preguntarSiQuiereSeguirBajandoJuegos()) {
                            if (bajarse(numJugador, vista.preguntarQueBajarParaJuego(vista.preguntarCantParaBajar()))) {
                                juegosBajados++;
                                vista.mostrarCartas(enviarManoJugador(numJugador));
                            }
                        }
                        if (juegosBajados>0) {
                            vista.mostrarNoPuedeBajarJuego(YA_NO_PUEDE_BAJAR);
                            juego.setPuedeBajar(numJugador);
                        }
                    } else {
                        vista.mostrarNoPuedeBajarJuego(YA_NO_PUEDE_BAJAR);
                    }
                    break;
                case ifVista.ELECCION_CORTAR:
                    corte = cortarTurno(numJugador, getRonda(), getJugadorPartida(numJugador).cortar(getRonda()));
                    if (corte) juego.setCorteRonda();
                    break;
                case ifVista.ELECCION_TIRAR_AL_POZO:
                    tirarAlPozoTurno(numJugador, enviarManoJugador(numJugador));
                    juego.setTurno(numJugador, false);
                    break;
                case ifVista.ELECCION_VER_JUEGOS_BAJADOS:
                    vista.mostrarJuegos(enviarJuegosJugador(numJugador));
                    break;
                case ifVista.ELECCION_VER_JUEGOS_BAJADOS_MESA:
                    enviarJuegosEnMesa(numJugador+1);
                    break;
            }
            if (juego.isManoEmpty(numJugador)) {
                juego.setTurno(numJugador,false);
                if (!juego.getCorteRonda()) {
                    juego.setCorteRonda();
                }
                vista.mostrarCorto(getJugadorPartida(numJugador).getNombre());
            } else {
                vista.mostrarCartas(enviarManoJugador(numJugador));
            }
        }
        vista.mostrarFinalizoTurno();
    }

    //OBSERVER-----------------------------------------------------
    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        juego = (ifJuego) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto modelo, Object cambio) throws RemoteException {
        if (cambio instanceof Integer) {
            int indice = (Integer) cambio;
            switch (indice) {
                case CANT_MIN_JUGADORES: //cuando es el turno de un jugador x
                case 1:
                case 2:
                case 3:
                case 4:
                case CANT_MAX_JUGADORES:
                    vista.actualizar(juego.getJugadores().get(indice), indice);
                    break;
                case NOTIFICACION_NUEVO_JUGADOR:
                    vista.actualizar(juego.getJugadores().get(juego.getJugadores().size()-1), indice);
                    break;
                case NOTIFICACION_RONDA_FINALIZADA:
                    vista.actualizar(getRonda(),indice);
                    break;
                case NOTIFICACION_NUEVA_PARTIDA:
                    vista.actualizar(juego.getPartidaActual(), indice);
                    break;
                case NOTIFICACION_COMIENZO_PARTIDA:
                    vista.actualizar(juego.getPartidaActual().getJugadoresActuales(), indice);
                    break;
            }
        }


        else if (cambio instanceof Object[] c) { //serializador
            if ((int)c[1] == NOTIFICACION_RECIBIDA_RANKING) {
                Object[] jugadores = ((Serializador) c[0]).readObjects();
                vista.actualizar(jugadores, NOTIFICACION_RANKING);
            } else if ((int)c[1] == NOTIFICACION_GANADOR) {
                vista.actualizar(c[0], NOTIFICACION_GANADOR);
            } else if ((int)c[1] == NOTIFICACION_DESARROLLO_TURNO) {
                ifJugador j = getJugadorPartida((int)c[0]);
                vista.actualizar(j, NOTIFICACION_DESARROLLO_TURNO);
            } else if ((int)c[1] == NOTIFICACION_ROBO) {
                ifJugador j = getJugadorPartida((int)c[0]);
                vista.actualizar(j, NOTIFICACION_ROBO);
            } else if ((int)c[1] == NOTIFICACION_HUBO_ROBO_CASTIGO) {
                vista.actualizar(c[0], NOTIFICACION_HUBO_ROBO_CASTIGO);
            }

        }

        else if (cambio instanceof String) {
            vista.actualizar(cambio, NOTIFICACION_JUGADOR_INICIO_PARTIDA);
        }

        else if (cambio instanceof int[] cambioA) { //robo con castigo
            vista.actualizar(null, NOTIFICACION_PUEDE_ROBO_CASTIGO);
        }

        else {
            vista.actualizar(cambio, NOTIFICACION_PUNTOS);
        }
    }

    public void robarConCastigo(int numJugador) throws RemoteException {
        juego.robarDelPozo(numJugador);
        juego.robarDelMazo(numJugador);
    }

    public void notificarHaRobadoConCastigo(int numJugador) throws RemoteException {
        juego.notificarHaRobadoConCastigo(numJugador);
    }

    public void notificarRondaFinalizada() throws RemoteException {
        juego.notificarRondaFinalizada();
    }

    public void partidaFinRonda() throws RemoteException {
        juego.partidaFinRonda();
    }

    public boolean getCorteRonda() throws RemoteException {
        return juego.getCorteRonda();
    }

    public boolean getRoboDelMazo(int numJugador) throws RemoteException {
        return juego.getRoboDelMazo(numJugador);
    }

    public void determinarGanador() throws RemoteException {
        juego.determinarGanador();
    }

    public int jugarPartidaRecienIniciada() throws RemoteException {
        int i = 0;
        int inicio = 0;
        boolean encontrado = false;
        ifPartida p = juego.getPartidaActual();
        if (p != null) { //si ya se llamo a crearPartida()
            //para iniciar la partida, esta debe tener cant de jugadoresActuales = cantDeseada
            int cantJugadoresActuales = p.getJugadoresActuales().size();
            int cantDeseadaJugadores = p.getCantJugadoresDeseada();
            //si p ya esta creada entonces tiene un jugador
            //hay que averiguar si el que llamo a esta funcion es el mismo que el que la creó
            //si la creó entonces tiene que se un jugadorActual
            while (i < cantJugadoresActuales && !encontrado) {
                if (p.getJugadoresActuales().get(i).getNombre().equals(vista.getNombreVista())) {
                    encontrado = true; //significa que el creo la partida, llamo a esta funcion
                    inicio = 1;
                }
                i++;
            }
            if (encontrado && cantJugadoresActuales == cantDeseadaJugadores) {
                p.setEstadoPartida();
                inicio = 2;
            }
            if (!encontrado) { //significa que la vista llamo a esta funcion pero no creo la partida
                juego.agregarJugadorAPartidaActual(vista.getNombreVista());
                p = juego.getPartidaActual();
                if (p.getJugadoresActuales().size() == p.getCantJugadoresDeseada()) {
                    p.setEstadoPartida();
                    inicio = 2;
                } else {
                    inicio = 1;
                }
            }
        }
        return inicio;
    }

    public int getTotalRondas() throws RemoteException {
        return juego.getPartidaActual().getTotalRondas();
    }

    public void iniciarCartasPartida() throws RemoteException {
        juego.iniciarCartasPartida();
    }

    public ifJugador getJugadorPartida(int numJugadorPartida) throws RemoteException {
        return juego.getPartidaActual().getJugadoresActuales().get(numJugadorPartida);
    }

    public boolean getRoboConCastigo(int numJugador) throws RemoteException {
        return juego.getRoboConCastigo(numJugador);
    }

    public int getRonda() throws RemoteException {
        return juego.getPartidaActual().getRonda();
    }

    public boolean getEstadoPartida() throws RemoteException {
        return juego.getPartidaActual().getEstadoPartida();
    }

    public void getRanking() throws RemoteException {
        juego.getRanking();
    }

    public ifCarta getPozo() throws RemoteException {
        return juego.getPartidaActual().sacarPrimeraDelPozo();
    }

    public int getCantJugadoresPartida() throws RemoteException {
        return juego.getCantJugadoresPartida();
    }

    public int getNumJugadorRoboCastigo() throws RemoteException {
        return juego.getNumJugadorRoboCastigo();
    }

    public void setNumJugadorRoboCastigo(int numJugador) throws RemoteException {
        juego.setNumJugadorRoboCastigo(numJugador);
    }

    public boolean getPuedeBajar(int numJugador) throws RemoteException {
        return juego.getPuedeBajar(numJugador);
    }

}