package components.processing;

import axoloti.Theme;
import java.awt.Dimension;
import axoloti.processing.PComponent;
import java.awt.Color;
import processing.core.PApplet;

public class PPopupIcon extends PComponent {
//    public interface PopupIconListener {
//        public void ShowPopup();
//    }
//
//    private PopupIconListener pl;

    private Dimension minSize = new Dimension(20, 20);

    public PPopupIcon(PApplet p) {
        super(p);
        setSize(minSize);
 //       addMouseListener(this);
    }

//    public void setPopupIconListener(PopupIconListener pl) {
//        this.pl = pl;
//    }
    
    public void setMinSize(int dim) {
        this.minSize = new Dimension(dim, dim);
    }
    
    @Override
    public void setup() {
        setBounds(PADDING, 
                PADDING,  
                (int) (minSize.getWidth()), 
                (int) (minSize.getHeight()));
    }
    
    @Override
    public void display() {
        PApplet p = this.getPApplet();
        Color c = Theme.getCurrentTheme().Component_Primary;
        p.fill(c.getRGB(), c.getAlpha());
        final float rmargin = getHeight() / 4.0f;
        final float htick = getHeight() / 4.0f;
        float[] xp = new float[]{getWidth() - rmargin - htick * 2, getWidth() - rmargin, getWidth() - rmargin - htick};
        final float vmargin = getHeight() / 3.0f;
        float[] yp = new float[]{vmargin, vmargin, vmargin + htick * 2};
        if (!isEnabled()) {
            p.noFill();
        }
        p.beginShape();
        for(int i = 0; i < xp.length; i++) {
            p.vertex(getBounds().x + xp[i], getBounds().y + yp[i]);
        }
        p.endShape(p.CLOSE);
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
