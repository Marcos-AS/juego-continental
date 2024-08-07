package src.main;

import rmimvc.src.RMIMVCException;
import rmimvc.src.Util;
import rmimvc.src.cliente.Cliente;
import src.controlador.Controlador;
import src.vista.VentanaInicio;
import src.vista.ifVista;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppClienteGUI {

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

        ifVista vista = new VentanaInicio(500, 700, false, null);
        Controlador ctrl = new Controlador(vista);
        vista.setControlador(ctrl);
        Cliente c = new Cliente(ip, Integer.parseInt(port), ipServer, Integer.parseInt(portServer));
        try {
            //se agrega el ctrl como observador y se setea el modelo como atributo del ctrl
            c.iniciar(ctrl);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (RMIMVCException e) {
            e.printStackTrace();
        }
    }
}
