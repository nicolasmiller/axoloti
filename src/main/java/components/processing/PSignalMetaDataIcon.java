package components.processing;

import axoloti.Theme;
import axoloti.datatypes.SignalMetaData;
import axoloti.processing.PComponent;
import processing.core.PApplet;

public class PSignalMetaDataIcon extends PComponent {

    private final SignalMetaData smd;

    public PSignalMetaDataIcon(PApplet p, SignalMetaData smd) {
        super(p);
        this.smd = smd;
    }
    private final int x1 = 2;
    private final int x2 = 5;
    private final int x2_5 = 7;
    private final int x3 = 9;
    private final int x4 = 12;
    private final int y1 = 12;
    private final int y2 = 2;

    @Override
    public void setup() {
        setBackground(Theme.getCurrentTheme().Object_Default_Background);
        setBounds(0, 0, 12, 14);
    }

    @Override
    public void display() {
        PApplet p = getPApplet();
        p.pushStyle();
        p.strokeWeight(1.0f);
        p.stroke(getForeground().getRGB(), getForeground().getAlpha());
        switch (smd) {
            case rising:
                p.line(x1, y1, x2_5, y1); // _
                p.line(x2_5, y1, x2_5, y2); // /
                p.line(x2_5, y2, x4, y2); // -
                break;
            case falling:
                p.line(x1, y2, x2_5, y2); // _
                p.line(x2_5, y1, x2_5, y2); // /
                p.line(x2_5, y1, x4, y1); // -
                break;
            case risingfalling:
                p.line(x1, y1, x2, y1); // _
                p.line(x2, y2, x3, y2); // -
                p.line(x3, y1, x4, y1); // _
                p.line(x2, y1, x2, y2); // /
                p.line(x3, y2, x3, y1); // \
                break;
            case pulse:
                p.line(x1, y1, x4, y1); // __
                p.line(x2_5, y1, x2_5, y2); // |
                break;
            case bipolar:
                p.line(6, 2, 6, 8); // verti
                p.line(3, 5, 9, 5); // hori

                p.line(3, 10, 9, 10); // hori
                break;
            case positive:
                p.line(6, 4, 6, 10); // verti
                p.line(3, 7, 9, 7); // hori
                break;
            default:
                break;

        }
    }
}
