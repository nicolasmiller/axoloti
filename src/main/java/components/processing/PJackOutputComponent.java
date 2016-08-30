package components.processing;

import axoloti.Theme;
import axoloti.poutlets.POutletInstanceView;
import axoloti.processing.PComponent;
import java.awt.Color;
import java.awt.Dimension;
import processing.core.PApplet;

public class PJackOutputComponent extends PComponent {

    private static final int SZ = 10;
    private static final int MARGIN = 2;
    private static final int INSET = 1;

    private static final Dimension DIM = new Dimension(SZ, SZ);
    final POutletInstanceView outletInstanceView;

    public PJackOutputComponent(PApplet p, POutletInstanceView outletInstanceView) {
        super(p);
        this.outletInstanceView = outletInstanceView;
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
        Color componentPrimary = Theme.getCurrentTheme().Component_Primary;
        p.stroke(componentPrimary.getRGB(), componentPrimary.getAlpha());
        p.noFill();
        int p2 = SZ - MARGIN - MARGIN;
        p.rect(MARGIN, MARGIN + 1, p2, p2);

        if (outletInstanceView.getOutletInstance().isConnected()) {
            p.fill(componentPrimary.getRGB(), componentPrimary.getAlpha());
            int p1 = SZ - (MARGIN + INSET) * 2 + 1;
            p.ellipse(MARGIN + INSET, MARGIN + INSET + 1, p1, p1);
            p.fill(getForeground().getRGB(), getForeground().getAlpha());
            p.ellipse(MARGIN + INSET - 1, MARGIN + INSET, p1, p1);
        } else {
            p.noFill();
            int p1 = SZ - (MARGIN + INSET) * 2;
            p.ellipse(MARGIN + INSET, MARGIN + INSET + 1, p1, p1);
            p.fill(getForeground().getRGB(), getForeground().getAlpha());
            p.ellipse(MARGIN + INSET - 1, MARGIN + INSET, p1, p1);
        }
        p.popStyle();
    }
}
