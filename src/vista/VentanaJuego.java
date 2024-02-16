package src.vista;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class VentanaJuego extends JFrame{
        JPanel panel;

        public VentanaJuego() {
            this.panel = new JPanel();
            this.getContentPane().add(panel, BorderLayout.CENTER);
            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().setBackground(new Color(56, 102, 65));
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setTitle("Partida en juego");
        }

        public void mostrarTurnoJugador(String nombreJugador) {
            JTextArea text = new JTextArea("Es el turno de: " + nombreJugador);
            JPanel panel = new JPanel();
            panel.add(text, BorderLayout.CENTER);
            getContentPane().add(panel, BorderLayout.NORTH);
        }

        public void mostrarCartasJugador(ArrayList<String> mano) {
            String cartas = "Cartas: ";
            for (String carta : mano) {
                cartas += carta + " - ";
            }
            JTextArea text = new JTextArea(cartas);
            JPanel panel = new JPanel();
            panel.add(text, BorderLayout.CENTER);
            getContentPane().add(panel, BorderLayout.SOUTH);
        }

        public String asociarRuta(String carta) {
            return "\\src\\vista\\cartas\\" + carta + ".png";
        }

        public JButton agregarImagenCartaAPanel(String carta) {
            String rutaImagen = asociarRuta(carta);
            JButton btnImg = new JButton(new ImageIcon(rutaImagen));
            btnImg.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.panel.add(btnImg);
            return btnImg;
        }

        public JPanel getPanel() {
            return this.panel;
        }

        public void actualizarValor(Object cambio) {

        }
}
