package src.vista;

import src.controlador.Controlador;

import java.rmi.RemoteException;

public interface ifVista {

    void actualizar(Object actualizacion, int indice);

    void setControlador(Controlador ctrl);

    int menuBienvenida();

    void mostrarReglas();

    void preguntarNombreNuevoJugador() throws RemoteException;

    void mostrarInicioPartida();

    void mostrarJugador(String nombreJugador, int numJugador);
}
