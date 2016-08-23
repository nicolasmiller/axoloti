package components.processing;

import axoloti.processing.PComponent;
import processing.core.PApplet;

public class PLabelComponent extends PComponent {
    private String text;

    public PLabelComponent(PApplet p, String text) {
        super(p);
        this.text = text;
    }
    
    private int textHeight = 20;
    
    @Override
    public void setup() {
        PApplet p = getPApplet();
        p.textSize(textHeight);
        int width = (int) Math.round(p.textWidth(text));
        setBounds(getX() + PADDING, getY() + PADDING, width + 2 * PADDING, textHeight + 2 * PADDING);
    }

    @Override
    public void display() {
        PApplet p = getPApplet();
        p.fill(getForeground().getRGB(), getForeground().getAlpha());
        p.text(text, getBounds().x, getBounds().y, getBounds().width, getBounds().height);
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public void setTextHeight(int textHeight) {
        this.textHeight = textHeight;
    }
}
