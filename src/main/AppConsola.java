package src.main;

import src.modelo.Partida;
import src.modelo.jugadorActual;
import src.vista.Consola;
import java.util.ArrayList;

import src.controlador.Controlador;

public class AppConsola {

    public static void main(String[] args) {
        Controlador ctrl = new Controlador();    
        Partida partidaNueva = new Partida();
        Consola consola = new Consola(ctrl);
        String j1 = "AnitaSSJ";
        String j2 = "Marcos";
        int eleccion = 0;
        boolean corte = false;
        partidaNueva.agregarJugador(j1);
        partidaNueva.agregarJugador(j2);
        try {
            partidaNueva.crearMazo();
            partidaNueva.repartirCartas();
            Thread.sleep(300);
            consola.mostrarCartasNombreJugador(j1);
            consola.mostrarCartasJugador(ctrl.enviarManoJugador(partidaNueva, j1));
            Thread.sleep(300);
            consola.mostrarCartasNombreJugador(j2);
            consola.mostrarCartasJugador(ctrl.enviarManoJugador(partidaNueva, j2));            
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<jugadorActual> jugadoresActuales = partidaNueva.getJugadoresActuales();
        ArrayList<String> mano = null;
        //empiezan las rondas
        while (partidaNueva.getRonda()<partidaNueva.getTotalRondas()) {
            int i = 0;

            while (!corte) {
                try {
                    consola.mostrarPozo(ctrl.enviarPrimeraCartaPozo(partidaNueva));
                    jugadorActual j = jugadoresActuales.get(i);
                    consola.mostrarTurnoJugador(j.getNombre());
                    mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                    Thread.sleep(300);
                    consola.mostrarCartasJugador(mano);                    
                    
                    //robar
                    eleccion = consola.menuRobar();
                    j.eleccionMenuRobo(eleccion);
                    mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                    Thread.sleep(300);            
                    consola.mostrarCartasJugador(mano);                    

                    //BAJARSE, ORDENAR O CORTAR
                    eleccion = consola.menuBajar();

                    //ordenar cartas en la mano
                    while (eleccion == consola.getEleccionOrdenarCartas()) {
                        int[] ordenar = consola.preguntarParaOrdenarCartas();
                        j.eleccionOrdenar(ordenar);
                        mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                        Thread.sleep(300);                
                        consola.mostrarCartasJugador(mano);                    
                        eleccion = consola.menuBajar();
                    }
                    
                    //bajarse
                    if (eleccion == consola.getEleccionBajarse()) {
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
                    }
                    //si quiere cortar, comprobar si puede
                    if (eleccion == consola.getEleccionCortar()) {
                        corte = ctrl.cortar(partidaNueva, j);
                        if (!corte) {
                            int[] triosYEscalerasQueFaltan = j.comprobarQueFaltaParaCortar();
                            consola.mostrarLoQueFaltaParaCortar(triosYEscalerasQueFaltan);
                            //ctrl.tratarDeBajarParaCortar(triosYEscalerasQueFaltan);
                        }
                    }         
                    //tirar
                    if (!corte) {
                        mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                        consola.mostrarCartasJugador(mano);                    
                        eleccion = consola.preguntarQueBajarParaPozo();
                        j.tirarAlPozo(eleccion);
                    }
                    i++;
                    if (i>jugadoresActuales.size()-1) i = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//while ronda
            partidaNueva.resetearJuegosJugadores();
        }//while partida
    }
}

