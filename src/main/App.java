package src.main;

import src.modelo.Partida;
import src.modelo.jugadorActual;
import src.vista.Consola;

import java.util.ArrayList;

import src.controlador.Controlador;

public class App {
    private static Controlador ctrl;
    public static void main(String[] args) {
        ctrl = new Controlador();
        Partida partidaNueva = new Partida();
        Consola consola = new Consola();
        String j1 = "AnitaSSJ";
        String j2 = "Marcos";
        int eleccion = 0;
        boolean corte = false;
        partidaNueva.agregarJugador(j1);
        partidaNueva.agregarJugador(j2);
        partidaNueva.crearMazo();
        partidaNueva.repartirCartas();
        consola.mostrarCartasNombreJugador(j1);
        consola.mostrarCartasJugador(ctrl.enviarManoJugador(partidaNueva, j1));
        consola.mostrarCartasNombreJugador(j2);
        consola.mostrarCartasJugador(ctrl.enviarManoJugador(partidaNueva, j2));

        //empiezan las rondas
        while (partidaNueva.getRonda()<partidaNueva.getTotalRondas()) {
            int i = 0;

            ArrayList<jugadorActual> jugadoresActuales = partidaNueva.getJugadoresActuales();
            while (!corte) {
                jugadorActual j = jugadoresActuales.get(i);
                consola.mostrarTurnoJugador(j.getNombre());

                //robar
                eleccion = consola.menuRobar();
                j.eleccionMenuRobo(eleccion);

                //BAJARSE, ORDENAR O CORTAR
                eleccion = consola.menuBajar();

                //ordenar cartas en la mano
                while (eleccion == consola.getEleccionOrdenarCartas()) {
                    int[] ordenar = consola.ordenarCartas();
                    j.eleccionOrdenar(ordenar);
                    consola.menuBajar();
                }

                //bajarse
                if (eleccion == consola.getEleccionBajarse()) {
                    ctrl.bajarJuego(j);
                }
                //si quiere cortar, comprobar si puede
                if (eleccion == consola.getEleccionCortar()) {
                    corte = ctrl.cortar(partidaNueva, j);
                } else {
                    int[] faltaParaCortar = j.comprobarQueFaltaParaCortar();
                    consola.mostrarLoQueFaltaParaCortar(faltaParaCortar);
                    eleccion = 0;
                    while ((faltaParaCortar[0] != 0 && faltaParaCortar[1] != 0) && eleccion != 2) {
                        ctrl.bajarJuego(j);
                        faltaParaCortar = j.comprobarQueFaltaParaCortar();
                        consola.mostrarLoQueFaltaParaCortar(faltaParaCortar);
                        eleccion = consola.preguntarSiDeseaContinuar();
                    }
                    if (faltaParaCortar[0] == 0 && faltaParaCortar[1] == 0) {
                        corte = ctrl.cortar(partidaNueva, j);
                    }
                }         
                //tirar
                if (!corte) {
                    eleccion = consola.menuTirar();
                    j.eleccionMenuTirar(eleccion);                    
                }
            }//while ronda
        }//while partida
    }
}

