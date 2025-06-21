/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package misClases;
//utimo avance
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import java.sql.*;
import java.util.*;

/**
 *
 * @author Eliba
 */
public class JFrameChat extends javax.swing.JFrame {
    private DataOutputStream salida;
    private JPopupMenu menuPreguntas;
    //atributos para el control de preguntas 
    private GameState currentState;
    private boolean isServer;//bandera que indica si el jugador es server y asi darle la primer pregunta
    private boolean preguntaPendiente = false;//bandera que representa la espera de pregunta
    //ATRIBUTOS PARA DEFINIR GANADOR
    private boolean esPreguntaFinal=false;
    private boolean esperarRespuesta=false;
    private JFrameChat() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    //enumeracion con los estados de juego actuando como turnos
    private enum GameState {
        MY_TURN,//representa el turno del servidor
        OPPONENT_TURN,//representa el tunro del jugador para preguntar
        AWAITING_REPLY//representa la espera de respuestas
    }
    
    //consutrcutor que recibe true si fue instanciado en el logico del servidor o false si fue en el de jugador
    public JFrameChat(boolean isServer) {
        initComponents();
        this.isServer = isServer;
        if (isServer) {
            currentState = GameState.MY_TURN;//indica que el servidor inicia preguntando
        } else {
            currentState = GameState.OPPONENT_TURN;//jugador inicia esperando pregunta
        }
        
        updateUIState();
        //configuracion inicial de listeners y los mensajes que son enviados a el textarea
        jButtonSi.addActionListener(e -> enviarRespuesta("SI"));
        jButtonNo.addActionListener(e -> enviarRespuesta("NO"));
        jButtonEnviar.addActionListener(e -> enviarMensaje());
        jButtonPreguntas.addActionListener(e -> menuPreguntas.show(jButtonPreguntas, 0, jButtonPreguntas.getHeight()));
        
        inicializacionPreguntas();
    }

