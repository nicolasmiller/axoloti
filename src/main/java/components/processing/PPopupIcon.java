package components.processing;

import axoloti.Theme;
import axoloti.processing.PComponent;
import java.awt.Color;
import java.awt.Dimension;
import processing.core.PApplet;

public class PPopupIcon extends PComponent {
//    public interface PopupIconListener {
//        public void ShowPopup();
//    }
//
//    private PopupIconListener pl;

    private final Dimension size = new Dimension(10, 12);

    public PPopupIcon(PApplet p) {
        super(p);

        //       addMouseListener(this);
    }

//    public void setPopupIconListener(PopupIconListener pl) {
//        this.pl = pl;
//    }
//    public void setMinSize(int dim) {
//        this.minSize = new Dimension(dim, dim);
//        setSize(minSize);
//    }
    @Override
    public void setup() {
        setBounds(0,
                0,
                (int) (size.getWidth()),
                (int) (size.getHeight()));
    }

    @Override
    public void display() {
        PApplet p = this.getPApplet();
        p.pushStyle();
        Color c = Theme.getCurrentTheme().Component_Primary;
        p.fill(c.getRGB(), c.getAlpha());
        final int htick = 3;
        int[] xp = new int[]{getWidth() - htick * 2, getWidth(), getWidth() - htick};
        int[] yp = new int[]{0, 0, htick * 2};

        if (!isEnabled()) {
            p.noFill();
        }
        p.pushMatrix();
        p.translate(-getWidth() / 5, getHeight() / 4);
        p.beginShape();
        for (int i = 0; i < xp.length; i++) {
            p.vertex(getBounds().x + xp[i], getBounds().y + yp[i]);
        }
        p.endShape(p.CLOSE);
        p.popMatrix();
//        displayBounds();
//        drawCenterLines();

        p.popStyle();

//        p.stroke(0);
//        p.strokeWeight(1);
//        p.noFill();
//        p.rect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
    }

//    @Override
//    public void mouseClicked(MouseEvent e) {
//    }
//
//    @Override
//    public void mousePressed(MouseEvent e) {
//        pl.ShowPopup();
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
//    }
//
//    @Override
//    public void mouseEntered(MouseEvent e) {
//    }
//
//    @Override
//    public void mouseExited(MouseEvent e) {
//    }
}
