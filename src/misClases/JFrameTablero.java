/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package misClases;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Eliba
 */
public class JFrameTablero extends javax.swing.JFrame {
    
    FondoPanel fondo = new FondoPanel("/misClases/recursos/portada1.jpeg");
    //atributos para recibir datos de otros JFrames
    private String nombre;//recibe los nombres del jugador
    private String personaje;//recibe el nombre del personaje escogido
    private String tipo;//recibe si el jugador es normal o jugador-servidor

    //atributos para el aspecto de tiempo transcurrido en la pantalla
    private Timer timer;
    private int minutos;
    private int segundos;
    //frame del chat para abrirlo
    private JFrameChat chat;
    private boolean timerIniciado = false;
    /**
     * Creates new form JFrameTablero
     */
    public JFrameTablero() {
        initComponents();
        
    }
    //constructor que recibe el nombre y tipo de jugador desde el frame principal
    public JFrameTablero(String nombre, String tipo, String personaje){
        initComponents();
        this.tipo=tipo;
        this.nombre=nombre;
        this.personaje=personaje;
        lblPersonaje.setText(personaje);
        lblNombre.setText(nombre);
        
        cambiarIcono(jButton1, ControlMusica.estaPausada()
            ? "/misClases/recursos/playBtn.png"
            : "/misClases/recursos/pauseBtn.png");
        
        if (!ControlMusica.estaPausada()) {
            ControlMusica.iniciarMusica("/misClases/recursos/MusicaInicio.wav");
        }
        //iniciarTiempoJuego();
        //condicional para verificar que tipo de jugador es
        if(tipo.equals("servidor")){
            //si es el servidor iniciara el servidor con ese metodo
            iniciarServidor();
            //iniciarTiempoJuego();
        }else{
            //sino sera el jugador y se iniciara como tal
            iniciarJugador();
        }
        generarTableroPersonajes();
    }
    public void iniciarServidor(){
        new Thread(() -> {//permite ejecutar el codigo en paralelo sin bloquearme la interfaz
            try {
                //instanciamos el frame del chat
                chat = new JFrameChat(true);
                //lo hacemos visible
                chat.setVisible(true);
                //IMPORTANTE despues de la instancia, instanciamos el server mandandole el frame
                Servidor server = new Servidor(this, chat);
                System.out.println("Iniciando servidor del juego");
                //usamos su metodo para iniciar el servidor y poder mandar mensajes
                server.startServer();
                //metodo del frame para representar que nuestra conexion fue lista y empezar el conteo de tiempo de partida
                conexionLista();
                
                        
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null, "Error al iniciar el servidor")
                );
                ex.printStackTrace();
            }
        }).start();//inicio de un hilo para evitar el congelamiento de la interfaz grafica
    }
    public void iniciarJugador(){
        new Thread(() -> {//permite ejecutar el codigo en paralelo sin bloquearme la interfaz
            try {
                //se pide la ip del servidor 
                String ipServidor = JOptionPane.showInputDialog("Introduce la IP del servidor");
                chat = new JFrameChat(false);//se instancia nuestro frame de chat
                chat.setVisible(true);//se muestra el frame
                //IMPORTANTE despues de la instancia ahora si instanciamos a jugador para mandare a chat
                Jugador jugador = new Jugador(ipServidor,chat);
    
                System.out.println("Iniciando como jugador");
                
                //iniciamos el conteo de partida
                SwingUtilities.invokeLater(this::iniciarTiempoJuego);
                //inicia el envio de mensajes
                jugador.startJugador();
                
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null, "Error al iniciar como jugador")
                );
                ex.printStackTrace();
            }
        }).start();//inicio de un hilo para evitar el congelamiento de la interfaz grafica
    }
    
    private void iniciarTiempoJuego(){
        if(timerIniciado)return;
        timerIniciado = true;
        System.out.println("timer ");
        timer = new Timer(1000, new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    segundos++;
                    if(segundos >=60){
                        segundos = 0;
                        minutos ++;
                    }
                   jLabelTiempo.setText(String.format("Duracion: %02d:%02d", minutos, segundos));
                }
                });
        timer.start();
    }
    public void conexionLista(){
        SwingUtilities.invokeLater(this::iniciarTiempoJuego);
    }
    void mostrarNombre(String nombre){
        
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new FondoPanel("/misClases/recursos/portada1.jpeg");
        jLabelTiempo = new javax.swing.JLabel();
        lblPersonaje = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnPersonaje = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        jLabelTiempo.setText("Timer");

        lblPersonaje.setText("Personaje");

        lblFecha.setText("Fecha");

        lblNombre.setText("Nombre");

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 608, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misClases/recursos/portada.jpeg"))); // NOI18N
        jLabel1.setText("jLabel1");

        btnPersonaje.setText("<html>Confirmar personaje<br>a adivinar<html>");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misClases/recursos/pauseBtn.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.setLabel("");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFecha)
                            .addComponent(lblPersonaje)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNombre)
                                    .addComponent(jLabelTiempo)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(btnPersonaje, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(75, 75, 75))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(lblFecha))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelTiempo)
                        .addGap(18, 18, 18)
                        .addComponent(lblNombre)
                        .addGap(34, 34, 34)
                        .addComponent(lblPersonaje)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btnPersonaje, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ControlMusica.pausarReanudar();

        if (ControlMusica.estaPausada()) {
            cambiarIcono(jButton1, "/misClases/recursos/playBtn.png");
        } else {
            cambiarIcono(jButton1, "/misClases/recursos/pauseBtn.png");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameTablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameTablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameTablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameTablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameTablero().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPersonaje;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelTiempo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPersonaje;
    // End of variables declaration//GEN-END:variables

    private void cambiarIcono(javax.swing.JButton boton, String rutaImagen) {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(rutaImagen));
        Image img = originalIcon.getImage().getScaledInstance(boton.getWidth(), boton.getHeight(), Image.SCALE_SMOOTH);
        boton.setIcon(new ImageIcon(img));
    }
    
    private void generarTableroPersonajes() {
            // Paso 1: Lista de imágenes
            List<String> imagenes = new ArrayList<>();
            Collections.addAll(imagenes,
                "/misClases/recursos/personajes/Alcalde Quimby.png",
                "/misClases/recursos/personajes/Apu.png",
                "/misClases/recursos/personajes/Barney.png",
                "/misClases/recursos/personajes/Bart.png",
                "/misClases/recursos/personajes/Bruja Lisa.png",
                "/misClases/recursos/personajes/Capitan Horatio.png",
                "/misClases/recursos/personajes/Carl.png",
                "/misClases/recursos/personajes/Duffman.png",
                "/misClases/recursos/personajes/Evil Flanders.png",
                "/misClases/recursos/personajes/Funzo.png",
                "/misClases/recursos/personajes/Gato Marge.png",
                "/misClases/recursos/personajes/Hans Moleman.png",
                "/misClases/recursos/personajes/Herman.png",
                "/misClases/recursos/personajes/Heroe Milhouse.png",
                "/misClases/recursos/personajes/Homero.png",
                "/misClases/recursos/personajes/Kodos.png",
                "/misClases/recursos/personajes/Krusty.png",
                "/misClases/recursos/personajes/Lenny.png",
                "/misClases/recursos/personajes/Lisa.png",
                "/misClases/recursos/personajes/Maggie.png",
                "/misClases/recursos/personajes/Marge.png",
                "/misClases/recursos/personajes/Melvin.png",
                "/misClases/recursos/personajes/Milhouse.png",
                "/misClases/recursos/personajes/Moe.png",
                "/misClases/recursos/personajes/Nick Riviera.png",
                "/misClases/recursos/personajes/Otto.png",
                "/misClases/recursos/personajes/Patty.png",
                "/misClases/recursos/personajes/Payaso Rapha.png",
                "/misClases/recursos/personajes/Pica.png",
                "/misClases/recursos/personajes/Rasca.png",
                "/misClases/recursos/personajes/Selma.png",
                "/misClases/recursos/personajes/Snake.png",
                "/misClases/recursos/personajes/Sr Burns.png",
                "/misClases/recursos/personajes/Willie.png"     
            );

            // Paso 2: Barajar y tomar solo 24
            Collections.shuffle(imagenes);
            List<String> seleccionados = imagenes;

            // Paso 3: Configurar el panel
            jPanel2.removeAll();
            jPanel2.setLayout(new GridLayout(5, 7, 20, 20));



            // Paso 4: Crear y añadir los botones
            for (String ruta : seleccionados) {
                JButton boton = new JButton();

                // Icono
                java.net.URL url = getClass().getResource(ruta);
                if (url != null) {
                    ImageIcon icono = new ImageIcon(url);
                    Image imagen = icono.getImage().getScaledInstance(65, 85, Image.SCALE_SMOOTH);
                    boton.setIcon(new ImageIcon(imagen));
                } else {
                    boton.setText("No encontrada");
                }


                boton.setText(null); // Quita texto
                boton.setBorderPainted(false); // Quita borde
                boton.setContentAreaFilled(false); // Hace fondo transparente
                boton.setFocusPainted(false); // Quita el marco cuando se enfoca

                boton.addActionListener(e -> {
                    // Cargar la imagen de portada
                    java.net.URL urlPortada = getClass().getResource("/misClases/recursos/portada.jpeg");

                    ImageIcon iconoPortada = new ImageIcon(urlPortada);
                    Image imagenPortada = iconoPortada.getImage().getScaledInstance(65, 85, Image.SCALE_SMOOTH);
                    ImageIcon portadaIcon = new ImageIcon(imagenPortada);
                    
                    // Cambiar ícono visible
                    boton.setIcon(portadaIcon);

                    // Establecer el ícono también como el "disabled icon"
                    boton.setDisabledIcon(portadaIcon);
                });

                jPanel2.add(boton);
            }

            jPanel2.revalidate();
            jPanel2.repaint();
        }


    
}
