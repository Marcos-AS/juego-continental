package src.vista;

import src.controlador.Controlador;

import javax.swing.*;

public interface ifVista {

    void actualizar(Object actualizacion, int indice);

    void setControlador(Controlador ctrl);
}
