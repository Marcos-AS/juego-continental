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
    private static final int NOTIFICACION_NUEVO_JUGADOR = 7;
    private static final int NOTIFICACION_NUEVA_PARTIDA = 6;
    private static final int NOTIFICACION_RONDA_POZO = 9;
    private static final int NOTIFICACION_NOMBRE_JUGADOR = 10;
    private static final int NOTIFICACION_PUEDE_ROBO_CASTIGO = 11;
    private static final int NOTIFICACION_HUBO_ROBO_CASTIGO = 12;
    private static final int NOTIFICACION_PUNTOS = 15;
    private static final int NOTIFICACION_RANKING = 16;


    public Controlador(ifVista vista) {
        this.vista = vista;
    }

    //jugador---------------------------

    private ArrayList<ifCarta> cartasToIfCarta(ArrayList<Carta> cartas) {
        ArrayList<ifCarta> cs = new ArrayList<ifCarta>();
        for (Carta c : cartas) {
            cs.add((ifCarta) c);
        }
        return cs;
    }

    public ArrayList<String> enviarManoJugador(ifJugador jA) throws RemoteException {
        ArrayList<String> manoString = new ArrayList<>();
        try {
            ArrayList<Carta> mano = jA.getMano();
            manoString = ifVista.cartasToStringArray(cartasToIfCarta(mano));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return manoString;
    }

    public ArrayList<ArrayList<String>> enviarJuegosJugador(ifJugador j) {
        ArrayList<ArrayList<Carta>> juegos = j.getJuegos();
        ArrayList<ArrayList<String>> juegosString = new ArrayList<>();
        for (ArrayList<Carta> juego : juegos) {
            juegosString.add(ifVista.cartasToStringArray(cartasToIfCarta(juego)));
        }
        return juegosString;
    }

    public void agregarNuevoJugador(String nombreJugador) throws RemoteException {
        vista.setNombreVista(nombreJugador);
        juego.agregarJugador(new Jugador(nombreJugador));
    }

    //cartas----------------------------------------------------------------------------

    public boolean bajarJuego(ifJugador j, int[] cartasABajar) throws RemoteException {
        boolean puedeBajar = false;
        if (cartasABajar.length >= 3) {
            int tipoJuego = j.bajarJuego(j.getJuego(cartasABajar)); //comprueba si puede
            if (tipoJuego < 2) {
                puedeBajar = true;
                j.addJuego(j.getJuego(cartasABajar));
                j.eliminarDeLaMano(j.getJuegos().get(j.getJuegos().size()-1));
                if (tipoJuego == 0) {
                    j.incrementarTriosBajados();
                } else if(tipoJuego == 1) {
                    j.incrementarEscalerasBajadas();
                }
            }
        }
        return puedeBajar;
    }

    public void robarDelMazo(ifJugador j) throws RemoteException {
        j.addCarta(juego.getPartidaActual().eliminarDelMazo());
    }

    public boolean robarDelPozo(ifJugador j) throws RemoteException {
        boolean pozoVacio = false;
        if (!juego.getPartidaActual().getPozo().isEmpty()) {
            j.addCarta(juego.getPartidaActual().eliminarDelPozo());
        } else {
            pozoVacio = true;
        }
        return pozoVacio;
    }

    public void robarConCastigo(ifJugador j) throws RemoteException {
        robarDelPozo(j);
        robarDelMazo(j);
    }

    public int transformarLetraCarta(String letraCarta) {
        int numCarta = 0;
        switch (letraCarta) {
            case "J":
                numCarta = 11;
                break;
            case "Q":
                numCarta = 12;
                break;
            case "K":
                numCarta = 13;
                break;
            case "A":
                numCarta = 1;
                break;
            case "COMODIN":
                numCarta = -1;
                break;
        }
        return numCarta;
    }

    //partida-------------------------------------------------------------

    //crea la partida y la inicia si hay al menos 2 jugadores
    public void crearPartida(ifVista vista, int cantJugadores) throws RemoteException {
        juego.crearPartida(vista.getNombreVista(), cantJugadores); //setea partida en juego y en ctrl
    }

    public void finalizoTurno(int numJugador, boolean corte) throws RemoteException {
        juego.finalizoTurno(numJugador, corte);
    }

    public void tirarAlPozo(ifCarta c) throws RemoteException {
        juego.getPartidaActual().agregarAlPozo((Carta) c);
    }

    public void bajarse(int numJugador, int [] cartasABajar) throws RemoteException {
        ifJugador j = getJugadorPartida(numJugador);
        if(bajarJuego(j, cartasABajar)) {
            j.setPuedeBajar();
            vista.mostrarJuegos(enviarJuegosJugador(j));
        } else {
            vista.mostrarNoPuedeBajarJuego(1);
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
        this.vista.mostrarCartas(mano);
        int eleccion = vista.preguntarQueBajarParaPozo(mano.size());
        ifCarta c = j.getCartaParaTirarAlPozo(eleccion);
        tirarAlPozo(c);
    }

    public ifJugador notificarTurno(int numJugador) throws RemoteException {
        return (ifJugador) juego.notificarTurno(numJugador);
    }

    public void desarrolloTurno(int iJugador) throws RemoteException {
        ifJugador j = getJugadorPartida(iJugador);
        //System.out.println("Nombre de la vista: " + this.nombreVista);
        vista.mostrarCartas(enviarManoJugador(j));
        int eleccion = vista.menuRobar();

        //si no roba del pozo, los demas pueden hacerlo, con "castigo"
        if (eleccion != ifVista.getEleccionRobarDelPozo()) {
            roboConCastigo(vista.getNombreVista());
        }

        if (eleccion == ifVista.getEleccionRobarDelPozo()) {
            if(!robarDelPozo(j)) {
                vista.mostrarNoPuedeRobarDelPozo();
            }
        }

        //si el pozo esta vacio, se roba del mazo. Si se eligio robar del mazo en un principio tambien sucede aca
        if(eleccion == ifVista.getEleccionRobarDelMazo()) {
            robarDelMazo(j);
        }

        vista.mostrarCartas(enviarManoJugador(j));

        eleccion = vista.menuBajar();
        boolean corte = false;

        //ordenar cartas en la mano
        while (eleccion == ifVista.getEleccionOrdenarCartas())
          eleccion = ordenarCartasTurno(getJugadorPartida(j.getNumeroJugador()), vista.preguntarParaOrdenarCartas());

        //acomodar en un juego
        while (eleccion == ifVista.getEleccionAcomodarJuegoPropio()) {
            ArrayList<ArrayList<String>> juegos = enviarJuegosJugador(j);
            if (!juegos.isEmpty()) {
                acomodarEnJuegoPropio(vista.preguntarCartaParaAcomodar(), j, juegos, vista.preguntarEnQueJuegoQuiereAcomodar());
                vista.mostrarCartas(enviarManoJugador(j));
                eleccion = vista.menuBajar();
            }
        }

        //bajarse
        while (eleccion == ifVista.getEleccionBajarse()) {
            if (j.getPuedeBajar()) {
                bajarse(j.getNumeroJugador(), vista.preguntarQueBajarParaJuego(vista.preguntarCantParaBajar()));
                vista.mostrarCartas(enviarManoJugador(getJugadorPartida(j.getNumeroJugador())));
                eleccion = vista.menuBajar();
            } else {
                vista.mostrarNoPuedeBajarJuego(2);
            }

        }

        //si quiere cortar, comprobar si puede
        if (eleccion == ifVista.getEleccionCortar()) {
            corte = cortarTurno(j, getRonda(), j.cortar(getRonda()));
            if (corte) juego.setCorteRonda();
        }

        //tirar
        if (!corte)
            tirarAlPozoTurno(j, enviarManoJugador(j));
        System.out.println("Finalizo su turno");
        finalizoTurno(j.getNumeroJugador(), corte);
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

    public boolean iniciarPartida() throws RemoteException {
        return vista.partida();
    }

    private void setRondaInicial() throws RemoteException {
        juego.getPartidaActual().setRonda(1);
    }

    public int getTotalRondas() throws RemoteException {
        return juego.getPartidaActual().getTotalRondas();
    }

    public void iniciarCartasPartida() throws RemoteException {
        juego.iniciarCartasPartida();
    }

    public void desarrolloRoboConCastigo(ArrayList<String> mano, ifJugador j, int numJNoPuedeRobar) throws RemoteException {
        vista.mostrarCartas(mano);
        vista.jugadorPuedeRobarConCastigo(j.getNombre());
        if (vista.menuRobarDelPozo() == ifVista.getEleccionRobarDelPozo()) {
            robarConCastigo(j);
            haRobadoConCastigo(j.getNumeroJugador(), numJNoPuedeRobar, true);
        } else {
            haRobadoConCastigo(j.getNumeroJugador(),numJNoPuedeRobar,false);
        }
    }

    public void roboConCastigo(String nombreJugador) throws RemoteException {
        juego.roboConCastigo(nombreJugador);
    }

    public void haRobadoConCastigo(int numJugador, int numJNoPuedeRobar, boolean robo) throws RemoteException {
        juego.haRobadoConCastigo(numJugador, numJNoPuedeRobar, robo);
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

    public String getNombreVista() {
        return vista.getNombreVista();
    }

    public void ordenarCartasEnMano(ifJugador j, int[] ordenar) {
        j.moverCartaEnMano(ordenar[0], ordenar[1]);
    }

    public void getRanking() throws RemoteException {
        juego.getRanking();
    }

    public void mostrarPozo() throws RemoteException {
        vista.mostrarPozo(juego.getPartidaActual().eliminarDelMazo());
    }

    public int getCantJugadoresPartida() throws RemoteException {
        return juego.getCantJugadores();
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
            } else if (indice == NOTIFICACION_RONDA_POZO) {
                vista.actualizar(juego.getPartidaActual(), NOTIFICACION_RONDA_POZO);
            }
        } else if (cambio instanceof jugadorActual) { //cuando es el turno de un jugador x
            vista.actualizar(cambio, ((jugadorActual) cambio).getNumeroJugador());
        } else if (cambio instanceof Object[] c) { //serializador
            if ((int)c[1] == NOTIFICACION_RECIBIDA_RANKING) {
                Object[] jugadores = ((Serializador) c[0]).readObjects();
                vista.actualizar(jugadores, NOTIFICACION_RANKING);
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