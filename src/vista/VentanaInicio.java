package src.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import src.controlador.Controlador;
import src.modelo.ifCarta;
import src.modelo.ifJugador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class VentanaInicio extends JFrame implements ifVista, ActionListener {
    protected Controlador ctrl;
    private String nombreVista;
    protected final JPanel panel;
    protected JPanel panelCartas;

    public VentanaInicio(int ancho, int alto, boolean juego, Component ubicacionVentana) {
        setSize(ancho, alto);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //cerrar ventana cuando se selecciona la X
        setLocationRelativeTo(ubicacionVentana);           // centrar la ventana
        JPanel jPanel = crearPanel();
        panel = jPanel;
        if (!juego) {
            agregarAPanelInicio(jPanel);
            SwingUtilities.invokeLater(() ->
                getContentPane().add(panel, BorderLayout.CENTER));   // agregar el panel a la ventana
        }
        agregarMenuBarra();
        setVisible(true); 
    }

    public JPanel crearPanel() { //lo que se ve en la pantalla inicial
        JPanel panel = new JPanel();                              
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(56, 102, 65));            
        return panel;
    }

    public void agregarAPanelInicio(JPanel panel) {
        panel.add(agregarTitulo()); //continental en grande
        
        // LABEL -------------------------------------------------
        panel.add(crearLabel("Ingrese su nombre", Component.CENTER_ALIGNMENT));
        panel.add(Box.createRigidArea(new Dimension(0, 0))); // Espacio entre la etiqueta y el campo

        // FIELD -------------------------------------------------
        JTextField campoNombre = crearInputField();
        panel.add(campoNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre el icono y el campo

        // BOTON ingresar texto
        JButton botonIngresarTexto = crearBoton("Ingresar nombre", Component.CENTER_ALIGNMENT, 150, 30, 15, true);
        panel.add(botonIngresarTexto);

        botonIngresarTexto.addActionListener(e -> {
            String nombreJugador = campoNombre.getText();
            try {
                ctrl.agregarNuevoJugador(nombreJugador); //setea nombre vista en ctrl y agrega jugador a juego
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });


        // BOTON -------------------------------------------------------------------------------------
        JButton botonCrear = crearBoton("Crear partida", Component.CENTER_ALIGNMENT, 200, 40, 25, true);
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

        JButton botonJugar = crearBoton("Iniciar partida", Component.CENTER_ALIGNMENT, 200, 50, 25, true);
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
        JMenuItem itemRanking = new JMenuItem("Mostrar el ranking");
        itemRanking.addActionListener(e -> {
            try {
                verRanking(ctrl.getRanking());
            } catch (RemoteException r) {
                throw new RuntimeException(r);
            }
        });
        menuRanking.add(itemRanking);

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

    public JLabel crearLabel(String texto, float alineacion) {
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

    public JButton crearBoton(String contenido, float alineacionHor, int ancho, int alto, int fontSize, boolean enabled) {
        JButton boton = new JButton(contenido);             
        boton.setAlignmentX(alineacionHor);
        boton.setMaximumSize(new Dimension(ancho, alto));                           
        boton.setPreferredSize(new Dimension(ancho, alto));                       
        boton.setFont(new Font("Josefin Sans Medium", Font.PLAIN, fontSize));
        boton.setForeground(Color.BLACK);                                       
        boton.setBackground(new Color(167, 201, 87));                           
        LineBorder bordePersonalizado2 = new LineBorder(Color.darkGray, 1);   
        boton.setBorder(bordePersonalizado2);
        boton.setEnabled(enabled);
        return boton;
    }

    public JMenuItem crearItemReglas() {
        JMenuItem itemReglas = new JMenuItem("Reglas");
        itemReglas.addActionListener(e -> {
            VentanaTexto ventanaReglas = new VentanaTexto(REGLAS, "Reglas");
            ventanaReglas.setVisible(true);
        });
        return itemReglas;
    }

    public JMenuItem crearItemSalir() {
        JMenuItem itemSalir = new JMenuItem("Salir del juego");
        itemSalir.addActionListener (e -> System.exit(0));
        return itemSalir;
    }

    public void verRanking(Object[] ranking) throws RemoteException {
        new VentanaTexto(ifVista.mostrarRanking(ranking).toString(), "Ranking").setVisible(true);
    }

    public void accionarBotonCrearPartida(ActionEvent e) {
        if(e.getActionCommand().equals("Crear")) {
            String input = JOptionPane.showInputDialog(this, "¿Cuántos jugadores quieres para la partida?", "Número de jugadores", JOptionPane.QUESTION_MESSAGE);
            int numJugadores = Integer.parseInt(input);
            try {
                ctrl.crearPartida(this, numJugadores); //crea partida y agrega al jugador, setea part. actual en ctrl
                    //noSePuedeIniciarPartida(1);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            //setPartidaIniciada();
        }
    }

    public void accionarBotonIniciarPartida() throws RemoteException {
        int inicioPartida = ctrl.jugarPartidaRecienIniciada();
        if(inicioPartida == PARTIDA_AUN_NO_CREADA) {
            noSePuedeIniciarPartida(PARTIDA_AUN_NO_CREADA);
        } else if (inicioPartida == FALTAN_JUGADORES){
            noSePuedeIniciarPartida(FALTAN_JUGADORES);
        } else if (inicioPartida == ifVista.INICIAR_PARTIDA) {
            //mostrarInicioPartida();
            SwingUtilities.invokeLater(() -> {
                try {
                    partida();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            mostrarFinalizoPartida();
        }
    }

    @Override
    public boolean partida() throws RemoteException {
        ctrl.notificarComienzoPartida();
        for (int j = 0; j < ctrl.getTotalRondas(); j++) {
            ctrl.notificarNuevaVentana(); //prepara la ventana para la partida
            ctrl.notificarComienzoRonda();
            ctrl.iniciarCartasPartida();
            int i = ctrl.getNumJugadorQueEmpiezaRonda();

            while (!ctrl.getCorteRonda()) {
                ctrl.notificarTurno(i);
                ctrl.notificarRobo(i);
                if (ctrl.getRoboDelMazo(i)) {
                    ctrl.setRoboDelMazo(i, false);
                    ctrl.notificarRoboConCastigo(i);
                    ctrl.resetearRoboConCastigo();
                }
                ctrl.notificarDesarrolloTurno(i);
                i++;
                if (i>ctrl.getCantJugadoresPartida()-1) {
                    i = 0;
                }
            }
            ctrl.notificarCorteRonda();
            ctrl.notificarRondaFinalizada();
            ctrl.partidaFinRonda(); //incrementa ronda
            ctrl.incNumJugadorQueEmpiezaRonda();
        }
        ctrl.determinarGanador(); //al finalizar las rondas
        mostrarFinalizoPartida();
        //lo siguiente es para poder seguir jugando otras partidas
        ctrl.removerObservadores();
        ctrl.sumarPartida();
        return false;
    }

    @Override
    public void actualizar(Object actualizacion, int indice) throws RemoteException {
        switch (indice) {//del 0 al 5 porque como maximo 6 jugadores
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                mostrarPozo(ctrl.getPozo());
                mostrarCombinacionRequerida(ctrl.getRonda());
                ifJugador jA = (ifJugador) actualizacion;
                String nombreJugador = jA.getNombre();
                if (!nombreJugador.equals(nombreVista)) {
                    mostrarTurnoJugador(nombreJugador);
                }
                if (nombreJugador.equals(nombreVista)) {
                    mostrarTurnoPropio();
                    ctrl.setTurno(indice, true);
                }
                break;
            }
            case NUEVA_PARTIDA: {
                mostrarInicioPartida();
                break;
            }
            case NUEVO_JUGADOR: {
                String nombreJugador = (String) actualizacion;
                mostrarUltimoJugadorAgregado(nombreJugador);
                break;
            }
            case ROBO: {
                ifJugador j = (ifJugador) actualizacion;
                if (j.getNombre().equals(nombreVista)) {
                    ctrl.desarrolloRobo(j.getNumeroJugador());
                }
                break;
            }
            case DESARROLLO_TURNO: {
                ifJugador j = (ifJugador) actualizacion;
                if (j.getNombre().equals(nombreVista)) {
                    ctrl.desarrolloTurno(j.getNumeroJugador()); //aca se modifica la variable corte del while
                }
                break;
            }
            case GANADOR: {
                String s = (String) actualizacion;
                mostrarGanador(s);
                break;
            }
            case ROBO_CASTIGO: {
                int numJugadorRoboCastigo = ctrl.getNumJugadorRoboCastigo();
                if (!ctrl.getRoboConCastigo(numJugadorRoboCastigo)) {
                    boolean roboConCastigo = false;
                    ifJugador j = ctrl.getJugadorPartida(numJugadorRoboCastigo);
                    mostrarPuedeRobarConCastigo(j.getNombre());
                    if (nombreVista.equals(j.getNombre())) {
                        if (ctrl.getPuedeBajar(numJugadorRoboCastigo)==0) {
                            mostrarCartas(ctrl.enviarManoJugador(numJugadorRoboCastigo));
                            if (preguntarSiQuiereRobarCastigo()) {
                                ctrl.robarConCastigo(numJugadorRoboCastigo);
                                ctrl.notificarHaRobadoConCastigo(numJugadorRoboCastigo);
                                ctrl.setRoboConCastigo(numJugadorRoboCastigo, true);
                                roboConCastigo = true;
                            }
                        }
                        if (!roboConCastigo) {
                            numJugadorRoboCastigo++;
                            if (numJugadorRoboCastigo > ctrl.getCantJugadoresPartida() - 1)
                                numJugadorRoboCastigo = 0;
                            ctrl.setNumJugadorRoboCastigo(numJugadorRoboCastigo);
                        }
                    }
                }
                break;
            }
            case HUBO_ROBO_CASTIGO: {
                String nombreJugador = ctrl.getJugadorPartida((int)actualizacion).getNombre();
                jugadorHaRobadoConCastigo(nombreJugador);
                break;
            }
            case RONDA_FINALIZADA: {
                int ronda = (int) actualizacion;
                System.out.println("La ronda " + ronda + " ha finalizado.");
                System.out.println("--------------------------");
                break;
            }
            case PUNTOS_RONDA: {
                int[] puntos = (int[]) actualizacion;
                mostrarPuntosRonda(puntos);
                break;
            }
            case 16: {
                mostrarRanking((Object[]) actualizacion);
                break;
            }
            case JUGADOR_INICIO_PARTIDA: {
                String s = (String) actualizacion;
                if (!s.equalsIgnoreCase(nombreVista)) {
                    JOptionPane.showMessageDialog(this, "El jugador " + s + " ha iniciado una partida nueva", "Aviso creación partida", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            }
            case COMIENZA_PARTIDA: {
                ArrayList<ifJugador> jugadores = (ArrayList<ifJugador>) actualizacion;
                mostrarComienzaPartida(jugadores);
                break;
            }
            case COMIENZA_RONDA: {
                mostrarComienzoRonda((int)actualizacion);
                break;
            }
            case CORTE_RONDA: {
                String nombreJugador = (String)actualizacion;
                if (!nombreJugador.equals(nombreVista)) {
                    mostrarCorto((String) actualizacion);
                } else {
                    mostrarCortoPropio();
                }
                break;
            }
            case NUEVA_VENTANA: {
                System.out.println("ejecutando nuevaVentana()");
                SwingUtilities.invokeLater(this::nuevaVentana);
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

    public void nuevaVentana() {
        System.out.println("Hilo: " + Thread.currentThread());
        System.out.println("ejecutandose en EDT? " + SwingUtilities.isEventDispatchThread());
        new VentanaJuego(VENTANA_ANCHO, VENTANA_ALTO, this);
    }

    public String asociarRuta(String carta) {
        return "src\\vista\\cartas\\" + carta + ".png";
    }

    public JButton agregarImagenCartaAPanel(String carta, float alineacionVertical, int cardIndex) {
        String rutaImagen = asociarRuta(carta.toLowerCase());
        ImageIcon icon = new ImageIcon(rutaImagen);
        Image imagenEscalada = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
        JButton btnImg = new JButton(iconoEscalado);
        btnImg.setPreferredSize(new Dimension(100, 100));
        btnImg.setAlignmentY(alineacionVertical);
        btnImg.putClientProperty("cardIndex", cardIndex); //para futura implementacion con botones
        btnImg.addActionListener(new VentanaJuego.CardSelectionListener());
        return btnImg;
    }

    @Override
    public int[] preguntarParaOrdenarCartas(int cantCartas) {
        int[] elecciones = new int[2];
        int cartaSeleccion = -1;
        while (cartaSeleccion < 0 || cartaSeleccion > cantCartas-1) {
            cartaSeleccion = Integer.parseInt(JOptionPane.showInputDialog(this, "Elije el número de carta que quieres mover: ", "Indice de carta a mover", JOptionPane.QUESTION_MESSAGE));
        }
        elecciones[0] = cartaSeleccion;

        cartaSeleccion = -1;
        while (cartaSeleccion < 0 || cartaSeleccion > cantCartas-1) {
            cartaSeleccion = Integer.parseInt(JOptionPane.showInputDialog(this, "Elije el número de destino al que quieres mover la carta: ", "Indice destino a mover", JOptionPane.QUESTION_MESSAGE));
        }
        elecciones[1] = cartaSeleccion;
        return elecciones;
    }

    @Override
    public int preguntarCantParaBajar() {
        int numCartas = 0;
        while (numCartas > 4 || numCartas < 3) {
            numCartas = Integer.parseInt(JOptionPane.showInputDialog(this, "Cuantas cartas quieres bajar para el juego? (entre 3 y 4)", "Cantidad cartas para bajar", JOptionPane.QUESTION_MESSAGE));
        }
        return numCartas;
    }

    private void jugadorHaRobadoConCastigo(String nombreJugador) {
        JOptionPane.showMessageDialog(this, "El jugador "+ nombreJugador + " ha robado con castigo.", "Hubo robo con castigo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarPuntosRonda(int[] puntos) throws RemoteException {
        StringBuilder s = new StringBuilder("Puntuación: ");
        for (int i = 0; i < puntos.length; i++) {
            String nombreJugador = ctrl.getJugadorPartida(i).getNombre();
            s.append("Jugador ").append(nombreJugador).append(": ").append(puntos[i]).append("\n");
        }
        JOptionPane.showMessageDialog(this, s, "Puntaje de la ronda", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarUltimoJugadorAgregado(String nombreJugador) {
        String s = "El jugador " + nombreJugador + " ha ingresado.";
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, s, "Nuevo jugador", JOptionPane.INFORMATION_MESSAGE));
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
        int i = 0;
        for (String c : cartas) {
            panelCartas.add(agregarImagenCartaAPanel(c, Component.BOTTOM_ALIGNMENT, i));
            i++;
        }
    }

    public int preguntarEnQueJuegoQuiereAcomodar() {
        return Integer.parseInt(JOptionPane.showInputDialog(this, "En qué número de juego quieres acomodar tu carta?", "Acomodar", JOptionPane.QUESTION_MESSAGE));
    }

    @Override
    public int[] preguntarQueBajarParaJuego(int cantCartas) {
        int[] cartasABajar = new int[cantCartas];
        String s;
        for (int i = 0; i < cartasABajar.length; i++) {
            s = "Carta " + (i + 1) + ": \nIndica el índice de la carta que quieres bajar: ";
            cartasABajar[i] = Integer.parseInt(JOptionPane.showInputDialog(this, s, "Selecciona una carta"));
        }
        return cartasABajar;
    }

    public int preguntarCartaParaAcomodar() {
        return Integer.parseInt(JOptionPane.showInputDialog(this, "Indica el número de carta que quieres acomodar en un juego", "Seleccione una carta", JOptionPane.QUESTION_MESSAGE));
    }

    @Override
    public void mostrarPuedeRobarConCastigo(String nombreJugador) {
        String s = "El jugador " + nombreJugador + " puede robar con castigo.";
        JOptionPane.showMessageDialog(this, s, "Robo con castigo", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public int preguntarQueBajarParaPozo(int cantCartas) {
        int eleccion = -1;
        String s = "Indica el índice de carta para tirar al pozo: ";
        while (eleccion < 0 || eleccion >= cantCartas) {
            eleccion = Integer.parseInt(JOptionPane.showInputDialog(this, s, "Seleccione una carta", JOptionPane.QUESTION_MESSAGE));
        }
        return eleccion;
    }

    public void mostrarNoPuedeRobarDelPozo() {
        JOptionPane.showMessageDialog(this,"No puedes robar del pozo porque no tiene cartas", "Pozo vacío", JOptionPane.INFORMATION_MESSAGE);
    }

    public int menuRobar() {
        String s = "Quieres robar del mazo o robar del pozo?\n1 - Robar del mazo\n2 - Robar del pozo\nElije una opción: ";
        return Integer.parseInt(JOptionPane.showInputDialog(this, s, "Robo", JOptionPane.QUESTION_MESSAGE));
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
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(this, "Se ha creado una partida", "Inicio partida", JOptionPane.INFORMATION_MESSAGE));
    }

    public void mostrarCombinacionRequerida(int ronda) {
        String s = "Deben bajarse para esta ronda ";
        switch (ronda) {
            case 1:
                s += "(1): 2 tríos";
                break;
            case 2:
                s += "(2): 1 trío y 1 escalera";
                break;
            case 3:
                s += "(3): 2 escaleras";
                break;
            case 4:
                s += "(4): 3 tríos";
                break;
            case 5:
                s += "(5): 2 tríos y 1 escalera";
                break;
            case 6:
                s += "(6): 1 tríos y 2 escaleras";
                break;
            case 7:
                s += "(7): 3 escaleras";
                break;
        }
        getContentPane().add(crearLabel(s, Component.TOP_ALIGNMENT));
    }

    @Override
    public void mostrarPozo(ifCarta c) {
//        panel.removeAll();
//        revalidate();
//        repaint();
        panel.add(agregarImagenCartaAPanel(ifVista.cartaToString(c), Component.CENTER_ALIGNMENT,-1));
    }

    @Override
    public void mostrarTurnoJugador(String nombreJugador) {
        String s = "Es el turno del jugador: " + nombreJugador;
        JOptionPane.showMessageDialog(this, s, "Turno del jugador", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarPuntosJugador(String nombre, int puntos) {
    }

    @Override
    public void mostrarGanador(String ganador) {
        JOptionPane.showMessageDialog(this, "El jugador " + ganador + " es el ganador!", "Ganador", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarFinalizoPartida() {
        JOptionPane.showMessageDialog(this, "La partida ha finalizado.", "Fin partida", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public int preguntarCantJugadores() {
        return 0;
    }

    @Override
    public void mostrarJuegos(ArrayList<ArrayList<String>> juegos) {
        if (juegos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tiene juegos bajados.", "No bajó juegos", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (ArrayList<String> juego : juegos) {
                mostrarCartas(juego);
            }
        }
    }



    @Override
    public void mostrarNoPuedeBajarJuego(int i) {
        if (i == YA_NO_PUEDE_BAJAR) {
            JOptionPane.showMessageDialog(this,"No puedes volver a bajar juegos en esta ronda (tampoco robar con castigo).", "Ya no puede bajar", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,"No puedes bajar porque la combinacion elegida no forma un juego valido para la ronda", "Juego inválido", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void mostrarNoPuedeAcomodarJuegoPropio() {
        JOptionPane.showMessageDialog(this,"No puede acomodar porque no tienes o no hay juegos bajados o porque la carta que deseas acomodar no hace juego con el juego elegido.", "No puede acomodar", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarLoQueFaltaParaCortar(int[] faltaParaCortar) {
        JOptionPane.showMessageDialog(this,"Para cortar: \nFaltan " + faltaParaCortar[0] + " trios y " + faltaParaCortar[1] + " escaleras", "Lo que falta para cortar", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public String getNombreVista() {
        return this.nombreVista;
    }

    @Override
    public void setNombreVista(String i) {
        nombreVista = i;
    }

    @Override
    public void noSePuedeIniciarPartida(int i) {
        if (i == PARTIDA_AUN_NO_CREADA) {
            JOptionPane.showMessageDialog(this, "La partida aun no ha sido creada. Seleccione la opcion 1: 'Iniciar partida nueva'", "Partida aún no creada", JOptionPane.INFORMATION_MESSAGE);
        } else if (i == FALTAN_JUGADORES) {
            JOptionPane.showMessageDialog(this, "Esperando que ingresen más jugadores...", "Esperando jugadores", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public int preguntarEnLosJuegosDeQueJugadorAcomodar() {
        return Integer.parseInt(JOptionPane.showInputDialog(null, "Ingresa el número de jugador en cuyo juegos bajados quieres acomodar: ", "Acomodar - elección de jugador", JOptionPane.QUESTION_MESSAGE));
    };

    @Override
    public void mostrarJuegosJugador(int numJugador) {
    };

    @Override
    public void mostrarAcomodoCarta() {
        JOptionPane.showMessageDialog(this, "Se acomodó la carta en el juego.", "Acomodo", JOptionPane.INFORMATION_MESSAGE);
    };

    @Override
    public void mostrarAdvertenciaBajarse() {
        JOptionPane.showMessageDialog(this, "Recuerda que sólo puedes bajar tus juegos dos veces durante la ronda, una en cualquier turno y otra si se procede a cortar.", "Advertencia al bajarse", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public boolean preguntarSiQuiereSeguirBajandoJuegos() {
        String resp = JOptionPane.showInputDialog(null, "Deseas bajar un juego? (Si/No)", "Bajar juego", JOptionPane.QUESTION_MESSAGE);
        return resp.equalsIgnoreCase("Si") || resp.equalsIgnoreCase("S");
    }

    @Override
    public void mostrarFinalizoTurno(){
        JOptionPane.showMessageDialog(this, "Finalizó su turno", "Fin turno", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarCorto(String nombreJugador){
        JOptionPane.showMessageDialog(this, "El jugador " + nombreJugador + " ha cortado.", "Corte", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarDebeQuedarle1o0Cartas() {
        JOptionPane.showMessageDialog(this, "Para cortar debe quedarte en la mano 1 o 0 cartas", "Cortar", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarDebeCortar() {
        JOptionPane.showMessageDialog(this, "Debes tener los juegos requeridos para la ronda y cortar si deseas bajar ahora.", "Debe cortar", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarTurnoPropio() {
        JOptionPane.showMessageDialog(this, "Es tu turno.", "Tu turno", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public boolean preguntarSiQuiereRobarCastigo() {
        int eleccion = menuRobarConCastigo();
        return eleccion == ifVista.ELECCION_ROBAR_DEL_POZO;
    }

    @Override
    public void mostrarComienzaPartida(ArrayList<ifJugador> jugadores) {
        StringBuilder s = new StringBuilder("\nCOMIENZA LA PARTIDA\nJugadores:\n");
        int i = 1;
        for (ifJugador j : jugadores) {
            s.append(i).append(" - ").append(j.getNombre()).append("\n");
            i++;
        }
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(this, s.toString(), "Comienza partida", JOptionPane.INFORMATION_MESSAGE));
    }

    @Override
    public void mostrarComienzoRonda(int ronda) {
        //SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(this, "Comienza la ronda " + ronda, "Comienza ronda", JOptionPane.INFORMATION_MESSAGE);//);
    }

    @Override
    public void mostrarCortoPropio() {
        JOptionPane.showMessageDialog(this,"Has cortado. Felicitaciones!", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public int menuRobarConCastigo() {
        return Integer.parseInt(JOptionPane.showInputDialog(null, "Quieres robar con castigo? (robar del pozo y robar del mazo)\n1 - No\n2 - Si\n", "Robo con castigo - elección", JOptionPane.QUESTION_MESSAGE));
    }

    public int menuBajar() {
        int eleccion = 0;
        String s;
        while (eleccion < 1 || eleccion > 8) {
            s = "Elije una opción: \n1 - Bajar uno o más juegos\n2 - Tirar al pozo" +
            "\n3 - Ordenar cartas\n4 - Acomodar en un juego bajado propio\n5 - Ver juegos bajados propios" +
            "\n6 - Acomodar en un juego bajado ajeno\n7 - Ver juegos bajados de todos los jugadores" +
            "\n8 - Ver pozo";
            eleccion = Integer.parseInt(JOptionPane.showInputDialog(this, s, "Menú bajar", JOptionPane.QUESTION_MESSAGE));
        }
        return eleccion;

    }
}