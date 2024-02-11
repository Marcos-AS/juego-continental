package src.main;

import rmimvc.src.RMIMVCException;
import rmimvc.src.Util;
import rmimvc.src.cliente.Cliente;
import src.controlador.Controlador;
import src.modelo.Partida;
import src.modelo.jugadorActual;
import src.serializacion.Serializador;
import src.vista.Consola;
import src.vista.VentanaInicio;
import src.vista.ifVista;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppCliente {

    public static void main(String[] args) {
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el cliente",
                "IP del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el cliente",
                "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );
        String ipServer = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que se ejecuta el servidor",
                "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );
        String portServer = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escucha peticiones el servidor",
                "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );

        //ifVista vista = new VentanaInicio(500, 700);
        ifVista vista = new Consola();
        Controlador ctrl = new Controlador(vista);
        vista.setControlador(ctrl);
        Cliente c = new Cliente(ip, Integer.parseInt(port), ipServer, Integer.parseInt(portServer));
        try {
            //se agrega el ctrl como observador y se setea el modelo como atributo del ctrl
            c.iniciar(ctrl);
            bienvenida(vista);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (RMIMVCException e) {
            e.printStackTrace();
        }
    }

    private static void bienvenida(ifVista vista) throws RemoteException {
        vista.preguntarNombreNuevoJugador();
        int eleccion = vista.menuBienvenida();
        while (eleccion != -1) {
            switch (eleccion) {
                case 1: {
                    iniciarPartidaNueva(vista);
                    break;
                }
                case 2: {
                    continuarPartida();
                    break;
                }
                case 3: {
                    ranking();
                    break;
                }
                case 4: {
                    vista.mostrarReglas();
                    break;
                }
            }
            eleccion = vista.menuBienvenida();
        }
    }

    private static void iniciarPartidaNueva(ifVista vista) throws RemoteException {
        Partida partidaNueva = new Partida();
        Serializador srl = new Serializador("jugadores.dat");
        partidaNueva.agregarJugadores();
        //reparto de cartas
        partidaNueva.crearMazo();
        partidaNueva.repartirCartas();


        ArrayList<jugadorActual> jugadoresActuales = partidaNueva.getJugadoresActuales();
        ArrayList<String> mano;
        int eleccion;
        boolean corte = false;
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

    private static void continuarPartida() {}

    private static void ranking() {}
}
