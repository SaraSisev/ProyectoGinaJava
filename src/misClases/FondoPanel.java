
package misClases;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

class FondoPanel extends JPanel {
    private Image imagen;

    public FondoPanel(String rutaImagen) {
        this.imagen = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        setOpaque(false);
        
    }
}
