package src.main;

import src.modelo.Partida;
import src.modelo.jugadorActual;
import src.vista.Consola;
import src.controlador.Controlador;

import java.util.ArrayList;
import java.rmi.RemoteException;
import rmimvc.src.RMIMVCException;
import rmimvc.src.cliente.Cliente;

public class AppConsola {

    public static void main(String[] args) {
        Controlador ctrl = new Controlador();    
        Partida partidaNueva = new Partida();
        Consola consola = new Consola(ctrl);
        // Cliente cli = new Cliente(null, 0, null, 0);
        // try {
        //     cli.iniciar(null);
        // } catch (RemoteException e) {
        //     // TODO: handle exception
        // } catch (RMIMVCException e) {
        // }
        String j1 = "AnitaSSJ";
        String j2 = "Marcos";
        int eleccion = 0;
        boolean corte = false;
        partidaNueva.agregarJugador(j1);
        partidaNueva.agregarJugador(j2);

        //reparto de cartas
        partidaNueva.crearMazo();
        partidaNueva.repartirCartas();
        consola.mostrarCartasNombreJugador(j1);
        consola.mostrarCartasJugador(ctrl.enviarManoJugador(partidaNueva, j1));
        consola.mostrarCartasNombreJugador(j2);
        consola.mostrarCartasJugador(ctrl.enviarManoJugador(partidaNueva, j2));            

        ArrayList<jugadorActual> jugadoresActuales = partidaNueva.getJugadoresActuales();
        ArrayList<String> mano = null;

        //empiezan las rondas
        while (partidaNueva.getRonda()<=partidaNueva.getTotalRondas()) {
            int i = 0;
            while (!corte) {
                try {
                    consola.mostrarCombinacionRequerida(partidaNueva.getRonda());
                    consola.mostrarPozo(ctrl.enviarPrimeraCartaPozo(partidaNueva));
                    jugadorActual j = jugadoresActuales.get(i);
                    consola.mostrarTurnoJugador(j.getNombre());
                    mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                    consola.mostrarCartasJugador(mano);                    
                    
                    //robar
                    eleccion = consola.menuRobar();
                    //si no roba del pozo, los demas pueden hacerlo, con "castigo"
                    if (eleccion != consola.getEleccionRobarDelPozo()) {
                        ArrayList<jugadorActual> jugadoresRoboCastigo = new ArrayList<>();
                        jugadoresRoboCastigo.addAll(jugadoresActuales);
                        jugadoresRoboCastigo.remove(i);
                        jugadorActual jugadorR = null;
                        int k;
                        int l = 0;
                        int eleccionR = eleccion;
                        while (eleccionR != consola.getEleccionRobarDelPozo() && l < jugadoresRoboCastigo.size()) {
                            k = i+1;
                            if (k > jugadoresRoboCastigo.size()-1) k = 0;
                            jugadorR = jugadoresRoboCastigo.get(k);                            
                            consola.jugadorPuedeRobarConCastigo(jugadorR.getNombre());
                            eleccionR = consola.menuRobarDelPozo();
                            l++;
                        }
                        if (eleccionR == consola.getEleccionRobarDelPozo()) {
                            jugadorActual jugador = partidaNueva.getJugador(jugadorR.getNombre());
                            jugador.robarConCastigo();
                            mano = ctrl.enviarManoJugador(partidaNueva, jugador.getNombre());
                            consola.mostrarCartasJugador(mano);  
                        }                  
                        consola.mostrarContinuaTurno(j.getNombre());
                    } 
                    if(!j.eleccionMenuRobo(eleccion)) {
                        consola.mostrarNoPuedeRobarDelPozo();
                        j.eleccionMenuRobo(consola.getEleccionRobarDelMazo());
                    }
                    mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());            
                    consola.mostrarCartasJugador(mano);                    
    
                    eleccion = consola.menuBajar();

                    //ordenar cartas en la mano
                    while (eleccion == consola.getEleccionOrdenarCartas()) {
                        int[] ordenar = consola.preguntarParaOrdenarCartas();
                        j.eleccionOrdenar(ordenar);
                        mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                        consola.mostrarCartasJugador(mano);                    
                        eleccion = consola.menuBajar();
                    }
                    
                    //bajarse
                    while (eleccion == consola.getEleccionBajarse()) {
                        Object [] cartasABajar = consola.preguntarQueBajarParaJuego();
                        if(!ctrl.bajarJuego(j, cartasABajar)) {
                            consola.mostrarNoPuedeBajarJuego();
                        } else {
                            ArrayList<ArrayList<String>> juegos = ctrl.enviarJuegosJugador(j);
                            int numJuego = 1;
                            for (ArrayList<String> juego : juegos) {
                                consola.mostrarJuego(numJuego);
                                consola.mostrarCartasJugador(juego);
                                numJuego++;
                            }
                        }
                        mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                        consola.mostrarCartasJugador(mano);
                        eleccion = consola.menuBajar();
                    }

                    //si quiere cortar, comprobar si puede
                    if (eleccion == consola.getEleccionCortar()) {
                        corte = ctrl.cortar(partidaNueva, j);
                        if (!corte) {
                            int[] triosYEscalerasQueFaltan = j.comprobarQueFaltaParaCortar();
                            consola.mostrarLoQueFaltaParaCortar(triosYEscalerasQueFaltan);
                        }
                    }         
                    //tirar
                    if (!corte) {
                        mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                        consola.mostrarCartasJugador(mano);                    
                        eleccion = consola.preguntarQueBajarParaPozo(mano.size());
                        j.tirarAlPozo(eleccion);
                    }
                    i++;
                    if (i>jugadoresActuales.size()-1) i = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//while ronda
            partidaNueva.resetearJuegosJugadores();
            partidaNueva.sumarPuntos();
            int[] puntos = partidaNueva.getPuntosJugadores();
            int m = 0;
            for (jugadorActual j : jugadoresActuales) {
                consola.mostrarPuntosJugador(j.getNombre(), puntos[m]);
                m++;
            }
        }//while partida
        String ganador = partidaNueva.determinarGanador();
        consola.mostrarGanador(ganador);
    }
}

