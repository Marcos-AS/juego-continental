package src.main;

import rmimvc.src.RMIMVCException;
import rmimvc.src.Util;
import rmimvc.src.cliente.Cliente;
import src.controlador.Controlador;
import src.vista.Consola;

import src.vista.ifVista;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppClienteConsola {

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
                ips.toArray(),
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

        ifVista vista = new Consola();
        Controlador ctrl = new Controlador(vista);
        vista.setControlador(ctrl);
        Cliente c = new Cliente(ip, Integer.parseInt(port), ipServer, Integer.parseInt(portServer));
        try {
            //se agrega el ctrl como observador y se setea el modelo como atributo del ctrl
            c.iniciar(ctrl);
            bienvenida(vista, ctrl);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (RMIMVCException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void bienvenida(ifVista vista, Controlador ctrl) throws RemoteException, InterruptedException {
        ctrl.agregarNuevoJugador(vista.preguntarNombreNuevoJugador()); //agrega jugador a juego y setea nombreVista
        int eleccion = 0;
        int cantJugadores = 2; //minimo
        boolean partidaIniciada = false;
        while (eleccion != -1 && !partidaIniciada) {
            eleccion = vista.menuBienvenida();
            switch (eleccion) {
                case 1: {
                    cantJugadores = vista.preguntarCantJugadores();
                    if (!ctrl.crearPartida(vista, cantJugadores)) { //crea partida y agrega al jugador, setea part. actual en ctrl
                        vista.noSePuedeIniciarPartida(1);
                    }
                    break;
                }
                case 2: {
                    ranking(ctrl);
                    break;
                }
                case 3: {
                    vista.mostrarReglas();
                    break;
                }
                case 4: {
                    int inicioPartida = ctrl.jugarPartidaRecienIniciada(vista.getNombreVista());
                    if(inicioPartida == 0) {
                        vista.noSePuedeIniciarPartida(2);
                    } else if (inicioPartida == 1){
                        vista.noSePuedeIniciarPartida(3);
                        partidaIniciada = true;
                    } else if (inicioPartida == 2) {
                        vista.mostrarFinalizoPartida();
                    }
                    break;
                }
            }
        }
    }

    private static void ranking(Controlador ctrl) throws RemoteException {
        ctrl.getRanking();
    }
}
