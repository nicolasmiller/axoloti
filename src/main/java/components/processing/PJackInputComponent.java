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

    private static PShape group;

    private static PShape shadowShape;
    private static PShape foregroundShape;

    public PJackInputComponent(PApplet p, PInletInstanceView inletInstanceView) {
        super(p);
        this.inletInstanceView = inletInstanceView;
    }

    @Override
    public void setup() {
        setBounds(0, 0, SZ, SZ);
        if (group == null) {
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

            group = p.createShape(p.GROUP);
            group.addChild(shadowShape);
            group.addChild(foregroundShape);
        }
    }

    @Override
    public void display() {
        PatchPApplet p = (PatchPApplet) this.getPApplet();

        // compensate for p3d strokeWeight bug
        float weight = STROKE_WEIGHT * p.getScaleFactor();
        shadowShape.setStrokeWeight(weight);
        foregroundShape.setStrokeWeight(weight);

        int foreground = p.color(getForeground().getRGB(), getForeground().getAlpha());
        foregroundShape.setStroke(foreground);
        if (inletInstanceView.getInletInstance().isConnected()) {
            foregroundShape.setFill(foreground);
        } else {
            foregroundShape.setFill(p.color(0, 0, 0, 0));
        }

        p.shape(group);
    }
}
