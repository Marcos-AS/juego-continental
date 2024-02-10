package src.vista;

import src.controlador.Controlador;

import javax.swing.*;

public interface ifVista {
    JPanel crearPanel();

    void agregarAPanelInicio(JPanel panel);

    void agregarMenuBarra();

    JLabel agregarTitulo();

    JLabel crearLabel();

    JTextField crearInputField();

    JButton crearBoton(String contenido, float alineacion, int ancho, int alto);

    JLabel getLabelImagen(String rutaImagen);

    JMenuItem crearItemReglas();

    JMenuItem crearItemSalir();

    JMenuItem crearItemRanking();

    void setVentanaJuego(VentanaJuego ventanaJuego);

    VentanaJuego getVentanaJuego();

    void actualizar(Object actualizacion, int indice);

    void setControlador(Controlador ctrl);
}
