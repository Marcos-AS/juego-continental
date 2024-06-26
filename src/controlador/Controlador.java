package src.controlador;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rmimvc.src.observer.IObservableRemoto;
import src.modelo.*;
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
    private static final int NOTIFICACION_NOMBRE_JUGADOR = 10;
    private static final int NOTIFICACION_PUEDE_ROBO_CASTIGO = 11;
    private static final int NOTIFICACION_HUBO_ROBO_CASTIGO = 12;
    private static final int NOTIFICACION_PUNTOS = 15;
    private static final int NOTIFICACION_RANKING = 16;
    private static final int JUEGO_INVALIDO = 2;
    private static final int TRIO = 0;
    private static final int ESCALERA = 1;
    private static final int YA_NO_PUEDE_BAJAR = 1;


    public Controlador(ifVista vista) {
        this.vista = vista;
    }

    //jugador---------------------------
    public ArrayList<String> enviarManoJugador(ifJugador jA) throws RemoteException {
        ArrayList<String> manoString = new ArrayList<>();
        try {
            ArrayList<ifCarta> cs = new ArrayList<>(jA.getMano());
            manoString = ifVista.cartasToStringArray(cs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return manoString;
    }

    public ArrayList<ArrayList<String>> enviarJuegosJugador(ifJugador j) {
        ArrayList<ArrayList<Carta>> juegos = j.getJuegos();
        ArrayList<ArrayList<String>> juegosString = new ArrayList<>();
        for (ArrayList<Carta> juego : juegos) {
            ArrayList<ifCarta> cs = new ArrayList<>(juego);
            juegosString.add(ifVista.cartasToStringArray(cs));
        }
        return juegosString;
    }

    public void agregarNuevoJugador(String nombreJugador) throws RemoteException {
        vista.setNombreVista(nombreJugador);
        juego.agregarJugador(new Jugador(nombreJugador));
    }


    //partida-------------------------------------------------------------

    //crea la partida y la inicia si hay al menos 2 jugadores
    public void crearPartida(ifVista vista, int cantJugadores) throws RemoteException {
        juego.crearPartida(vista.getNombreVista(), cantJugadores); //setea partida en juego y en ctrl
    }

    public void guardarPartida() throws RemoteException {
        juego.guardarPartida();
    }

    public void bajarse(ifJugador j, int [] cartasABajar) throws RemoteException {
        int tipoJuego = j.juegoValido(cartasABajar);
        if(tipoJuego != JUEGO_INVALIDO) {
            j.addJuego(cartasABajar);
            j.eliminarDeLaMano(j.getJuegos().get(j.getJuegos().size() - 1));
            j.setPuedeBajar();
            if (tipoJuego == TRIO) {
                j.incrementarTriosBajados();
            } else if (tipoJuego == ESCALERA) {
                j.incrementarEscalerasBajadas();
            }
            vista.mostrarJuegos(enviarJuegosJugador(j));
        } else {
             vista.mostrarNoPuedeBajarJuego(JUEGO_INVALIDO);
        }
    }

    public void acomodarEnJuegoPropio(int iCarta, ifJugador j, ArrayList<ArrayList<String>> juegos, int numJuego) throws RemoteException {
        vista.mostrarJuegos(juegos);
        if(j.acomodarCartaJuegoPropio(iCarta, numJuego, getRonda())) {
            juegos = enviarJuegosJugador(j);
            ArrayList<String> juego = juegos.get(numJuego);
            vista.mostrarCartas(juego);
        } else {
            vista.mostrarNoPuedeAcomodarJuegoPropio();
        }
    }

    public boolean cortarTurno(ifJugador j, int ronda, boolean corte) throws RemoteException {
        if (!corte) {
            int[] triosYEscalerasQueFaltan = j.comprobarQueFaltaParaCortar(ronda);
            vista.mostrarLoQueFaltaParaCortar(triosYEscalerasQueFaltan);
        }
        return corte;
    }

    public int ordenarCartasTurno(ifJugador j, int[] ordenar) throws RemoteException {
        ordenarCartasEnMano(j, ordenar);
        ArrayList<String> mano = enviarManoJugador(j);
        vista.mostrarCartas(mano);
        return vista.menuBajar();
    }

    public void tirarAlPozoTurno(ifJugador j, ArrayList<String> mano) throws RemoteException {
        vista.mostrarCartas(mano);
        int eleccion = vista.preguntarQueBajarParaPozo(mano.size());
        juego.getPartidaActual().agregarAlPozo(j.getCartaFromMano(eleccion));
    }

    public void notificarTurno(int numJugador) throws RemoteException {
        juego.notificarObservadores(juego.getPartidaActual().getJugadoresActuales().get(numJugador));
    }

    public void setTurno(int numJugador, boolean valor) throws RemoteException {
        juego.setTurno(numJugador, valor);
    }

    public void notificarDesarrolloTurno(int numJugador) throws RemoteException {
        juego.notificarDesarrolloTurno(numJugador);
    }
    
    public void desarrolloTurno(ifJugador j) throws RemoteException {
        vista.mostrarCartas(enviarManoJugador(j));
        int eleccion = vista.menuRobar();
        //1 - robar del mazo
        //2 - robar del pozo
        int numJugador = j.getNumeroJugador();

        //si no roba del pozo, los demas pueden hacerlo, con "castigo"
        if (!juego.isPozoEmpty()) {
            if (eleccion != ifVista.getEleccionRobarDelPozo()) {
                boolean puedeRobar;
                int i = 0;
                if (i == numJugador) {
                    i++;
                }
                do {
                    puedeRobar = juego.notificarRoboConCastigo(i, numJugador);
                    i++;
                } while (!puedeRobar && i<getCantJugadoresPartida());
            }
        }

        if (eleccion == ifVista.getEleccionRobarDelPozo()) {
            if(robarDelPozo(j)) {
                vista.mostrarNoPuedeRobarDelPozo();
            }
        }

        //si el pozo esta vacio, se roba del mazo. Si se eligio robar del mazo en un principio tambien sucede aca
        if(eleccion == ifVista.getEleccionRobarDelMazo()) {
            robarDelMazo(j);
        }

        vista.mostrarCartas(enviarManoJugador(j));
//-------------------------------------------------------------------------------------------------------
        while(juego.getTurno(numJugador)) {
            eleccion = vista.menuBajar();
            boolean corte;
            switch (eleccion) {
                case ifVista.ELECCION_ORDENAR_CARTAS:
                    ordenarCartasTurno(getJugadorPartida(numJugador), vista.preguntarParaOrdenarCartas());
                    break;
                case ifVista.ELECCION_ACOMODAR_JUEGO_PROPIO:
                    ArrayList<ArrayList<String>> juegos = enviarJuegosJugador(j);
                    if (!juegos.isEmpty()) {
                        acomodarEnJuegoPropio(vista.preguntarCartaParaAcomodar(), j, juegos, vista.preguntarEnQueJuegoQuiereAcomodar());
                    }
                    break;
                case ifVista.ELECCION_BAJARSE:
                    if (j.getPuedeBajar()) {
                        bajarse(j, vista.preguntarQueBajarParaJuego(vista.preguntarCantParaBajar()));
                    } else {
                        vista.mostrarNoPuedeBajarJuego(YA_NO_PUEDE_BAJAR);
                    }
                    break;
                case ifVista.ELECCION_CORTAR:
                    corte = cortarTurno(j, getRonda(), j.cortar(getRonda()));
                    if (corte) juego.setCorteRonda();
                    break;
                case ifVista.ELECCION_TIRAR_AL_POZO:
                    tirarAlPozoTurno(j, enviarManoJugador(j));
                    juego.setTurno(numJugador, false);
                    break;
            }
            if (j.isManoEmpty()) {
                juego.setTurno(numJugador,false);
            } else {
                vista.mostrarCartas(enviarManoJugador(j));
            }
        }
        System.out.println("Finalizo su turno");
        //juego.finalizoTurno(numJugador, corte);
    }

    public void robarConCastigo(ifJugador j) throws RemoteException {
        robarDelPozo(j);
        robarDelMazo(j);
    }

    public boolean robarDelPozo(ifJugador j) throws RemoteException {
        boolean pozoVacio = juego.isPozoEmpty();
        if (!pozoVacio) {
            j.addCarta(juego.getPartidaActual().eliminarDelPozo());
        }
        return pozoVacio;
    }

    public void robarDelMazo(ifJugador j) throws RemoteException {
        j.addCarta(juego.getPartidaActual().eliminarDelMazo());
    }

    public void haRobadoConCastigo(int numJugador) throws RemoteException {
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

    public void determinarGanador() throws RemoteException {
        juego.determinarGanador();
    }

    public int jugarPartidaRecienIniciada() throws RemoteException {
        int i = 0;
        int inicio = 0;
        boolean encontrado = false;
        Partida p = juego.getPartidaActual();
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

    public int getRonda() throws RemoteException {
        return juego.getPartidaActual().getRonda();
    }

    public boolean getEstadoPartida() throws RemoteException {
        return juego.getPartidaActual().getEstadoPartida();
    }

    public void ordenarCartasEnMano(ifJugador j, int[] ordenar) {
        j.moverCartaEnMano(ordenar[0], ordenar[1]);
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

    //OBSERVER-----------------------------------------------------
    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        juego = (ifJuego) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto modelo, Object cambio) throws RemoteException {
        if (cambio instanceof Integer) {
            int indice = (Integer) cambio;
            if (indice == NOTIFICACION_NUEVO_JUGADOR) {
                vista.actualizar(juego.getJugadores(), indice);
            } else if (indice == NOTIFICACION_NUEVA_PARTIDA) {
                vista.actualizar(juego.getPartidaActual(), NOTIFICACION_NUEVA_PARTIDA);
            }
        } else if (cambio instanceof jugadorActual) { //cuando es el turno de un jugador x
            vista.actualizar(cambio, ((jugadorActual) cambio).getNumeroJugador());
        } else if (cambio instanceof Object[] c) { //serializador
            if ((int)c[1] == NOTIFICACION_RECIBIDA_RANKING) {
                Object[] jugadores = ((Serializador) c[0]).readObjects();
                vista.actualizar(jugadores, NOTIFICACION_RANKING);
            } else if ((int)c[1] == NOTIFICACION_DESARROLLO_TURNO) {
                ifJugador j = (ifJugador) c[0];
                vista.actualizar(j, NOTIFICACION_DESARROLLO_TURNO);
            }
        } else if (cambio instanceof String) {
            vista.actualizar(cambio, NOTIFICACION_NOMBRE_JUGADOR);
        } else if (cambio instanceof int[] cambioA) { //robo con castigo
            if (cambioA[0] == -1) { //para mostrar los puntos de la ronda finalizada
                vista.actualizar(cambio, NOTIFICACION_PUNTOS);
            }
            if (cambioA[1] == NOTIFICACION_PUEDE_ROBO_CASTIGO) {
                vista.actualizar(cambioA, cambioA[1]);
            } else if (cambioA[1] == NOTIFICACION_HUBO_ROBO_CASTIGO) {
                vista.actualizar(cambioA[0], cambioA[1]);
            }
        }
    }

}