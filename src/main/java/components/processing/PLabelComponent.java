package components.processing;

import axoloti.processing.PComponent;
import axoloti.processing.PatchPApplet;
import java.util.HashMap;
import java.util.Map;
import processing.core.PApplet;
import processing.core.PFont;

public class PLabelComponent extends PComponent {

    private String text;

    static float[] sizes;
    static PFont[] fonts;

    public PLabelComponent(PApplet p, String text) {
        super(p);
        this.text = text;
        if (text == null) {
            this.text = "";
        }
    }

    private int textHeight = 9;
//    private PFont font;

    @Override
    public void setup() {
        PApplet p = getPApplet();
//        if (fonts == null) {
//            fonts = new PFont[1000];
//            sizes = new float[1000];
//
//            float size = 9.0f;
//            for (int i = 90; i < 1000; i++) {
//                fonts[i] = p.createFont("SansSerif", size, false);
//                sizes[i] = size;
//                size += 0.1;
//            }
//        }

//        p.textSize(Constants.FONT_POINT_SIZE);
        int width = (int) Math.round(p.textWidth(text));
        setBounds(getX(), getY(), width, textHeight);

//        font = p.createFont("SansSerf", 48);
//        p.textFont(font);
    }

    static Map<Integer, PFont> fontsMap = new HashMap<>();

    public PFont getFont(PApplet p, Integer size) {
        PFont f = fontsMap.get(size);
        if (f == null) {
            f = p.createFont("SansSerif", size, true);
            fontsMap.put(size, f);
        }
        return f;
    }
//    static Map<Integer, PFont> fontsMap = new HashMap<Integer, PFont>();
//
//    public PFont getFont(PApplet p, Integer size) {
//        System.out.println(size);
//        PFont f = fontsMap.get(size);
//        if (f == null) {
//            f = p.createFont("SansSerif", size, true);
//            fontsMap.put(size, f);
//        }
//        return f;
//    }

    @Override
    public void display() {
        // TODO make this faster
        PatchPApplet p = (PatchPApplet) getPApplet();
        p.pushMatrix();
        p.scale(p.getScaleInverse(), p.getScaleInverse());

        p.textFont(getFont(p, p.getScale()));
        p.textSize(p.getScale());
        p.fill(getForeground().getRGB(), getForeground().getAlpha());

        p.text(text, getBounds().x * p.getScaleFactor(),
                (int) ((getBounds().y + getBounds().height * 4f / 5f) * p.getScaleFactor()));
        p.popMatrix();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextHeight(int textHeight) {
        this.textHeight = textHeight;
    }
}
