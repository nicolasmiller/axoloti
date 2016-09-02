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

    private static PShape connectedGroup;
    private static PShape unconnectedGroup;

    // consolodate into single pshape
    private static PShape unconnectedShadow;
    private static PShape unconnectedForeground;

    private static PShape connectedShadow;
    private static PShape connectedForeground;

    private static PShape unconnectedForegroundRect;
    private static PShape unconnectedShadowRect;
    private static PShape connectedForegroundRect;
    private static PShape connectedShadowRect;

    @Override
    public void setup() {
        PatchPApplet p = (PatchPApplet) this.getPApplet();
        setBounds(0, 0, SZ, SZ);
        if (connectedGroup == null) {
            int p2 = SZ - MARGIN - MARGIN;
            connectedShadowRect = p.createShape(p.RECT, MARGIN, MARGIN + 1, p2, p2);
            connectedShadowRect.setFill(p.color(0, 0, 0, 0));
            Color componentPrimary = Theme.getCurrentTheme().Component_Primary;
            connectedShadowRect.setStroke(p.color(componentPrimary.getRGB(), componentPrimary.getAlpha()));

            int p1 = SZ - (MARGIN + INSET) * 2 + 1;
            connectedShadow = p.createShape(p.ELLIPSE, MARGIN + INSET, MARGIN + INSET + 1, p1, p1);
            connectedShadow.setFill(p.color(componentPrimary.getRGB(), componentPrimary.getAlpha()));
            connectedShadow.setStrokeWeight(0);

            connectedForeground = p.createShape(p.ELLIPSE, MARGIN + INSET - 1, MARGIN + INSET, p1, p1);
            connectedForeground.setStrokeWeight(0);

            connectedForegroundRect = p.createShape(p.RECT, MARGIN - 1, MARGIN, p2, p2, 0);
            connectedForegroundRect.setFill(p.color(0, 0, 0, 0));

            p1 = SZ - (MARGIN + INSET) * 2;

            unconnectedShadowRect = p.createShape(p.RECT, MARGIN, MARGIN + 1, p2, p2);
            unconnectedShadowRect.setFill(p.color(0, 0, 0, 0));
            unconnectedShadowRect.setStroke(p.color(componentPrimary.getRGB(), componentPrimary.getAlpha()));

            unconnectedShadow = p.createShape(p.ELLIPSE, MARGIN + INSET, MARGIN + INSET + 1, p1, p1);
            unconnectedShadow.setFill(p.color(0, 0, 0, 0));
            unconnectedShadow.setStroke(p.color(componentPrimary.getRGB(), componentPrimary.getAlpha()));

            unconnectedForeground = p.createShape(p.ELLIPSE, MARGIN + INSET - 1, MARGIN + INSET, p1, p1);
            unconnectedForeground.setFill(p.color(0, 0, 0, 0));

            unconnectedForegroundRect = p.createShape(p.RECT, MARGIN - 1, MARGIN, p2, p2, 0);
            unconnectedForegroundRect.setFill(p.color(0, 0, 0, 0));

            unconnectedGroup = p.createShape(p.GROUP);
            // unfortunately children cannot be reused across parents
            unconnectedGroup.addChild(unconnectedShadowRect);
            unconnectedGroup.addChild(unconnectedShadow);
            unconnectedGroup.addChild(unconnectedForeground);
            unconnectedGroup.addChild(unconnectedForegroundRect);

            connectedGroup = p.createShape(p.GROUP);
            connectedGroup.addChild(connectedShadowRect);
            connectedGroup.addChild(connectedShadow);
            connectedGroup.addChild(connectedForeground);
            connectedGroup.addChild(connectedForegroundRect);
        }
    }

    @Override
    public void display() {
        PatchPApplet p = (PatchPApplet) this.getPApplet();
        int foreground = p.color(getForeground().getRGB(), getForeground().getAlpha());

        connectedForeground.setFill(foreground);
        unconnectedForeground.setStroke(foreground);
        unconnectedForegroundRect.setStroke(foreground);
        connectedForegroundRect.setStroke(foreground);

        // compensate for p3d strokeWeight bug
        float weight = STROKE_WEIGHT * p.getScaleFactor();
        connectedShadowRect.setStrokeWeight(weight);
        connectedForegroundRect.setStrokeWeight(weight);
        unconnectedShadowRect.setStrokeWeight(weight);
        unconnectedForegroundRect.setStrokeWeight(weight);

        if (outletInstanceView.getOutletInstance().isConnected()) {
            p.shape(connectedGroup);
        } else {
            unconnectedShadow.setStrokeWeight(weight);
            unconnectedForeground.setStrokeWeight(weight);
            p.shape(unconnectedGroup);
        }
    }
}
