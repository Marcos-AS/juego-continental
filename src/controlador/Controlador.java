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

    public ArrayList<String> enviarMazo(Partida p) {
        ArrayList<Carta> mazo = p.getMazo();
        return ifVista.cartasToStringArray(cartasToIfCarta(mazo));
    }

    public boolean bajarJuego(String nombreJugador, Object[] cartasABajar) throws RemoteException {
        jugadorActual j = this.partidaActual.getJugador(nombreJugador);
        boolean puedeBajar = false;
        if (cartasABajar.length >= 6) {
            if (j.bajarJuego(cartasABajar)) {
                puedeBajar = true;
                j.setPuedeBajar();
            }
        }
        return puedeBajar;
    }

    public void robarDelMazo(ifJugador j) {
        j.addCarta(this.partidaActual.eliminarDelMazo());
    }

    public void robarDelPozo(ifJugador j) {
        j.addCarta(this.partidaActual.eliminarDelPozo());
    }

    public void robarConCastigo(ifJugador j) {
        robarDelPozo(j);
        robarDelMazo(j);
    }

    /*public String enviarPrimeraCartaPozo() {
        Carta c = this.partidaActual.sacarPrimeraDelPozo();

        return carta;
    }*/

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
    public boolean crearPartida(ifVista vista) throws RemoteException {
        return this.juego.crearPartida(vista.getNombreVista());
    }

    public void iniciarPartida() throws RemoteException {
        this.juego.iniciarPartida();
    }

    public void finalizoTurno(int numJugador, boolean corte) throws RemoteException {
        //Serializador srl = new Serializador("partidas.dat");
        //srl.writeOneObject(this.partidaActual);
        //this.juego.finalizoTurno(srl, numJugador, corte);
        this.juego.finalizoTurno((Partida) this.partidaActual, numJugador, corte);
    }

    public void tirarAlPozo(ifCarta c) {
        this.partidaActual.agregarAlPozo((Carta) c);
    }

    public boolean jugarPartidaRecienIniciada(String nombreVista) throws RemoteException {
        int i = 0;
        boolean encontrado = false;
        if (this.partidaActual != null) {
            while (i < this.partidaActual.getJugadoresActuales().size() && !encontrado) {
                if (this.partidaActual.getJugadoresActuales().get(i).getNombre().equals(nombreVista)) {
                    encontrado = true;
                }
                i++;
            }
            if (!encontrado) {
                this.juego.agregarJugadorAPartidaActual(nombreVista);
                this.vista.mostrarInicioPartida();
                this.juego.iniciarPartida();
            }
        }
        return encontrado;
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
        } else if (cambio instanceof Object[] c) {
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
            if (cambioA[1] == 11) {
                vista.actualizar(cambioA, cambioA[1]);
            } else if (cambioA[1] == 12 || cambioA[1] == 13) {
                vista.actualizar(cambioA[0], cambioA[1]);
            }
        }
    }
}