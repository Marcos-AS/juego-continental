package src.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextArea;

public class VentanaJuego extends VentanaInicio{
        JPanel panel;

        public VentanaJuego() {
            getContentPane().setLayout(new BorderLayout());
            JPanel panel = iniciarPanel();
            getContentPane().add(panel, BorderLayout.CENTER);
            //getContentPane().setLayout(new BorderLayout());
            //pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setTitle("Partida en juego");    
            setVisible(true);
        }

        public JPanel iniciarPanel() {
            JPanel panel = new JPanel();
            panel.setBackground(new Color(56, 102, 65));
            //JPanel panel = crearPanel();
            //JLabel titulo = agregarTitulo();
            //panel.add(titulo); //CONTINENTAL
            this.panel = panel;
            //JLabel labelImagen = getLabelImagen("src\\vista\\imgs\\cartas_inicio.png");
            // if (labelImagen != null) 
            //     panel.add(labelImagen);
            return panel;
        }

        public void mostrarTurnoJugador(String nombreJugador) {
            JTextArea text = new JTextArea("Es el turno de: " + nombreJugador);
            JPanel panel = new JPanel();
            panel.add(text, BorderLayout.CENTER);
            getContentPane().add(panel, BorderLayout.NORTH);
            // this.panel.add(text);
            // this.panel.revalidate();
            // this.panel.repaint();
        }

        public void mostrarCartasJugador(ArrayList<String> mano) {
            String cartas = "Cartas: ";
            for (String carta : mano) {
                cartas += carta + " - ";
            }
            JTextArea text = new JTextArea(cartas);
            JPanel panel = new JPanel();
            //Rectangle r = new Rectangle(5, 3, 1, 1);
            //panel.setBounds(r);
            panel.add(text, BorderLayout.CENTER);
            getContentPane().add(panel, BorderLayout.SOUTH);
        }

        public String asociarRuta(String carta) {
            return "\\src\\vista\\cartas\\" + carta + ".png";
        }

        public JLabel agregarImagenCartaAPanel(String carta) {
            //JLabel labelImagen = getLabelImagen(rutaImagen);
            //return labelImagen;
            //try {
            String rutaImagen = asociarRuta(carta);
            JLabel label = new JLabel();
            URL imgUrl = getClass().getResource(rutaImagen);
            if (imgUrl != null) {
                label.setIcon(new ImageIcon(imgUrl));
                //this.panel.add(label);
            }
            return label;
                //BufferedImage myPicture = ImageIO.read(new File(rutaImagen));
                //JLabel labelImagen = new JLabel(new ImageIcon(myPicture));
                //this.panel.add(labelImagen);
            //} catch (Exception e) {
                //e.printStackTrace();
            //}
            //this.validate(); 
            // this.panel.revalidate();
            // this.panel.repaint();
        }

        public JPanel getPanel() {
            return this.panel;
        }
}
