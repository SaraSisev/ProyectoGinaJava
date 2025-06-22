
package misClases;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private long startTime;//timepo del inicio de partida
    private int duracionSegundos;//almacena la duracion total de la paritda
    //frame del chat para abrirlo
    private JFrameChat chat;
    private boolean timerIniciado = false;
    
    public String getPersonaje(){
        return personaje;
    }

    /**
     * Creates new form JFrameTablero
     */
    public void setPersonaje(String personaje) {    
        this.personaje = personaje;
    }

    //constructor que recibe el nombre y tipo de jugador desde el frame principal
    public JFrameTablero(String nombre, String tipo, String personaje) {
        initComponents();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        setLocation(screenWidth - frameWidth, (screenHeight - frameHeight) / 2);

        generarTableroPersonajes();
        this.tipo=tipo;
        this.nombre=nombre;
        this.personaje=personaje;
        
        String rutaImagen = "/misClases/recursos/personajes/" + personaje + ".png";
        java.net.URL url = getClass().getResource(rutaImagen);

        if (url != null) {
            ImageIcon icono = new ImageIcon(url);
            Image imagenEscalada = icono.getImage().getScaledInstance(85, 100, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(imagenEscalada));
            lblImg.setText(""); // Quita texto
        } else {
            lblImg.setText("Imagen no encontrada");
        }
        
        lblPersonaje.setText("Personaje: " + personaje);
        lblNombre.setText("Nombre: " + nombre);
        
        cambiarIcono(jButton1, ControlMusica.estaPausada()
                ? "/misClases/recursos/playBtn.png"
                : "/misClases/recursos/pauseBtn.png");
        
        if (!ControlMusica.estaPausada()) {
            ControlMusica.iniciarMusica("/misClases/recursos/MusicaInicio.wav");
        }
        
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblFecha.setText("Fecha: " + LocalDate.now().format(formato));

        //condicional para verificar que tipo de jugador es
        if(tipo.equals("servidor")){
            //si es el servidor iniciara el servidor con ese metodo
            iniciarServidor();
            //iniciarTiempoJuego();
        }else{
            //sino sera el jugador y se iniciara como tal
            iniciarJugador();
        }
    }
    
    public JFrameTablero() {
        initComponents();
        
    }
    
    public void iniciarServidor(){
        new Thread(() -> {//permite ejecutar el codigo en paralelo sin bloquearme la interfaz
            try {
                //instanciamos el frame del chat
                chat = new JFrameChat(true, this, nombre, personaje, tipo);
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
                chat = new JFrameChat(false, this, nombre, personaje, tipo);//se instancia nuestro frame de chat
                chat.setVisible(true);//se muestra el frame
                //IMPORTANTE despues de la instancia ahora si instanciamos a jugador para mandare a chat
                Jugador jugador = new Jugador(ipServidor,chat,nombre,this);
    
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
        if(timerIniciado)return;//condcional para que no se ejecute el timer dos veces
        timerIniciado = true;//bandera para indicar que ya se ejecuto un timer
        System.out.println("timer ");
        startTime = System.currentTimeMillis();//se registra el inicio
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
    //metodo para detener el timepo de la partida
    public void detenerTiempo(){
        if(timer != null && timer.isRunning()){//mientras que el timer no sea nulo y se este ejecutando
            timer.stop();//se detiene el conteo por parte del timer
            duracionSegundos = minutos * 60 +segundos;
        }
    }
    public int getDuracionSegundos(){
        return duracionSegundos;
    }
    public void conexionLista(){
        SwingUtilities.invokeLater(this::iniciarTiempoJuego);
    }
    void mostrarNombre(String nombre){
        
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
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
        jButton1 = new javax.swing.JButton();
        lblImg = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        jLabelTiempo.setFont(new java.awt.Font("Tempus Sans ITC", 2, 14)); // NOI18N
        jLabelTiempo.setText("Duración");
        jLabelTiempo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblPersonaje.setFont(new java.awt.Font("Tempus Sans ITC", 3, 14)); // NOI18N
        lblPersonaje.setText("Personaje");

        lblFecha.setFont(new java.awt.Font("Tempus Sans ITC", 0, 14)); // NOI18N
        lblFecha.setText("Fecha");

        lblNombre.setFont(new java.awt.Font("Tempus Sans ITC", 3, 14)); // NOI18N
        lblNombre.setText("Nombre");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 790, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 473, Short.MAX_VALUE)
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misClases/recursos/pauseBtn.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/misClases/recursos/portada.jpeg"))); // NOI18N
        lblImg.setText("jLabel1");
        lblImg.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.black, java.awt.Color.black));

        jLabel1.setFont(new java.awt.Font("Tempus Sans ITC", 3, 14)); // NOI18N
        jLabel1.setText("<html>Adivina Quién <br> Edición Los Simpsons<html>");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblImg, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNombre)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(lblPersonaje))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(lblFecha))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabelTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(29, 29, 29))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(97, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblFecha)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelTiempo)
                        .addGap(51, 51, 51)
                        .addComponent(lblPersonaje)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblImg, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblNombre)
                        .addGap(113, 113, 113))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelTiempo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblImg;
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
