package components.processing;

import axoloti.Theme;
import axoloti.pinlets.PInletInstanceView;
import axoloti.processing.PComponent;
import java.awt.Color;
import java.awt.Dimension;
import processing.core.PApplet;

public class PJackInputComponent extends PComponent {

    private static final int SZ = 10;
    private static final int MARGIN = 2;
    private static final Dimension DIM = new Dimension(SZ, SZ);
    final PInletInstanceView inletInstanceView;

    public PJackInputComponent(PApplet p, PInletInstanceView inletInstanceView) {
        super(p);
        this.inletInstanceView = inletInstanceView;
    }

    @Override
    public void setup() {
        setBounds(0, 0, SZ, SZ);
    }

    @Override
    public void display() {
        PApplet p = this.getPApplet();
        p.pushStyle();
        p.strokeWeight(1.5f);
        if (inletInstanceView.getInletInstance().isConnected()) {
            Color componentPrimary = Theme.getCurrentTheme().Component_Primary;
            p.stroke(componentPrimary.getRGB(), componentPrimary.getAlpha());
            int p1 = MARGIN + 1;
            int p2 = SZ - MARGIN - MARGIN;
            p.noFill();
            p.ellipse(p1, p1, p2, p2);
            p.fill(getForeground().getRGB(), getForeground().getAlpha());
            p.ellipse(MARGIN, MARGIN, p2, p2);
        } else {
            Color componentPrimary = Theme.getCurrentTheme().Component_Primary;
            p.stroke(componentPrimary.getRGB(), componentPrimary.getAlpha());
            int p1 = MARGIN + 1;
            int p2 = SZ - MARGIN - MARGIN;
            p.noFill();
            p.ellipse(p1, p1, p2, p2);
            p.stroke(getForeground().getRGB(), getForeground().getAlpha());
            p.ellipse(MARGIN, MARGIN, p2, p2);
        }
        p.popStyle();
    }

}
