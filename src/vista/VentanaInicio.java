package src.vista;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

import src.controlador.Controlador;
import src.modelo.ifJugador;
import src.modelo.ifPartida;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class VentanaInicio extends JFrame implements ifVista, ActionListener {
    private VentanaJuego ventanaJuego;
    private Controlador ctrl;
    private String nombreVista;
    private int numJugadores;
    private JPanel panel;

    //PUBLIC------------------------------------------------
    public VentanaInicio(int ancho, int alto) {
        setSize(ancho, alto);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //cerrar ventana cuando se selecciona la X
        setLocationRelativeTo(null);           // centrar la ventana 
        JPanel panel = crearPanel();
        this.panel = panel;
        agregarAPanelInicio(panel);  
        agregarMenuBarra();
        setVisible(true); 
    }

    public VentanaInicio() {}

    public JPanel crearPanel() { //lo que se ve en la pantalla inicial
        JPanel panel = new JPanel();                              
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(56, 102, 65));            
        return panel;
    }

    public void agregarAPanelInicio(JPanel panel) {
        panel.add(agregarTitulo()); //continental en grande
        
        // LABEL -------------------------------------------------
        panel.add(crearLabel());
        panel.add(Box.createRigidArea(new Dimension(0, 0))); // Espacio entre la etiqueta y el campo

        // FIELD -------------------------------------------------
        JTextField campoNombre = crearInputField();
        panel.add(campoNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre el icono y el campo
        // BOTON ingresar texto
        JButton botonIngresarTexto = crearBoton("Ingresar texto", Component.CENTER_ALIGNMENT, 100, 30, 10);
        panel.add(botonIngresarTexto);

        botonIngresarTexto.addActionListener(e -> {
            String nombreJugador = campoNombre.getText();
            try {
                this.ctrl.agregarNuevoJugador(nombreJugador); //setea nombre vista en ctrl y agrega jugador a juego
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });


        // BOTON -------------------------------------------------------------------------------------
        JButton botonIniciar = crearBoton("Iniciar juego", Component.CENTER_ALIGNMENT, 200, 40, 25);
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

        //esto tengo que hacer
        botonIniciar.setActionCommand("Iniciar");
        botonIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accionarBotonCrearPartida(e);
            }
        });

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

    public JButton crearBoton(String contenido, float alineacion, int ancho, int alto, int fontSize) {
        JButton boton = new JButton(contenido);             
        boton.setAlignmentX(alineacion);                        
        boton.setMaximumSize(new Dimension(ancho, alto));                           
        boton.setPreferredSize(new Dimension(ancho, alto));                       
        boton.setFont(new Font("Josefin Sans Medium", Font.PLAIN, fontSize));
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

    public void accionarBotonCrearPartida(ActionEvent e) {
        if(e.getActionCommand().equals("Iniciar")) {
            String input = JOptionPane.showInputDialog(null, "¿Cuántos jugadores quieres para la partida?", "Número de jugadores", JOptionPane.QUESTION_MESSAGE);
            numJugadores = Integer.parseInt(input);
            try {
                if (!this.ctrl.crearPartida(this, numJugadores)) { //crea partida y agrega al jugador, setea part. actual en ctrl
                    noSePuedeIniciarPartida(1);
                } else {
                    JButton botonIniciar = crearBoton("Iniciar partida", Component.RIGHT_ALIGNMENT, 100, 50, 25);
                    this.panel.add(botonIniciar);
                    botonIniciar.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                accionarBotonIniciarPartida();
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }

            //setPartidaIniciada();
        }
    }

    public void accionarBotonIniciarPartida() throws RemoteException {
        int inicioPartida = ctrl.jugarPartidaRecienIniciada(nombreVista);
        if(inicioPartida == 0) {
            noSePuedeIniciarPartida(2);
        } else if (inicioPartida == 1){
            noSePuedeIniciarPartida(3);
        } else if (inicioPartida == 2) {
            mostrarFinalizoPartida();
        }
    }

    @Override
    public void actualizar(Object actualizacion, int indice) throws RemoteException {
        switch (indice) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                ifJugador jA = (ifJugador) actualizacion;
                String nombreJugador = jA.getNombre();
                mostrarTurnoJugador(nombreJugador);
                if (this.nombreVista.equals(nombreJugador)) {
                    ctrl.desarrolloTurno(jA);
                }
                break;
            }
            case 6: {
                mostrarInicioPartida();
                break;
            }
            case 7:
            case 8: {
                ArrayList<ifJugador> js = (ArrayList<ifJugador>) actualizacion;
                if (indice == 7) {
                    mostrarUltimoJugadorAgregado(js, 1);
                } else {
                    mostrarUltimoJugadorAgregado(js,2);
                }
                break;
            }
            case 9: {
                mostrarCombinacionRequerida(((ifPartida) actualizacion).getRonda());
                mostrarPozo((ifPartida)actualizacion);
                break;
            }
            case 10: {
                String s = (String) actualizacion;
                if (!this.ctrl.getEstadoPartida()) {
                    mostrarGanador(s);
                } else if (!s.equalsIgnoreCase(this.nombreVista)) {
                    System.out.println("El jugador " + s + " ha iniciado una partida nueva");
                }
                break;
            }
            case 11: { //un jugador puede robar con castigo
                int[] a = (int[]) actualizacion;
                ifJugador j = this.ctrl.getJugadorPartida(a[0]);
                if (this.nombreVista.equals(j.getNombre())) {
                    ctrl.desarrolloRoboConCastigo(ctrl.enviarManoJugador(j), j, a[2]);
                }
                break;
            }
            case 12: {
                String nombreJugador = this.ctrl.getJugadorPartida((int)actualizacion).getNombre();
                jugadorHaRobadoConCastigo(nombreJugador);
                break;
            }
            case 14: {
                System.out.println("Esta ronda ha finalizado.");
                System.out.println("--------------------------");
                break;
            }
            case 15: {
                int[] puntos = (int[]) actualizacion;
                mostrarPuntosRonda(puntos);
                break;
            }
        }

    }

    private void jugadorHaRobadoConCastigo(String nombreJugador) {
    }

    private void mostrarPuntosRonda(int[] puntos) {
    }

    private void mostrarUltimoJugadorAgregado(ArrayList<ifJugador> js, int i) {
    }

    public int preguntarEnQueJuegoQuiereAcomodar() {
        return 0;
    }

    public int preguntarCartaParaAcomodar() {
        return 0;
    }


    public Object[] preguntarQueBajarParaJuego() {
        return null;
    }

    @Override
    public void jugadorPuedeRobarConCastigo(String nombreJugador) {

    }

    @Override
    public int menuRobarDelPozo() {
        return 0;
    }

    public int menuBajar() {
        return 0;
    }

    @Override
    public int preguntarQueBajarParaPozo(int cantCartas) {
        return 0;
    }

    public void mostrarNoPuedeRobarDelPozo() { }

    public int menuRobar() {
    return 0;
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
    public String preguntarNombreNuevoJugador() throws RemoteException {
        return null;
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
        if (i == 1) {
            JOptionPane.showMessageDialog(null, "No se puede iniciar la partida porque faltan jugadores para la cantidad deseada.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else if (i == 2) {
            JOptionPane.showMessageDialog(null, "La partida aun no ha sido creada. Seleccione la opcion 1: 'Iniciar partida nueva'", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else if (i == 3) {
            JOptionPane.showMessageDialog(null, "No se puede iniciar la partida porque faltan jugadores (minimo 2)", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void mostrarFinalizoPartida() {

    }

    @Override
    public int preguntarCantJugadores() {
        return 0;
    }

    @Override
    public void mostrarJuegos(ArrayList<ArrayList<String>> juegos) {

    }

    @Override
    public void mostrarNoPuedeBajarJuego() {

    }

    @Override
    public void mostrarNoPuedeAcomodarJuegoPropio() {

    }

    @Override
    public void mostrarLoQueFaltaParaCortar(int[] faltaParaCortar) {

    }

    public void setVentanaJuego(VentanaJuego ventanaJuego) {
        this.ventanaJuego = ventanaJuego;
    }

    public VentanaJuego getVentanaJuego() {
        return this.ventanaJuego;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}