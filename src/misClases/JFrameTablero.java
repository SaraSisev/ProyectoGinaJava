/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package misClases;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Eliba
 */
public class JFrameTablero extends javax.swing.JFrame {
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
        if(timerIniciado)return;//condcional para que no se ejecute el timer dos veces
        timerIniciado = true;//bandera para indicar que ya se ejecuto un timer
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

        jPanel1 = new javax.swing.JPanel();
        jLabelTiempo = new javax.swing.JLabel();
        lblPersonaje = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabelTiempo.setText("jLabel1");

        lblPersonaje.setText("Personaje");

        lblFecha.setText("jLabel1");

        lblNombre.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(304, Short.MAX_VALUE)
                .addComponent(lblFecha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelTiempo)
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNombre)
                    .addComponent(lblPersonaje))
                .addGap(82, 82, 82))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTiempo)
                    .addComponent(lblFecha)
                    .addComponent(lblPersonaje))
                .addGap(26, 26, 26)
                .addComponent(lblNombre)
                .addContainerGap(224, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JLabel jLabelTiempo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPersonaje;
    // End of variables declaration//GEN-END:variables
}
