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

    public ArrayList<String> enviarManoJugador(ifJugador jA) throws RemoteException {
        ArrayList<String> manoString = new ArrayList<>();
        try {
            ArrayList<Carta> mano = jA.getMano();
            manoString = ifVista.cartasToStringArray(mano);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return manoString;
    }

    public ArrayList<ArrayList<String>> enviarJuegosJugador(ifJugador j) {
        ArrayList<ArrayList<Carta>> juegos = j.getJuegos();
        ArrayList<ArrayList<String>> juegosString = new ArrayList<>();
        for (ArrayList<Carta> juego : juegos) {
            juegosString.add(ifVista.cartasToStringArray(juego));
        }
        return juegosString;
    }

    public void agregarNuevoJugador(String nombreJugador) throws RemoteException {
        this.juego.agregarJugador(new Jugador(nombreJugador));
    }

    //cartas------------------------------

    public ArrayList<String> enviarMazo(Partida p) {
        ArrayList<Carta> mazo = p.getMazo();
        return ifVista.cartasToStringArray(mazo);
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
        j.addCarta(this.partidaActual.sacarPrimeraDelMazo());
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
    public void crearPartida() throws RemoteException {
        //Serializador srl = new Serializador("partidas.dat");
        this.juego.crearPartida();
        //srl.writeOneObject(p);
        //this.juego.setPartidaActual(srl); //setea part. actual en clase juego
        //p = this.juego.getPartidaActual();
    }

//    public void desarrolloTurno(jugadorActual j, int indVista) throws RemoteException {
//
//        this.partidaActual.avisarTurno(j);
//    }


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
            this.vista.setNumVista(((jugadorActual) cambio).getNumeroJugador());
            vista.actualizar(cambio, ((jugadorActual) cambio).getNumeroJugador());
        } else if (cambio instanceof Partida) { //cuando se inicia la partida
            vista.actualizar(cambio,6);
        } else if (cambio instanceof Serializador) {
            this.partidaActual = (Partida)((Serializador) cambio).readFirstObject();
            if (this.partidaActual.getPozo() == null) {
                vista.actualizar(this.partidaActual, 6);
            } else {
                vista.actualizar(this.partidaActual, 9);
            }
        }
    }
}