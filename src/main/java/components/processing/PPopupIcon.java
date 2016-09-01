package components.processing;

import axoloti.Theme;
import axoloti.processing.PComponent;
import axoloti.processing.PatchPApplet;
import java.awt.Color;
import java.awt.Dimension;
import processing.core.PApplet;
import processing.core.PShape;

public class PPopupIcon extends PComponent {
//    public interface PopupIconListener {
//        public void ShowPopup();
//    }
//
//    private PopupIconListener pl;

    private final Dimension size = new Dimension(10, 12);

    private static PShape shape;

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
        if (shape == null) {
            PatchPApplet p = (PatchPApplet) getPApplet();
            shape = p.createShape();
            Color c = Theme.getCurrentTheme().Component_Primary;
            shape.setFill(p.color(c.getRGB(), c.getAlpha()));
            final int htick = 3;
            int[] xp = new int[]{getWidth() - htick * 2, getWidth(), getWidth() - htick};
            int[] yp = new int[]{0, 0, htick * 2};
            shape.beginShape();
            for (int i = 0; i < xp.length; i++) {
                shape.vertex(xp[i], yp[i]);
            }
            shape.endShape(p.CLOSE);
            shape.translate(-getWidth() / 5, getHeight() / 4);
        }
    }

    @Override
    public void display() {
        PApplet p = this.getPApplet();
        // TODO does this ever happen?
//        if (!isEnabled()) {
//            shape.noFill();
//        }
        p.shape(shape);
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
