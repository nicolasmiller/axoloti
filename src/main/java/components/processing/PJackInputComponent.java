package components.processing;

import axoloti.Theme;
import axoloti.pinlets.PInletInstanceView;
import axoloti.processing.PComponent;
import axoloti.processing.PatchPApplet;
import java.awt.Color;
import processing.core.PApplet;
import processing.core.PShape;

public class PJackInputComponent extends PComponent {

    private static final int SZ = 10;
    private static final int MARGIN = 2;
    private static final float STROKE_WEIGHT = 1.5f;

    final PInletInstanceView inletInstanceView;

    private static PShape shadowShape;
    private static PShape foregroundShape;

    public PJackInputComponent(PApplet p, PInletInstanceView inletInstanceView) {
        super(p);
        this.inletInstanceView = inletInstanceView;
    }

    @Override
    public void setup() {
        setBounds(0, 0, SZ, SZ);
        if (shadowShape == null) {
            PApplet p = this.getPApplet();
            int p1 = MARGIN + 1;
            int p2 = SZ - MARGIN - MARGIN;
            int transparent = p.color(0, 0, 0, 0);

            shadowShape = p.createShape(p.ELLIPSE, p1, p1, p2, p2);
            shadowShape.setFill(transparent);
            shadowShape.setStrokeWeight(STROKE_WEIGHT);

            Color componentPrimary = Theme.getCurrentTheme().Component_Primary;
            shadowShape.setStroke(p.color(componentPrimary.getRGB(), componentPrimary.getAlpha()));
            foregroundShape = p.createShape(p.ELLIPSE, MARGIN, MARGIN, p2, p2);
            foregroundShape.setFill(transparent);
            foregroundShape.setStrokeWeight(STROKE_WEIGHT);
        }
    }

    @Override
    public void display() {
        PatchPApplet p = (PatchPApplet) this.getPApplet();
        p.pushStyle();

        // compensate for p3d strokeWeight bug
        float weight = STROKE_WEIGHT * p.getScaleFactor();
        shadowShape.setStrokeWeight(weight);
        foregroundShape.setStrokeWeight(weight);

        p.shape(shadowShape);

        if (inletInstanceView.getInletInstance().isConnected()) {
            foregroundShape.setStrokeWeight(0);
            foregroundShape.setFill(p.color(getForeground().getRGB(), getForeground().getAlpha()));
        } else {
            foregroundShape.setFill(p.color(0, 0, 0, 0));
            foregroundShape.setStroke(p.color(getForeground().getRGB(), getForeground().getAlpha()));
        }
        p.shape(foregroundShape);
        p.popStyle();
    }
}
