package src.main;

import src.modelo.Partida;
import src.modelo.jugadorActual;
import src.vista.Consola;

import java.util.ArrayList;
//import java.rmi.RemoteException;
//import rmimvc.src.RMIMVCException;
//import rmimvc.src.cliente.Cliente;

public class AppConsola {

    public static void main(String[] args) {
        ejecutarPartida();
    }

    private static void ejecutarPartida() {
        Partida partidaNueva = new Partida();
        Consola consola = new Consola();
        // Cliente cli = new Cliente(null, 0, null, 0);
        // try {
        //     cli.iniciar(null);
        // } catch (RemoteException e) {
        //     // TODO: handle exception
        // } catch (RMIMVCException e) {
        // }
        String j1 = "Juan";
        String j2 = "Marcos";
        int eleccion;
        boolean corte = false;
        partidaNueva.agregarJugador(j1);
        partidaNueva.agregarJugador(j2);

        //reparto de cartas
        partidaNueva.crearMazo();
        partidaNueva.repartirCartas();
        consola.mostrarCartasNombreJugador(j1);
        consola.mostrarCartasJugador(j1);
        consola.mostrarCartasNombreJugador(j2);
        consola.mostrarCartasJugador(j2);

        ArrayList<jugadorActual> jugadoresActuales = partidaNueva.getJugadoresActuales();
        ArrayList<String> mano;

        //empiezan las rondas
        while (partidaNueva.getRonda()<=partidaNueva.getTotalRondas()) {
            int i = 0;
            while (!corte) {
                try {
                    consola.mostrarCombinacionRequerida(partidaNueva.getRonda());
                    consola.mostrarPozo(partidaNueva);
                    jugadorActual j = jugadoresActuales.get(i);
                    consola.mostrarTurnoJugador(j.getNombre());
                    mano = consola.getCartasJugador(j.getNombre());
                    consola.mostrarCartas(mano);
                    
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
                            mano = consola.getCartasJugador(jugador.getNombre());
                            consola.mostrarCartas(mano);
                        }                  
                        consola.mostrarContinuaTurno(j.getNombre());
                    } 
                    //si el pozo esta vacio, se roba del mazo. Si se eligio robar del mazo en un principio tambien sucede aca
                    if(!j.eleccionMenuRobo(eleccion)) {
                        consola.mostrarNoPuedeRobarDelPozo();
                        j.eleccionMenuRobo(consola.getEleccionRobarDelMazo());
                    }
                    mano = consola.getCartasJugador(j.getNombre());
                    consola.mostrarCartas(mano);
    
                    eleccion = consola.menuBajar();//-------------------------------

                    //ordenar cartas en la mano
                    while (eleccion == consola.getEleccionOrdenarCartas()) {
                        int[] ordenar = consola.preguntarParaOrdenarCartas();
                        j.eleccionOrdenar(ordenar);
                        mano = consola.getCartasJugador(j.getNombre());
                        consola.mostrarCartas(mano);
                        eleccion = consola.menuBajar();
                    }

                    //acomodar en un juego
                    while (eleccion == consola.getEleccionAcomodarJuegoPropio()) {
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
                    }
                    
                    //bajarse
                    while (eleccion == consola.getEleccionBajarse()) {
                        Object [] cartasABajar = consola.preguntarQueBajarParaJuego();
                        consola.bajarse(partidaNueva, j.getNombre(), cartasABajar);
                        mano = consola.getCartasJugador(j.getNombre());
                        consola.mostrarCartas(mano);
                        eleccion = consola.menuBajar();
                    }


                    //si quiere cortar, comprobar si puede
                    if (eleccion == consola.getEleccionCortar()) {
                        corte = j.cortar(partidaNueva);
                        if (!corte) {
                            int[] triosYEscalerasQueFaltan = j.comprobarQueFaltaParaCortar();
                            consola.mostrarLoQueFaltaParaCortar(triosYEscalerasQueFaltan);
                        }
                    }         
                    //tirar
                    if (!corte) {
                        mano = consola.getCartasJugador(j.getNombre());
                        consola.mostrarCartas(mano);
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