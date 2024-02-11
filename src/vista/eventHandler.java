package src.vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.main.AppGui;

public class eventHandler extends VentanaInicio implements ActionListener{
    AppGui app = new AppGui();

    public void eventHandler(){}

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Iniciar")) {
            //setVentanaJuego(ventanaJuego);
            //setPartidaIniciada();
            VentanaJuego ventanaJuego = new VentanaJuego();
            //this.app.iniciarPartida(ventanaJuego);
        }
    }
}
