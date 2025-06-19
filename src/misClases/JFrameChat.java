/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package misClases;

import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 *
 * @author Eliba
 */
public class JFrameChat extends javax.swing.JFrame {
    private DataOutputStream salida;
    private JPopupMenu menuPreguntas;
    //atributos de control de preguntas
    private boolean miTurno = false;
    private boolean preguntaEnviada = false;
        
    /**
     * Creates new form JFrameChat
     */
    public JFrameChat() {
        initComponents();
        this.jButtonSi.setEnabled(false);
        this.jButtonNo.setEnabled(false);
        jButtonSi.addActionListener(e -> enviarRespuesta("SÍ"));
        jButtonNo.addActionListener(e -> enviarRespuesta("NO"));

        this.inicializacionPreguntas();
        this.jButtonEnviar.addActionListener(e->enviarMensaje());
        this.jButtonPreguntas.addActionListener(e -> menuPreguntas.show(jButtonPreguntas, 0, jButtonPreguntas.getHeight()));

    }
    public void setSalida(DataOutputStream salida){
        this.salida = salida;
    }
    public void mostrarMensaje(String mensaje){
        SwingUtilities.invokeLater(() -> {
                this.jTextAreaChat.append(mensaje + "\n");
                });
    }
    private void enviarMensaje(){
        String mensaje = this.jTextFieldMensajes.getText().trim();
        if (!mensaje.isEmpty()) {
            if (!miTurno || preguntaEnviada) {
                mostrarMensaje("Debes esperar tu turno o una respuesta antes de nuevo");
                return;
            }

            try {
                // Tratamos cualquier mensaje enviado por este botón como pregunta
                String mensajeConPrefijo = "PREGUNTA:" + mensaje;
                salida.writeUTF(mensajeConPrefijo);
                mostrarMensaje("YO: " + mensaje);
                this.jTextFieldMensajes.setText("");

                preguntaEnviada = true;
                miTurno = false;
                this.jButtonPreguntas.setEnabled(false);
                this.jButtonEnviar.setEnabled(false);
            } catch (IOException e) {
                mostrarMensaje("Error al enviar mensaje");
            }
        }
    }
    private void inicializacionPreguntas(){
        menuPreguntas = new JPopupMenu();
        //categoria 1 de preguntas: color pelo
        JMenu colorPeloMenu = new JMenu("COLOR DE PELO");
        String[] colores = {"Castano", "Azul", "Rubio", "Gris", "Verde", "Pelirrojo", "Negro"};
        for(String color : colores){
            JMenuItem item = new JMenuItem(color);
            item.addActionListener(e -> enviarMensajePredefinido("TU COLOR DE PELO ES " + color +"?"));
            colorPeloMenu.add(item);
        }
        menuPreguntas.add(colorPeloMenu);
        //categoria 2 de preguntas: genero
        JMenu generoMenu = new JMenu("GENERO DE PERSONAJE");
        String[] generos = {"Hombre", "Mujer"};
        for(String genero : generos){
            JMenuItem item = new JMenuItem(genero);
            item.addActionListener(e -> enviarMensajePredefinido("TU PERSONAJE ES " + genero + "?"));
            generoMenu.add(item);
        }
        menuPreguntas.add(generoMenu);
        //categoria 3 de preguntas : madurez
        JMenu madurezMenu = new JMenu("MADUREZ DEL PERSONAJE");
        String[] madurez = {"ADULTO", "INFANTE"};
        for(String edades : madurez){
            JMenuItem item = new JMenuItem(edades);
            item.addActionListener(e -> enviarMensajePredefinido("TU PERSONAJE ES UN" + madurez + "?"));
            madurezMenu.add(item);
        }
        menuPreguntas.add(madurezMenu);
        //categoria 4 de preguntas : tipo de ser
        JMenu tipoMenu = new JMenu("TIPO DE SER");
        String[] tipo = {"HUMANO", "NO HUMANO"};
        for(String tipos : tipo){
            JMenuItem item = new JMenuItem(tipos);
            item.addActionListener(e -> enviarMensajePredefinido("TU PERSONAJE ES UN " + tipo + "?"));
            tipoMenu.add(item);
        }
        menuPreguntas.add(tipoMenu);
    }
    private void enviarMensajePredefinido(String mensaje){
        if(!miTurno || preguntaEnviada){
            mostrarMensaje("Debes esperar tu turno o una respuesta antes de nuevo");
            return;
        }
        try{
            salida.writeUTF("PREGUNTA:" + mensaje);
            mostrarMensaje("YO:" +mensaje);
            preguntaEnviada = true;
            miTurno = false;
            this.jButtonPreguntas.setEnabled(false);
            this.jButtonEnviar.setEnabled(false);
        }catch(IOException e){
            mostrarMensaje("Error");
        }
    }
    public void procesarMensajeRecibido(String mensaje){
        if (mensaje.startsWith("PREGUNTA:")) {
            mostrarMensaje("OPONENTE: " + mensaje.substring(9));
            miTurno = false;
            this.jButtonPreguntas.setEnabled(false); // no puede hacer preguntas mientras responde
            activarBotonesSiNo();
        } else if (mensaje.startsWith("RESPUESTA:")) {
            mostrarMensaje("RESPUESTA: " + mensaje.substring(10));
            preguntaEnviada = false;
            miTurno = true;
            this.jButtonPreguntas.setEnabled(true); // ahora puedes hacer otra pregunta
            this.jButtonEnviar.setEnabled(true);
        } else {
            mostrarMensaje("OPONENTE: " + mensaje); // mensaje normal
        }
    }
    private void enviarRespuesta(String respuesta){
        try {
            salida.writeUTF("RESPUESTA:" + respuesta);
            mostrarMensaje("YO RESPONDÍ: " + respuesta);
            miTurno = true;
            preguntaEnviada = false;
            this.jButtonPreguntas.setEnabled(true);
            this.jButtonEnviar.setEnabled(true);
            desactivarBotonesSiNo(); // para no volver a responder
        } catch(IOException e){
            mostrarMensaje("Error al enviar respuesta.");
        }
    }
    public void habilitarTurnoInicial(){
        this.miTurno=true;
        this.jButtonPreguntas.setEnabled(true);
        this.jButtonEnviar.setEnabled(true);
    }
    private void activarBotonesSiNo() {
        jButtonSi.setEnabled(true);
        jButtonNo.setEnabled(true);
    }

    private void desactivarBotonesSiNo() {
        jButtonSi.setEnabled(false);
        jButtonNo.setEnabled(false);
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
