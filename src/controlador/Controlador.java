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

    public Controlador(ifVista vista) {
        this.vista = vista;
    }

    //PRIVATE----------------------------------------------------------------------

    //PUBLIC---------------------------------------------------------------------

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
        //ifJugador jA = getJugadorPartida(numJugador);
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
        this.vista.setNombreVista(nombreJugador);
        this.juego.agregarJugador(new Jugador(nombreJugador));
    }

    //cartas------------------------------

    public boolean bajarJuego(ifJugador j, Object[] cartasABajar) throws RemoteException {
        boolean puedeBajar = false;
        if (cartasABajar.length >= 6) {
            int tipoJuego = j.bajarJuego(j.getJuego(cartasABajar)); //comprueba si puede
            if (tipoJuego < 2) {
                puedeBajar = true;
                j.addJuego(j.getJuego(cartasABajar));
                j.eliminarDeLaMano(cartasABajar);
                if (tipoJuego == 0) {
                    j.incrementarTriosBajados();
                } else if(tipoJuego == 1) {
                    j.incrementarEscalerasBajadas();
                }
            }
            //this.juego.cambioPartida((Partida) this.partidaActual); //envia el cambio al modelo
        }
        return puedeBajar;
    }

    public void robarDelMazo(ifJugador j) {
        j.addCarta(this.partidaActual.eliminarDelMazo());
    }

    public boolean robarDelPozo(ifJugador j) {
        boolean pozoVacio = false;
        if (!this.partidaActual.getPozo().isEmpty()) {
            j.addCarta(this.partidaActual.eliminarDelPozo());
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

    //partida------------------

    //crea la partida y la inicia si hay al menos 2 jugadores
    public boolean crearPartida(ifVista vista, int cantJugadores) throws RemoteException {
        return this.juego.crearPartida(vista.getNombreVista(), cantJugadores);
    }

    public void finalizoTurno(int numJugador, boolean corte) throws RemoteException {
        this.juego.finalizoTurno((Partida) this.partidaActual, numJugador, corte);
    }

    public void tirarAlPozo(ifCarta c) {
        this.partidaActual.agregarAlPozo((Carta) c);
    }

    public void bajarse(int numJugador, Object [] cartasABajar) throws RemoteException {
        ifJugador j = getJugadorPartida(numJugador);
        if(!bajarJuego(j, cartasABajar)) {
            this.vista.mostrarNoPuedeBajarJuego();
        } else {
            this.vista.mostrarJuegos(enviarJuegosJugador(j));
        }
    }

    public void acomodarEnJuegoPropio(int iCarta, ifJugador j, ArrayList<ArrayList<String>> juegos, int numJuego) throws RemoteException {
        this.vista.mostrarJuegos(juegos);
        if(j.acomodarCartaJuegoPropio(iCarta, numJuego, getRonda())) {
            juegos = enviarJuegosJugador(j);
            ArrayList<String> juego = juegos.get(numJuego);
            this.vista.mostrarCartas(juego);
        } else {
            this.vista.mostrarNoPuedeAcomodarJuegoPropio();
        }
    }

    public boolean cortarTurno(ifJugador j, int ronda, boolean corte) throws RemoteException {
        if (!corte) {
            int[] triosYEscalerasQueFaltan = j.comprobarQueFaltaParaCortar(ronda);
            this.vista.mostrarLoQueFaltaParaCortar(triosYEscalerasQueFaltan);
        }
        return corte;
    }

    public int ordenarCartasTurno(ifJugador j, int[] ordenar) throws RemoteException {
        ordenarCartasEnMano(j, ordenar);
        ArrayList<String> mano = enviarManoJugador(j);
        this.vista.mostrarCartas(mano);
        return this.vista.menuBajar();
    }

    public void tirarAlPozoTurno(ifJugador j, ArrayList<String> mano) throws RemoteException {
        this.vista.mostrarCartas(mano);
        int eleccion = this.vista.preguntarQueBajarParaPozo(mano.size());
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
        //while (eleccion == ELECCION_ORDENAR_CARTAS)
        //  eleccion = ctrl.ordenarCartasTurno(ctrl.getJugadorPartida(j.getNumeroJugador()), preguntarParaOrdenarCartas());

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
        boolean bajo = false;
        while (eleccion == ifVista.getEleccionBajarse()) {
            if (j.getPuedeBajar()) {
                bajo = true;
                bajarse(j.getNumeroJugador(), vista.preguntarQueBajarParaJuego());
                vista.mostrarCartas(enviarManoJugador(getJugadorPartida(j.getNumeroJugador())));
                eleccion = vista.menuBajar();
            } else {
                vista.mostrarNoPuedeBajarJuego();
            }

        }
        if (bajo)
            j.setPuedeBajar();

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
        if (this.partidaActual != null) { //si ya se llamo a crearPartida()
            while (i < this.partidaActual.getJugadoresActuales().size() && !encontrado) {
                if (this.partidaActual.getJugadoresActuales().get(i).getNombre().equals(nombreVista)) { //vista es la que llama a esta funcion
                    encontrado = true; //significa que el creo la partida, llamo a esta funcion
                    inicio = 1;
                }
                i++;
            }
            if (!encontrado) { //significa que la vista llamo a esta funcion pero no creo la partida
                this.juego.agregarJugadorAPartidaActual(nombreVista);
                if (this.juego.getPartidaActual().getJugadoresActuales().size() == this.partidaActual.getCantJugadoresDeseada()) {
                    this.vista.mostrarInicioPartida();
                    this.juego.iniciarPartida(); //esto inicia el funcionamiento del juego
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
        this.juego.roboConCastigo(nombreJugador);
    }

    public void haRobadoConCastigo(int numJugador, int numJNoPuedeRobar, boolean robo) throws RemoteException {
        this.juego.haRobadoConCastigo(numJugador, numJNoPuedeRobar, robo, (Partida) this.partidaActual);
    }

    public ifJugador getJugadorPartida(int numJugadorPartida) {
        return this.partidaActual.getJugadoresActuales().get(numJugadorPartida);
    }

    public int getRonda() {
        return this.partidaActual.getRonda();
    }

    public boolean getEstadoPartida() {
        return this.partidaActual.getEstadoPartida();
    }

    //OBSERVER-----------------------------------------------------
    public Object getValor(int accion) throws RemoteException {
        Object o = null;
        switch (accion) {
            case 7: { //nuevo jugador agregado
                o = this.juego.getJugadores();
                break;
            }
            case 8: { //jugador ingreso a partida
                o = this.partidaActual.getJugadoresActuales();
                break;
            }
            case 9: {
                o = this.partidaActual.getRonda();
                break;
            }
        }
        return o;
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.juego = (ifJuego) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto modelo, Object cambio) throws RemoteException {
        if (cambio instanceof Integer) { //cuando se agregan jugadores
            int indice = (Integer) cambio;
            vista.actualizar(getValor(indice), indice);
        } else if (cambio instanceof jugadorActual) { //cuando es el turno de un jugador x
            vista.actualizar(cambio, ((jugadorActual) cambio).getNumeroJugador());
        } else if (cambio instanceof Partida) { //cuando se inicia la partida
            vista.actualizar(cambio, 6);
        } else if (cambio instanceof Object[] c) { //serializador
            this.partidaActual = (Partida) ((Serializador) c[0]).readFirstObject(); //setea partida actual
            if (c[1] == null) {
                if (this.partidaActual.getPozo() == null) { // cuando recien se inicia la partida
                    vista.actualizar(this.partidaActual, 6);
                } else {
                    vista.actualizar(this.partidaActual, 9);
                }
            }
        } else if (cambio instanceof String) {
            vista.actualizar(cambio, 10);
        } else if (cambio instanceof int[] cambioA) { //robo con castigo
            if (cambioA[0] == -1) { //para mostrar los puntos de la ronda finalizada
                vista.actualizar(cambio, 15);
            }
            if (cambioA[1] == 11) { //notificar al jugador cambioA[0] que puede robar con castigo
                vista.actualizar(cambioA, cambioA[1]);
            } else if (cambioA[1] == 12 || cambioA[1] == 13) {
                vista.actualizar(cambioA[0], cambioA[1]);
            }
        }
    }

    public void ordenarCartasEnMano(ifJugador j, int[] ordenar) {
        j.moverCartaEnMano(ordenar[0], ordenar[1]);
    }
}