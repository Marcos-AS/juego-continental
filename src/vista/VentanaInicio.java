package src.vista;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

import src.controlador.Controlador;
import src.modelo.ifPartida;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class VentanaInicio extends JFrame implements ifVista {
    private VentanaJuego ventanaJuego;
    private Controlador ctrl;

    //PUBLIC------------------------------------------------
    public VentanaInicio(int ancho, int alto) {
        setSize(ancho, alto);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //cerrar ventana cuando se selecciona la X
        setLocationRelativeTo(null);           // centrar la ventana 
        JPanel panel = crearPanel();
        agregarAPanelInicio(panel);  
        agregarMenuBarra();
        setVisible(true); 
    }

    public VentanaInicio() {}

    public JPanel crearPanel() {
        JPanel panel = new JPanel();                              
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));    // establecer el layout del panel como BoxLayout en el eje Y
        panel.setBackground(new Color(56, 102, 65));            
        return panel;
    }

    public void agregarAPanelInicio(JPanel panel) {
        panel.add(agregarTitulo());
        
        // LABEL -------------------------------------------------
        panel.add(crearLabel());
        panel.add(Box.createRigidArea(new Dimension(0, 0))); // Espacio entre la etiqueta y el campo

        // FIELD -------------------------------------------------
        panel.add(crearInputField());
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre el icono y el campo

        // BOTON -------------------------------------------------------------------------------------
        JButton botonIniciar = crearBoton("Iniciar juego", Component.CENTER_ALIGNMENT, 200, 40);
        panel.add(botonIniciar);

        // Eventos del boton
        botonIniciar.addMouseListener(new java.awt.event.MouseAdapter() {   // color al pasar el mouse
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonIniciar.setBackground(new Color(200, 220, 120)); 
            }

            public void mouseExited(java.awt.event.MouseEvent evt) { // vuelve al color original cuando sale el mouse
                botonIniciar.setBackground(new Color(167, 201, 87)); 
            }
        });

        botonIniciar.setActionCommand("Iniciar");
        eventHandler eventHandler = new eventHandler();
        botonIniciar.addActionListener(eventHandler);

        panel.add(Box.createVerticalGlue());          // agregar espacio vertical antes del botón
        panel.add(botonIniciar);                           

        panel.add(getLabelImagen("src\\vista\\imgs\\cartas_inicio.png"));

        getContentPane().add(panel, BorderLayout.CENTER);   // agregar el panel a la ventana
    }

    public void agregarMenuBarra() {
        JMenuBar menuBarra = new JMenuBar();

        // ITEM "MENU" -------------------------------------------------------------------------------
        JMenu menuPrincipal = new JMenu("Menu");

        // agregar los subitems al item "menu"
        menuPrincipal.add(crearItemReglas());
        menuPrincipal.addSeparator();       // separador entre items
        menuPrincipal.add(crearItemSalir());

        // ITEM "RANKING" ----------------------------------------------------------------------------
        JMenu menuRanking = new JMenu("Ranking");

        // SUBITEM "MOSTRAR RANKING" 
        menuRanking.add(crearItemRanking());  

        // agregar los menus a la barra de menu
        menuBarra.add(menuPrincipal);
        menuBarra.add(menuRanking);

        setJMenuBar(menuBarra);             // establecer la barra de menú en la ventana
    }

    public JLabel agregarTitulo() {
        JLabel labelTitulo = new JLabel("CONTINENTAL");
        labelTitulo.setFont(new Font("Hollywood Hills", Font.BOLD, 100));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);              
        labelTitulo.setForeground(new Color(242, 232, 207));                
        labelTitulo.setBorder(new EmptyBorder(20, 0, 0, 0));              
        return labelTitulo;
    }

    public JLabel crearLabel() {
        JLabel labelNombre = new JLabel("Ingrese su nombre");                   // etiqueta para pedir el nombre del usuario
        labelNombre.setAlignmentX(Component.CENTER_ALIGNMENT);                   // alineacion
        labelNombre.setForeground(new Color(242, 232, 207));                    // color del texto
        labelNombre.setFont(new Font("Josefin Sans Medium", Font.PLAIN, 20));   // establece la fuente, el tipo de letra y el tamaño
        labelNombre.setBorder(new EmptyBorder(40, 0, 10, 0));                   // padding
        return labelNombre;
    }

    public JTextField crearInputField() {
        JTextField textFieldNombre = new JTextField();                              // Campo de texto para el nombre del usuario
        textFieldNombre.setAlignmentX(Component.CENTER_ALIGNMENT);                  // alineacion
        textFieldNombre.setToolTipText("Escriba aquí su nombre de usuario");        // texto de referencia 
        textFieldNombre.setMaximumSize(new Dimension(200, 40));                     // ancho maximo
        textFieldNombre.setPreferredSize(new Dimension(200, 20));                   // ancho y alto
        textFieldNombre.setBackground(new Color(108, 71, 73));                      // color de fondo
        textFieldNombre.setForeground(new Color(242, 232, 207));                    // color del texto
        textFieldNombre.setFont(new Font("Josefin Sans Medium", Font.PLAIN, 20));   // cambiar la fuente, el tipo y el tamaño de la letra
        textFieldNombre.setBorder(new EmptyBorder(40, 0, 10, 0));                       // padding
        LineBorder bordePersonalizado1 = new LineBorder(Color.GRAY, 1);
        textFieldNombre.setBorder(bordePersonalizado1);
        return textFieldNombre;
    }

    public JButton crearBoton(String contenido, float alineacion, int ancho, int alto) {
        JButton boton = new JButton(contenido);             
        boton.setAlignmentX(alineacion);                        
        boton.setMaximumSize(new Dimension(ancho, alto));                           
        boton.setPreferredSize(new Dimension(ancho, alto));                       
        boton.setFont(new Font("Josefin Sans Medium", Font.PLAIN, 25));         
        boton.setForeground(Color.BLACK);                                       
        boton.setBackground(new Color(167, 201, 87));                           
        LineBorder bordePersonalizado2 = new LineBorder(Color.darkGray, 1);   
        boton.setBorder(bordePersonalizado2);
        return boton;
    }

    public JLabel getLabelImagen(String rutaImagen) {
        JLabel labelImagen = null;
        try {
            ImageIcon imagen = new ImageIcon(rutaImagen); 
            //Thread.sleep(300);
            // boolean cargoImagen = imagen.getImageLoadStatus() == MediaTracker.COMPLETE;
            // while (!cargoImagen) {            
            //     cargoImagen = imagen.getImageLoadStatus() == MediaTracker.COMPLETE;
            // }
            labelImagen = new JLabel(imagen);
            labelImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return labelImagen;
    }

    public JMenuItem crearItemReglas() {
        JMenuItem itemReglas = new JMenuItem("Reglas");
        itemReglas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaReglas ventanaReglas = new VentanaReglas();  // abrir una nueva ventana con las reglas
                ventanaReglas.setVisible(true);
            }
        });
        return itemReglas;
    }

    public JMenuItem crearItemSalir() {
        JMenuItem itemSalir = new JMenuItem("Salir del juego");
        itemSalir.addActionListener (new ActionListener(){        
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        return itemSalir;
    }

    public JMenuItem crearItemRanking() {
        JMenuItem itemVerRanking = new JMenuItem("Mostrar el ranking");

        itemVerRanking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(VentanaInicio.this, "Mostrar Ranking");
                // *** RELLENAR CON CODIGO NECESARIO ***
            }
        });
        return itemVerRanking;
    }

    //GETTERS Y SETTERS, OBSERVER-----------------
    // public void setPartidaIniciada() {
    //     this.partidaIniciada = !partidaIniciada;
    //     //firePartidaIniciadaEvent();
    // }

    // public boolean getPartidaIniciada() {
    //     return this.partidaIniciada;
    // }

    public void setControlador(Controlador ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public int menuBienvenida() {
        return 0;
    }

    @Override
    public void mostrarReglas() {

    }

    @Override
    public void preguntarNombreNuevoJugador() throws RemoteException {

    }

    @Override
    public void mostrarInicioPartida() {

    }

    @Override
    public void mostrarJugador(String nombreJugador, int numJugador) {

    }

    static void mostrarCombinacionRequerida(int ronda) {

    }

    @Override
    public void mostrarPozo(ifPartida p) {

    }

    @Override
    public void mostrarTurnoJugador(String nombreJugador) {

    }

    @Override
    public void mostrarCartas(ArrayList<String> cartas) {

    }

    @Override
    public void mostrarPuntosJugador(String nombre, int punto) {

    }

    @Override
    public void mostrarGanador(String ganador) {

    }

    @Override
    public String getNombreVista() {
        return null;
    }

    @Override
    public void setNombreVista(String i) {

    }

    @Override
    public void noSePuedeIniciarPartida(int i) {

    }

    public void setVentanaJuego(VentanaJuego ventanaJuego) {
        this.ventanaJuego = ventanaJuego;
    }

    public VentanaJuego getVentanaJuego() {
        return this.ventanaJuego;
    }

    @Override
    public void actualizar(Object actualizacion, int indice) {
    switch (indice) {
        case 1:
    }
    }
}