package src.vista;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

import src.controlador.Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaInicio extends JFrame {
    Controlador ctrl = new Controlador();
    VentanaJuego ventanaJuego;
    boolean partidaIniciada = false;

    public VentanaInicio(int ancho, int alto) {
        setSize(ancho, alto);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // cerrar ventana cuando se selecciona la X
        setLocationRelativeTo(null);                    // centrar la ventana 
        agregarAPanelInicio(crearPanel());  
        agregarMenuBarra();
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

        botonIniciar.addMouseListener(new java.awt.event.MouseAdapter() {   // color al pasar el mouse
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonIniciar.setBackground(new Color(200, 220, 120)); 
            }

            public void mouseExited(java.awt.event.MouseEvent evt) { // vuelve al color original cuando sale el mouse
                botonIniciar.setBackground(new Color(167, 201, 87)); 
            }
        });

        iniciarPartidaConBoton(botonIniciar);
        // botonIniciar.addActionListener(new ActionListener() {  // realizar accion cuando se hace click
        //     public void actionPerformed(ActionEvent e) {
        //         VentanaJuego ventanaJuego = new VentanaJuego();
                
        //         ventanaJuego.setVisible(true);
        //         setPartidaIniciada();
        //     }
        // });

        panel.add(Box.createVerticalGlue());          // agregar espacio vertical antes del botón
        panel.add(botonIniciar);                           

        panel.add(agregarImagen("src\\vista\\imgs\\cartas_inicio.png"));

        getContentPane().add(panel, BorderLayout.CENTER);   // agregar el panel a la ventana
    }

    public void iniciarPartidaConBoton(JButton botonIniciar) {
        botonIniciar.addActionListener(new ActionListener() {  // realizar accion cuando se hace click
            public void actionPerformed(ActionEvent e) {
                VentanaJuego ventanaJuego = new VentanaJuego();
                ventanaJuego.setVisible(true);
                setPartidaIniciada();
            }
        });
        this.ventanaJuego = ventanaJuego;
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

    public void iniciarVentana(String titulo) {
        this.setTitle(titulo);                
        this.setVisible(true); 
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

    public JLabel agregarImagen(String rutaImagen) {
        ImageIcon imagen = new ImageIcon(rutaImagen); 
        JLabel labelImagen = new JLabel(imagen);
        labelImagen.setAlignmentX(Component.CENTER_ALIGNMENT);      // centrar la imagen horizontalmente
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

    public void setPartidaIniciada() {
        this.partidaIniciada = !partidaIniciada;
    }

    public boolean getPartidaIniciada() {
        return this.partidaIniciada;
    }

    public VentanaJuego getVentanaJuego() {
        return this.ventanaJuego;
    }
}