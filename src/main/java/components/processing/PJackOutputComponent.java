package components.processing;

import axoloti.Theme;
import axoloti.poutlets.POutletInstanceView;
import axoloti.processing.PComponent;
import axoloti.processing.PatchPApplet;
import java.awt.Color;
import processing.core.PApplet;
import processing.core.PShape;

public class PJackOutputComponent extends PComponent {

    private static final int SZ = 10;
    private static final int MARGIN = 2;
    private static final int INSET = 1;
    private static final float STROKE_WEIGHT = 1.5f;

    final POutletInstanceView outletInstanceView;

    public PJackOutputComponent(PApplet p, POutletInstanceView outletInstanceView) {
        super(p);
        this.outletInstanceView = outletInstanceView;
    }

    private static PShape unconnectedShadowShape;
    private static PShape unconnectedForegroundShape;

    private static PShape connectedShadowShape;
    private static PShape connectedForegroundShape;

    private static PShape rectShape;
    private static PShape shadowRectShape;

    @Override
    public void setup() {
        PatchPApplet p = (PatchPApplet) this.getPApplet();
        setBounds(0, 0, SZ, SZ);
        if (unconnectedShadowShape == null) {
            int p2 = SZ - MARGIN - MARGIN;
            shadowRectShape = p.createShape(p.RECT, MARGIN, MARGIN + 1, p2, p2);
            shadowRectShape.setFill(p.color(0, 0, 0, 0));
            Color componentPrimary = Theme.getCurrentTheme().Component_Primary;
            shadowRectShape.setStroke(p.color(componentPrimary.getRGB(), componentPrimary.getAlpha()));

            int p1 = SZ - (MARGIN + INSET) * 2 + 1;
            connectedShadowShape = p.createShape(p.ELLIPSE, MARGIN + INSET, MARGIN + INSET + 1, p1, p1);
            connectedShadowShape.setStroke(p.color(componentPrimary.getRGB(), componentPrimary.getAlpha()));
            connectedShadowShape.setFill(p.color(0, 0, 0, 0));

            connectedForegroundShape = p.createShape(p.ELLIPSE, MARGIN + INSET - 1, MARGIN + INSET, p1, p1);
            connectedForegroundShape.setStrokeWeight(0);

            p1 = SZ - (MARGIN + INSET) * 2;

            unconnectedShadowShape = p.createShape(p.ELLIPSE, MARGIN + INSET, MARGIN + INSET + 1, p1, p1);
            unconnectedShadowShape.setFill(p.color(0, 0, 0, 0));
            unconnectedShadowShape.setStroke(p.color(componentPrimary.getRGB(), componentPrimary.getAlpha()));

            unconnectedForegroundShape = p.createShape(p.ELLIPSE, MARGIN + INSET - 1, MARGIN + INSET, p1, p1);
            unconnectedForegroundShape.setFill(p.color(0, 0, 0, 0));

            rectShape = p.createShape(p.RECT, MARGIN - 1, MARGIN, p2, p2);
            rectShape.setFill(p.color(0, 0, 0, 0));
        }
    }

    @Override
    public void display() {
        PatchPApplet p = (PatchPApplet) this.getPApplet();

        connectedForegroundShape.setFill(p.color(getForeground().getRGB(), getForeground().getAlpha()));
        unconnectedForegroundShape.setStroke(p.color(getForeground().getRGB(), getForeground().getAlpha()));
        rectShape.setStroke(p.color(getForeground().getRGB(), getForeground().getAlpha()));

        // compensate for p3d strokeWeight bug
        float weight = STROKE_WEIGHT * p.getScaleFactor();
        shadowRectShape.setStrokeWeight(weight);
        p.shape(shadowRectShape);

        if (outletInstanceView.getOutletInstance().isConnected()) {
            connectedShadowShape.setStrokeWeight(weight);
            p.shape(connectedShadowShape);
            p.shape(connectedForegroundShape);
        } else {
            unconnectedShadowShape.setStrokeWeight(weight);
            unconnectedForegroundShape.setStrokeWeight(weight);
            p.shape(unconnectedShadowShape);
            p.shape(unconnectedForegroundShape);
        }

        rectShape.setStrokeWeight(weight);
        p.shape(rectShape);
    }
}
