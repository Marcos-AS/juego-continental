package src.vista;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VentanaJuego extends VentanaInicio{
        JPanel panel;

        public VentanaJuego() {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setTitle("Partida en juego");                     
            iniciarVentana();
            setVisible(true);
        }

        public void iniciarVentana() {
            this.panel = crearPanel();
            JLabel titulo = agregarTitulo();
            this.panel.add(titulo); //CONTINENTAL
            getContentPane().add(panel, BorderLayout.CENTER);
        }

        public String asociarRuta(String carta) {
            return "\\src\\vista\\cartas\\" + carta + ".png";
        }

        public void agregarImagenCartaAPanel(ArrayList<String> cartas) {
            String rutaImagen;
            for (String carta : cartas) {
                rutaImagen = asociarRuta(carta);
                this.panel.add(agregarImagen(rutaImagen)); 
                this.panel.add(crearLabel());
            }   
            // this.panel.revalidate();
            // this.panel.repaint();
        }

        public JPanel getPanel() {
            return this.panel;
        }
}
