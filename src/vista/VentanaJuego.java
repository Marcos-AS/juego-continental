package src.vista;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class VentanaJuego extends JFrame{
        VentanaInicio vi = new VentanaInicio();

        public VentanaJuego() {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setTitle("Partida en juego");                     
            setVisible(true);
            iniciarVentana();
        }

        public void iniciarVentana() {
            JPanel panel = vi.crearPanel();
            getContentPane().add(panel, BorderLayout.CENTER);
        }
}
