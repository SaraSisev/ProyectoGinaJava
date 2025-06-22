
package misClases;

public class JFramePerdedor extends javax.swing.JFrame {

    FondoPanel fondo = new FondoPanel("/misClases/recursos/perder.jpeg");
    
    private String nombre;//recibe los nombres del jugador
    private String tipo;//recibe si el jugador es normal o jugador-servidor
    
    
    public JFramePerdedor(String nombre, String tipo) {
        initComponents();
        this.tipo=tipo;
        this.nombre=nombre;
        txtTexto.setText(
            "<html><div style='text-align: center;'>" +
            "¡Suerte para la próxima " + nombre + "!!!<br>" +
            "Espero que te hayas divertido mucho jugando nuestro juego<br>" +
            "Si quieres volver a jugar, presiona el botón <b>Jugar de Nuevo</b><br>"+
            "o si quieres regresar a la pantalla inicial presiona el botón <b>Regresar a Inicio</b>." +
            "</div></html>"
        );
    }
    
    public JFramePerdedor() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new FondoPanel("/misClases/recursos/perder.jpeg");
        jLabel1 = new javax.swing.JLabel();
        btnDeNuevo = new javax.swing.JButton();
        btnInicio = new javax.swing.JButton();
        txtTexto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setBackground(new java.awt.Color(200, 156, 175));
        jLabel1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 18)); // NOI18N
        jLabel1.setText("PERDEDOR");
        jLabel1.setOpaque(true);

        btnDeNuevo.setBackground(new java.awt.Color(178, 138, 177));
        btnDeNuevo.setFont(new java.awt.Font("Tempus Sans ITC", 0, 15)); // NOI18N
        btnDeNuevo.setText("Jugar de Nuevo");
        btnDeNuevo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.black, java.awt.Color.black));
        btnDeNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeNuevoActionPerformed(evt);
            }
        });

        btnInicio.setBackground(new java.awt.Color(178, 138, 177));
        btnInicio.setFont(new java.awt.Font("Tempus Sans ITC", 0, 15)); // NOI18N
        btnInicio.setText("Regresar a Inicio");
        btnInicio.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.black, java.awt.Color.black));
        btnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioActionPerformed(evt);
            }
        });

        txtTexto.setBackground(new java.awt.Color(200, 156, 175));
        txtTexto.setFont(new java.awt.Font("Tempus Sans ITC", 2, 16)); // NOI18N
        txtTexto.setText("Texto");
        txtTexto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        txtTexto.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(216, 216, 216))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDeNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 229, Short.MAX_VALUE)
                .addComponent(btnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(txtTexto)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(txtTexto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 185, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioActionPerformed
        this.dispose();  
        JFramePresentacionJuego presentacion = new JFramePresentacionJuego();
        presentacion.setVisible(true);
    }//GEN-LAST:event_btnInicioActionPerformed

    private void btnDeNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeNuevoActionPerformed
        this.dispose();  
        JFrameMenu men = new JFrameMenu(nombre, tipo);
        men.setVisible(true);
    }//GEN-LAST:event_btnDeNuevoActionPerformed


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
            java.util.logging.Logger.getLogger(JFramePerdedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFramePerdedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFramePerdedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFramePerdedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFramePerdedor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeNuevo;
    private javax.swing.JButton btnInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel txtTexto;
    // End of variables declaration//GEN-END:variables
}
