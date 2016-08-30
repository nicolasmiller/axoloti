package components.processing;

import axoloti.processing.PComponent;
import axoloti.utils.Constants;
import processing.core.PApplet;

public class PLabelComponent extends PComponent {

    private String text;

    public PLabelComponent(PApplet p, String text) {
        super(p);
        this.text = text;
    }

    private int textHeight = 10;

    @Override
    public void setup() {
        PApplet p = getPApplet();
        p.textSize(Constants.FONT_POINT_SIZE);
        int width = (int) Math.round(p.textWidth(text));
        setBounds(getX(), getY(), width, textHeight);
    }

    @Override
    public void display() {
        PApplet p = getPApplet();
        p.fill(getForeground().getRGB(), getForeground().getAlpha());
        p.text(text, getBounds().x, getBounds().y + getHeight() * 4 / 5);
//        displayBounds();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextHeight(int textHeight) {
        this.textHeight = textHeight;
    }
}
