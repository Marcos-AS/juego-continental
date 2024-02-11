package src.controlador;

import java.rmi.RemoteException;
import java.util.ArrayList;

import rmimvc.src.observer.IObservableRemoto;
import src.modelo.*;
import rmimvc.src.cliente.IControladorRemoto;
import src.vista.ifVista;


public class Controlador implements IControladorRemoto {
    ifJuego juego;
    ifVista vista;
    public Controlador(ifVista vista) {
        this.vista = vista;
    }

    //PRIVATE----------------------------------------------------------------------
    private String transformarNumCarta(int numCarta) {
        String num = ((Integer) numCarta).toString();
        if(numCarta <= 1 || numCarta >= 11) { 
            switch(num) {
                case "1":
                    num = "A";
                    break;
                case "11":
                    num = "J";
                    break;
                case "12":
                    num = "Q";
                    break;
                case "13":
                    num = "K";
                    break;
                case "-1":
                    num = "COMODIN";
                    break;
            }
        }
        return num;
    }  

    private ArrayList<String> cartasToStringArray(ArrayList<Carta> mano) {
        ArrayList<String> manoString = new ArrayList<>();
        for (Carta c : mano) {
    		String numString = transformarNumCarta(c.getNumero());
            Palo palo = c.getPalo();
            String carta = "";
            if (palo == Palo.COMODIN) {
                carta = "COMODIN";
            } else {
                carta = numString + " de " + palo;
            }
            manoString.add(carta);
        }
        return manoString;
    }

    //PUBLIC---------------------------------------------------------------------

    //jugador---------------------------

    public ArrayList<String> enviarManoJugador(String nombreJugador) throws RemoteException {
        ArrayList<String> manoString = new ArrayList<>();
        try {
            //Jugador j = Juego.getJugador(nombreJugador);
            //Partida p = j.getPartidaActiva();
            //jugadorActual jA = p.getJugador(nombreJugador);
            //ArrayList<Carta> mano = jA.getMano();
            //manoString = cartasToStringArray(mano);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return manoString;
    }

    public ArrayList<ArrayList<String>> enviarJuegosJugador(Partida p, String nombreJugador) {
        jugadorActual j = p.getJugador(nombreJugador);
        ArrayList<ArrayList<Carta>> juegos = j.getJuegos();
        ArrayList<ArrayList<String>> juegosString = new ArrayList<>();
        for (ArrayList<Carta> juego : juegos) {
            juegosString.add(cartasToStringArray(juego));
        }
        return juegosString;
    }

    public void agregarNuevoJugador(String nombreJugador) throws RemoteException {
        this.juego.agregarJugador(new Jugador(nombreJugador));
    }


    //cartas------------------------------

    public ArrayList<String> enviarMazo(Partida p) {
        ArrayList<Carta> mazo = p.getMazo();
        return cartasToStringArray(mazo);
    }

    public boolean bajarJuego(Partida p, String nombreJugador, Object[] cartasABajar) throws RemoteException {
        jugadorActual j = p.getJugador(nombreJugador);
        boolean puedeBajar = false;
        if (cartasABajar.length >= 6) {
            if(j.bajarJuego(cartasABajar)) {
                puedeBajar = true;
                j.setPuedeBajar();
            }
        }
        return puedeBajar;
    }

    public String enviarPrimeraCartaPozo(Partida p) {
        Carta c = p.sacarPrimeraDelPozo();
        String carta = transformarNumCarta(c.getNumero()) + " de " + c.getPalo().name();
        return carta;
    }

    public int transformarLetraCarta(String letraCarta) {
        int numCarta = 0;
        switch(letraCarta) {
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

    public Object getValor(int accion) throws RemoteException {
        Object o = null;
        switch (accion) {
            case 1: { //nuevo jugador agregado
                o = this.juego.getJugadores();
                break;
            }
        }
        return o;
    }

    //OBSERVER----------------------------------------------
    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.juego = (ifJuego) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto modelo, Object cambio) throws RemoteException {
        if (cambio instanceof Integer) {
            int indice = (Integer) cambio;
            vista.actualizar(getValor(indice), indice);
        }
    }
}