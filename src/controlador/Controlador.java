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
    ifPartida partidaActual;
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

    public void robarDelMazo(ifJugador j) {
        j.addCarta(partidaActual.eliminarDelMazo());
    }

    public boolean robarDelPozo(ifJugador j) {
        boolean pozoVacio = false;
        if (!partidaActual.getPozo().isEmpty()) {
            j.addCarta(partidaActual.eliminarDelPozo());
        } else {
            pozoVacio = true;
        }
        return pozoVacio;
    }

    public void robarConCastigo(ifJugador j) {
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
    public boolean crearPartida(ifVista vista, int cantJugadores) throws RemoteException {
        return juego.crearPartida(vista.getNombreVista(), cantJugadores);
    }

    public void finalizoTurno(int numJugador, boolean corte) throws RemoteException {
        juego.finalizoTurno((Partida) partidaActual, numJugador, corte);
    }

    public void tirarAlPozo(ifCarta c) {
        partidaActual.agregarAlPozo((Carta) c);
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

    public void desarrolloTurno(ifJugador jA) throws RemoteException {
        ifJugador j = getJugadorPartida(jA.getNumeroJugador()); //con esto obtengo el objeto jugador que mantiene actualizado al ctrl
        //System.out.println("Nombre de la vista: " + this.nombreVista);
        ArrayList<String> mano;
        mano = enviarManoJugador(j);
        vista.mostrarCartas(mano);
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

        mano = enviarManoJugador(j);
        vista.mostrarCartas(mano);

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
                mano = enviarManoJugador(j);
                vista.mostrarCartas(mano);
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
        if (eleccion == ifVista.getEleccionCortar())
            corte = cortarTurno(j, getRonda(), j.cortar(getRonda()));

        //tirar
        if (!corte)
            tirarAlPozoTurno(j, enviarManoJugador(j));
        System.out.println("Finalizo su turno");
        finalizoTurno(j.getNumeroJugador(), corte);
    }

    public int jugarPartidaRecienIniciada(String nombreVista) throws RemoteException {
        int i = 0;
        int inicio = 0;
        boolean encontrado = false;
        if (partidaActual != null) { //si ya se llamo a crearPartida()
            while (i < partidaActual.getJugadoresActuales().size() && !encontrado) {
                if (partidaActual.getJugadoresActuales().get(i).getNombre().equals(nombreVista)) { //vista es la que llama a esta funcion
                    encontrado = true; //significa que el creo la partida, llamo a esta funcion
                    inicio = 1;
                }
                i++;
            }
            if (!encontrado) { //significa que la vista llamo a esta funcion pero no creo la partida
                juego.agregarJugadorAPartidaActual(nombreVista);
                if (juego.getPartidaActual().getJugadoresActuales().size() == this.partidaActual.getCantJugadoresDeseada()) {
                    juego.nuevaVentana(); //para gui
                    vista.mostrarInicioPartida(); //para consola
                    juego.iniciarPartida(); //esto inicia el funcionamiento del juego
                    inicio = 2; //indica partida finalizada
                } else {
                    inicio = 1;
                }
            }
        }
        return inicio;
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
        juego.haRobadoConCastigo(numJugador, numJNoPuedeRobar, robo, (Partida) this.partidaActual);
    }

    public ifJugador getJugadorPartida(int numJugadorPartida) {
        return partidaActual.getJugadoresActuales().get(numJugadorPartida);
    }

    public int getRonda() {
        return partidaActual.getRonda();
    }

    public boolean getEstadoPartida() {
        return partidaActual.getEstadoPartida();
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
            } else {
                vista.actualizar(null, indice);
            }
        } else if (cambio instanceof jugadorActual) { //cuando es el turno de un jugador x
            vista.actualizar(cambio, ((jugadorActual) cambio).getNumeroJugador());
        } else if (cambio instanceof Object[] c) { //serializador
            if (c[1] == null) {
                partidaActual = (Partida) ((Serializador) c[0]).readFirstObject(); //setea partida actual
                if (partidaActual.getPozo() == null) {
                    vista.actualizar(partidaActual, NOTIFICACION_NUEVA_PARTIDA);
                } else {
                    vista.actualizar(partidaActual, NOTIFICACION_RONDA_POZO);
                }
            } else if ((int)c[1] == NOTIFICACION_RECIBIDA_RANKING) {
                Object[] jugadores = ((Serializador) c[0]).readObjects();
                vista.actualizar(jugadores, NOTIFICACION_RANKING);
            } else {
                partidaActual = (Partida) ((Serializador) c[0]).readFirstObject();
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