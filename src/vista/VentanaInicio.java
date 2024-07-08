package src.vista;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

import src.controlador.Controlador;
import src.modelo.ifCarta;
import src.modelo.ifJugador;
import src.modelo.ifPartida;
import src.serializacion.Serializador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class VentanaInicio extends JFrame implements ifVista, ActionListener {
    private VentanaJuego ventanaJuego;
    private Controlador ctrl;
    private String nombreVista;
    private int numJugadores;
    private JPanel panel;
    private JPanel panelCartas;
    private static final int VENTANA_NUEVA_PARTIDA = 27;

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
        JButton botonCrear = crearBoton("Crear partida", Component.CENTER_ALIGNMENT, 200, 40, 25);
        panel.add(botonCrear);

        // Eventos del boton
        botonCrear.addMouseListener(new MouseAdapter() {   // color al pasar el mouse
            public void mouseEntered(MouseEvent evt) {
                botonCrear.setBackground(new Color(200, 220, 120));
            }

            public void mouseExited(MouseEvent evt) { // vuelve al color original cuando sale el mouse
                botonCrear.setBackground(new Color(167, 201, 87));
            }
        });

        //esto tengo que hacer
        botonCrear.setActionCommand("Crear");
        botonCrear.addActionListener(this::accionarBotonCrearPartida);
        panel.add(Box.createVerticalGlue());          // agregar espacio vertical antes del botón
        panel.add(botonCrear);

        JButton botonJugar = crearBoton("Iniciar partida", Component.RIGHT_ALIGNMENT, 200, 50, 25);
        panel.add(Box.createVerticalGlue());          // agregar espacio vertical antes del botón
        panel.add(botonJugar);
        botonJugar.addActionListener(e -> {
            try {
                accionarBotonIniciarPartida();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });


        JLabel lblImgInicial = new JLabel(new ImageIcon("src\\vista\\imgs\\cartas_inicio.png"));
        lblImgInicial.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblImgInicial);

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
        JTextField textFieldNombre = new JTextField();
        textFieldNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        textFieldNombre.setToolTipText("Escriba aquí su nombre de usuario");
        textFieldNombre.setMaximumSize(new Dimension(200, 40));
        textFieldNombre.setPreferredSize(new Dimension(200, 20));
        textFieldNombre.setBackground(new Color(108, 71, 73));
        textFieldNombre.setForeground(new Color(242, 232, 207));
        textFieldNombre.setFont(new Font("Josefin Sans Medium", Font.PLAIN, 20));
        textFieldNombre.setBorder(new EmptyBorder(40, 0, 10, 0));
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

    public JMenuItem crearItemReglas() {
        JMenuItem itemReglas = new JMenuItem("Reglas");
        itemReglas.addActionListener(e -> {
            VentanaReglas ventanaReglas = new VentanaReglas();
            ventanaReglas.setVisible(true);
        });
        return itemReglas;
    }

    public JMenuItem crearItemSalir() {
        JMenuItem itemSalir = new JMenuItem("Salir del juego");
        itemSalir.addActionListener (e -> System.exit(0));
        return itemSalir;
    }

    public JMenuItem crearItemRanking() {
        JMenuItem itemVerRanking = new JMenuItem("Mostrar el ranking");
        itemVerRanking.addActionListener(e -> {
            try {
                ctrl.getRanking();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });
        return itemVerRanking;
    }

    public void accionarBotonCrearPartida(ActionEvent e) {
        if(e.getActionCommand().equals("Crear")) {
            String input = JOptionPane.showInputDialog(this, "¿Cuántos jugadores quieres para la partida?", "Número de jugadores", JOptionPane.QUESTION_MESSAGE);
            numJugadores = Integer.parseInt(input);
            //try {
                //if (!this.ctrl.crearPartida(this, numJugadores)) { //crea partida y agrega al jugador, setea part. actual en ctrl
                  //  noSePuedeIniciarPartida(1);
                //}
            //} catch (RemoteException ex) {
              //  throw new RuntimeException(ex);
            //}

            //setPartidaIniciada();
        }
    }

    public void accionarBotonIniciarPartida() throws RemoteException {
        int inicioPartida = ctrl.jugarPartidaRecienIniciada();
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
                    ctrl.desarrolloTurno(jA.getNumeroJugador());
                }
                break;
            }
            case 6: {
                //mostrarInicioPartida();
                break;
            }
            case 7: {
                ArrayList<ifJugador> js = (ArrayList<ifJugador>) actualizacion;
                mostrarUltimoJugadorAgregado(js);
                break;
            }
            case 9: {
                String combinacion = mostrarCombinacionRequerida(((ifPartida) actualizacion).getRonda());
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, combinacion, "Mensaje", JOptionPane.INFORMATION_MESSAGE));
                mostrarPozo(((ifPartida) actualizacion).sacarPrimeraDelPozo());
                break;
            }
            case 10: {
                String s = (String) actualizacion;
                if (!this.ctrl.getEstadoPartida()) {
                    mostrarGanador(s);
                } else if (!s.equalsIgnoreCase(this.nombreVista)) {
                    String mensaje = "El jugador " + s + " ha iniciado una partida nueva.";
                    JOptionPane.showMessageDialog(this, mensaje, "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            }
            case 11: { //un jugador puede robar con castigo
                int[] a = (int[]) actualizacion;
                ifJugador j = this.ctrl.getJugadorPartida(a[0]);
                if (this.nombreVista.equals(j.getNombre())) {
                    //ctrl.desarrolloRoboConCastigo(ctrl.enviarManoJugador(j), j, a[2]);
                }
                break;
            }
            case 12: {
                String nombreJugador = this.ctrl.getJugadorPartida((int)actualizacion).getNombre();
                jugadorHaRobadoConCastigo(nombreJugador);
                break;
            }
            case 14: {
                JOptionPane.showMessageDialog(null, "Esta ronda ha finalizado.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            case 15: {
                int[] puntos = (int[]) actualizacion;
                mostrarPuntosRonda(puntos);
                break;
            }
            case 16: {
                mostrarRanking(((Serializador) actualizacion).readObjects());
                break;
            }
            case VENTANA_NUEVA_PARTIDA: {
                nuevaVentana();
                break;
            }
        }

    }

    private void mostrarRanking(Object[] rankingJugadores) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < rankingJugadores.length; i++) {
            ifJugador j = (ifJugador) rankingJugadores[i];
            s.append((i + 1)).append(": ").append(j.getNombre()).append(" --- puntos: ").append(j.getPuntosAlFinalizar()).append("\n");
        }
        String finalS = s.toString();
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(this, finalS, "Ranking", JOptionPane.INFORMATION_MESSAGE));
    }

    //GETTERS Y SETTERS, OBSERVER-----------------
    // public void setPartidaIniciada() {
    //     this.partidaIniciada = !partidaIniciada;
    //     //firePartidaIniciadaEvent();
    // }

    // public boolean getPartidaIniciada() {
    //     return this.partidaIniciada;
    // }

    public void nuevaVentana() {
        //this.setVisible(false);
        this.panel.removeAll();
        revalidate();
        repaint();
        //this.getContentPane().removeAll();
        //this.ventanaJuego = new VentanaJuego();
        //this.ventanaJuego.setVisible(true);
    }

    public String asociarRuta(String carta) {
        return "src\\vista\\cartas\\" + carta + ".png";
    }

    public JButton agregarImagenCartaAPanel(String carta) {
        String rutaImagen = asociarRuta(carta.toLowerCase());
        ImageIcon icon = new ImageIcon(rutaImagen);
        Image imagenEscalada = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
        JButton btnImg = new JButton(iconoEscalado);
        btnImg.setPreferredSize(new Dimension(100, 100));
        //JLabel lblImg = new JLabel();
        //lblImg.setIcon(icon);
        //btnImg.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        btnImg.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        //this.add(lblImg);
        //getContentPane().add(btnImg);
        //pack(); //para el size
        return btnImg;
    }

    @Override
    public int[] preguntarParaOrdenarCartas(int cantCartas) {
        return new int[0];
    }

    @Override
    public int preguntarCantParaBajar() {
        return 0;
    }

    @Override
    public boolean partida() throws RemoteException {
    return false;
    }

    private void jugadorHaRobadoConCastigo(String nombreJugador) {
    }

    private void mostrarPuntosRonda(int[] puntos) {
    }

    private void mostrarUltimoJugadorAgregado(ArrayList<ifJugador> js) {
        String s = "El jugador " + js.get(js.size()-1).getNombre() + " ha ingresado.";
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, s, "Mensaje", JOptionPane.INFORMATION_MESSAGE));
    }

    public int preguntarEnQueJuegoQuiereAcomodar() {
        return 0;
    }

    @Override
    public int[] preguntarQueBajarParaJuego(int cantCartas) {
        return new int[0];
    }

    public int preguntarCartaParaAcomodar() {
        return 0;
    }


    public Object[] preguntarQueBajarParaJuego() {
        return null;
    }

    @Override
    public void mostrarPuedeRobarConCastigo(String nombreJugador) {

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

    public static String mostrarCombinacionRequerida(int ronda) {
        String s = "Para esta ronda deben bajarse: ";
        switch (ronda) {
            case 1:
                s += "Ronda 1: 2 tríos";
                break;
            case 2:
                s += "Ronda 2: 1 trío y 1 escalera";
                break;
            case 3:
                s += "Ronda 3: 2 escaleras";
                break;
            case 4:
                s += "Ronda 4: 3 tríos";
                break;
            case 5:
                s += "Ronda 5: 2 tríos y 1 escalera";
                break;
            case 6:
                s += "Ronda 6: 1 tríos y 2 escaleras";
                break;
            case 7:
                s += "Ronda 7: 3 escaleras";
                break;
        }
        return s;
    }

    @Override
    public void mostrarPozo(ifCarta c) {
        panel.removeAll();
        revalidate();
        repaint();
        panel.add(agregarImagenCartaAPanel(ifVista.cartaToString(c)));
    }

    @Override
    public void mostrarTurnoJugador(String nombreJugador) {
        String s = "Es el turno del jugador: " + nombreJugador;
        JOptionPane.showMessageDialog(this, s, "Mensaje", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarCartas(ArrayList<String> cartas) {
        if (panelCartas==null) {
            GridLayout gridLayout = new GridLayout(0, 13); //filas dinamicas
            JPanel jPanel = new JPanel(gridLayout);
            panelCartas = jPanel;
            getContentPane().add(jPanel, BorderLayout.SOUTH);
        }
            panelCartas.removeAll();
            revalidate();
            repaint();
        for (String c : cartas) {
            panelCartas.add(agregarImagenCartaAPanel(c));
        }
    }

    @Override
    public void mostrarPuntosJugador(String nombre, int punto) {

    }

    @Override
    public void mostrarGanador(String ganador) {

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
    public void mostrarNoPuedeBajarJuego(int i) {

    }

    @Override
    public void mostrarNoPuedeAcomodarJuegoPropio() {

    }

    @Override
    public void mostrarLoQueFaltaParaCortar(int[] faltaParaCortar) {

    }

    @Override
    public String getNombreVista() {
        return this.nombreVista;
    }

    @Override
    public void setNombreVista(String i) {
        this.nombreVista = i;
    }

    @Override
    public void noSePuedeIniciarPartida(int i) {
        if (i == 1) {
            JOptionPane.showMessageDialog(this, "No se puede iniciar la partida porque faltan jugadores para la cantidad deseada.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else if (i == 2) {
            JOptionPane.showMessageDialog(this, "La partida aun no ha sido creada. Seleccione la opcion 1: 'Iniciar partida nueva'", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else if (i == 3) {
            JOptionPane.showMessageDialog(this, "No se puede iniciar la partida porque faltan jugadores (minimo 2)", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        }
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

    @Override
    public int preguntarEnLosJuegosDeQueJugadorAcomodar() {
        return 0;
    };

    @Override
    public void mostrarJuegosJugador(int numJugador) {
    };

    @Override
    public void mostrarAcomodoCarta() {};

    @Override
    public void mostrarAdvertenciaBajarse() {
    }

    @Override
    public boolean preguntarSiQuiereSeguirBajandoJuegos() {
        return false;
    }

    @Override
    public void mostrarFinalizoTurno(){}

    @Override
    public void mostrarCorto(String nombreJugador){}

    @Override
    public void mostrarDebeQuedarle1o0Cartas() {}

    @Override
    public void mostrarDebeCortar() {}
}