package axoloti.piccolo;

import axoloti.Net;
import axoloti.PatchViewPiccolo;
import axoloti.Theme;
import axoloti.inlets.IInletInstanceView;
import axoloti.outlets.IOutletInstanceView;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import org.piccolo2d.util.PPaintContext;

public class PNetDragging extends PNetView {

    public PNetDragging(PatchViewPiccolo patchView) {
        this(new Net(), patchView);
    }

    public PNetDragging(Net net, PatchViewPiccolo patchView) {
        super(net, patchView);
    }

    Point p0;

    public void SetDragPoint(Point p0) {
        this.p0 = p0;

        updateBounds();
        repaint();
    }

    @Override
    protected void paint(PPaintContext paintContext) {
        Graphics2D g2 = paintContext.getGraphics();
        float shadowOffset = 0.5f;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        Color c;
        if (net.isValidNet()) {
            if (selected) {
                g2.setStroke(strokeValidSelected);
            } else {
                g2.setStroke(strokeValidDeselected);
            }

            c = net.getDataType().GetColor();
        } else {
            if (selected) {
                g2.setStroke(strokeBrokenSelected);
            } else {
                g2.setStroke(strokeBrokenDeselected);
            }

            if (net.getDataType() != null) {
                c = net.getDataType().GetColor();
            } else {
                c = Theme.getCurrentTheme().Cable_Shadow;
            }
        }
        if (p0 != null) {
            Point2D from = this.globalToLocal(p0);
            for (IInletInstanceView i : getDestinationViews()) {
                Point p1 = i.getJackLocInCanvas();

                Point2D to = this.globalToLocal(p1);
                g2.setColor(Theme.getCurrentTheme().Cable_Shadow);
                DrawWire(g2, (int) from.getX() + shadowOffset, (int) from.getY() + shadowOffset, (int) to.getX() + shadowOffset, (int) to.getY() + shadowOffset);
                g2.setColor(c);
                DrawWire(g2, (int) from.getX(), (int) from.getY(), (int) to.getX(), (int) to.getY());
            }
            for (IOutletInstanceView i : getSourceViews()) {
                Point p1 = i.getJackLocInCanvas();

                Point2D to = this.globalToLocal(p1);
//                Point to = SwingUtilities.convertPoint(patchView.Layers, p1, this);
                g2.setColor(Theme.getCurrentTheme().Cable_Shadow);
                DrawWire(g2, (int) from.getX() + shadowOffset, (int) from.getY() + shadowOffset, (int) to.getX() + shadowOffset, (int) to.getY() + shadowOffset);
                g2.setColor(c);
                DrawWire(g2, (int) from.getX(), (int) from.getY(), (int) to.getX(), (int) to.getY());

            }
        }
    }

    @Override
    public void updateBounds() {
        int min_y = Integer.MAX_VALUE;
        int min_x = Integer.MAX_VALUE;
        int max_y = Integer.MIN_VALUE;
        int max_x = Integer.MIN_VALUE;

        if (p0 != null) {
            min_x = p0.x;
            max_x = p0.x;
            min_y = p0.y;
            max_y = p0.y;
        }

        for (IInletInstanceView i : getDestinationViews()) {
            Point p1 = i.getJackLocInCanvas();
            min_x = Math.min(min_x, p1.x);
            min_y = Math.min(min_y, p1.y);
            max_x = Math.max(max_x, p1.x);
            max_y = Math.max(max_y, p1.y);
        }
        for (IOutletInstanceView i : getSourceViews()) {
            Point p1 = i.getJackLocInCanvas();
            min_x = Math.min(min_x, p1.x);
            min_y = Math.min(min_y, p1.y);
            max_x = Math.max(max_x, p1.x);
            max_y = Math.max(max_y, p1.y);
        }

        setBounds(min_x, min_y, Math.max(1, max_x - min_x),
                (int) CtrlPointY(min_x, min_y, max_x, max_y) - min_y);
    }
}