    //para estar actualizando constantemente el UI segun el estado del juego
    private void updateUIState() {
        SwingUtilities.invokeLater(() -> {
         switch (currentState) {
                case MY_TURN:
                    enableQuestionMode();
                    break;
                    
                case OPPONENT_TURN:
                    disableAllControls();
                    break;
                    
                case AWAITING_REPLY:
                    if (preguntaPendiente) {
                        disableQuestionControls();
                    } else {
                        enableAnswerMode();
                    }
                    break;
            }   
        });
    }
    //metodo que representa el estado de pregunta
    private void enableQuestionMode() {
        jButtonEnviar.setEnabled(true);
        jButtonPreguntas.setEnabled(true);
        jButtonSi.setEnabled(false);
        jButtonNo.setEnabled(false);
        jTextFieldMensajes.setEnabled(true);
    }
    //metodo que representa el estado de respuesta
    private void enableAnswerMode() {
        jButtonEnviar.setEnabled(false);
        jButtonPreguntas.setEnabled(false);
        jButtonSi.setEnabled(true);
        jButtonNo.setEnabled(true);
        jTextFieldMensajes.setEnabled(false);
    }
    //metodo que desactiva todos los botones 
    private void disableQuestionControls(){
        jButtonEnviar.setEnabled(false);
        jButtonPreguntas.setEnabled(false);
        jButtonSi.setEnabled(false);
        jButtonNo.setEnabled(false);
        jTextFieldMensajes.setEnabled(false); 
    }
    private void disableAllControls(){
        disableQuestionControls();
    }
    //metodo que envia el mensaje y lo muestra en el textarea
    private void enviarMensaje() {
        String mensaje = jTextFieldMensajes.getText().trim();
        if (!mensaje.isEmpty()) {
            try {
                salida.writeUTF("PREGUNTA:" + mensaje);
                mostrarMensaje("YO: " + mensaje);
                jTextFieldMensajes.setText("");
                
                // Actualizar estado
                currentState = GameState.AWAITING_REPLY;
                preguntaPendiente=true;
                updateUIState();
            } catch (IOException e) {
                mostrarMensaje("Error al enviar mensaje");
            }
        }
    }
    //metodo que manda los mensajes de las preguntas ya definidas
    private void enviarMensajePredefinido(String mensaje) {
        try {
            salida.writeUTF("PREGUNTA:" + mensaje);
            mostrarMensaje("YO: " + mensaje);
            
            // Actualizar estado
            currentState = GameState.AWAITING_REPLY;
            preguntaPendiente=true;
            updateUIState();
        } catch (IOException e) {
            mostrarMensaje("Error al enviar pregunta");
        }
    }
    //metodo para mandar la respuesta a la pregunta
    private void enviarRespuesta(String respuesta) {
    try {
        salida.writeUTF("RESPUESTA:" + respuesta);
        mostrarMensaje("YO RESPONDÍ: " + respuesta);
        
        // Manejar respuesta a pregunta final (solo para el que responde)
        if (esPreguntaFinal) {
            if (respuesta.equals("SI")) {
                SwingUtilities.invokeLater(() -> {
                    JFramePerdedor framePer = new JFramePerdedor();
                    framePer.setVisible(true);//se acepta que es el presonaje y perdi
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    JFrameGanador frameGan = new JFrameGanador();
                    frameGan.setVisible(true);//no es mi personaje asi que gane
                });
            }
            disableAllControls();
            return;
        }
        
        currentState = GameState.MY_TURN;
        preguntaPendiente = false;
        updateUIState();
    } catch (IOException e) {
        mostrarMensaje("Error al enviar respuesta");
    }
}
    //metodo para procesar el mensaje que se recibio
    public void procesarMensajeRecibido(String mensaje) {
        if (mensaje.startsWith("PREGUNTA:")) {
            String pregunta = mensaje.substring(9);
            mostrarMensaje("OPONENTE: " + pregunta);
            
            //detectar pregunta final
            if(pregunta.startsWith("TU PERSONAJE ES ") && pregunta.endsWith("?")){
                esPreguntaFinal = true;
            }
            //cambiar a modo de respuesta
            currentState = GameState.AWAITING_REPLY;
            preguntaPendiente=false;
            updateUIState();
            
        } else if (mensaje.startsWith("RESPUESTA:")) {
            String respuesta = mensaje.substring(10);
            mostrarMensaje("RESPUESTA: " + respuesta);
            
            //manejar respuesta a pregunta final
            if (esPreguntaFinal) {
            if (respuesta.equals("SI")) {
                //si responden si gane
                SwingUtilities.invokeLater(() -> {
                    JFrameGanador frameGan = new JFrameGanador();
                    frameGan.setVisible(true);
                });
            } else {
                //sí respondieron no se perdio
                SwingUtilities.invokeLater(() -> {
                    JFramePerdedor framePer = new JFramePerdedor();
                    framePer.setVisible(true);
                });
            }
            esPreguntaFinal = false;
            disableAllControls();
            return;
            }
            //despues de recibir respuesta y verificar que no sea la ultima es mi turno para pregntar
            currentState = GameState.MY_TURN;
            preguntaPendiente = false;
            updateUIState();
            
            updateUIState();
        } else {
            mostrarMensaje("OPONENTE: " + mensaje);
        }
    }
   //metodo para mostrar el mensaje en nuestro textarea
    public void mostrarMensaje(String mensaje){
        SwingUtilities.invokeLater(() -> {
                this.jTextAreaChat.append(mensaje + "\n");
                });
    }
    //metodo para inicializar las preguntas que tenemos en la BD ya definidas en listas que tienen sublistas
    private void inicializacionPreguntas(){
        menuPreguntas = new JPopupMenu();
        Map<String, List<String>> preguntasPorCategoria = new HashMap<>();

        try (Connection conn = ConBD.conectar()) {
            String query = "SELECT categoria, valor FROM preguntas";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String categoria = rs.getString("categoria");
                String valor = rs.getString("valor");

                preguntasPorCategoria
                    .computeIfAbsent(categoria, k -> new ArrayList<>())
                    .add(valor);
            }

            for (Map.Entry<String, List<String>> entry : preguntasPorCategoria.entrySet()) {
                String categoria = entry.getKey();
                List<String> opciones = entry.getValue();

                JMenu subMenu = new JMenu(categoria);
                for (String opcion : opciones) {
                    JMenuItem item = new JMenuItem(opcion);
                    if(categoria.equals("PREGUNTA FINAL")){
                        item.addActionListener(e -> {
                                esPreguntaFinal = true;
                                esperarRespuesta = true;
                        });
                    }
                    //para construir la pregunta direcamente 
                    item.addActionListener(e -> enviarMensajePredefinido("TU PERSONAJE ES " + opcion + "?"));
                    subMenu.add(item);
                }
                menuPreguntas.add(subMenu);
            }

        } catch (SQLException e) {
            mostrarMensaje("Error al cargar preguntas de la base de datos.");
            e.printStackTrace();
        }
    }
    //okay para logica del ganador, tengo pensado mostrar ya como pregunta final la adivinanza del personaje
    //para esto necesito necesito condicional que si mandan esa pregunta y respuesta es verdadera,sale ganador sino sale perdedor
    public void setSalida(DataOutputStream salida) {
        this.salida = salida;
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaChat = new javax.swing.JTextArea();
        jButtonEnviar = new javax.swing.JButton();
        jTextFieldMensajes = new javax.swing.JTextField();
        jButtonPreguntas = new javax.swing.JButton();
        jButtonSi = new javax.swing.JButton();
        jButtonNo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextAreaChat.setEditable(false);
        jTextAreaChat.setColumns(20);
        jTextAreaChat.setRows(5);
        jScrollPane1.setViewportView(jTextAreaChat);

        jButtonEnviar.setText("Enviar");

        jTextFieldMensajes.setText("jTextField1");

        jButtonPreguntas.setText("Preguntas");

        jButtonSi.setText("SI");
        jButtonSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSiActionPerformed(evt);
            }
        });

        jButtonNo.setText("NO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextFieldMensajes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonEnviar))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonPreguntas)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonSi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonNo)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonEnviar)
                            .addComponent(jTextFieldMensajes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(21, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonPreguntas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonSi)
                            .addComponent(jButtonNo))
                        .addGap(82, 82, 82))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSiActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameChat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonEnviar;
    private javax.swing.JButton jButtonNo;
    private javax.swing.JButton jButtonPreguntas;
    private javax.swing.JButton jButtonSi;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaChat;
    private javax.swing.JTextField jTextFieldMensajes;
    // End of variables declaration//GEN-END:variables
}
