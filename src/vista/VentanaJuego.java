package src.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class VentanaJuego extends VentanaInicio{
    protected JPanel panelJuego;

    public VentanaJuego(int ancho, int alto, Component ubicacionVentana) {
        super(ancho, alto, true, ubicacionVentana);
        //agregarBotones();
        panelJuego = crearPanel();
        getContentPane().add(panelJuego, BorderLayout.CENTER);
    }

    public void agregarBotones() {
        JButton[] botones = new JButton[5];
        botones[0] = crearBoton("Bajar juegos", Component.TOP_ALIGNMENT, 140, 50, 15, false);
        botones[1] = crearBoton("Tirar al pozo", Component.TOP_ALIGNMENT, 140, 50, 15, false);
        botones[2] = crearBoton("Ordenar cartas", Component.TOP_ALIGNMENT, 140, 50, 15, false);
        botones[3] = crearBoton("Acomodar en juego propio", Component.TOP_ALIGNMENT, 140, 50, 15, false);
        botones[4] = crearBoton("Acomodar en juego ajeno", Component.TOP_ALIGNMENT, 140, 50, 15, false);
        for (int i = 0; i < 5; i++) {
            panelJuego.add(botones[i]);
            panelJuego.add(Box.createRigidArea(new Dimension(10, 0)));
        }

        botones[0].addActionListener((e -> eventoBotonBajar()));
    }

    public void eventoBotonBajar() {

    }

    protected static class CardSelectionListener implements ActionListener {
        protected Set<JButton> selectedCards = new HashSet<>();
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (selectedCards.contains(source)) {
                selectedCards.remove(source);
                source.setBackground(null); // reset color
            } else {
                selectedCards.add(source);
                source.setBackground(Color.GREEN); // indicate selection
            }
        }
    }

}
