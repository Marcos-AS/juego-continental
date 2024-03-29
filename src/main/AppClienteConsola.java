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

        //creacion de la vista y el controlador
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

    private static void bienvenida(ifVista pVista, Controlador pCtrl) throws RemoteException, InterruptedException {
        pCtrl.agregarNuevoJugador(pVista.preguntarNombreNuevoJugador()); //agrega jugador a juego y setea nombreVista
        int eleccion;
        int cantJugadores = 2; //minimo
        boolean partidaIniciada = false;
        do {
            eleccion = pVista.menuBienvenida();
            //1 - crear partida
            //2 - ver ranking mejores jugadores
            //3 - ver reglas de juego
            //4 - jugar partida recien creada (por ahora, luego poder jugar partidas guardadas)
            switch (eleccion) {
                case 1: {
                    cantJugadores = pVista.preguntarCantJugadores();
                    pCtrl.crearPartida(pVista, cantJugadores);
                    break;
                }
                case 2: {
                    ranking(pCtrl);
                    break;
                }
                case 3: {
                    pVista.mostrarReglas();
                    break;
                }
                case 4: {
                    int inicioPartida = pCtrl.jugarPartidaRecienIniciada();
                    if(inicioPartida == 0) {
                        pVista.noSePuedeIniciarPartida(0); // partida aun no creada
                    } else if (inicioPartida == 1){
                        pVista.noSePuedeIniciarPartida(1); //faltan jugadores para la cant deseada
                    } else if (inicioPartida == 2) {
                        partidaIniciada = true;
                        pVista.mostrarInicioPartida();
                        if (!pVista.partida()) {
                            //partida pausada, guardar
                        } else {
                            //partida finalizada
                        }; //esto inicia el funcionamiento del juego
                    }
                    break;
                }
            }
        } while (eleccion != -1 && !partidaIniciada);
    }

    private static void ranking(Controlador ctrl) throws RemoteException {
        ctrl.getRanking();
    }
}
