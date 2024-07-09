package src.vista;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

import src.controlador.Controlador;
import src.modelo.ifCarta;
import src.modelo.ifJugador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class VentanaInicio extends JFrame implements ifVista, ActionListener {
    private Controlador ctrl;
    private String nombreVista;
    private final JPanel panel;
    private JPanel panelCartas;

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

    public JPanel crearPanel() { //lo que se ve en la pantalla inicial
        JPanel panel = new JPanel();                              
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(56, 102, 65));            
        return panel;
    }

    public void agregarAPanelInicio(JPanel panel) {
        panel.add(agregarTitulo()); //continental en grande
        
        // LABEL -------------------------------------------------
        panel.add(crearLabel()); //ingrese su nombre
        panel.add(Box.createRigidArea(new Dimension(0, 0))); // Espacio entre la etiqueta y el campo

        // FIELD -------------------------------------------------
        JTextField campoNombre = crearInputField();
        panel.add(campoNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre el icono y el campo

        // BOTON ingresar texto
        JButton botonIngresarTexto = crearBoton("Ingresar nombre", Component.CENTER_ALIGNMENT, 150, 30, 15);
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

        JButton botonJugar = crearBoton("Iniciar partida", Component.CENTER_ALIGNMENT, 200, 50, 25);
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
            partida();
            mostrarFinalizoPartida();
        }
    }

    @Override
    public boolean partida() throws RemoteException {
        ctrl.notificarComienzoPartida();
        while (ctrl.getRonda() <= ctrl.getTotalRondas()) {
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

    public void nuevaVentana() {
        Runnable runner = new Runnable() {
            @Override
            public void run() {
                panel.removeAll();
            }
        };
        try {
            EventQueue.invokeAndWait(runner);
            revalidate();
            repaint();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
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

    private void jugadorHaRobadoConCastigo(String nombreJugador) {
    }

    private void mostrarPuntosRonda(int[] puntos) {
    }

    private void mostrarUltimoJugadorAgregado(String nombreJugador) {
        String s = "El jugador " + nombreJugador + " ha ingresado.";
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, s, "Nuevo jugador", JOptionPane.INFORMATION_MESSAGE));
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
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(this, "Se ha creado una partida", "Inicio partida", JOptionPane.INFORMATION_MESSAGE));
    }

    public void mostrarCombinacionRequerida(int ronda) {
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
        JOptionPane.showMessageDialog(this, s, "Turno del jugador", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, s, "Comienza partida", JOptionPane.INFORMATION_MESSAGE));
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
        return Integer.parseInt(JOptionPane.showInputDialog(null, "Quieres robar con castigo? (robar del pozo y robar del mazo)\n1 - No\n2 - Si\n", "Robo con castigo", JOptionPane.QUESTION_MESSAGE));
    }
}