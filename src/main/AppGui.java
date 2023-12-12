package src.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import src.controlador.Controlador;
import src.modelo.Partida;
import src.modelo.jugadorActual;
import src.vista.PartidaEventListener;
import src.vista.VentanaInicio;
import src.vista.VentanaJuego;

public class AppGui implements Observer {
    private static final String NOMBRE_VENTANA = "Continental";
    
    public static void main(String[] args) {
        new VentanaInicio(1000, 700, NOMBRE_VENTANA);
        //SwingUtilities.invokeLater(() -> {
    }

    public void AppGui() {}

    public void actualizar(Object o){

    }

    public void iniciarPartida(VentanaJuego guiPartida) {
        Controlador ctrl = new Controlador();
        ctrl.addObserver(guiPartida);
        Partida partidaNueva = new Partida();
        String j1 = "AnitaSSJ";
        String j2 = "Marcos";
        int eleccion = 0;
        boolean corte = false;
        partidaNueva.agregarJugador(j1);
        partidaNueva.agregarJugador(j2);
        try {
            partidaNueva.crearMazo();
            partidaNueva.repartirCartas();
            ArrayList<jugadorActual> jugadoresActuales = partidaNueva.getJugadoresActuales();
            //SwingUtilities.invokeLater(() -> {
            // for (jugadorActual jugadorActual : jugadoresActuales) {
            //     ArrayList<String> mano = ctrl.enviarManoJugador(partidaNueva, jugadorActual.getNombre());
            //     for (String carta : mano) {
            //         JLabel labelImagen = guiPartida.agregarImagenCartaAPanel(carta);
            //         guiPartida.getContentPane().add(labelImagen);
            //     }
            // }

            guiPartida.mostrarTurnoJugador(j1);
            guiPartida.mostrarCartasJugador(ctrl.enviarManoJugador(partidaNueva, j1));
            Thread.sleep(300);
            //guiPartida.mostrarTurnoJugador(j2);
            //guiPartida.mostrarCartasJugador(ctrl.enviarManoJugador(partidaNueva, j2));            
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // ArrayList<String> mano = null;
        //empiezan las rondas
        // while (partidaNueva.getRonda()<partidaNueva.getTotalRondas()) {
        //     int i = 0;

        //     while (!corte) {
        //         try {
                    //gui.mostrarPozo(ctrl.enviarPrimeraCartaPozo(partidaNueva));
                    //jugadorActual j = jugadoresActuales.get(i);
                    //gui.mostrarTurnoJugador(j.getNombre());
                    //mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                    //Thread.sleep(300);
                    //gui.mostrarCartasJugador(mano);                    
                    
                    //robar
                    //eleccion = gui.menuRobar();
                    //j.eleccionMenuRobo(eleccion);
                    //mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                    //Thread.sleep(300);            
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
                //     if (!corte) {
                //         mano = ctrl.enviarManoJugador(partidaNueva, j.getNombre());
                //         //gui.mostrarCartasJugador(mano);                    
                //         //eleccion = gui.preguntarQueBajarParaPozo();
                //         j.tirarAlPozo(eleccion);
                //     }
                //     i++;
                //     if (i>jugadoresActuales.size()-1) i = 0;
                // } catch (Exception e) {
                //     e.printStackTrace();
                // }
            //}//while ronda
            //partidaNueva.resetearJuegosJugadores();
        }//while partida
        catch (Exception e) {}
    }        
}