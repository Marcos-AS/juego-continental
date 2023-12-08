package src.main;

import java.util.ArrayList;

import src.controlador.Controlador;
import src.modelo.Partida;
import src.modelo.jugadorActual;
import src.vista.VentanaInicio;

public class AppGui {
    private static final String NOMBRE_VENTANA = "Continental";
    public static void main(String[] args) {
        Controlador ctrl = new Controlador();    
        Partida partidaNueva = new Partida();
        VentanaInicio gui = new VentanaInicio();
        gui.iniciarVentana(gui, NOMBRE_VENTANA);

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
            //gui.mostrarCartasNombreJugador(j1);
            //gui.mostrarCartasJugador(ctrl.enviarManoJugador(partidaNueva, j1));
            Thread.sleep(300);
            //gui.mostrarCartasNombreJugador(j2);
            //gui.mostrarCartasJugador(ctrl.enviarManoJugador(partidaNueva, j2));            
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
                    //gui.mostrarPozo(ctrl.enviarPrimeraCartaPozo(partidaNueva));
                    jugadorActual j = jugadoresActuales.get(i);
                    //gui.mostrarTurnoJugador(j.getNombre());
                    mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                    Thread.sleep(300);
                    //gui.mostrarCartasJugador(mano);                    
                    
                    //robar
                    //eleccion = gui.menuRobar();
                    j.eleccionMenuRobo(eleccion);
                    mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                    Thread.sleep(300);            
                    //gui.mostrarCartasJugador(mano);                    

                    //BAJARSE, ORDENAR O CORTAR
                    //eleccion = gui.menuBajar();

                    //ordenar cartas en la mano
                    /*while (eleccion == gui.getEleccionOrdenarCartas()) {
                        int[] ordenar = gui.preguntarParaOrdenarCartas();
                        j.eleccionOrdenar(ordenar);
                        mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                        Thread.sleep(300);                
                        gui.mostrarCartasJugador(mano);                    
                        eleccion = gui.menuBajar();
                    }*/
                    
                    //bajarse
                    /*if (eleccion == gui.getEleccionBajarse()) {
                        Object [] cartasABajar = gui.preguntarQueBajarParaJuego();
                        if(!ctrl.bajarJuego(j, cartasABajar)) {
                            gui.mostrarNoPuedeBajarJuego();
                        } else {
                            ArrayList<ArrayList<String>> juegos = ctrl.enviarJuegosJugador(j);
                            int numJuego = 1;
                            for (ArrayList<String> juego : juegos) {
                                gui.mostrarJuego(numJuego);
                                gui.mostrarCartasJugador(juego);
                                numJuego++;
                            }
                        }
                    }*/
                    //si quiere cortar, comprobar si puede
                    /*if (eleccion == gui.getEleccionCortar()) {
                        corte = ctrl.cortar(partidaNueva, j);
                        if (!corte) {
                            int[] triosYEscalerasQueFaltan = j.comprobarQueFaltaParaCortar();
                            gui.mostrarLoQueFaltaParaCortar(triosYEscalerasQueFaltan);
                            //ctrl.tratarDeBajarParaCortar(triosYEscalerasQueFaltan);
                        }
                    }*/         
                    //tirar
                    if (!corte) {
                        mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                        //gui.mostrarCartasJugador(mano);                    
                        //eleccion = gui.preguntarQueBajarParaPozo();
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
//}
